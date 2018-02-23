package in.amarjeet.ngramanalyzer.service;

import java.io.IOException;

import in.amarjeet.ngramanalyzer.pojo.FileNode;
import in.amarjeet.ngramanalyzer.util.StrUtils;

/**
 * This class represents heap. Each node contains a FileNode object. Each
 * FileNode object represents a file in normalized folder which is sorted
 * descending by word's frequency and contains data in format :-
 * WORD[TAB]FREQUENCY[TAB]STRING_OF_DIGITS[TAB]STRING_OF_DIGITS
 * 
 * @author amarjeetanandsingh
 *
 */

public class WordHeap {

	private int heapSize = 0;
	private FileNode[] arr = null;

	/**
	 * The length is the array of Heap. Or simply the number of normalized files
	 * in the normalized folder.
	 * 
	 * @param length
	 */
	public WordHeap(int length) {
		this.arr = new FileNode[length];
	}

	/**
	 * Insert a new node to the heap.
	 * 
	 * @param fileNode
	 */
	public void insert(FileNode fileNode) {
		heapSize++;
		arr[heapSize - 1] = fileNode;
		for (int i = heapSize - 1; i > 0 && StrUtils.isLarger(arr[i].getWord().getFreq(),
				arr[getParent(i)].getWord().getFreq()); i = getParent(i)) {
			swap(i, getParent(i));
		}

	}

	private void maxHeapify(int i) {
		int L = getLeft(i);
		int R = getRight(i);
		int largest = -1;

		if (L < heapSize && StrUtils.isLarger(arr[L].getWord().getFreq(), arr[i].getWord().getFreq())) {
			largest = L;
		} else {
			largest = i;
		}
		if (R < heapSize && StrUtils.isLarger(arr[R].getWord().getFreq(), arr[largest].getWord().getFreq())) {
			largest = R;
		}
		if (largest != i) {
			swap(i, largest);
			maxHeapify(largest);
		}
	}

	/**
	 * Returns the string representation of the word at the top of heap in
	 * WORD[TAB]FREQUENCY[TAB]VOLUME_COUNT format.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String extractMax() throws IOException {
		if (heapSize < 0)
			throw new RuntimeException("Cannot extract from empty heap.");

		FileNode fileNode0 = arr[0];
		String result = fileNode0.getWord().toString();
		arr[0] = arr[heapSize - 1];
		heapSize--;
		maxHeapify(0);

		if (fileNode0.increment()) {
			insert(fileNode0);
		}
		return result;
	}

	private int getLeft(int i) {
		return (i << 1) + 1;
	}

	private int getRight(int i) {
		return (i << 1) + 2;
	}

	private int getParent(int i) {
		return i >> 1;
	}

	private void swap(int i, int j) {
		FileNode temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public int getHeapSize() {
		return this.heapSize;
	}
}
