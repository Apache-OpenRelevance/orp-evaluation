package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface CollectionResource {

	/**
	 * Test collections are stored and managed in another service. In this 
	 * service, clients just need to provide with access to the collection they
	 * need.<br>
	 * 
	 * @return the access to test collection used in this evaluation and
	 * its description
	 */
	@Get
	public Representation getCollection();
}
