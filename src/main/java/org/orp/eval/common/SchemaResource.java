package org.orp.eval.common;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.httpclient.HttpException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface SchemaResource {
	
	/**
	 * @return the schema used in this evaluation.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Get
	public Representation present() throws SQLException, HttpException, IOException;
	
	@Post
	public Representation execute(JsonRepresentation entity);
}
