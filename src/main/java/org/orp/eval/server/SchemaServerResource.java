package org.orp.eval.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.orp.eval.common.SchemaResource;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.orp.eval.utils.EvaluationUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;


public class SchemaServerResource extends WadlServerResource implements SchemaResource{

	private DBHandler handler;
	private String id;
	
	@Override
	public void doInit(){
		handler = DBHandlerImpl.newHandler("jdbc:sqlite:db/evaluation.db");
		id = getRequest().getResourceRef().getIdentifier().split("/")[4];
	}
	
	public Representation present() 
			throws SQLException, HttpException, IOException {
		Map<String, Object> info = handler.selectAllById("EVALUATION", id);
		String host = (String)info.get("host");
		host += "/solr/schema";
		String schema = getResponse(host);
		if(schema == null)
			return EvaluationUtils.message("No schema return. Please check your host URL.");
		return new JsonRepresentation(schema);
	}

	public Representation execute(JsonRepresentation entity) {
		return null;
	}
	
	@Override
	public void doRelease(){
		
	}
	
	@Override
	public void doCatch(Throwable ex){
		Throwable cause = ex.getCause();
		if(cause instanceof SQLException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof HttpException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		if(cause instanceof IOException)
			System.out.println(cause.getClass().getName() + ": " + cause.getMessage());
		
		ex.printStackTrace();
	}

	private String getResponse(String url) 
			throws HttpException, IOException{
		GetMethod get = new GetMethod(url);
		new HttpClient().executeMethod(get);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] arr = new byte[1024];
		int count = 0;
		while((count = get.getResponseBodyAsStream()
				.read(arr, 0, arr.length)) > 0)
			os.write(arr, 0, count);
		return new String(os.toByteArray(), "UTF-8");
	}
}
