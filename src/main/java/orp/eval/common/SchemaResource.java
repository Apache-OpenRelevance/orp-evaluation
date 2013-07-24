package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface SchemaResource {
	
	/**
	 * @return the schema used in this evaluation.
	 */
	@Get
	public Representation getSchema();
}
