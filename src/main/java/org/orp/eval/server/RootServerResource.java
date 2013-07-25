package org.orp.eval.server;

import org.orp.eval.common.RootResource;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class RootServerResource extends WadlServerResource implements RootResource{

	public Representation present() {
		return null;
	}


}
