package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface EvaluationsResource {

	/**
	 * 
	 * @return list all the evaluations and their metadata
	 */
	@Get
	public Representation getEvaluations();
}
