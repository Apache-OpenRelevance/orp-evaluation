package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface CollectionResource {

	/**
	 * 
	 * @return Information about the collection
	 */
	@Get
	public Representation present();
}
