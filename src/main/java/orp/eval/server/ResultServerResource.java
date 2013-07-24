package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.ResultResource;

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
