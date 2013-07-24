package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.SchemaResource;

public class SchemaServerResource extends WadlServerResource implements SchemaResource{

	public Representation getSchema() {
		return new StringRepresentation("return the schema used in this evaluation");
	}

}
