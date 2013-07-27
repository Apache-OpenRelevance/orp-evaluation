package org.orp.eval.server;

import org.orp.eval.common.CollectionResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;


public class CollectionServerResource extends WadlServerResource implements CollectionResource{

	@Get
	public Representation present() {
		return null;
	}
}
