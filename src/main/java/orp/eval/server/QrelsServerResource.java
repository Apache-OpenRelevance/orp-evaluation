package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import orp.eval.common.QrelsResource;

public class QrelsServerResource extends WadlServerResource implements QrelsResource{

	@Get
	public Representation getQrels() {
		return new StringRepresentation("return qrels used in the test collection");
	}

}
