import java.util.*;

public class LanguageData implements java.io.Serializable {

	private static final long serialVersionUID = -8812200515319550014L;

	private String languageName;
	
	private int numCharRead, numWordsRead, numSentRead, numTwoGramsRead, numThreeGramsRead, numFourGramsRead;
	
	private double avgWordLength, avgSentenceLength;
	
	private List<String> mostCommonNGrams;
	
	public LanguageData(String lang_name) {
		languageName = lang_name;
		numCharRead = 0;
		numWordsRead = 0;
		numSentRead = 0;
		numTwoGramsRead = 0;
		numThreeGramsRead = 0;
		numFourGramsRead = 0;
		
		avgWordLength = 0;
		avgSentenceLength = 0;

		mostCommonNGrams = new ArrayList<String>(512);
	}
	
	public void addChars(int numChars) {
		numCharRead += numChars;
	}
	
	public void addWord() {
		numWordsRead++;
	}
	
	public void addTwoGram() {
		numTwoGramsRead++;
	}
	
	public void addThreeGram() {
		numThreeGramsRead++;
	}
	
	public void addFourGram() {
		numFourGramsRead++;
	}
	
	public void addSentence() {
		numSentRead++;
	}

	public void addCommonNGram(String ngram) {
		mostCommonNGrams.add(ngram);
	}
	
	/*
	 * getters and setters
	 */
	public String getLanguageName() {
		return languageName;
	}

	public int getNumCharRead() {
		return numCharRead;
	}

	public int getNumWordsRead() {
		return numWordsRead;
	}
	
	public int getNumTwoGramsRead() {
		return numTwoGramsRead;
	}
	
	public int getNumThreeGramsRead() {
		return numThreeGramsRead;
	}
	
	public int getNumFourGramsRead() {
		return numFourGramsRead;
	}

	public int getNumSentRead() {
		return numSentRead;
	}

	public double getAvgWordLength() {
		return avgWordLength;
	}

	public void setAvgWordLength(double avgWordLength) {
		this.avgWordLength = avgWordLength;
	}

	public double getAvgSentenceLength() {
		return avgSentenceLength;
	}

	public void setAvgSentenceLength(double avgSentenceLength) {
		this.avgSentenceLength = avgSentenceLength;
	}
	
	public List<String> getMostCommonNGrams() {
		return mostCommonNGrams;
	}
	
	public void clear() {
		numCharRead = 0;
		numWordsRead = 0;
		numSentRead = 0;
		numTwoGramsRead = 0;
		numThreeGramsRead = 0;
		numFourGramsRead = 0;
		
		avgWordLength = 0;
		avgSentenceLength = 0;
		
		mostCommonNGrams.clear();
	}

	@Override
	public String toString() {
		return "Characters read:\n" + numCharRead + "\nWords read:\n" + numWordsRead + "\nSentences read:\n" + 
				numSentRead + "\nTwo Grams Read: " + numTwoGramsRead + "\nThree Grams Read: " + numThreeGramsRead + 
				"\nFour Grams Read: " + numFourGramsRead + "\nAvg word length:\n" + avgWordLength +
				"\nAvg sentence length:\n" + avgSentenceLength;
	}
	
}