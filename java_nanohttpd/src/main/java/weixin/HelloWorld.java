package weixin;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.util.ServerRunner;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;

public class HelloWorld extends RouterNanoHTTPD {

	public HelloWorld() {
		super(90);
		addMappings();
	}

	public HelloWorld(int port) {
		super(port);
		addMappings();
	}

	@Override
	public void addMappings() {
		super.addMappings();
		addRoute("/user", UserHandler.class);
	}

	public static class UserHandler extends DefaultHandler {

		private WxMpService wxService;
		private WxMpMessageRouter wxMpMessageRouter;

		protected WxMpInMemoryConfigStorage config;
		public UserHandler() {
			config = new WxMpInMemoryConfigStorage();
			config.setAppId("wx0b2d9cd9ed8baca7"); // 设置微信公众号的appid
			config.setSecret("68d494c86302a974223c6e478acea32f"); // 设置微信公众号的app
																	// corpSecret
			config.setToken("bsarki1390014166"); // 设置微信公众号的token
			config.setAesKey("111111122222222249489789456446551231849asdf"); // 设置微信公众号的EncodingAESKey

			wxService = new WxMpServiceImpl();
			wxService.setWxMpConfigStorage(config);

			WxMpMessageHandler handler = new WxMpMessageHandler() {

				public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> arg1, WxMpService arg2,
						WxSessionManager arg3) throws WxErrorException {
					WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content("测试加密消息")
							.fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
					return m;
				}
			};

			wxMpMessageRouter = new WxMpMessageRouter(wxService);
			wxMpMessageRouter.rule().async(false).content("哈哈") // 拦截内容为“哈哈”的消息
					.handler(handler).end();
		}

		@Override
		public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
			return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), getText(uriResource,urlParams,session));
		}

		@Override
		public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
			return super.post(uriResource, urlParams, session);
		}

		public String getText(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
			String signature = urlParams.get("signature");
			String nonce = urlParams.get("nonce");
			String timestamp = urlParams.get("timestamp");

			if (!wxService.checkSignature(timestamp, nonce, signature)) {
				// 消息签名不正确，说明不是公众平台发过来的消息
				return ("非法请求");
			}

			String echostr = urlParams.get("echostr");
			if (StringUtils.isNotBlank(echostr)) {
				// 说明是一个仅仅用来验证的请求，回显echostr
				return (echostr);
			}

			String encryptType = StringUtils.isBlank(urlParams.get("encrypt_type")) ? "raw"
					: urlParams.get("encrypt_type");

			if ("raw".equals(encryptType)) {
				// 明文传输的消息
				WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml("");
				WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
				return (outMessage.toXml());

			}

			if ("aes".equals(encryptType)) {
				// 是aes加密的消息
				String msgSignature = urlParams.get("msg_signature");
				WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml("", config,
						timestamp, nonce, msgSignature);
				WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
				return (outMessage.toEncryptedXml(config));
			}

			return ("不可识别的加密类型");
		}

		@Override
		public String getMimeType() {
			return "text/html;charset=utf-8";
		}

		@Override
		public IStatus getStatus() {
			return NanoHTTPD.Response.Status.OK;
		}

		@Override
		public String getText() {
			throw new RuntimeException();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws WxErrorException
	 */
	public static void main(String args[]) throws WxErrorException {

		// 用户的openid在下面地址获得
		// https://mp.weixin.qq.com/debug/cgi-bin/apiinfo?t=index&type=用户管理&form=获取关注者列表接口%20/user/get
//		String openid = "...";
//		WxMpKefuMessage message = WxMpKefuMessage.TEXT().toUser(openid).content("Hello World").build();
//		wxService.getKefuService().sendKefuMessage(message);

		ServerRunner.run(HelloWorld.class);
	}

}
