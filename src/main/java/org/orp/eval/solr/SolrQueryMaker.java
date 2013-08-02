package org.orp.eval.solr;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class SolrQueryMaker {
	
	private SolrServer solr;
	private String resultsFile;
	
	public SolrQueryMaker(SolrServer solr, String resultsFile){
		this.solr = solr;
		this.resultsFile = resultsFile;
	}
	
	public SolrDocumentList query(int tid, Map<Integer, Map<String, Object>> topMap) 
			throws SolrServerException, FileNotFoundException{
		String q = String.valueOf(topMap.get(tid).get("title")).trim();

		String[] subqs = q.split(" ");
		StringBuilder query = new StringBuilder("(" + q + ")||(");
		for(int i = 0; i < subqs.length; i ++)
			if(i != subqs.length - 1)
				query.append(subqs[i] + "||");
			else
				query.append(subqs[i] + ")");
		query.append("||(");
		for(int i = 0; i < subqs.length; i ++)
			if(i != subqs.length - 1)
				query.append(subqs[i] + "&&");
			else
				query.append(subqs[i] + ")");
		
		SolrQuery params = new SolrQuery();
		params.setFields("docno", "score");
		params.set("defType", "edismax");
		params.set("q", query.toString());
		params.set("qf", "headline^5 body^10");
		params.set("sort", "score desc");
		params.setRows(14000);

		System.out.println(params.toString());
		QueryResponse response = solr.query(params);
		if(response == null) 
			throw new RuntimeException("Shouldn't be null");
		return response.getResults();
	}
	
	public void writeResults(Set<Integer> tidSet, Map<Integer,Map<String, Object>> topMap)
			throws SolrServerException, FileNotFoundException{
		PrintWriter writer = new PrintWriter(resultsFile);
		for(Integer id : tidSet){
			SolrDocumentList docList = query(id, topMap);
			for(int i = 0; i < docList.size(); i ++){
				String docno = (String)docList.get(i).getFieldValue("docno");
				Float score = (Float)docList.get(i).getFieldValue("score");
				writer.printf("%d\t%s\t%s\t%d\t%f\t%s\n", id, "Q0", docno, i, score, "STANDARD" );
			}
		}
		
		writer.close();
	}
}
