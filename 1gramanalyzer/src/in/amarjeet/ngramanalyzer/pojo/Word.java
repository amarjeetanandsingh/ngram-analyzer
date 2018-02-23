package in.amarjeet.ngramanalyzer.pojo;

import java.util.Comparator;

public class Word implements Comparator<Word> {

	private String data = null;
	private String freq = null;
	private String vol = null;
	
	public Word() {

	}

	public Word(String data, String freq, String vol) {
		super();
		this.data = data;
		this.freq = freq;
		this.vol = vol;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	@Override
	public int compare(Word w1, Word w2) {
		if (w1.getFreq().length() < w2.getFreq().length()) {
			return 1;
		} else if (w1.getFreq().length() > w2.getFreq().length()) {
			return -1;
		}
		return w2.getFreq().compareTo(w1.getFreq());
	}

	@Override
	public String toString() {
		return data + "\t" + freq + "\t" + vol;
	}

}
