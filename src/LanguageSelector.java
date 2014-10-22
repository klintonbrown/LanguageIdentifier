import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;


public class LanguageSelector extends Dialog {

	private ArrayList<LanguageData> languages;
	private ArrayList<String> langList;
	protected Shell shell;
	private Text textLangData;
	private Button btnNewLanguage;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public LanguageSelector(Shell parent, ArrayList<LanguageData> langs) {
		super(parent);
		setText("Language Selector");
		languages = langs;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public void open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setSize(480, 384);
		shell.setText(getText());
		
		/**
		 * All languages for which data exists are listed.
		 */
		final List listLangs = new List(shell, SWT.BORDER | SWT.V_SCROLL);

		listLangs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (listLangs.getSelectionCount() == 1) {
					LanguageData selected = null;
					for (LanguageData ld : languages) {
						if (ld.getLanguageName().equals(listLangs.getSelection()[0])) {
							selected = ld;
						}
					}
					if (selected != null)	textLangData.setText(selected.toString());
				}
			}
		});
		listLangs.setBounds(10, 10, 230, 295);
		
		langList = new ArrayList<String>();
		for (LanguageData ld : languages) {
			langList.add(ld.getLanguageName());
		}
		listLangs.setItems(langList.toArray(new String[langList.size()]));
		
		/**
		 * Button to add more data to an existing language.
		 */
		Button btnAddData = new Button(shell, SWT.NONE);
		btnAddData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selected = listLangs.getSelection();
				if (selected.length > 0) {
					LanguageData chosen = null;
					for (LanguageData ld : languages) {
						if (ld.getLanguageName().equals(selected[0])) {
							chosen = ld;
						}
					}
					if (chosen != null) {
						FileDialog fd = new FileDialog(shell);
						fd.setText("Choose input file");
					
						String path = fd.open();
						if (path != null) {
							chosen.clear();
							TextAnalyzer ta = new TextAnalyzer(chosen, path, true);							
							ta.readInput();

							MessageBox mb = new MessageBox(shell, SWT.OK);
							mb.setMessage("Done reading input.");
							mb.open();
						}
					}
				}
				else {
					MessageBox mb = new MessageBox(shell, SWT.OK);
					mb.setMessage("Select a language from the list.");
					mb.open();
				}
			}
		});
		btnAddData.setBounds(10, 311, 160, 30);
		btnAddData.setText("Update Selected");
		
		textLangData = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		textLangData.setEditable(false);
		textLangData.setCapture(true);
		textLangData.setBounds(246, 10, 218, 295);
		
		/**
		 * Button to add a new language.
		 */
		btnNewLanguage = new Button(shell, SWT.NONE);
		btnNewLanguage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LanguageNameInputDialog lnd = new LanguageNameInputDialog(shell, langList);
				String name = lnd.open();
				
				if (name != null) {
					LanguageData newLang = new LanguageData(name);
					FileDialog fd = new FileDialog(shell);
					fd.setText("Choose input file");
				
					String path = fd.open();
					if (path != null) {
						TextAnalyzer ta = new TextAnalyzer(newLang, path, true);
						ta.readInput();
		
						MessageBox mb = new MessageBox(shell, SWT.OK);
						mb.setMessage("Done reading input.");
						mb.open();
						
						languages.add(newLang);
						langList.add(newLang.getLanguageName());
						listLangs.setItems(langList.toArray(new String[langList.size()]));
					}
				}
			}
		});
		btnNewLanguage.setBounds(176, 311, 160, 30);
		btnNewLanguage.setText("Add New Language");
		
		Button btnDone = new Button(shell, SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnDone.setBounds(374, 311, 90, 30);
		btnDone.setText("Done");
	}
}
