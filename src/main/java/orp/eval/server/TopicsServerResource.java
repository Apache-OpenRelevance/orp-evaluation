package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.TopicsResource;

public class TopicsServerResource extends WadlServerResource implements TopicsResource{

	public Representation getTopics() {
		return new StringRepresentation(
				"return topics used in the test collection.");
	}

}
