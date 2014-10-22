import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class LanguageNameInputDialog extends Dialog {

	protected String result;
	protected Shell shlLanguageName;
	private Text textInput;
	ArrayList<String> langNames;
	private Button btnCancel;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public LanguageNameInputDialog(Shell parent, ArrayList<String> nameList) {
		super(parent);
		setText("SWT Dialog");
		result = null;
		langNames = nameList;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open() {
		createContents();
		shlLanguageName.open();
		shlLanguageName.layout();
		Display display = getParent().getDisplay();
		while (!shlLanguageName.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlLanguageName = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlLanguageName.setSize(170, 141);
		shlLanguageName.setText("Language Name");
		
		Label lblInputLanguageName = new Label(shlLanguageName, SWT.NONE);
		lblInputLanguageName.setBounds(10, 10, 152, 20);
		lblInputLanguageName.setText("Input language name:");
		
		textInput = new Text(shlLanguageName, SWT.BORDER);
		textInput.setBounds(10, 36, 142, 26);
		
		Button btnDone = new Button(shlLanguageName, SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String input = textInput.getText();
				if (!input.isEmpty()) {
					if (!langNames.contains(input)) {
						result = textInput.getText();
						shlLanguageName.dispose();
						
					}
					else {
						MessageBox mb = new MessageBox(shlLanguageName, SWT.OK | SWT.ICON_ERROR);
						mb.setText("Invalid Name");
						mb.setMessage("Name already exists.");
						mb.open();
					}
				}
				else {
					MessageBox mb = new MessageBox(shlLanguageName, SWT.OK | SWT.ICON_ERROR);
					mb.setText("Invalid Name");
					mb.setMessage("You must input a name.");
					mb.open();
				}
			}
		});
		btnDone.setBounds(10, 68, 71, 30);
		btnDone.setText("Done");
		
		btnCancel = new Button(shlLanguageName, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shlLanguageName.dispose();
			}
		});
		btnCancel.setBounds(87, 68, 71, 30);
		btnCancel.setText("Cancel");
	}
}
