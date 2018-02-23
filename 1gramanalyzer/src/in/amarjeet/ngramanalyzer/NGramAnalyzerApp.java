package in.amarjeet.ngramanalyzer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import in.amarjeet.ngramanalyzer.service.MergeNormalized;
import in.amarjeet.ngramanalyzer.service.Normaliser;
import in.amarjeet.ngramanalyzer.service.Unify;
import in.amarjeet.ngramanalyzer.util.Constants;

public class NGramAnalyzerApp {

	private static Logger log = Logger.getLogger(NGramAnalyzerApp.class.getName());
	private Path baseDir = null;
	private String freqThreshold = "100000";

	public NGramAnalyzerApp(String path) {
		if (path == null || path.equals("")) {
			log.log(Level.SEVERE, "Empty path input");
			throw new RuntimeException("Empty path input.");
		}
		File dir = new File(path);
		if (dir != null && dir.isDirectory() && dir.exists()) {
			this.baseDir = Paths.get(path);
		} else {
			log.log(Level.SEVERE, "Invalid directory path input.");
			throw new RuntimeException("Invalid directory path input.");
		}
	}

	public NGramAnalyzerApp(String path, String freqThreshold) {
		this(path);
		if (freqThreshold.matches(Constants.FREQUENCY_THRESHOLD_REGEX)) {
			this.freqThreshold = freqThreshold;
		} else {
			log.warning(
					"Invalid frequency input. It must contain only digits[0-9] without leading zeros. Default frequency threshold 100000 is set.");
		}
	}

	public void generateWordList() {
		log.info(
				"Merging duplicate words in ngram files. It can take upto 10-15 minutes depending upon the size of raw data provided.");
		try {
			new Unify().doAction(baseDir);
			log.info("\n\nDuplicates merged successfully. Starting to normalize the raw data...\n\n");
			new Normaliser().normalise(baseDir, freqThreshold);
			log.info("Normalized raw data successfully. Starting merge for final list output.");
			new MergeNormalized().merge(baseDir);
			log.info("Final word list has been created at "
					+ baseDir.resolve(Constants.FINAL_WORD_LIST_FILE).toAbsolutePath().toString() + "\n\n\n DONE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
