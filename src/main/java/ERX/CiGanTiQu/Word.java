package ERX.CiGanTiQu;

public class Word {
	String word;
	String nSuffixes;
	String ustem;
	String basetagname;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getnSuffixes() {
		return nSuffixes;
	}

	public void setnSuffixes(String nSuffixes) {
		this.nSuffixes = nSuffixes;
	}

	public String getUstem() {
		return ustem;
	}

	public void setUstem(String ustem) {
		this.ustem = ustem;
	}

	public String getBasetagname() {
		return basetagname;
	}

	public void setBasetagname(String basetagname) {
		this.basetagname = basetagname;
	}

	public Word() {
		// TODO Auto-generated constructor stub
	}

	public Word(String word, String nsuffixes, String ustem, String basetagname) {
		this.word = word;
		this.nSuffixes = nsuffixes;
		this.ustem = ustem;
		this.basetagname = basetagname;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.word + "#" + this.nSuffixes + "#" + this.ustem + "#"
				+ this.basetagname;
	}
}
