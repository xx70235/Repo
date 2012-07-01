/**
 * 
 */
package org.cnstar.browser.impl;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public abstract class ShellManager {
	
	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;
	
	private Shell shell;	
	
	public ShellManager(){
		this(null);
	}

	public ShellManager(Display externalDisplay){
	
		final Display display;
		if(externalDisplay != null){
			display = externalDisplay;
		} else {
			display = DisplayManager.getInstance().getDisplay();
		}
		
		display.syncExec(new Runnable() {
			public void run() {
				shell = new Shell(display);
				shell.setText("Shell");
				shell.setSize(WIDTH, HEIGHT);
				shell.setLayout(new FillLayout());

				configureShell(shell);
				
				shell.open();
			}
		});
		
	}
	
	public abstract void configureShell(Shell shell);

	public void dispose() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.dispose();
			}
		});
	}
	
	public boolean isDisposed()
	{
	    return shell.isDisposed();
	}

	public void syncExec(Runnable runnable) {
		Display.getDefault().syncExec(runnable);		
	}
	
}