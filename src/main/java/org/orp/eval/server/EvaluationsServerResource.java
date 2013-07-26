package org.orp.eval.server;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
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
	
	public Representation list() {
		Set<Map<String, Object>> rs = handler.selectAll("EVALUATION");
		JSONArray evals = new JSONArray();
		String prefix = getRequest().getReferrerRef().getIdentifier();
		for(Map<String, Object> key : rs){
			key.put("uri", prefix + "/" + key.get("id"));
			evals.put(key);
		}
		Map<String, JSONArray> result = new HashMap<String, JSONArray>();
		result.put("evaluations", evals);	
		return new JsonRepresentation(result);
	}

	public Representation run(JsonRepresentation entity) {
		String cmd = null;
		try {
			Map<String, Object> params = JsonUtils.toMap(entity);
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>)params.get("evaluate");
			cmd = params.keySet().iterator().next();
			if(cmd.equals("evaluate")){
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				String prefix = getRequest().getResourceRef().getIdentifier();
				String uri = prefix + "/" + id;
				String timestamp = EvaluationUtils.dateFormat(new Date(System.currentTimeMillis()));
				// TODO Get from Collection service.
				String corpus = "N/A";
				
				data.put("id", id);
				data.put("evaluate_time", timestamp);
				data.put("corpus", corpus);
				handler.insert("EVALUATION", data);
				
				data.put("uri", uri);
				
				return new JsonRepresentation(data);
			}else
				return EvaluationUtils.message("Invalid Commands");
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
}
