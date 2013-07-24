package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface FieldTypesResource {
	/**
	 * 
	 * @return all the field types and their analyzers specified in the schema
	 */
	@Get
	public Representation getFieldTypes();
}
