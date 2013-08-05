package org.orp.eval.trec;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.orp.eval.utils.DocParseUtils;


public class TopicParser {

private File topFile;

public TopicParser(String path){
	topFile = new File(path);
}

	public Map<Integer, Map<String, Object>> getTopics()
			throws XMLStreamException, IOException{
		Map<Integer, Map<String, Object>> topMap = new HashMap<Integer, Map<String, Object>>();
		if(topFile.isDirectory()){
			File[] files = topFile.listFiles();
			for(File f : files){
				Map<String, Object> t = getTopic(new FileInputStream(f));
				topMap.put((Integer)t.get("id"), t);
			}
		}else if(topFile.isFile()){
			List<String> topList = DocParseUtils.splitDocuments(topFile, "top");
			for(String top : topList){
				Map<String, Object> t = getTopic(new ByteArrayInputStream(top.getBytes("UTF-8")));
				topMap.put((Integer)t.get("id"), t);
			}
	}
	
		return topMap;
	}
	
	private Map<String, Object> getTopic(InputStream is)
			throws UnsupportedEncodingException, XMLStreamException{
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLEventReader xer = xif.createXMLEventReader(is);
		Map<String, Object> topic = new HashMap<String, Object>();
		
		while(xer.hasNext()){
			XMLEvent event = xer.nextEvent();
			if(event.isStartElement()){
				StartElement se = event.asStartElement();
				if(se.getName().getLocalPart().equals("num")){
					event = xer.nextEvent();
					topic.put("id", 
							Integer.parseInt((event.asCharacters().getData().replaceAll("[^0-9]", ""))));
				}else if(se.getName().getLocalPart().equals("title")){
					event = xer.nextEvent();
					topic.put("title",
							event.asCharacters().getData());
				}else if(se.getName().getLocalPart().equals("descr")){
					event = xer.nextEvent();
					topic.put("description",
							event.asCharacters().getData());
				}else if(se.getName().getLocalPart().equals("narr")){
					event = xer.nextEvent();
					topic.put("narrative", 
							event.asCharacters().getData());
				}
			}
		}

		return topic;
}

}

