package ERX.CiGanTiQu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Stems {
	HashMap<String, Word> dict;
	// 0加载所有词库失败
	// 1加载词库和形态化词库失败
	// 2加载词库和加载用户词库失败
	// 3加载词库失败
	// 4加载形态化词库和用户词库失败
	// 5加载形态化词库失败
	// 6加载用户词库失败
	// 7加载数据成功
	String dictName = "dict\\CiGan.dict";
	List<String> nounSuffixes;
	int userDict = 0;
	int ciganDict = 0;
	int noun_suffie = 0;

	/**
	 * 获取加载数据后的状态
	 * 
	 * @return 0加载所有词库失败 1加载词库和形态化词库失败 2加载词库和加载用户词库失败 3加载词库失败 4加载形态化词库和用户词库失败
	 *         5加载形态化词库失败 6加载用户词库失败 7加载数据成功
	 */
	public int getState() {
		return ciganDict * 4 + noun_suffie * 2 + userDict;
	}

	/**
	 * 初始化
	 * 
	 * @return 成功加载数据 返回true，否则返回false
	 */
	boolean initSystemDict() {
		try {
			File file = new File(getAppPath() + dictName);
			File file2 = new File(getAppPath() + "\\dict\\noun_suffixes.dict");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = "";
			String[] str;
			Word word;
			while ((temp = reader.readLine()) != null) {
				str = temp.split("#");
				word = new Word(str[0], str[1], str[2], str[3]);
				dict.put(str[0], word);
			}
			reader.close();
			reader = new BufferedReader(new FileReader(file2));
			while ((temp = reader.readLine()) != null) {
				nounSuffixes.add(temp);
			}
			reader.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		return false;
	}

	boolean initSystemDict(String dictPath) {
		try {
			File file = new File(dictPath + "\\CiGan.dict");
			File file2 = new File(dictPath + "\\noun_suffixes.dict");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = "";
			String[] str;
			Word word;
			while ((temp = reader.readLine()) != null) {
				str = temp.split("#");
				word = new Word(str[0], str[1], str[2], str[3]);
				dict.put(str[0], word);
			}
			reader.close();
			reader = new BufferedReader(new FileReader(file2));
			while ((temp = reader.readLine()) != null) {
				nounSuffixes.add(temp);
			}
			reader.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		return false;
	}

	/**
	 * 带用户词典数据的初始化函数
	 * 
	 * @param path
	 *            用户词典绝对路径
	 * @return 成功加载返回true，否则返回false
	 */
	boolean initUserDict(File userDict) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(userDict));
			String temp = "";
			String[] str;
			Word word;
			while ((temp = reader.readLine()) != null) {
				str = temp.split("#");
				word = new Word(str[0], str[1], str[2], str[3]);
				dict.put(str[0], word);
			}
			reader.close();
			File file2 = new File(getAppPath() + "\\dict\\noun_suffixes.dict");
			reader = new BufferedReader(new FileReader(file2));
			while ((temp = reader.readLine()) != null) {
				nounSuffixes.add(temp);
			}
			reader.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	void init() {
		ciganDict = initSystemDict() == true ? 1 : 0;
	}

	void init(File file) {
		userDict = initUserDict(file) == true ? 1 : 0;
	}

	/**
	 * 构造函数，数据文件在程序启动位置的dict文件目录里
	 */
	public Stems() {
		// TODO Auto-generated constructor stub
		dict = new HashMap<String, Word>();
		nounSuffixes = new ArrayList<String>();
		init();
		userDict = 1;
	}

	/**
	 * 构造函数
	 * 
	 * @param userDictPath
	 *            用户词典文件
	 */
	public Stems(File userDict) {
		// TODO Auto-generated constructor stub
		dict = new HashMap<String, Word>();
		nounSuffixes = new ArrayList<String>();
		init();
		init(userDict);
	}

	/**
	 * 构造函数
	 * 
	 * @param dictPath
	 *            词库所在的路径(文件夹路径)
	 */
	public Stems(String dictPath) {
		dict = new HashMap<String, Word>();
		nounSuffixes = new ArrayList<String>();
		noun_suffie = initSystemDict(dictPath) == true ? 1 : 0;
		userDict = 1;
	}

	/**
	 * 构造函数
	 * 
	 * @param dictPath
	 *            词库所在的路径(文件夹路径)
	 * @param userDict
	 *            用户词典文件
	 */
	public Stems(String dictPath, File userDict) {
		dict = new HashMap<String, Word>();
		nounSuffixes = new ArrayList<String>();
		noun_suffie = initSystemDict(dictPath) == true ? 1 : 0;
		init(userDict);
	}

	/**
	 * 返回当前路径
	 * 
	 * @return 返回当前路径 如D:\er\
	 * @author Erxat
	 */
	public String getAppPath() {
		return System.getProperty("user.dir") + "\\";
	}

	/**
	 * 获取单词的词干
	 * 
	 * @param word
	 *            单词
	 * @return 返回字符串 如果库中找到返回 单词#词缀#词干#词类（如果返回里面存在not have英文字母，意思就是不存在该字段）
	 *         如果找不到库中就返回Unknown#单词
	 * 
	 */
	public String getStems(String word) {
		if (dict.containsKey(word)) {
			return dict.get(word).toString();
		} else {
			return "Unknown#" + word;
		}
	}

	// public static String realAll(File f) {
	// try {
	// BufferedReader reader = new BufferedReader(new FileReader(f));
	// String temp = "";
	// StringBuffer sb = new StringBuffer();
	// while ((temp = reader.readLine()) != null) {
	// sb.append(temp + "\r\n");
	// }
	// reader.close();
	// return sb.toString();
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return "error";
	// }

	List<String> getInflectedWordNoun(String word) {
		List<String> inflectedWord = new ArrayList<String>();
		for (String string : nounSuffixes) {
			inflectedWord.add(word + string);
		}
		return inflectedWord;
	}

	/**
	 * 获取维吾尔语单词形态化后的结果
	 * 
	 * @param word
	 *            单词
	 * @param wordClass
	 *            词类，如名词，副词，动词等等
	 * @return 返回list
	 */
	public List<String> getInflectedWord(String word, WordClass wordClass) {
		switch (wordClass) {
		case Nouns:
			return getInflectedWordNoun(word);
		default:
			break;
		}
		return null;
	}

	public static void main(String[] args) {
		Stems tiqu = new Stems();
		System.out.println(tiqu.getStems("ئوقۇغۇچىلارنىڭ"));
		List<String> list = tiqu.getInflectedWord("كىتاب", WordClass.Nouns);
		for (String string : list) {
			System.out.println(string);
		}
		// Splitor splitor = new Splitor();
		// StringBuffer sball = new StringBuffer();
		// StringBuffer sbunAll = new StringBuffer();
		// try {
		// File f = new File("D:\\维文网站采集结果");
		// // System.out.println(f.getName());
		// File[] files = f.listFiles();
		// Set<String> list;
		// StringBuffer sb;
		// String outpath = "D:\\维文网站采集结果\\test";
		// File fi;
		//
		// String[] str;
		// for (File file : files) {
		// sb = new StringBuffer();
		// list = splitor.splitWordsSet(realAll(file));
		// for (String string : list) {
		// str = tiqu.getStems(string).split("#");
		// if (str.length == 2) {
		// sbunAll.append(tiqu.getStems(string) + "\r\n");
		// } else if (str.length == 4) {
		// sball.append(tiqu.getStems(string) + "\r\n");
		// }
		// sb.append(tiqu.getStems(string) + "\r\n");
		// }
		// fi = new File(outpath + "\\out_" + file.getName());
		// try {
		// FileWriter out = new FileWriter(fi);
		// out.write(sb.toString());
		// out.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// System.out.println(e.getMessage());
		// }
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// File ff = new File("D:\\维文网站采集结果\\test\\all.txt");
		// File ff2 = new File("D:\\维文网站采集结果\\test\\Unknownall.txt");
		// try {
		// FileWriter out = new FileWriter(ff);
		// out.write(sball.toString());
		// out.close();
		// FileWriter out2 = new FileWriter(ff2);
		// out2.write(sbunAll.toString());
		// out2.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// System.out.println(e.getMessage());
		// }
		// System.out.println("完毕!");
	}
}
