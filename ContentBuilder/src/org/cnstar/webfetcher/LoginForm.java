package org.cnstar.webfetcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class LoginForm extends Dialog {

    protected Object result;
    protected Shell shell;
    private Text txUsername;
    private Text txPassword;
    private String username;
    private String password;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public LoginForm(Shell parent, int style) {
	super(parent, style);
	setText("SWT Dialog");
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
	createContents();
	shell.open();
	shell.layout();
	Display display = getParent().getDisplay();
	while (!shell.isDisposed()) {
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
	shell = new Shell(getParent(), getStyle());
	shell.setSize(386, 192);
	shell.setText("\u767B\u5F55\u8BBE\u7F6E");
	
	Label lbUsername = new Label(shell, SWT.NONE);
	lbUsername.setBounds(32, 33, 61, 17);
	lbUsername.setText("\u7528\u6237\u540D\uFF1A");
	
	txUsername = new Text(shell, SWT.BORDER);
	txUsername.setBounds(99, 30, 242, 23);
	
	txPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
	txPassword.setBounds(99, 81, 242, 23);
	
	Label lbPassword = new Label(shell, SWT.NONE);
	lbPassword.setBounds(32, 84, 61, 17);
	lbPassword.setText("\u5BC6\u7801\uFF1A");
	
	Button btOK = new Button(shell, SWT.NONE);
	btOK.setBounds(65, 127, 80, 27);
	btOK.setText("\u786E\u5B9A");
	btOK.addSelectionListener(new SetUserInfo());
	
	Button btCancel = new Button(shell, SWT.NONE);
	btCancel.setBounds(239, 127, 80, 27);
	btCancel.setText("\u53D6\u6D88");
	btCancel.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e)
	    {
		shell.dispose();
	    }
	});

    }
    
    class SetUserInfo implements SelectionListener{

	@Override
	public void widgetSelected(SelectionEvent e) {
	    username = txUsername.getText();
	    password = txPassword.getText();
	    try {
		FileOutputStream out = new FileOutputStream(new File("Login.txt"),false);
		out.write((username+"\n").getBytes());
		out.write((password+"\n").getBytes());
		out.close();
		shell.dispose();
	    } catch (FileNotFoundException e1) {
		e1.printStackTrace();
	    } catch (IOException e2) {
		e2.printStackTrace();
	    }
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	    // TODO Auto-generated method stub
	    
	}
	
    }
}
