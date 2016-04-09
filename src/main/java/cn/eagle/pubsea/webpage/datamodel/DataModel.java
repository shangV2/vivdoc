package cn.eagle.pubsea.webpage.datamodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DataModel {
	public final static String defaultScoreFileName = "scores.o";
	private String scoreFileName = "/tmp/" + defaultScoreFileName;

	private static boolean debug = false;

	private ConcurrentHashMap<String, Integer> statMap = new ConcurrentHashMap<>();
	private int totalFileNum = 0;

	private final static int defaultTopN = 5;
	private int topKeyNum = defaultTopN;

	private String pageDir = null;

	public static int DEBUG_LEVEL = 0;
	public static int INFO_LEVEL = 1;
	public static int ERROR_LEVEL = 2;
	public static void reportMsg(String msg, int level){
		if (level > DEBUG_LEVEL || debug) {
			System.out.println(msg);
		}
	}

	public DataModel() {}

	public DataModel(String pageDir, String modelFile, int topN, boolean debugMode) {
		this.pageDir = pageDir;
		this.scoreFileName = modelFile;
		this.topKeyNum = topN;
		debug = debugMode;
	}

	/*
	 * build data model
	 */
	public static void main(String[] args) {
		DataModel dm = new DataModel();

		if (args.length < 1) {
			reportMsg("Usage:", INFO_LEVEL);
			reportMsg("build model: DataModel -b -d webpage_path [-debug]", INFO_LEVEL);
			reportMsg("calc topN key words: DataModel [-n topN] [-m model_file] [-c] -d webpage_path [-debug]", INFO_LEVEL);
			reportMsg("build & calc: DataModel [-n topN] [-m model_file] -b -c -d webpage_path [-debug]", INFO_LEVEL);
			return;
		}

		boolean buildMode = false;
		boolean calcMode = false;
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-b":
				buildMode = true;
				break;

			case "-c":
				calcMode = true;
				break;

			case "-d":
				dm.pageDir = args[++i];
				break;

			case "-n":
				dm.topKeyNum = Integer.parseInt(args[++i]);
				break;

			case "-m":
				dm.scoreFileName = args[++i];
				break;

			case "-debug":
				debug = true;
				break;

			default:
				break;
			}
		}

		File dir = new File(dm.pageDir);
		if (buildMode && calcMode) {
//			deploy model
			dm.buildAndCalc(dir);
		}else if (buildMode) {
//			build model
			dm.buildMode(dir);
		}else{
//			calc topN key words
			dm.calcKeywords(dir);
		}
	}

	/*
	 * build the model on dir, then calc topN keywords in the same dir
	 */
	public void buildAndCalc(File dir){
		buildMode(dir);
		calcKeywords(dir);
	}

	/*
	 * calc keywords in dir
	 */
	public void calcKeywords(File dir) {
		parseDirectory(dir, new LocalLineHandler(), null, new FileParseHandler());
	}

	/*
	 * build model in dir
	 */
	public void buildMode(File dir) {
		if (dir.isDirectory() && dir.exists()) {
			parseDirectory(dir, new GlobalLineHandler(), null, null);
			(new DirParseHandler()).handle(statMap, totalFileNum, scoreFileName);
		}else{
			reportMsg("directory don's exist", ERROR_LEVEL);
		}
	}

	private void parseDirectory(File dir, LineHandler handler, DirParseHandler dirParseHandler, FileParseHandler fileParseHandler) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					parseDirectory(file, handler, dirParseHandler, fileParseHandler);
				}else{
					parseFile(file, handler, fileParseHandler);
				}
			}

			if (dirParseHandler != null) {
				dirParseHandler.handle(statMap, totalFileNum, scoreFileName);
			}
		}else{
			parseFile(dir, handler, fileParseHandler);
		}
	}

	private void parseFile(File file, LineHandler lineHandler, FileParseHandler fileParseHandler) {
		try {
			reportMsg("start parse file, file:" + file.getName() + " fileNum:" + totalFileNum, DEBUG_LEVEL);

			HashMap<String, Integer> localExistMap = new HashMap<>();

			String line = null;
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			totalFileNum++;

			while((line = bReader.readLine()) != null){
				lineHandler.handle(line, localExistMap, statMap);
			}

			bReader.close();

			if (fileParseHandler != null) {
				fileParseHandler.handle(localExistMap, file.getAbsolutePath(), scoreFileName, topKeyNum);
			}

			reportMsg("end parse file, file:" + file.getName() + " fileNum:" + totalFileNum, DEBUG_LEVEL);
		} catch (IOException e) {
			reportMsg("file read error, file:" + file.getAbsolutePath() + " msg:" + e.getMessage(), ERROR_LEVEL);
		}
	}

	public String getScoreFilePath() {
		return scoreFileName;
	}

	public int getTopN() {
		return topKeyNum;
	}
}
