package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface FieldsResource {

	/**
	 * 
	 * @return all the fields specified in the schema
	 */
	
	@Get
	public Representation getFields();
}
