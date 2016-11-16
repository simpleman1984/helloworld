package api;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * Java Api官方文档 <br/>
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-api.html <br/>
 * @author xuaihua
 *
 */
public class ESClient {
	
	static class ElasticSearchConfig
	{
		static String NODE_NAME = "nodename";
		
		static String DATA_PATH = "data_path";
		
		static String CLUSTER_NAME = "CLUSTER_NAME";
	}

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		//嵌入式计算引擎 
		Settings.Builder settingsBuilder = Settings.builder();
		
        settingsBuilder.put("node.name", ElasticSearchConfig.NODE_NAME);
        settingsBuilder.put("path.data", ElasticSearchConfig.DATA_PATH);
        settingsBuilder.put("path.home", ElasticSearchConfig.DATA_PATH);
        settingsBuilder.put("http.enabled", true);

        Settings settings = settingsBuilder.build();

        Node node = NodeBuilder.nodeBuilder()
                          .settings(settings)
                          .clusterName(ElasticSearchConfig.CLUSTER_NAME)
                          .data(true).local(false).node();
        
        //连接远程服务器
//		Client client = TransportClient.builder().build()
//				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300))
//				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.138"), 9300));
        
        //连接本地节点
        Client client = node.client();

        //更新索引内容
//		UpdateRequest updateRequest = new UpdateRequest();
//		updateRequest.index("dept");
//		updateRequest.type("employee");
//		updateRequest.id("32");
//		updateRequest.doc(XContentFactory.jsonBuilder()
//		        .startObject()
//		            .field("gender", "male")
//		            .field("xxx", "333")
//		            .field("yyy", "4234234")
//		        .endObject());
//		client.update(updateRequest).get();
		
		//查询索引内容也可以进行插入！！！！！！！！！！
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("user", "kimchy")
				.field("postDate", new Date()).field("message", "trying out Elasticsearch").endObject();

		IndexResponse response = client.prepareIndex("twitter", "tweet", "1").setSource(builder).get();
		System.err.println(response);
		
		
		client.close();
		
		Thread.sleep(50000);
	}
}
