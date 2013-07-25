package org.orp.eval.common;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface EvaluationsResource {

	/**
	 * 
	 * @return all the evaluations, with their IDs, timestamps, host URLs,  
	 * measurements, scores, tester's name, test collection IDs, schema URIs, results URIs. 
	 */
	@Get
	public Representation list();
	
	/**
	 * @param host URL, test collection ID, measurement, tester's name
	 * @return evaluation ID, timestamp, test collection, corpus, score 
	 */
	@Post
	public Representation run(JsonRepresentation entity);
}