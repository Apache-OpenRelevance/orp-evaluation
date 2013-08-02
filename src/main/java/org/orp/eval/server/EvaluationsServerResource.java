package org.orp.eval.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.orp.eval.common.EvaluationsResource;
import org.orp.eval.config.EvalConfig;
import org.orp.eval.solr.SolrEval;
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
			throws JsonParseException, JsonMappingException, IOException, SQLException, CompressorException{
			if(entity == null)
				return EvaluationUtils.message("No data available.");
			Map<String, Object> params = JsonUtils.toMap(entity);
			String cmd = params.keySet().iterator().next().toLowerCase();
			if(cmd.equals("evaluate")){
				//1. Check if the evaluation exists
				String id = null;
				
				//Get wanted values, trim the values and ignore noises
				List<String> keys = Arrays.asList(new String[]{"host", "tester", "model", "measurement", "collection_id"});
				Map<String, Object> data = EvaluationUtils.extractValues(
						(Map<String, Object>)params.get("evaluate"), keys);
				
				//Determine if the model is supported
				String model = (String)data.get("model");
				model = model.toLowerCase();
				if(!isSupport(model))
					return EvaluationUtils.message("Currently not support search engine: " + model);
				
				//Clean data
				data.put("model", model);
				String host = (String)data.get("host");
				if(host.endsWith("/")) 
					data.put("host", host.substring(0, host.length() - 1));
				
				//2.Check if there is a similar evaluation being done. 
				Map<String, Object> conds = new HashMap<String, Object>();
				conds.putAll(data);
				conds.remove("tester");
				Set<Map<String, Object>> rs = handler.select("EVALUATION", conds);
				if(rs.isEmpty()){
				
					//3(1). If not, parse input data and generate evaluation info and update to DB
					id = UUID.randomUUID().toString().replaceAll("-", "");
					// TODO Get corpus info from Collection service.
					String corpus = "N/A";
					data.put("id", id);
					data.put("evaluate_time", EvaluationUtils.dateFormat(new Date(System.currentTimeMillis())));
					data.put("corpus", corpus);
					handler.insert("EVALUATION", data);
					data.put("uri", getRequest().getResourceRef().getIdentifier() + "/" + id);
	
					//4. Create repository
					createRepo(id);
					
					//5. Fetch test collection
					fetchCollection(id, (String)data.get("collection_id"));
					
					//6. Run evaluation
					if(model.equals("solr")){
						SolrEval solr = new SolrEval(host, id);
						solr.eval();
					}
					
					//7. Return summary
					return new JsonRepresentation(data);
				}else{
					id = (String)rs.iterator().next().get("id");
					return EvaluationUtils.message("The new evaluation is similar with " + id);
				}
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
	
	private boolean isSupport(String model){
		if(model.equals("solr")) return true;
		return false;
	}
	
	private void fetchCollection(String eid, String cid) 
			throws HttpException, IOException, CompressorException{
		String repo = "evaluations/" + eid;
		String colUri = EvalConfig.COLLECTION_HOST + "/" + cid;
		String topics = repo + "/topics.xml";
		String qrels = repo + "/qrels.txt";
		
		fetchCompressedFile(colUri + "/topics", topics);
		fetchCompressedFile(colUri + "/qrels", qrels);
		
		
	}
	
	private void createRepo(String id){
		File repo = new File("evaluations/" + id);
		if(!repo.exists()){
			repo.mkdir();
		}else if(!repo.isDirectory()){
			System.err.println("The repository is not a directory");
			repo.delete();
			repo.mkdir();
		}
	}
	
		
	
	private void fetchCompressedFile(String uri, String localDir) 
			throws HttpException, IOException, CompressorException{
		// TODO Lots of validation to be done
		GetMethod get = new GetMethod(uri);
		new HttpClient().executeMethod(get);
		CompressorInputStream in = new CompressorStreamFactory()
			.createCompressorInputStream("gz", new BufferedInputStream(get.getResponseBodyAsStream()));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(localDir));
		
		int read = 0;
		byte[] bytes = new byte[1024];
		while((read = in.read(bytes)) != -1)
			out.write(bytes, 0, read);
		in.close();
		out.close();
	}
	
	
	
}
