/**
 * 
 */
package org.gbif.portal.harvest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Split the input file to sperate files of maximum record size
 * @author timrobertson
 */
public class FileUtils {
	protected Log log = LogFactory.getLog(this.getClass());

	/**
	 * For the given file's path, returns a proposed new filename (including path) with the extension 
	 * index and suffix
	 * So a file of "/tmp/input.txt" -> "/tmp/input_part_10.txt"  
	 * @param input File
	 * @param suffix E.g. part
	 * @param index E.g. 10
	 * @param extension E.g. ".txt"
	 * @return The proposed name
	 */
	public static String getName(File input, String suffix, int index, String extension) {
		return (input.getAbsolutePath()).replaceAll(extension, "") + "_" + suffix + "_" + index + extension; 
	}

	/**
	 * For the given file's path, returns a proposed new filename (including path) with the extension 
	 * and suffix
	 * So a file of "/tmp/input.txt" -> "/tmp/input_part.txt"  
	 * @param input File
	 * @param suffix E.g. part
	 * @param extension E.g. ".txt"
	 * @return The proposed name
	 */
	public static String getName(File input, String suffix, String extension) {
		return (input.getAbsolutePath()).replaceAll(extension, "") + "_" + suffix + extension; 
	}
	
	/**
	 * Splits the supplied file into files of set line size and with a suffix
	 * @param input To split up
	 * @param suffix the suffic to append "split" will result in ..._split_12.txt, _split_13.txt etc
	 * @param linesPerOutput Lines per split file
	 * @param extension The file extension to use - e.g. ".txt"
	 * @return The split files
	 * @throws IOException
	 */
	public List<File> split(File input, String suffix, int linesPerOutput, String extension) throws IOException {
		log.debug("Splitting File[" + input.getAbsolutePath() + "]");
		long timer = System.currentTimeMillis();
		List<File> splitFiles = new LinkedList<File>(); 
		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = br.readLine();
		int lineCount = 0;
		int fileCount = 0;
		File splitFile = new File(FileUtils.getName(input, suffix, fileCount, extension));
		fileCount++;
		splitFiles.add(splitFile);
		FileWriter fw = new FileWriter(splitFile);
		try {
			while (line != null) {
				if (lineCount==linesPerOutput) {
					fw.flush();
					fw.close();
					splitFile = new File(FileUtils.getName(input, suffix, fileCount, extension));
					splitFiles.add(splitFile);
					// is ok to reuse, as last one is closed, and this will always get closed - see finally below
					fw = new FileWriter(splitFile);
					fileCount++;
					lineCount=0;
				}
				fw.write(line);
				fw.write("\n");
				line = br.readLine();
				lineCount++;
			}
			fw.flush();
		} finally {
			fw.close();
		}
		log.debug("File[" + input.getAbsolutePath() + "] split successfully into[" + splitFiles.size() + "] parts in secs[" + (1+System.currentTimeMillis()-timer)/1000 + "]");
		return splitFiles;
	}
	
	/**
	 * Sorts the input file into the output file using the supplied lineComparator
	 * @param input To sort
	 * @param output The sorted version of the input
	 * @param suffix To use in temporary files during sorting
	 * @param linesPerMemorySort number of lines that will be written in memory before flushed to disk
	 * @param extension Of the temporary files (suggest "sort")
	 * @param lineComparator To use during comparison
	 * @throws IOException
	 */
	public void sort(File input, File output, String suffix, int linesPerMemorySort, String extension, Comparator<Object> lineComparator) throws IOException {
		log.debug("Sorting File[" + input.getAbsolutePath() + "]");
		long timer = System.currentTimeMillis();
		List<File> sortFiles = new LinkedList<File>(); 
		BufferedReader br = new BufferedReader(new FileReader(input));
		int fileCount;
		List<String> linesToSort;
		try {
			String line = br.readLine();
			fileCount = 0;
			
			linesToSort = new LinkedList<String>(); 
			while(line != null) {
				linesToSort.add(line);
				
				// if buffer is full, then sort and write to file
				if (linesToSort.size() == linesPerMemorySort) {
					sortFiles.add(sortAndWrite(input, suffix, extension, lineComparator, fileCount, linesToSort));
					linesToSort = new LinkedList<String>();
					fileCount++;
				}
				
				line = br.readLine();
			}
			// catch the last lot
			if (linesToSort.size() > 0) {
				sortFiles.add(sortAndWrite(input, suffix, extension,lineComparator, fileCount, linesToSort));
			}
			
		} finally {
			br.close();
		}

		// now merge the sorted files into one single sorted file
		mergedSortedFiles(sortFiles, output, lineComparator);
		
		log.debug("File[" + input.getAbsolutePath() + "] sorted successfully using[" + sortFiles.size() + "] parts to do sorting in secs[" + (1+System.currentTimeMillis()-timer)/1000 + "]");
	}

