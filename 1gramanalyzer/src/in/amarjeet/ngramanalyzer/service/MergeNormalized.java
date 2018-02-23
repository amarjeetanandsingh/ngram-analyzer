package in.amarjeet.ngramanalyzer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import in.amarjeet.ngramanalyzer.pojo.FileNode;
import in.amarjeet.ngramanalyzer.util.Constants;

public class MergeNormalized {

	private static Logger log = Logger.getLogger(MergeNormalized.class.getName());

	public void merge(Path baseDir) throws IOException {
		long startTime = System.currentTimeMillis();

		Path normalizedDir = baseDir.resolve(Constants.NORMALIZED_FOLDER);
		File[] files = normalizedDir.toFile().listFiles();

		Path finalWordListFile = baseDir.resolve(Constants.FINAL_WORD_LIST_FILE);
		Files.deleteIfExists(finalWordListFile);
		Files.createFile(finalWordListFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(finalWordListFile.toFile()));

		WordHeap heap = new WordHeap(files.length);
		for (int k = 0; k < files.length; k++) {
			BufferedReader br = new BufferedReader(new FileReader(files[k]));
			FileNode fileNode = new FileNode(br);
			if (fileNode.getWord() == null) {
				System.out.println(files[k].getName() + " is NOT added to heap.");
				continue;
			}
			heap.insert(fileNode);
			log.info(files[k].getName() + " is picked.");
		}

		while (heap.getHeapSize() > 0) {
			String line = heap.extractMax();
			bw.write(line + "\n");
		}
		bw.close();

		log.info("\n---------------------------------------------------------\nTime to merge " + files.length
				+ " sorted files is : " + (System.currentTimeMillis() - startTime)
				+ " ms\n=========================================================");
	}
}
