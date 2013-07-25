package org.orp.eval.server;

import org.orp.eval.common.ResultResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class ResultServerResource extends WadlServerResource implements ResultResource{

	public Representation summary() {
		return null;
	}

	public Representation execute(Representation data) {
		return null;
	}


}
