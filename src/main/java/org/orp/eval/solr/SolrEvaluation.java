package org.orp.eval.solr;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.httpclient.HttpException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.orp.eval.trec.TopicParser;
import org.orp.eval.trec.TrecEvaluator;
import org.orp.eval.utils.DBHandler;
import org.orp.eval.utils.DBHandlerImpl;
import org.orp.eval.utils.EvaluationUtils;
import org.orp.eval.utils.DocParseUtils;

public class SolrEvaluation{
	
	private String host;
	private String id;
	private String repo;
	
	public SolrEvaluation(String host, String id) 
			throws SQLException{
		this.host = host;
		this.id = id;
		repo = "evaluations/" + id;
	}
	
	public void eval() 
			throws HttpException, IOException{
		//Run evaluation
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					String topics = repo + "/topics.txt";
					String qrels = repo + "/qrels.txt";
					String results = repo + "/results.txt";
					String scores = repo + "/scores.txt";
					DBHandler handler = DBHandlerImpl.newHandler(
							"jdbc:sqlite:evaluations/" + id + "/stat.db");
					
					TopicParser parser = new TopicParser(topics);
					Set<Integer> tidSet = DocParseUtils.parseQrel(qrels);
					SolrQueryMaker queryMaker = new SolrQueryMaker(new HttpSolrServer(host), results);
					queryMaker.writeResults(tidSet, parser.getTopics());
					TrecEvaluator evaluator = new TrecEvaluator(qrels, results, handler);
					PrintWriter writer = new PrintWriter(new File(scores));
					writer.print(evaluator.evaluateAll());
					
					writer.flush();
					writer.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
		//Get files
		EvaluationUtils.fetchFile(host + "/admin/file/?charset=utf-8&file=schema.xml", 
			"evaluations/" + id + "/schema.xml");
		EvaluationUtils.fetchFile(host + "/admin/file/?charset=utf-8&file=solrconfig.xml",
			"evaluations/" + id + "/config.xml");
		EvaluationUtils.fetchFile(host + "/admin/file/?charset=utf-8&file=stopwords.txt",
			"evaluations/" + id + "/stopwords.txt");
	}
	
}
