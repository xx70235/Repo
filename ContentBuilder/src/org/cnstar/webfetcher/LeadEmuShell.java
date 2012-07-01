package org.cnstar.webfetcher;

import org.cnstar.browser.impl.BrowserImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class LeadEmuShell extends Shell {

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String args[]) {
	try {
	    Display display = Display.getDefault();
	    LeadEmuShell shell = new LeadEmuShell(display);
	    shell.open();
	    shell.layout();
	    while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) {
		    display.sleep();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Create the shell.
     * @param display
     */
    public LeadEmuShell(Display display) {
	super(display, SWT.SHELL_TRIM);
	createContents();
    }

    /**
     * Create contents of the shell.
     */
    protected void createContents() {
	setText("SWT Application");
	setSize(450, 300);
	BrowserImpl browser = new BrowserImpl();
    }

    @Override
    protected void checkSubclass() {
	// Disable the check that prevents subclassing of SWT components
    }

}
