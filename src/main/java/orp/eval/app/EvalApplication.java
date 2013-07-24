package orp.eval.app;

import org.restlet.Restlet;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.routing.Router;

import orp.eval.server.AccountServerResource;
import orp.eval.server.CollectionServerResource;
import orp.eval.server.EvaluationServerResource;
import orp.eval.server.EvaluationsServerResource;
import orp.eval.server.FieldTypesServerResource;
import orp.eval.server.FieldsServerResource;
import orp.eval.server.MeasureServerResource;
import orp.eval.server.QrelsServerResource;
import orp.eval.server.ResultServerResource;
import orp.eval.server.ResultsFileServerResource;
import orp.eval.server.RootServerResource;
import orp.eval.server.SchemaServerResource;
import orp.eval.server.TRECServerResource;
import orp.eval.server.TopicsServerResource;

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
