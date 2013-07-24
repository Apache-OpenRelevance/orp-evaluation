package org.orp.eval.application;

import org.orp.eval.server.AccountServerResource;
import org.orp.eval.server.CollectionServerResource;
import org.orp.eval.server.EvaluationServerResource;
import org.orp.eval.server.EvaluationsServerResource;
import org.orp.eval.server.FieldTypesServerResource;
import org.orp.eval.server.FieldsServerResource;
import org.orp.eval.server.MeasureServerResource;
import org.orp.eval.server.QrelsServerResource;
import org.orp.eval.server.ResultServerResource;
import org.orp.eval.server.ResultsFileServerResource;
import org.orp.eval.server.RootServerResource;
import org.orp.eval.server.SchemaServerResource;
import org.orp.eval.server.TRECServerResource;
import org.orp.eval.server.TopicsServerResource;
import org.restlet.Restlet;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.routing.Router;


public class EvalApplication extends WadlApplication{
	public EvalApplication(){
		
	}
	
	@Override
	public Restlet createInboundRoot(){
		Router router = new Router(getContext());
		router.attach("/", RootServerResource.class);
		router.attach("/{accountId}", AccountServerResource.class);
		router.attach("/{accountId}/evaluations", EvaluationsServerResource.class);
		router.attach("/{accountId}/trec", TRECServerResource.class);
		router.attach("/{accountId}/trec/{evalId}", EvaluationServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/schema", SchemaServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/schema/fields", FieldsServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/schema/fieldtypes", FieldTypesServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/collection", CollectionServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/collection/topics", TopicsServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/collection/qrels", QrelsServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/result", ResultServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/result/{measureId}", MeasureServerResource.class);
		router.attach("/{accountId}/trec/{evalId}/result/file", ResultsFileServerResource.class);
		return router;
	}
}
