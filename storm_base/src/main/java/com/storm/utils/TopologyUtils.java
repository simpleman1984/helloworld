package com.storm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.storm.shade.org.apache.commons.exec.util.MapUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 请参考 http://storm.apache.org/releases/1.0.1/STORM-UI-REST-API.html
 **/
public class TopologyUtils {
	/**
	 * 删除某个任务
	 * @param host
	 * @param topologyId
	 * @param waitTime
	 */
	public static void kill(String host, String topologyName, int waitTime) {
		String topologyId = topologyId(host,topologyName);
		String url = "http://" + host + ":8080/api/v1/topology/" + topologyId + "/kill/" + waitTime;
		req(url,true);
	}
	
	/**
	 *获取任务名称对应的示例id
	 *
	 **/
	public static String topologyId(String host,String topologyName){
		String summary = req("http://" + host + ":8080/api/v1/topology/summary",false);
		try{
			ObjectMapper o = new ObjectMapper();
			Map topologiesMap = o.readValue(summary, Map.class);		
			List<Map> o1 = (List<Map>)topologiesMap.get("topologies");
			for(Map  topology : o1)
			{
				String name = (String) topology.get("name");
				String id   = (String) topology.get("id");
				if(topologyName.equals(name))
				{
					return id;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String req(String _url,boolean isPost) {
		try {
			URL url = new URL(_url);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(isPost)
			{
				// 发送POST请求必须设置如下两行
				connection.setDoOutput(true);
				connection.setDoInput(true);
			}
			connection.connect();

			if(isPost)
			{
				// 获取URLConnection对象对应的输出流
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				// 发送请求参数
				// out.print(param);
				// flush输出流的缓冲
				out.flush();
			}
			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			String result = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.err.println("post请求返回值如下:" + result);
			
			return result;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}

	public static void main(String[] args) {
//		String topologyId = TopologyUtils.topologyId("127.0.0.1","word-count");
		
		TopologyUtils.kill("127.0.0.1", "word-count", 1);
	}

}
