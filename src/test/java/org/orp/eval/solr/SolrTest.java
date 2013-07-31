package org.orp.eval.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import junit.framework.TestCase;

public class SolrTest extends TestCase{
	
	public void testSolr(){
		SolrServer server = new HttpSolrServer("http://localhost:9090");
	}
}
