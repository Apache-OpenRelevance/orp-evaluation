package org.orp.eval.common;

import java.io.IOException;
import java.sql.SQLException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface EvaluationResource {
	/***
	 * 
	 * @return evaluation ID, timestamp, host URL, test collection ID, corpus,
	 * collection name, collection URI, schema URI, result URI, scoring model,
	 * measurement and score by this measurement.  
	 */
	
	@Get
	public Representation present()
		throws SQLException;
	
	/**
	 * @param command-data combinations, including:
	 * 1.re-run: re-run the evaluation, potentially with different test collection or 
	 * scoring model.If running with a new collection, a data block like 
	 * {"collectionId":"[collection ID]", "scoring-model":"[scoring-model name]", "tester":
	 * "[tester name](optional))} is required. The timestamp will be automatically 
	 * updated. 
	 * 
	 * 2.check-progress: check the progress of evaluation. Send a JSON data block like
	 * {"check-progress":""}. Any data following the key will be ignored. 
	 * 
	 * 
	 * @return 
	 * 1. re-run: an updated summary of the evaluation, as is returned by calling GET
	 * method. 
	 * 2. check-in: evaluation status. 
	 */
	@Post("json:json")
	public Representation execute(JsonRepresentation entity)
			throws JsonParseException, JsonMappingException, IOException, SQLException;
	
	@Delete
	public Representation remove() throws SQLException;
	
}
