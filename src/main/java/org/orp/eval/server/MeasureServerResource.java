package org.orp.eval.server;

import org.orp.eval.common.MeasureResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class MeasureServerResource extends WadlServerResource implements MeasureResource{

	public Representation getScore() {
		return new StringRepresentation(
				"return score by this measurement.");
	}

}
