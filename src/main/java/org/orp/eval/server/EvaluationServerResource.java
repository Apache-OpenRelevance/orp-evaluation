package org.orp.eval.server;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.orp.eval.common.EvaluationResource;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.orp.eval.utils.EvaluationUtils;
import org.orp.eval.utils.JsonUtils;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class EvaluationServerResource extends WadlServerResource implements EvaluationResource{

	private DBHandler handler;
	private String id;
	private String prefix;
	
	@Override
	public void doInit(){
		handler = DBHandlerImpl.newHandler("jdbc:sqlite:db/evaluation.db");
		prefix = getRequest().getResourceRef().getIdentifier();
		id = prefix.split("/")[4];
	}
	
	public Representation present() 
			throws SQLException {
		Map<String, Object> info = handler.selectAllById("EVALUATION", id);
		if(info.isEmpty()){
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return EvaluationUtils.message("Evaluation " + id + " not found.");
		}
		List<String> keys = Arrays.asList(new String[]{
				"id", "evaluate_time", "host", "model", "collection_id", "corpus", "measurement",
				"score", "scoring_model"});
		Map<String, Object> data = EvaluationUtils.extractValues(info, keys);
		if(data.get("score") == null){
			// TODO Check and return the status
			data.put("score", "N/A");
		}
		data.put("schema", prefix + "/schema");
		data.put("result", prefix + "/result");
		
		return new JsonRepresentation(data);
	}

	@SuppressWarnings("unchecked")
	public Representation execute(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException, SQLException {
		if(entity == null)
			return EvaluationUtils.message("No data available.");
		Map<String, Object> params = JsonUtils.toMap(entity);
		String cmd = params.keySet().iterator().next();
		if(cmd.equals("re-run")){
			//Update info
			List<String> keys = Arrays.asList(new String[]{
					"collection_id", "scoring_model", "tester"});
			Map<String, Object> data = EvaluationUtils.extractValues(
					(Map<String, Object>)params.get("re-run"), keys);
			data.put("evaluate_time", EvaluationUtils.dateFormat(new Date(System.currentTimeMillis())));
			handler.updateById("EVALUATION", data, id);
			
			// TODO Run the evaluation(new thread)
			
			//Return updated result
			Map<String, Object> info = handler.selectAllById("EVALUATION", id);
			if(info.get("score") == null){
				// TODO Check and return the status
				info.put("score", "N/A");
			}
			return new JsonRepresentation(info);
		}else if(cmd.equals("check-in")){
			// TODO Check the status
			System.out.println("Checking in");
		}else
			return EvaluationUtils.message("Invalid command.");
		return null;
	}
	
	public Representation remove() 
			throws SQLException{
		handler.deleteById("EVALUATION", id);
		EvaluationUtils.deleteFile(new File("evaluations/" + id + "/"));
		return EvaluationUtils.message("Evaluation " + id + " has been removed.");
	}
	
	@Override
	public void doRelease(){
		try {
			handler.clean();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doCatch(Throwable ex){
		Throwable cause = ex.getCause();
		if(cause instanceof JsonParseException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof JsonMappingException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof IOException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof SQLException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		System.err.println(cause.getClass().getName() + ": " + cause.getMessage());
	}
}
