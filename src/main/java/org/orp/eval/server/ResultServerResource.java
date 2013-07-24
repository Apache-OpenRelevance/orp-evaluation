package org.orp.eval.server;

import org.orp.eval.common.ResultResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class ResultServerResource extends WadlServerResource implements ResultResource{

	public Representation getResult() {
		return new StringRepresentation(
				"return scores by common measurements such as precision/recall" +
				", mean reciprocal rank, etc. And also the scoring model.");
	}

	public Representation getMetrics(Representation data) {
		return new StringRepresentation(
				"accept a JSON/XML document indicating showing all the available " +
				"measurements. Return all the measurements and their IDs, names and descriptions.");
	}

}
