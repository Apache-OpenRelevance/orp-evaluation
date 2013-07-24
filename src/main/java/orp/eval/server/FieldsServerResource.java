package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.FieldsResource;

public class FieldsServerResource extends WadlServerResource implements FieldsResource{

	public Representation getFields() {
		return new StringRepresentation("return all the fields specified in the schema");
	}

}
