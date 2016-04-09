package cn.eagle.pubsea.webpage.datamodel;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalLineHandler implements LineHandler{

	@Override
	public void handle(String line, HashMap<String, Integer> localMap, ConcurrentHashMap<String, Integer> globalMap) {
		line = FileParser.parse(line);

		String[] words = line.split("\\s");
		for (String word : words) {
			if (word.length() < 1 || localMap.containsKey(word)) {
				continue;
			}

//			put word to statistics map
			if (globalMap.containsKey(word)) {
				globalMap.put(word, globalMap.get(word) + 1);
			}else{
				globalMap.put(word, 1);
			}

			localMap.put(word, 1);
		}
	}

}
