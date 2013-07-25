package org.orp.eval.common;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
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
	public Representation present();
	
	/**
	 * @param command-data combinations, including:
	 * 1.re-run: re-run the evaluation, potentially with different test collection or 
	 * scoring model.If running with a new collection, a data block like 
	 * {"collectionId":"[collection ID]", "scoring-model":"[scoring-model name]"} is 
	 * required. If the collection stays same, type in "unchanged" instead of a 
	 * collection ID. The timestamp will be automatically updated. 
	 * 
	 * 2.check-progress: check the progress of evaluation. Send a JSON data block like
	 * {"check-progress":""}. Any data following the key will be ignored. 
	 * 
	 * 
	 * @return 
	 * 1. re-run: an updated summary of the evaluation, as is returned by calling GET
	 * method. 
	 * 2. check-progress: evaluation status. 
	 */
	@Post
	public Representation execute(JsonRepresentation entity);
	
}
