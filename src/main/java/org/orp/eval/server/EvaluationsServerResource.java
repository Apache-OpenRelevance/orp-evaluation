package org.orp.eval.server;

import org.orp.eval.common.EvaluationsResource;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class EvaluationsServerResource extends WadlServerResource implements EvaluationsResource{

	public Representation list() {
		return null;
	}

	public Representation run(JsonRepresentation entity) {
		return null;
	}

	
}
