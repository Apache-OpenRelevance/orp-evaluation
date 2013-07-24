package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.FieldTypesResource;

public class FieldTypesServerResource extends WadlServerResource implements FieldTypesResource{

	public Representation getFieldTypes() {
		return new StringRepresentation("return all the field types and their analyzers specified in the schema");
	}
	
}
