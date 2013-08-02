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


public class TopicParser {

private File topFile;

public TopicParser(String path){
	topFile = new File(path);
}

	public Map<Integer, Topic> getTopics()
			throws XMLStreamException, IOException{
		Map<Integer, Topic> topMap = new HashMap<Integer, Topic>();
		if(topFile.isDirectory()){
			File[] files = topFile.listFiles();
			for(File f : files){
				Topic t = getTopic(new FileInputStream(f));
				topMap.put(t.getId(), t);
			}
		}else if(topFile.isFile()){
			List<String> topList = ParseUtils.splitDocuments(topFile, "top");
			for(String top : topList){
				Topic t = getTopic(new ByteArrayInputStream(top.getBytes("UTF-8")));
				topMap.put(t.getId(), t);
			}
	}
	
		return topMap;
	}
	
	private Topic getTopic(InputStream is)
			throws UnsupportedEncodingException, XMLStreamException{
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLEventReader xer = xif.createXMLEventReader(is);
		Topic topic = new Topic();
	
		while(xer.hasNext()){
			XMLEvent event = xer.nextEvent();
			if(event.isStartElement()){
				StartElement se = event.asStartElement();
				if(se.getName().getLocalPart().equals("num")){
					event = xer.nextEvent();
					topic.setId(Integer.parseInt((event.asCharacters().getData().replaceAll("[^0-9]", ""))));
				}else if(se.getName().getLocalPart().equals("title")){
					event = xer.nextEvent();
					topic.setTitle(event.asCharacters().getData());
				}else if(se.getName().getLocalPart().equals("descr")){
					event = xer.nextEvent();
					topic.setDescription(event.asCharacters().getData());
				}else if(se.getName().getLocalPart().equals("narr")){
					event = xer.nextEvent();
					topic.setNarrative(event.asCharacters().getData());
				}
			}
		}

		return topic;
}

}

