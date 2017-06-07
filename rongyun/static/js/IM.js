/**
 * 初始化IM相关。
 */
function initIM(){
    //初始化监听器
    initListener();
    //连接IM服务器
    connect(userId);
}

function initListener(){
    // 初始化
    // RongIMClient.init(appkey, [dataAccessProvider],[options]);
    // appkey:官网注册的appkey。
    // dataAccessProvider:自定义本地存储方案的实例，不传默认为内存存储，自定义需要实现WebSQLDataProvider所有的方法，此参数必须是传入实例后的对象。
    RongIMClient.init("3argexb63ur5e");

    // 设置连接监听状态 （ status 标识当前连接状态）
    // 连接状态监听器
    RongIMClient.setConnectionStatusListener({
        onChanged: function (status) {
            switch (status) {
                //链接成功
                case RongIMLib.ConnectionStatus.CONNECTED:
                    console.log('链接成功');
                    break;
                //正在链接
                case RongIMLib.ConnectionStatus.CONNECTING:
                    console.log('正在链接');
                    break;
                //重新链接
                case RongIMLib.ConnectionStatus.DISCONNECTED:
                    console.log('断开连接');
                    break;
                //其他设备登录
                case RongIMLib.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT:
                    console.log('其他设备登录');
                    document.write("其他设备已登录，请重新登录！");
                    break;
                //网络不可用
                case RongIMLib.ConnectionStatus.NETWORK_UNAVAILABLE:
                    console.log('网络不可用');
                    break;
            }
        }});

    // 消息监听器
    RongIMClient.setOnReceiveMessageListener({
        // 接收到的消息
        onReceived: function (message) {
            // 判断消息类型
            switch(message.messageType){
                case RongIMClient.MessageType.TextMessage:
                    // 发送的消息内容将会被打印
                    console.log(message);
                    var content = message.content.content;
                    var extra   = message.content.extra;
                    var sender  = message.senderUserId;
                    message.type = extra.type ? extra.type : "plain";

                    //保存数据库，并展示
                    storeMessage(DBName,Store_Msg,message).then(function(){
                        showDialog([{
                            id : message.messageUId,
                            type : message.type,
                            author : _members[sender],
                            content : content,
                            pause : 500,
                        }]);
                    });

                    break;
                case RongIMClient.MessageType.VoiceMessage:
                    // 对声音进行预加载
                    // message.content.content 格式为 AMR 格式的 base64 码
                    RongIMLib.RongIMVoice.preLoaded(message.content.content);
                    break;
                case RongIMClient.MessageType.ImageMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.DiscussionNotificationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.LocationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.RichContentMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.DiscussionNotificationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.InformationNotificationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.ContactNotificationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.ProfileNotificationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.CommandNotificationMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.CommandMessage:
                    // do something...
                    break;
                case RongIMClient.MessageType.UnknownMessage:
                    // do something...
                    break;
                default:
                // 自定义消息
                // do something...
            }
        }
    });
}

/**
 * 聊天消息服务相关的工具类
 **/
function connect(userid){
    var token;
    if(userid == "1"){
        token = "EnmglHK3s3oF2UJ8lHxKO96/2ckBd6flKy3YCH304zPy3/2codaAiX3/v+E7w7KOJUBsBBqUxPs=";
    }
    if(userid == "2")
    {
        token = "canxD+fpqZ1t1Ob0c5ppMCDipR0Wfhnelde4J7fLJn3A8qv80uAU08m4ivETW99+q4oyteKXQ8SeAwEJgyohDw==";
    }
    if(userid == "3")
    {
        token = "WzbtziZhMIE0q+hFwjBDmyDipR0Wfhnelde4J7fLJn3A8qv80uAU08/IW8kFk7RmZASBkpAb/TieAwEJgyohDw==";
    }
    // 连接融云服务器。
    RongIMClient.connect(token, {
        onSuccess: function(userId) {
            console.log("Login successfully." + userId);
        },
        onTokenIncorrect: function() {
            console.log('token无效');
        },
        onError:function(errorCode){
            var info = '';
            switch (errorCode) {
                case RongIMLib.ErrorCode.TIMEOUT:
                    info = '超时';
                    break;
                case RongIMLib.ErrorCode.UNKNOWN_ERROR:
                    info = '未知错误';
                    break;
                case RongIMLib.ErrorCode.UNACCEPTABLE_PaROTOCOL_VERSION:
                    info = '不可接受的协议版本';
                    break;
                case RongIMLib.ErrorCode.IDENTIFIER_REJECTED:
                    info = 'appkey不正确';
                    break;
                case RongIMLib.ErrorCode.SERVER_UNAVAILABLE:
                    info = '服务器不可用';
                    break;
            }
            console.log(errorCode);
        }
    });
};

/**
 * 发送消息
 * @param targetId
 * @param content
 * @returns {Promise}
 */
function sendMessage(targetId,content){
    var message = content.type ? content : {content:content,type:"plain"};
    var content = message.content;
    return new Promise(function(resolve, reject){
        // 定义消息类型,文字消息使用 RongIMLib.TextMessage
        var msg = new RongIMLib.TextMessage({content:content,extra:{type:message.type}});
        //或者使用RongIMLib.TextMessage.obtain 方法.具体使用请参见文档
        //var msg = RongIMLib.TextMessage.obtain("hello");
        var conversationtype = RongIMLib.ConversationType.GROUP; // 私聊
//        var targetId = "2"; // 目标 Id
        RongIMClient.getInstance().sendMessage(conversationtype, targetId, msg, {
                // 发送消息成功
                onSuccess: function (_message) {
                    //message 为发送的消息对象并且包含服务器返回的消息唯一Id和发送消息时间戳
                    console.log("Send successfully",_message);
                    //设置id
                    _message.type = message.type;
                    _message.id   = _message.messageUId;
                    //数据发送成功后，保存本地数据；然后继续原来的任务
                    storeMessage(DBName,Store_Msg,_message).then(function () {
                        resolve(_message);
                    });
                },
                onError: function (errorCode,message) {
                    var info = '';
                    switch (errorCode) {
                        case RongIMLib.ErrorCode.TIMEOUT:
                            info = '超时';
                            break;
                        case RongIMLib.ErrorCode.UNKNOWN_ERROR:
                            info = '未知错误';
                            break;
                        case RongIMLib.ErrorCode.REJECTED_BY_BLACKLIST:
                            info = '在黑名单中，无法向对方发送消息';
                            break;
                        case RongIMLib.ErrorCode.NOT_IN_DISCUSSION:
                            info = '不在讨论组中';
                            break;
                        case RongIMLib.ErrorCode.NOT_IN_GROUP:
                            info = '不在群组中';
                            break;
                        case RongIMLib.ErrorCode.NOT_IN_CHATROOM:
                            info = '不在聊天室中';
                            break;
                        default :
                            info = x;
                            break;
                    }
                    console.log('发送失败:' + info);
                }
            }
        );
    });
};

