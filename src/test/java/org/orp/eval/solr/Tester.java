package org.orp.eval.solr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.orp.eval.utils.JsonUtils;
import org.restlet.ext.json.JsonRepresentation;

public class Tester {
	public static void main(String[] args) 
			throws HttpException, IOException {
//		String url = "http://localhost:9090/solr/schema";
//		GetMethod get = new GetMethod(url);
//		new HttpClient().executeMethod(get);
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		byte[] arr = new byte[1024];
//		int c = 0;
//		while((c = get.getResponseBodyAsStream()
//				.read(arr, 0, arr.length)) > 0){
//			os.write(arr, 0, c);
//		}
//		String response = new String(os.toByteArray(), "UTF-8");
//		JsonRepresentation rep = new JsonRepresentation(response);
//		Map<String, Object> map = JsonUtils.toMap(rep);
//		for(String key : map.keySet())
//			System.out.println(map.get(key));
		
		String host = "http://localhost:9090/";
		System.out.println(host.endsWith("/"));
				
	}

}
