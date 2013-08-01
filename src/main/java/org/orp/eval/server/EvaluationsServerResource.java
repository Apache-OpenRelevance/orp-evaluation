package org.orp.eval.server;

import java.io.File;
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
		/*
		 * 1. Fetch evaluation info from DB 
		 */
		Set<Map<String, Object>> rs = handler.selectAll("EVALUATION");
		if(rs.isEmpty())
			return EvaluationUtils.message("No evaluation found.");
		
		/*
		 * 2. Generate summary in JSON
		 */
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
		
		/*
		 * 3. Return summary
		 */
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
				/*
				 * 1. Check if the evaluation exists
				 */
				String id = null;
				
				//Get wanted values, trim the values and ignore noises
				List<String> keys = Arrays.asList(new String[]{"host", "tester", "model", "measurement", "collection_id"});
				Map<String, Object> data = EvaluationUtils.extractValues(
						(Map<String, Object>)params.get("evaluate"), keys);
				Map<String, Object> conds = new HashMap<String, Object>();
				conds.putAll(data);
				conds.remove("tester");
				Set<Map<String, Object>> rs = handler.select("EVALUATION", conds);
				
				/*
				 * 2.Check if there is a similar evaluation being done. 
				 */
				if(rs.isEmpty()){
					/*
					 * 3(1). If not, parse input data and generate evaluation info and update to DB
					 */
					id = UUID.randomUUID().toString().replaceAll("-", "");
					// TODO Get corpus info from Collection service.
					String corpus = "N/A";
					
					data.put("id", id);
					data.put("evaluate_time", EvaluationUtils.dateFormat(new Date(System.currentTimeMillis())));
					data.put("corpus", corpus);
					handler.insert("EVALUATION", data);
				}else{
					/*
					 * 3(2). If yes, re-run the evaluation as nothing is changed.
					 */
					String tester = (String)data.get("tester");
					data = rs.iterator().next();
					data.put("tester", tester);
					data.put("evaluate_time", EvaluationUtils.dateFormat(new Date(System.currentTimeMillis())));
					id = (String)data.get("id");
					handler.updateById("EVALUATION", data, id);
				}
				
				data.put("uri", getRequest().getResourceRef().getIdentifier() + "/" + id);

				/*
				 * 3. Create repository
				 */
				File repo = new File("evaluations/" + id);
				if(!repo.exists()){
					repo.mkdir();
					downloadCollection();
				}else if(!repo.isDirectory()){
					System.err.println("The repository is not a directory");
					repo.delete();
					repo.mkdir();
					downloadCollection();
				}
				
				/*
				 * 4. Run evaluation
				 */
			
				// TODO Start a new thread and run evaluation
				
				/*
				 * 5. Return summary
				 */
				
				return new JsonRepresentation(data);
			}else
				return EvaluationUtils.message("Invalid Commands");
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
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof IOException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof JsonMappingException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof SQLException)
			System.err.print(cause.getClass().getName() + ": " + cause.getMessage());
		
		ex.printStackTrace();
	} 
	
	private void downloadCollection(){
		// TODO Download the test collection
	}
	
}
