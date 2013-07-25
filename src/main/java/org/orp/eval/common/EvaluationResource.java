package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface EvaluationResource {
	/***
	 * 
	 * @return a summary of each evaluation, including id, time stamp(id?), 
	 * URL of evaluated search engine, schema access, test collection access,
	 * result access and scores(if any).  
	 */
	
	@Get
	public Representation present();
	
	@Post
	public Representation execute();
	
}
