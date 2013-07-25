package org.orp.eval.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	
	
}
