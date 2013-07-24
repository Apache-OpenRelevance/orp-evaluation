package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface TopicsResource {
	/**
	 * 
	 * @return topics used in the test collection. 
	 */
	@Get
	public Representation getTopics();
}
