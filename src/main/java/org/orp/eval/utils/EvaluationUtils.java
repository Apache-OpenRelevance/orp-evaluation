package org.orp.eval.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.restlet.ext.json.JsonRepresentation;

public class EvaluationUtils {
	public static JsonRepresentation message(String content){
		Map<String, Object> msg = new HashMap<String, Object>();
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		msg.put("time", pattern.format(curDate));
		msg.put("message", content);
		
		return new JsonRepresentation(msg);
			
	}
	
	public static void deleteFile(File file){
		if(file.isDirectory()){
			if(file.list().length == 0) 
				file.delete();
			else{
				File[] files = file.listFiles();
				for(File f : files) deleteFile(f);
				deleteFile(file);
			}
		} else 
			file.delete();
	}
	
	public static String dateFormat(Date date){
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		return pattern.format(date);
	}
	
	public static Map<String, Object> extractValues(Map<String, Object> raw, Iterable<String> keys){
		Map<String, Object> values = new HashMap<String, Object>();
		for(String k : keys){
			if(!raw.containsKey(k)) continue;
			Object v = raw.get(k);
			if(v instanceof String){
				String t = (String)v;
				values.put(k, t.trim());
			}else
				values.put(k, v);
		}
		return values;
	}
	
	public static void fetchFile(String uri, String localDir) 
			throws HttpException, IOException{
		GetMethod get = new GetMethod(uri);
		new HttpClient().executeMethod(get);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] arr = new byte[1024];
		int count = 0;
		while((count = get.getResponseBodyAsStream()
				.read(arr, 0, arr.length)) > 0)
			os.write(arr, 0, count);
		FileOutputStream fs = new FileOutputStream(localDir);
		fs.write(os.toByteArray());
		fs.flush();
		fs.close();
	}
}
