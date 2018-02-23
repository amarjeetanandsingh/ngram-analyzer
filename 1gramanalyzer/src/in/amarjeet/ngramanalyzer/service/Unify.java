package in.amarjeet.ngramanalyzer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import in.amarjeet.ngramanalyzer.util.Constants;
import in.amarjeet.ngramanalyzer.util.StrUtils;

/**
 * This class merges duplicate words. It expects the data in the file is sorted
 * alphabetically by words.
 * 
 * @author amarjeetanandsingh
 *
 */
public class Unify {

	private static Logger log = Logger.getLogger("Unify");

	public void doAction(Path baseDir) throws Exception {
		long start = System.currentTimeMillis();

		File[] files = baseDir.toFile().listFiles();

		// check if directory is empty.
		if (files == null || files.length == 0) {
			throw new RuntimeException("No raw data file in directory selected.");
		}

		Stream.of(files).parallel().forEach(file -> {
			Long startTime = System.currentTimeMillis();
			try {
				String line = null;
				String token[] = null;
				Path targetFile = baseDir.resolve(Constants.DUPLICATE_MERGED_FOLDER).resolve(file.getName());
				Files.createDirectories(targetFile.getParent());
				Files.createFile(targetFile);
				BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile.toFile()));
				BufferedReader br = new BufferedReader(new FileReader(file));

				String prevWord = "";
				String prevFreqCount = "0";
				String prevVolCount = "0";
				String lineToWrite = "";

				while ((line = br.readLine()) != null) {
					token = line.split("\t");
					if (token == null || token.length != 4) {
						bw.close();
						br.close();
						log.log(Level.ALL,
								"Invalid input file format. File should contain data in \"string[TAB]string[TAB]string[TAB]string\" format");
						throw new RuntimeException(
								"Invalid input file format. File should contain data in \"string[TAB]string[TAB]string[TAB]string\" format");
					}
					if (prevWord.equals(token[0])) {
						prevFreqCount = StrUtils.addStr(prevFreqCount, token[2]);
						prevVolCount = StrUtils.addStr(prevVolCount, token[3]);
					} else {
						lineToWrite = prevWord + "\t" + prevFreqCount + "\t" + prevVolCount;
						bw.write(lineToWrite + "\n");
						prevWord = token[0];
						prevFreqCount = "0";
						prevVolCount = "0";
					}
				}
				bw.write(prevWord + "\t" + prevFreqCount + "\t" + prevVolCount);

				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("Merged duplicate in " + file.getName() + " in " + (System.currentTimeMillis() - startTime)
					+ " ms");
		});

		log.info(
				"--------------------------------------------------------------------\nTotal Time to merge duplicates in "
						+ files.length + " files : " + ((System.currentTimeMillis() - start) / 1000) + " s\n"
						+ "====================================================================\n\n");
	}
}
