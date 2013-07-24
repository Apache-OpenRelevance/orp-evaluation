package org.orp.eval.server;

import org.orp.eval.common.TRECResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class TRECServerResource extends WadlServerResource implements TRECResource{

	public Representation getIntro() {
		return new StringRepresentation("Show introduction of TREC evaluation.");
	}

	public Representation runTrecEval(Representation request) {
		return new StringRepresentation(
					"accept a JSON/XML document from client containing:" 
				 +  "1. URL of the evaluated search engine." 
				 +	"2. access to the test collection." 
				 +  "3. measurement(OPTIONAL)" 
				 + "Evaluation will fail if the test collection" 
				 +	"is not available in the repository.");
	}

}
