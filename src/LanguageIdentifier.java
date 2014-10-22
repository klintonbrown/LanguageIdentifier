import java.io.*;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class LanguageIdentifier {
	private Text textFilePath;
	private static final String[] extension = {"*.txt"};

	protected static ArrayList<LanguageData> languages;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		languages = null;
		readLanguageData();
		if (languages == null) {
			languages = new ArrayList<LanguageData>();
		}
		try {
			LanguageIdentifier window = new LanguageIdentifier();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the main shell window.
	 */
	public void open() {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
		shell.setSize(512, 192);
		shell.setText("Language Identifier");
		shell.setLayout(null);
		
		textFilePath = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textFilePath.setBounds(10, 10, 486, 103);
		
		Button btnIdentify = new Button(shell, SWT.NONE);
		btnIdentify.setToolTipText("Identify the language of text in the text field or of an input file.");
		
		/**
		 * Handles Identify Language button press.
		 */
		btnIdentify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!languages.isEmpty()) {
					
					/**
					 * if there is text in the text box, that text is identified. Otherwise, a file selection
					 * dialog opens and the .txt file to identify is chosen.
					 */
					if (textFilePath.getText().isEmpty()) {
						FileDialog fd = new FileDialog(shell);
						fd.setText("Choose input file");
						fd.setFilterExtensions(extension);
						String path = fd.open();
						if (path != null) {
							LanguageData unknown = new LanguageData("unknown");
							TextAnalyzer ta = new TextAnalyzer(unknown, path, true);
							ta.readInput();
							LanguageComparer lc = new LanguageComparer(unknown, languages);
							
							ComparisonData[] comparisons = lc.compareUnknownToKnown();
							ComparisonDialog cd = new ComparisonDialog(shell, comparisons);
							cd.open();
						}
					}
					else {
						LanguageData unknown = new LanguageData("unknown");
						TextAnalyzer ta = new TextAnalyzer(unknown, textFilePath.getText(), false);
						ta.readInput();
						LanguageComparer lc = new LanguageComparer(unknown, languages);
						
						ComparisonData[] comparisons = lc.compareUnknownToKnown();
						ComparisonDialog cd = new ComparisonDialog(shell, comparisons);
						cd.open();
					}
				}
				else {
					MessageBox mb = new MessageBox(shell, SWT.OK);
					mb.setMessage("No language data for identification.");
					mb.open();
				}
			}
		});
		btnIdentify.setBounds(10, 119, 131, 30);
		btnIdentify.setText("Identify Language");
		
		Button btnAddNewData = new Button(shell, SWT.NONE);
		btnAddNewData.setToolTipText("Add data for a new language, or add more data for a known language.");
		btnAddNewData.setBounds(147, 119, 110, 30);
		
		/**
		 * Handles Add New Data button press.
		 */
		btnAddNewData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LanguageSelector ls = new LanguageSelector(shell, languages);
				ls.open();
				
				try {
					writeLanguageData();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnAddNewData.setText("Add New Data");
		
		/**
		 * Disposes of the main shell.
		 */
		Button btnDone = new Button(shell, SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnDone.setBounds(406, 119, 90, 30);
		btnDone.setText("Done");

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Writes the list of LanguageData objects to an external file.
	 * @throws IOException
	 */
	private static void writeLanguageData() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(new File("data/languagedata.ser"));
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(languages);
		out.close();
		fileOut.close();
	}
	/**
	 * Reads a list of LanguageData objects from an external file.
	 */
	@SuppressWarnings("unchecked")
	private static void readLanguageData() {
		try {
			FileInputStream fileIn = new FileInputStream("data/languagedata.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			languages = (ArrayList<LanguageData>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
