import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class ComparisonDialog extends Dialog {

	//protected Object result;
	protected Shell shell;
	private Text textCompData;
	private ComparisonData[] compData;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ComparisonDialog(Shell parent, ComparisonData[] cd) {
		super(parent);
		setText("Language Comparison");
		compData = cd;
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
		shell.setSize(480, 320);
		shell.setText(getText());
		
		final List listLangs = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		listLangs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (listLangs.getSelectionCount() == 1) {
					ComparisonData selected = null;
					for (ComparisonData cd : compData) {
						if (cd.getLanguage().getLanguageName().equals(listLangs.getSelection()[0])) {
							selected = cd;
						}
					}
					if (selected != null)	textCompData.setText(selected.toString());
				}
			}
		});
		listLangs.setBounds(10, 10, 160, 267);
		
		ArrayList<String> langList = new ArrayList<String>();
		for (ComparisonData cd : compData) {
			langList.add(cd.getLanguage().getLanguageName());
		}
		listLangs.setItems(langList.toArray(new String[langList.size()]));
		
		textCompData = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		textCompData.setEditable(false);
		textCompData.setBounds(176, 10, 288, 267);

	}
}
