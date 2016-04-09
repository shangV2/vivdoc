package cn.eagle.pubsea.webpage.datamodel;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LocalLineHandler implements LineHandler {

	@Override
	public void handle(String line, HashMap<String, Integer> localMap, ConcurrentHashMap<String, Integer> globalMap) {
		line = FileParser.parse(line);

		String[] words = line.split("\\s");
		for (String word : words) {
			if (word.length() < 1) {
				continue;
			}

			if (localMap.containsKey(word)) {
				localMap.put(word, localMap.get(word) + 1);
			}else{
				localMap.put(word, 1);
			}
		}
	}

}
