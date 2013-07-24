package org.orp.eval.server;

import org.orp.eval.common.EvaluationResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class EvaluationServerResource extends WadlServerResource implements EvaluationResource{

	public Representation getSummary() {
		return new StringRepresentation(
				"a summary of each evaluation, including id, time stamp(id?)," 
			 + "URL of evaluated search engine, schema access, test "
			 + "collection access, result access and scores(if any).");
	}

	public Representation run(Representation request) {
		return new StringRepresentation(
					"accept a JSON/XML document from client containing:" 
				 +  "1. URL of the evaluated search engine." 
				 +	"2. access to the test collection." 
				 +  "3. measurement(OPTIONAL)" 
				 + "Evaluation will fail if the test collection" 
				 +	"is not available in the repository.");
	}

}
