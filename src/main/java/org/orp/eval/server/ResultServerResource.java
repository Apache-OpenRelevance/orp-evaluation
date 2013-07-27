package org.orp.eval.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.orp.eval.common.ResultResource;
import org.orp.eval.utils.EvaluationUtils;
import org.orp.eval.utils.JsonUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class ResultServerResource extends WadlServerResource implements ResultResource{

	public Representation summary() {
		return null;
	}


	public Representation execute(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException{
		Map<String, Object> params = JsonUtils.toMap(entity);
		String cmd = params.keySet().iterator().next();
		if(cmd.equals("score")){
			
		}else if(cmd.equals("download")){
			
		}else{
			EvaluationUtils.message("Invalid command.");
		}
		
		return null;
	}
	
	@Override
	public void doCatch(Throwable ex){
		Throwable cause = ex.getCause();
		if(cause instanceof JsonParseException)
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		if(cause instanceof JsonMappingException)
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		if(cause instanceof IOException)
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		if(cause instanceof SQLException)
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		
		cause.printStackTrace();
		ex.printStackTrace();
	}


}
