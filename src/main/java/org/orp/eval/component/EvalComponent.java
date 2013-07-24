package org.orp.eval.component;

import org.orp.eval.application.EvalApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;


public class EvalComponent extends Component{
	public static void main(String[] args){
		try {
			new EvalComponent().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EvalComponent(){
		getServers().add(Protocol.HTTP, 8090);
		getDefaultHost().attachDefault(new EvalApplication());
	}
}
