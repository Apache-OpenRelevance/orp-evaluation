package org.orp.eval.common;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface SchemaResource {
	
	/**
	 * @return the schema used in this evaluation.
	 */
	@Get
	public Representation present();
	
	@Post
	public Representation execute(JsonRepresentation entity);
}
