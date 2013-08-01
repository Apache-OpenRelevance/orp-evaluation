package org.orp.eval.common;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.compress.compressors.CompressorException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface EvaluationsResource {

	/**
	 * 
	 * @return all the evaluations, with their IDs, timestamps, host URLs,  
	 * measurements, scores, tester's name, test collection IDs, schema URIs, 
	 * results URIs. 
	 */
	@Get
	public Representation list()
		throws SQLException;
	
	/**
	 * @param host URL, model of search engine, test collection ID, measurement, tester's name
	 * @return evaluation ID, timestamp, URI, test collection, corpus 
	 */
	@Post
	public Representation run(JsonRepresentation entity) 
			throws JsonParseException, JsonMappingException, IOException, SQLException, CompressorException;
}