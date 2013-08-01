package org.orp.eval.server;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.httpclient.HttpException;
import org.orp.eval.common.SchemaResource;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.orp.eval.utils.EvaluationUtils;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.ext.xml.SaxRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;


public class SchemaServerResource extends WadlServerResource implements SchemaResource{

	private DBHandler handler;
	private String id;
	private File schema;
	
	@Override
	public void doInit(){
		handler = DBHandlerImpl.newHandler("jdbc:sqlite:db/evaluation.db");
		id = getRequest().getResourceRef().getIdentifier().split("/")[4];
		schema = new File("evaluations/" + id + "/schema.xml");
		
	}
	
	public Representation present() 
			throws SQLException, HttpException, IOException {
		String model = (String)handler.selectAllById("EVALUATION", id).get("model");
		if(model.equals("solr")){
			if(!schema.exists())
				return EvaluationUtils.message("No schema found.");
			if(!schema.isFile())
				return EvaluationUtils.message("Problematic file");
			return new SaxRepresentation(new FileRepresentation(schema, MediaType.APPLICATION_XML));
		}
		
		return EvaluationUtils.message("Currently no schema available for this the search engine model: " + model);
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
}
