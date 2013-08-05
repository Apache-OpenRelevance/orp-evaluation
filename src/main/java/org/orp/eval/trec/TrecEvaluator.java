package org.orp.eval.trec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.orp.eval.utils.DBHandler;

public class TrecEvaluator {
	private String qrels;
	private String results;
	private DBHandler handler;
	
	public TrecEvaluator(String pathToQrels, String pathToResults, DBHandler handler){
		qrels = pathToQrels;
		results = pathToResults;
		this.handler = handler;
	}
	
	public Set<Map<String, Object>> evaluateAll() 
			throws IOException, InterruptedException, SQLException{
		String trecEval = findTrecEval();
		if(trecEval.isEmpty())
			throw new RuntimeException("No trec_eval installation found!");
		else{
			Process p = Runtime.getRuntime().exec(trecEval + " -q " + qrels + " " + results);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
			Map<String, Object> row = new HashMap<String, Object>();
			int count = 0;
			while(br.ready()){
				String line = br.readLine();
				String[] field = line.split("[ \t]+");
				if(count == 0) row.put("topic_no", field[1]);
				row.put(field[0].toLowerCase().replaceAll("[-.]+", "_"), field[2]);
				count ++;
				if(count == 27){
					result.add(row);
					handler.insert("SCORES", row);
					count = 0;
					row = new HashMap<String, Object>();
				}
			}
			
			br.close();
			return result;
		}
	}
	
	public String findTrecEval() 
			throws IOException, InterruptedException{
		String trecEval = "trec/trec_eval";
		File trecEvalExe = new File(trecEval);
		if(trecEvalExe.exists() && trecEvalExe.isFile() && trecEvalExe.canExecute())
			return trecEval;
		
		Process p = Runtime.getRuntime().exec("locate trec_eval");
		p.waitFor();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		trecEval = "";
		while(br.ready()){
			String line = br.readLine();
			if(new File(line).isFile() && line.endsWith("trec_eval")){
				trecEval = line;
				break;
			}
		}
		return trecEval;
	}
}