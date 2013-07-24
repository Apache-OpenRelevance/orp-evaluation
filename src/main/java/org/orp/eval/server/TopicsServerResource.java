package org.orp.eval.server;

import org.orp.eval.common.TopicsResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class TopicsServerResource extends WadlServerResource implements TopicsResource{

	public Representation getTopics() {
		return new StringRepresentation(
				"return topics used in the test collection.");
	}

}
