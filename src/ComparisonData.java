public class ComparisonData {

	private LanguageData languageComparedTo;

	private double totalDiff;
	
	public ComparisonData(LanguageData comparedTo) {
		languageComparedTo = comparedTo;
		
		totalDiff = 0;
	}
	
	public void setTotal(int diff) {
		totalDiff = diff;
	}
	
	public double getTotal() {
		return totalDiff;
	}
	
	public LanguageData getLanguage() {
		return languageComparedTo;
	}

	@Override
	public String toString() {
		return "Total difference:\n" + totalDiff;
	}
}