	/**
	 * Merges the sorted files
	 * @param sortFiles To merge
	 * @param output To merge to
	 * @param lineComparator To use when determining the order (reuse the one that was used to sort the individual files)
	 * @throws IOException 
	 */
	public void mergedSortedFiles(List<File> sortFiles, File output, Comparator<Object> lineComparator) throws IOException {
		List<BufferedReader> partReaders = new LinkedList<BufferedReader>();
		FileWriter fw = new FileWriter(output);
		try {
			List<String> partReaderLine = new LinkedList<String>();
			for (File f : sortFiles) {
				partReaders.add(new BufferedReader(new FileReader(f)));
			}
			boolean moreData = false;
			// load first line in
			for (BufferedReader partReader : partReaders) {
				String partLine = partReader.readLine();
				if (partLine != null) {
					moreData = true;
				}
				// we still add the "null" to keep the partReaders and partLineReader indexes in sync - ALWAYS
				partReaderLine.add(partLine);
			}
			// keep going until all readers are exhausted
			while (moreData) {
				int index = FileUtils.lowestValueIndex(partReaderLine, lineComparator);
				if (index >= 0) {
					fw.write(partReaderLine.get(index));
					fw.write("\n");
					BufferedReader r = partReaders.get(index);
					String partLine = r.readLine();
					synchronized (partReaderLine) {
						partReaderLine.add(index,partLine);
						partReaderLine.remove(index+1);
					}
				} else {
					moreData = false;
				}
			}
		} finally {
			for (BufferedReader b : partReaders) {
				try {
					b.close();
				} catch (RuntimeException e) {
				}
			}
			// I assume it periodically flushes anyway, so only need to do once at end...
			fw.flush();
			fw.close();
		}
	}
		
	/**
	 * For the given list, finds the index of the lowest value using the given comparator 
	 * @param values To compare
	 * @param comparator To use
	 * @return The index of the lowest value, or -1 if they are all null
	 */
	static int lowestValueIndex(List<String> values, Comparator<Object> comparator) {
		int index = 0;
		String lowestValue = null;
		for (int i=0; i<values.size(); i++) {
			String value = values.get(i);
			if (lowestValue != null) {
				if (comparator.compare(lowestValue, value) > 0) {
					lowestValue = value;
					index = i;
				}
			} else {
				lowestValue = value;
				index = i;
			}
		}
		
		if (lowestValue != null) {
			return index;
		} else {
			return -1;
		}
		
	}

	/**
	 * Sorts the lines and writes to file using the 
	 * @param input File to base the name on
	 * @param suffix To use with the input file name
	 * @param extension to use as the extension for the generated file
	 * @param lineComparator To compare the lines for sorting
	 * @param fileCount Used for the file name
	 * @param linesToSort To actually sort
	 * @return The written file
	 * @throws IOException 
	 */
	protected File sortAndWrite(File input, String suffix, String extension,
			Comparator<Object> lineComparator, int fileCount,
			List<String> linesToSort) throws IOException {
		long timer = System.currentTimeMillis();
		Collections.sort(linesToSort, lineComparator);
		// When implementing a comparator, make it SUPER quick!!!
		//log.debug("Collections.sort took msec[" + (System.currentTimeMillis() - timer) + "] to sort records[" + linesToSort.size() + "]");
		File sortFile = new File(FileUtils.getName(input, suffix, fileCount, extension));
		FileWriter fw = new FileWriter(sortFile);
		try {
			for (String s : linesToSort) {
				fw.write(s);
				fw.write("\n");
			}
		} finally {
			fw.close();
		}
		return sortFile;
	}
}