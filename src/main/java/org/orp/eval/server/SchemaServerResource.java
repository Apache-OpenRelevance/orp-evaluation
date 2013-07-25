package org.orp.eval.server;

import org.orp.eval.common.SchemaResource;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class SchemaServerResource extends WadlServerResource implements SchemaResource{

	public Representation present() {
		return null;
	}

	public Representation execute(JsonRepresentation entity) {
		return null;
	}

}
