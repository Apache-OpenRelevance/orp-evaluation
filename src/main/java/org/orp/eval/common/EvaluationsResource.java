package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface EvaluationsResource {

	/**
	 * 
	 * @return list all the evaluations and their metadata
	 */
	@Get
	public Representation list();
	
	@Post
	public Representation run();
}