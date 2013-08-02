package org.orp.eval.trec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrecEvaluator {
	private String qrels;
	private String results;
	
	public TrecEvaluator(String pathToQrels, String pathToResults){
		qrels = pathToQrels;
		results = pathToResults;
	}
	
	public String evaluateAll() 
			throws IOException, InterruptedException{
		String trecEval = findTrecEval();
		if(trecEval.isEmpty())
			throw new RuntimeException("No trec_eval installation found!");
		else{
			Process p = Runtime.getRuntime().exec(trecEval + " -q " + qrels + " " + results);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder r = new StringBuilder();
			while(br.ready()){
				String line = br.readLine();
				r.append(line + "\n");
//				String[] fields = line.split("[ \t]+");
//				r.append(fields[0] + ": " + fields[2] + "\n");
			}
			results = r.toString();

			br.close();
			return results;
		}
	}
	
	public String evaluateQuery(int tid) 
			throws IOException, InterruptedException{
		String trecEval = findTrecEval();
		if(trecEval.isEmpty())
			throw new RuntimeException("No trec_eval installation found!");
		else{
			Process p = Runtime.getRuntime().exec(trecEval + " -q " + qrels + " " + results);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder r = new StringBuilder();
			while(br.ready()){
				String line = br.readLine();
				String[] fields = line.split("[ \t]+");
				if(fields[1].equals(String.valueOf(tid)))
					r.append(fields[0] +  ": " + fields[2] + "\n");
			}
			results = r.toString();
			br.close();
			return results;
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
