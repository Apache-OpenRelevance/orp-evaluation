package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.CollectionResource;

public class CollectionServerResource extends WadlServerResource implements CollectionResource{

	public Representation getCollection() {
		return new StringRepresentation(
				"Test collections are stored and managed in " +
				"another service. In this service, clients " +
				"just need to provide with access to the collection they need.<br> " +
				"return the access to test collection used in this evaluation and " +
				"its description");
	}

}
