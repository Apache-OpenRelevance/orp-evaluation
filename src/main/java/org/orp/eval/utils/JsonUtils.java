package org.orp.eval.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;

public class JsonUtils {
	public static String getSimpleValue(JsonRepresentation entity, String key) 
			throws JSONException{
		String value = entity.getJsonObject().getString(key);
		return value;
	}
	
	public static String getCommand(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException{
		Map<String, Object> data = toMap(entity);
		if(data.size() != 1)
			throw new JsonParseException("Invalid command.", null);
		return data.keySet().iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException{
		return new ObjectMapper().readValue(entity.getStream(), HashMap.class);
	}
}
