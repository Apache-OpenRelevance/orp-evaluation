package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface ResultResource {
	
	/**
	 * 
	 * @return scores by common measurements such as precision/recall, mean
	 * reciprocal rank, etc. And also the scoring model. 
	 */
	@Get
	public Representation summary();
	
	/**
	 * 
	 * @param accept a JSON/XML document indicating showing all the available
	 * measurements.
	 * 
	 * @return all the measurements and their IDs, names and descriptions.
	 */
	@Post
	public Representation execute(Representation data);
	
}
