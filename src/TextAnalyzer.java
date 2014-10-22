import java.util.*;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextAnalyzer {

	//512 is the maximum number of ngrams saved and used for comparison purposes.
	private static final int MAX_NGRAMS_USED = 512;
	
	private LanguageData language;
	private String inputText;
	private boolean fromFile;
	private HashMap<String, Integer> nGramOccTemp;
	
	/**
	 * Create a TextAnalyzer.
	 * @param langName
	 * @param input
	 * @param from true: read input from file	false: read input passed in
	 */
	public TextAnalyzer(LanguageData lang, String input, boolean from) {
		language = lang;
		inputText = input;
		fromFile = from;
		nGramOccTemp = new HashMap<String, Integer>(1024);
	}
	
	/**
	 * Reads the input text by line.
	 */
	public void readInput() {
		if (fromFile) {
			Scanner s = null;
			try {
				s = new Scanner(new FileInputStream(new File(inputText)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String line = null;
			while (s.hasNextLine()) {
				line = s.nextLine().toLowerCase();
				analyzeLine(line);
			}
			refreshData();
			s.close();
			
			try {
			PrintWriter pw = new PrintWriter(new File("ngrams.txt"));
			List<String> temp = language.getMostCommonNGrams();
			for (String str : temp) {
				pw.println(str);
			}
			pw.close();
			}
			catch (IOException e) {
				
			}
		}
		else {
			analyzeLine(inputText);
			refreshData();
		}
	}
	
	/**
	 * Parses and analyzes a line (paragraph) of text.
	 * @param line
	 */
	private void analyzeLine(String line) {
		String[] sentences = line.split("[!?.]+");
		for (int i = 0; i < sentences.length; i++) {
			analyzeSentence(sentences[i]);
		}
	}
	
	/**
	 * Parses and analyzes a sentence.
	 * @param sentence
	 */
	private void analyzeSentence(String sentence) {
		language.addSentence();
		String[] words = sentence.split("[^\\p{L}]+");
		for (int i = 0; i < words.length; i++) {
			analyzeWord(words[i]);
		}
	}
	
	/**
	 * Analyzes a word.
	 * @param word
	 */
	private void analyzeWord(String word) {
		
		int length = word.length();
		
		language.addChars(length);
		language.addWord();
		
		for (int i = 0; i < length; i++) {
			addNGramOcc(word.substring(i, i+1));
			if (i > 0) {
				language.addTwoGram();
				addNGramOcc(word.substring(i-1, i+1));
			}
			if (i > 1) {
				language.addThreeGram();
				addNGramOcc(word.substring(i-2, i+1));
			}
			if (i > 2) {
				language.addFourGram();
				addNGramOcc(word.substring(i-3, i+1));
			}
		}
	}
	
	/**
	 * Adds an occurrence of an ngram.
	 * @param ngram
	 */
	private void addNGramOcc(String ngram) {
		int newValue = 1;
		if (nGramOccTemp.containsKey(ngram))
			newValue += nGramOccTemp.get(ngram);
		nGramOccTemp.put(ngram, newValue);
	}
	
	/**
	 * Calculates the averages and frequencies for a language.
	 */
	public void refreshData() {
		
		language.setAvgWordLength((double) language.getNumCharRead() / (double) language.getNumWordsRead());
		language.setAvgSentenceLength((double) language.getNumWordsRead() / (double) language.getNumSentRead());
		
		ArrayList<String> nGramOccSet = sortByComparator(nGramOccTemp);
		//only the 512 most common ngrams are used for efficiency
		for (int i = 0; i < MAX_NGRAMS_USED && i < nGramOccSet.size(); i++) {
			language.addCommonNGram(nGramOccSet.get(i));
		}
	}
	
	/**
	 * Sorts ngrams into descending order according to the number of occurrences.
	 * @param unsortMap
	 * @return sortedList - the sorted list of ngrams.
	 */
    private static ArrayList<String> sortByComparator(Map<String, Integer> unsortMap)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {

            	return o2.getValue().compareTo(o1.getValue());
            }
        });

        ArrayList<String> sortedList = new ArrayList<String>();
        for (Entry<String, Integer> entry : list)
        {
            sortedList.add(entry.getKey());
        }

        return sortedList;
    }
}