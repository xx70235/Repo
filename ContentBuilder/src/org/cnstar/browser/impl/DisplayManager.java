package org.cnstar.browser.impl;

import java.util.concurrent.CountDownLatch;

import org.eclipse.swt.widgets.Display;

public class DisplayManager {



	private static DisplayManager sc = null;

	private Display display;
	private CountDownLatch initDisplay;
	private boolean ownDisplay = true; 

	public synchronized static DisplayManager getInstance() {
		if (sc == null) {
			sc = new DisplayManager();
		}

		return sc;
	}

	private DisplayManager() {

		initDisplay = new CountDownLatch(1);

		Thread t = new Thread("SWT-Thread") {
			public void run() {

				try {
				
				display = new Display();
				initDisplay.countDown();

				while (true) {
					try {
						if (!display.readAndDispatch())
							display.sleep();
					} catch (Exception e) {
						break;
					}
				}
				
				} catch(Exception e){
					display = Display.getDefault();
					ownDisplay = false;
					initDisplay.countDown();
				}

			}
		};

		t.setDaemon(true);
		t.start();

		try {
			initDisplay.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Display getDisplay() {
		return display;
	}

}
