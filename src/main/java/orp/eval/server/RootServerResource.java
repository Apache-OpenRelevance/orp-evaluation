package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.RootResource;

public class RootServerResource extends WadlServerResource implements RootResource{

	public Representation getHomepage() {
		return new StringRepresentation("Show the homepage of ORP");
	}

}
