package org.orp.eval.server;

import org.orp.eval.common.QrelsResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;


public class QrelsServerResource extends WadlServerResource implements QrelsResource{

	@Get
	public Representation getQrels() {
		return new StringRepresentation("return qrels used in the test collection");
	}

}
