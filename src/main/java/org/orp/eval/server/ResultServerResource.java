package org.orp.eval.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.orp.eval.common.ResultResource;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.orp.eval.utils.EvaluationUtils;
import org.orp.eval.utils.JsonUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class ResultServerResource extends WadlServerResource implements ResultResource{

	private String id;
	private String prefix;
	private DBHandler evalHandler;
	private DBHandler statHandler;
	
	@Override
	public void doInit(){
		prefix = getRequest().getResourceRef().getIdentifier();
		id = prefix.split("/")[4];
		evalHandler = DBHandlerImpl.newHandler("jdbc:sqlite:db/evaluation.db");
		statHandler = DBHandlerImpl.newHandler("jdbc:sqlite:evaluations/" + id + "/stat.db");
	}
	
	public Representation summary() 
			throws SQLException {
		Map<String, Object> info = evalHandler.selectAllById("EVALUATION", id);
		String measurement = String.valueOf(info.get("measurement")).toLowerCase()
				.replaceAll("[-.]+", "_");
		String timestamp = String.valueOf(info.get("evaluate_time"));
		String scoreModel = String.valueOf(info.get("scoring_model"));
		Map<String, Object> stat = statHandler.selectAllById("SCORES", "all");
		
		List<String> keys = Arrays.asList(new String[]{"num_ret", "num_rel", 
				"num_rel_ret", measurement});
 		Map<String, Object> result = EvaluationUtils.extractValues(stat, keys);
		result.put("id", id);
		result.put("evaluate_time", timestamp);
		result.put("scoring_model", scoreModel);
		result.put("measurement", measurement);

		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("result", result);
		
		return new JsonRepresentation(result);
	}


	@SuppressWarnings("unchecked")
	public Representation execute(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException, SQLException{
		Map<String, Object> params = JsonUtils.toMap(entity);
		String cmd = params.keySet().iterator().next();
		if(cmd.equals("score")){
			Map<String, Object> data = (Map<String, Object>)params.get("score");
			String measurement = (String)data.get("measurement");
			if(measurement == null)
				return EvaluationUtils.message("No measurement data found. Measurement data is required.");
			
			
			Map<String, Object> info = EvaluationUtils.extractValues(evalHandler.selectAllById("EVALUATION", id),
					Arrays.asList(new String[]{"id", "evaluate_time", "scoring_model"}));
			Map<String, Object> basics = EvaluationUtils.extractValues(statHandler.selectAllById("SCORES", "all"), 
					Arrays.asList(new String[]{"num_ret", "num_rel", "num_rel_ret"}));
			
			Integer topicNum = (Integer)data.get("topic_no");
			Map<String, Object> scores = null;
			if(measurement.equals("all")){
				if(topicNum != null){
					scores = statHandler.selectAllById("SCORES", String.valueOf(topicNum));
					scores.remove("num_q");
					scores.remove("gm_ap");
				}else{
					scores = statHandler.selectAllById("SCORES", "all");
				}
			}else{
				if(topicNum != null){
					scores = EvaluationUtils.extractValues(statHandler.selectAllById("SCORES", String.valueOf(topicNum)),
							Arrays.asList(new String[]{measurement}));
					scores.remove("num_q");
					scores.remove("gm_ap");
				}else{
					scores = EvaluationUtils.extractValues(statHandler.selectAllById("SCORES", "all"),
							Arrays.asList(new String[]{measurement}));
				}
			}
			
			if(scores.isEmpty())
				return EvaluationUtils.message("Invalid measurement or topic number.");
			
			scores.putAll(info);
			scores.putAll(basics);
			
			return new JsonRepresentation(scores);
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
