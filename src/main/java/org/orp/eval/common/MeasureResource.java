package org.orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface MeasureResource {

	/**
	 * 
	 * @return score by this measurement.
	 */
	@Get
	public Representation getScore();
}
