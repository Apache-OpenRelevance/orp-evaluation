package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.EvaluationsResource;

public class EvaluationsServerResource extends WadlServerResource implements EvaluationsResource{

	public Representation getEvaluations() {
		return new StringRepresentation(
				"list all the evaluations and their metadata");
	}
}
