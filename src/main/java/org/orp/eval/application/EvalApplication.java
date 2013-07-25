package org.orp.eval.application;

import org.orp.eval.server.CollectionServerResource;
import org.orp.eval.server.EvaluationServerResource;
import org.orp.eval.server.EvaluationsServerResource;
import org.orp.eval.server.ResultServerResource;
import org.orp.eval.server.RootServerResource;
import org.orp.eval.server.SchemaServerResource;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.restlet.Restlet;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.routing.Router;


public class EvalApplication extends WadlApplication{
	
	public EvalApplication(){
		DBHandler handler = DBHandlerImpl.newHandler(
				"jdbc:sqlite:db/evaluation.db");
		if(!handler.exist("evaluation"))
			handler.createTable("CREATE TABLE EVALUATION(" +
					"ID VARCHAR(20) PRIMARY KEY NOT NULL," +
					"URI VARCHAR(50) NOT NULL," +
					"CREATE_TIME DATE NOT NULL," +
					"CORPUS VARCHAR(20))");
		handler.clean();
	}
	
	@Override
	public Restlet createInboundRoot(){
		Router router = new Router(getContext());
		router.attach("/", RootServerResource.class);
		router.attach("/evaluations", EvaluationsServerResource.class);
		router.attach("/evaluations/{evalId}", EvaluationServerResource.class);
		router.attach("/evaluations/{evalId}/schema", SchemaServerResource.class);
		router.attach("/evaluations/{evalId}/collection", CollectionServerResource.class);
		router.attach("/evaluations/{evalId}/result", ResultServerResource.class);
		return router;
	}
}
