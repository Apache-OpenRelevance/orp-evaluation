package org.orp.eval.server;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.orp.eval.common.EvaluationsResource;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.orp.eval.utils.EvaluationUtils;
import org.orp.eval.utils.JsonUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class EvaluationsServerResource extends WadlServerResource implements EvaluationsResource{

	private DBHandler handler;
	@Override
	public void doInit(){
		handler = DBHandlerImpl.newHandler("jdbc:sqlite:db/evaluation.db");
	}
	
	public Representation list() 
			throws SQLException {
		Set<Map<String, Object>> rs = handler.selectAll("EVALUATION");
		if(rs.isEmpty())
			return EvaluationUtils.message("No evaluation found.");
		JSONArray evals = new JSONArray();
		String prefix = getRequest().getResourceRef().getIdentifier();
		for(Map<String, Object> key : rs){
			key.put("uri", prefix + "/" + key.get("id"));
			// TODO May check status of the evaluation
			if(key.get("score") == null) key.put("score", "N/A");
			evals.put(key);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("evaluations", evals);	
		return new JsonRepresentation(result);
	}
	@SuppressWarnings("unchecked")
	public Representation run(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException, SQLException{
			if(entity == null)
				return EvaluationUtils.message("No data available.");
			Map<String, Object> params = JsonUtils.toMap(entity);
			String cmd = params.keySet().iterator().next();
			if(cmd.equals("evaluate")){
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				String prefix = getRequest().getResourceRef().getIdentifier();
				String uri = prefix + "/" + id;
				String timestamp = EvaluationUtils.dateFormat(new Date(System.currentTimeMillis()));
				// TODO Get from Collection service.
				String corpus = "N/A";
				
				//Get wanted values, trim the values and ignore noises
				List<String> keys = Arrays.asList(new String[]{"host", "tester", "measurement", "collection_id"});
				Map<String, Object> data = EvaluationUtils.extractValues(
						(Map<String, Object>)params.get("evaluate"), keys);
				data.put("id", id);
				data.put("evaluate_time", timestamp);
				data.put("corpus", corpus);
				handler.insert("EVALUATION", data);
				
				data.put("uri", uri);
				
				return new JsonRepresentation(data);
			}else
				return EvaluationUtils.message("Invalid Commands");
	}
	
	@Override
	public void doCatch(Throwable ex){
		Throwable cause = ex.getCause();
		if(cause instanceof JsonParseException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof IOException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof JsonMappingException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof SQLException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		
		ex.printStackTrace();
		
	} 

	
}
