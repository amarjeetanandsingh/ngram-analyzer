package in.amarjeet.ngramanalyzer.pojo;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A class representing a node of the WordHeap. It contains a word with highest
 * frequency in word field.
 * 
 * @author amarjeetanandsingh
 *
 */
public class FileNode {

	private Word word = null;
	private BufferedReader br = null;

	public FileNode(BufferedReader br) {
		this.br = br;
		try {
			String line = br.readLine();
			if (line != null) {
				String[] token = line.split("\t");
				this.word = new Word(token[0], token[1], token[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tell the FileNode to read the next line from the file in "br" and store
	 * in its Word field.
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean increment() throws IOException {
		String line = null;
		if ((line = br.readLine()) != null) {
			String[] token = line.split("\t");
			word.setData(token[0]);
			word.setFreq(token[1]);
			word.setVol(token[2]);
			return true;
		}
		// we have reached to the end of the file. Close br
		br.close();
		return false;
	}

	public Word getWord() {
		return this.word;
	}
}
