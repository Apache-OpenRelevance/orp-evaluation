package org.orp.eval.server;

import org.orp.eval.common.FieldsResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class FieldsServerResource extends WadlServerResource implements FieldsResource{

	public Representation getFields() {
		return new StringRepresentation("return all the fields specified in the schema");
	}

}
