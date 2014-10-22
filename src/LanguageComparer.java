import java.util.*;

public class LanguageComparer {

	private LanguageData toIdentify;
	private ArrayList<LanguageData> knownLangs;
	private ArrayList<ComparisonData> langComparisons;
	
	public LanguageComparer(LanguageData unknown, ArrayList<LanguageData> known) {
		toIdentify = unknown;
		knownLangs = known;
		langComparisons = new ArrayList<ComparisonData>();
	}
	
	/**
	 * Compares the unknown language to each of the known languages.
	 * @return An array of ComparisonData objects sorted from highest totalSimilarity value to lowest.
	 */
	public ComparisonData[] compareUnknownToKnown() {
		for (LanguageData known : knownLangs) {
			langComparisons.add(compareToCurrent(known));
		}
		return sortBySimilarity();
	}
	
	/**
	 * Performs the comparison of the unknown language to one of the known languages.
	 * @param current
	 * @return
	 */
	private ComparisonData compareToCurrent(LanguageData current) {
		ComparisonData comp = new ComparisonData(current);
		
		comp.setTotal(calcDifference(toIdentify.getMostCommonNGrams(), current.getMostCommonNGrams()));
		
		return comp;
	}
	
	/**
	 * Calculates the total difference between the places of the ngrams in the list of ngrams of the unknown language
	 * and the list of ngrams of the current known language.
	 * @param unknown
	 * @param current
	 * @return diff - the total difference
	 */
	private int calcDifference(List<String> unknown, List<String> current) {
		int diff = 0;
		for (String nGram : unknown) {
			if (current.contains(nGram)) {
				int temp = Math.abs(current.indexOf(nGram) - unknown.indexOf(nGram));
				diff += temp;
			}
			else diff += 512;
		}
		return diff;
	}
	
	/**
	 * Sorts ComparisonData objects by their totalDiff value, from lowest to highest.
	 * @return
	 */
	private ComparisonData[] sortBySimilarity() {
		ComparisonData[] comps = new ComparisonData[langComparisons.size()];
		langComparisons.toArray(comps);
		for (int i = 0; i < comps.length - 1; i++) {
			for (int j = i+1; j < comps.length; j++) {
				if (comps[i].getTotal() > comps[j].getTotal()) {
					swap(i, j, comps);
				}
			}
		}
		return comps;
	}
	
	private void swap(int i, int j, ComparisonData[] arr) {
		ComparisonData temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}