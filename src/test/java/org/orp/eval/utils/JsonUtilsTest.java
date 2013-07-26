package org.orp.eval.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.restlet.ext.json.JsonRepresentation;

import junit.framework.TestCase;

public class JsonUtilsTest extends TestCase {
	public void testGetCommand(){
		try{
			Map<String, Object> root = new HashMap<String, Object>();
			Map<String, Object> child1 = new HashMap<String, Object>();
			Map<String, Object> child2 = new HashMap<String, Object>();
			
			child1.put("name", "child1");
			child1.put("id", "123");
			
			child2.put("name", "child2");
			child2.put("id", "456");
			
			root.put("cmd1", child1);
			root.put("cmd2", child2);
			
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			System.out.println(uuid.length());
			
			JsonRepresentation entity = new JsonRepresentation(root);
			System.out.println(JsonUtils.getCommand(entity));
			
			
		}catch(Exception e){
		}
	}
}
