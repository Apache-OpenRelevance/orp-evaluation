package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface QrelsResource {
	/**
	 * 
	 * @return Qrels used in the test collection
	 */
	
	@Get
	public Representation getQrels();
}
