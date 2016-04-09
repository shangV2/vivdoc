package datamodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DirParseHandler {
	public void handle(ConcurrentHashMap<String, Integer> statMap, int fileNum, String scoreFile) {
		HashMap<String, Double> scores = new HashMap<>();

		double totalNum = fileNum * 1.0;
		double defaultScore = Math.log(totalNum);
		Enumeration<String> keys = statMap.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			int existNum = statMap.get(key);

			double score = Math.log(totalNum/(1.0 + existNum));
			scores.put(key, score);
		}

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(scoreFile)));
			oos.writeObject(scores);
			oos.writeInt(fileNum);
			oos.writeDouble(defaultScore);
			DataModel.reportMsg("default score:" + defaultScore, DataModel.DEBUG_LEVEL);
			oos.close();
		} catch (IOException e) {
			DataModel.reportMsg("output scores error, msg:" + e.getMessage(), DataModel.ERROR_LEVEL);
		}
	}
}
