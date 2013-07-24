package org.orp.eval.server;

import org.orp.eval.common.ResultsFileResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class ResultsFileServerResource extends WadlServerResource implements ResultsFileResource{

	public Representation getResultsFile() {
		return new StringRepresentation(
				"return the searching results file generated by the evaluator.");
	}

}