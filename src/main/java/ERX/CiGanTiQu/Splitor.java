package ERX.CiGanTiQu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Splitor {
	private char[] uyAlpha = "چۋېرتيۇڭوپھسداەىقكلزشغۈبنمئژفگخجۆ".toCharArray();

	public Splitor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param strText
	 *            分词内容
	 * @return 返回分词后的单词列表 List
	 * @author Erxat
	 */
	public List<String> splitWordsList(String strText) {
		List<String> list = new ArrayList<String>();
		int start, end, length;
		try {
			if (strText == null || strText.isEmpty()) {
				return null;
			}
			start = 0;
			end = 0;
			length = strText.length();
			// asd123dwa13e
			boolean flag = false; // 控制状态
			for (int i = 0; i < length; i++) {
				if (isAlpha(strText.charAt(i))) {
					if (!flag) {
						flag = true;
						start = end;
					}
				} else {
					if (flag) {
						list.add(strText.substring(start, end));
						flag = false;
					}
				}
				end++;
			}
			if (start != end && flag) {
				list.add(strText.substring(start, end));
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;
	}

	/**
	 * 
	 * @param strText
	 *            分词内容
	 * @return 返回分词后的单词列表 List
	 * @author Erxat
	 */
	public Set<String> splitWordsSet(String strText) {
		Set<String> list = new HashSet<String>();
		int start, end, length;
		try {
			if (strText == null || strText.isEmpty()) {
				return null;
			}
			start = 0;
			end = 0;
			length = strText.length();
			// asd123dwa13e
			boolean flag = false; // 控制状态
			for (int i = 0; i < length; i++) {
				if (isAlpha(strText.charAt(i))) {
					if (!flag) {
						flag = true;
						start = end;
					}
				} else {
					if (flag) {
						list.add(strText.substring(start, end));
						flag = false;
					}
				}
				end++;
			}
			if (start != end && flag) {
				list.add(strText.substring(start, end));
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;
	}

	/**
	 * 是否维吾尔语字母
	 * 
	 * @param c
	 *            判断字符
	 * @return 是维吾尔语字母返回true，否则返回false
	 */
	public boolean isAlpha(char c) {
		for (int i = 0; i < uyAlpha.length; i++) {
			if (uyAlpha[i] == c) {
				return true;
			}
		}
		return false;
	}

	//
	// public static boolean is(char[] s1, char c) {
	// for (int i = 0; i < s1.length; i++) {
	// if (s1[i] == c) {
	// return true;
	// }
	// }
	// return false;
	// }

	public static void main(String[] args) {
		String string = "بېيجىڭ شەھەرلىك 2-ئوتتۇرا خەلق سوت مەھكىمىسى 4-دېكابىر دۆلەت ئەرزىيەت ئىدارىسىنىڭ سابىق مۇئاۋىن باشلىقى شۇ جېيىنىڭ پارىخورلۇق جىنايىتىگە 1-سوتتا مۇددەتلىك 13 يىللىق قاماق جازاسى بېرىش، 130 مىڭ يۈەنلىك مال مۈلكىنى مۇسادىرە قىلىشقا ھۆكۈم قىلدى.";
		// String string="1ئىكەن.2";
		Splitor splitor = new Splitor();
		List<String> list = splitor.splitWordsList(string);
		if (list == null) {
			System.out.println("分词失败！参数不能为空");
		} else {
			for (String string2 : list) {
				System.out.print(string2 + "#");
			}
		}

		// char[] Alpha = "qwertyuiopasdfghjklzxcvbnm".toCharArray();
		// int start, end;
		// start = 0;
		// end = 0;
		// boolean flag = false;
		// for (int i = 0; i < string.length(); i++) {
		// if (is(Alpha, string.charAt(i))) {
		// if (!flag) {
		// flag = true;
		// start = end;
		// }
		// } else {
		// if (flag) {
		// System.out.println(string.substring(start, end) + "#");
		// flag = false;
		// }
		// }
		// end++;
		// }
		// if (start != end) {
		// System.out.println(string.substring(start, end) + "#");
		// }
	}
}