package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.MeasureResource;

public class MeasureServerResource extends WadlServerResource implements MeasureResource{

	public Representation getScore() {
		return new StringRepresentation(
				"return score by this measurement.");
	}

}
