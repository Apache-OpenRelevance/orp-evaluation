package org.orp.eval.server;

import org.orp.eval.common.EvaluationResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class EvaluationServerResource extends WadlServerResource implements EvaluationResource{

	public Representation present() {
		return null;
	}

	public Representation execute() {
		return null;
	}

}
