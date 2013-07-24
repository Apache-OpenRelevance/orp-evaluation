package org.orp.eval.server;

import org.orp.eval.common.RootResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class RootServerResource extends WadlServerResource implements RootResource{

	public Representation getHomepage() {
		return new StringRepresentation("Show the homepage of ORP");
	}

}
