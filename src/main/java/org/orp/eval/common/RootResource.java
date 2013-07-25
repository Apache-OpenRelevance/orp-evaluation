package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface RootResource {
	
	/***
	 * 
	 * @return the homepage of Open Relevance Project
	 */
	@Get
	public Representation present();
}

