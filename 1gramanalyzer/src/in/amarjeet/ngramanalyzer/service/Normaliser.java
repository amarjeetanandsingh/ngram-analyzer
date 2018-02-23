package in.amarjeet.ngramanalyzer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import in.amarjeet.ngramanalyzer.pojo.Word;
import in.amarjeet.ngramanalyzer.util.Constants;
import in.amarjeet.ngramanalyzer.util.StrUtils;

/**
 * It removes the words which are below threshold frequency or invalid (contains
 * special character);
 * 
 * @author amarjeetanandsingh
 *
 */
public class Normaliser {

	private static Logger log = Logger.getLogger(Normaliser.class.getName());

	public void normalise(Path baseDir, String freqThreshold) throws Exception {
		long enteredTime = System.currentTimeMillis();
		Path duplicateMergedDir = baseDir.resolve(Constants.DUPLICATE_MERGED_FOLDER);
		File[] files = duplicateMergedDir.toFile().listFiles();
		if (files == null || files.length < 1) {
			log.log(Level.SEVERE, "No file found to normalize");
			return;
		}

		for (int k = 0; k < files.length; k++) {
			Long startTime = System.currentTimeMillis();
			Map<String, Word> wordMap = getWordMap(files[k]);
			TreeSet<Word> wordTree = getTreeSet(wordMap);

			Path targetFilePath = baseDir.resolve(Constants.NORMALIZED_FOLDER).resolve(files[k].getName());
			Files.createDirectories(targetFilePath.getParent());
			Files.createFile(targetFilePath);
			writeToFile(wordTree, targetFilePath, freqThreshold);

			log.info("Normalized " + files[k].getName() + " in : " + (System.currentTimeMillis() - startTime) + " ms");
		}
		log.info("---------------------------------------------------------\nTotal time to normalize " + files.length
				+ " files : " + (System.currentTimeMillis() - enteredTime)
				+ " ms\n=========================================================\n\n");
	}

	private void writeToFile(TreeSet<Word> wordTree, Path targetFilePath, String freqThreshold) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(targetFilePath.toFile()));
		for (Word word : wordTree) {
			if (StrUtils.isLarger(word.getFreq(), freqThreshold)) {
				bw.write(word.toString() + "\n");
			}
		}
		bw.close();
	}

	private TreeSet<Word> getTreeSet(Map<String, Word> wordMap) {
		TreeSet<Word> wordTree = new TreeSet<Word>(new Word());
		for (String wordStr : wordMap.keySet()) {
			Word word = wordMap.get(wordStr);
			wordTree.add(word);
		}
		return wordTree;
	}

	public Map<String, Word> getWordMap(File file) throws Exception {
		String line = null;
		String token[] = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		Map<String, Word> strMap = new HashMap<>();

		while ((line = br.readLine()) != null) {
			token = line.split("\t");
			String wordStr = getValidWord(token[0]);
			if (wordStr == null)
				continue;

			if (strMap.containsKey(wordStr)) {
				Word word = strMap.get(wordStr);
				word.setFreq(StrUtils.addStr(token[1], word.getFreq()));
				word.setVol(StrUtils.addStr(token[2], word.getVol()));
			} else {
				strMap.put(wordStr, new Word(wordStr, token[1], token[2]));
			}
		}
		br.close();
		return strMap;
	}

	public String getValidWord(String str) {
		if (!(str.matches(Constants.WORD_REGEX)) || str.endsWith("_NUM"))
			return null;

		if (str.endsWith("_NOUN") || str.endsWith("_VERB")) {
			str = str.substring(0, str.length() - 5);
		} else if (str.endsWith("_ADJ") || str.endsWith("_ADV") || str.endsWith("_DET")) {
			str = str.substring(0, str.length() - 4);
		} else {
			if (str.indexOf('_') > -1) {
				String tmp = str.substring(0, str.lastIndexOf('_'));
				return tmp.matches(Constants.VALID_WORD_REGEX) ? tmp : null;
			}
		}
		return str.toLowerCase();
	}
}
