package cn.eagle.pubsea.webpage.datamodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FileParseHandler {
	private static HashMap<String, Double> scoreMap = null;
	private static double defaultScore = 0.0;

	public void handle(HashMap<String, Integer> statMap, String fileName, String scoreFile, int topN) {
		if (scoreMap != null || (scoreMap == null && loadScoreMap(scoreFile))) {
			List<WordScore> wList = new ArrayList<>();
			Set<String> keys = statMap.keySet();
			for (String key : keys) {
				double score = statMap.get(key) * (scoreMap.containsKey(key) ? scoreMap.get(key) : defaultScore);
//				sort and print topN
				wList.add(new WordScore(key, score));
			}

			Collections.sort(wList);

			StringBuilder topNWrods = new StringBuilder();
			Iterator<WordScore> iterator = wList.iterator();
			for(int i = 0; i < topN && iterator.hasNext(); i++){
				WordScore ws = iterator.next();
				topNWrods.append(ws.getWord() + ":" + ws.getScore() + ",");
			}

			DataModel.reportMsg("FileName:" + fileName + " topN:" + topNWrods.toString(), DataModel.INFO_LEVEL);
		}
	}

	private static boolean loadScoreMap(String modelFile) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(modelFile)));
			scoreMap = (HashMap<String, Double>) ois.readObject();
			int totalFileNum = ois.readInt();
			defaultScore = ois.readDouble();
			ois.close();
			return true;
		} catch (IOException | ClassNotFoundException e) {
			DataModel.reportMsg("load score map failed, msg:" + e.getMessage(), DataModel.ERROR_LEVEL);
			return false;
		}
	}
	
	class WordScore implements Comparable<WordScore>{
		String word;
		double score;

		public WordScore(String word, double score){
			this.word = word;
			this.score = score;
		}

		public String getWord(){
			return word;
		}
		public void setWord(String word){
			this.word = word;
		}
		public double getScore(){
			return score;
		}
		public void setScore(double score){
			this.score = score;
		}

		public int compareTo(WordScore w2){
			return (this.score > w2.getScore()) ? -1 : (this.score == w2.score) ? 0 : 1;
		}
	}
}
