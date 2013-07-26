package org.orp.eval.component;



import org.orp.eval.application.EvalApplication;
import org.restlet.data.Protocol;
import org.restlet.ext.wadl.WadlComponent;


public class EvalComponent extends WadlComponent{
	public static void main(String[] args){
		try {
			new EvalComponent().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EvalComponent(){
		getServers().add(Protocol.HTTP, 8112);
		try{
			getDefaultHost().attachDefault(new EvalApplication());
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
