package cn.eagle.pubsea.webpage.datamodel;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface LineHandler {
	public void handle(String line, HashMap<String, Integer> localMap, ConcurrentHashMap<String, Integer> globalMap);
}
