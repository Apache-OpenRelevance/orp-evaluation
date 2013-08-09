package org.orp.eval.server;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.httpclient.HttpException;
import org.orp.eval.common.ConfigResource;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;

public class ConfigServerResource extends WadlServerResource implements ConfigResource{

	@Override
	public Representation present() throws SQLException, HttpException,
			IOException {
		return null;
	}

	@Override
	public Representation execute(JsonRepresentation entity) {
		return null;
	}

}
