import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIWebBrowser;

public class SimpleBrowser
{
	public static void main(String args[])
	{
		final Browser browser;
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(800, 600);
		shell.open();

		try
		{
			browser = new Browser(shell, SWT.MOZILLA);

		}
		catch (SWTError e)
		{
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			return;
		}
		browser.setBounds(shell.getClientArea());
		browser.setUrl("http://www.google.com");
		
		browser.addProgressListener(new ProgressListener()
		{
			
			@Override
			public void completed(ProgressEvent event)
			{
				nsIWebBrowser webBrowser = (nsIWebBrowser)browser.getWebBrowser();
				nsIDOMWindow window = webBrowser.getContentDOMWindow();
				nsIDOMDocument document = window.getDocument();
				System.out.println(document);
			}
			
			@Override
			public void changed(ProgressEvent event)
			{
				// TODO Auto-generated method stub
				
			}
		});
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
	}

}
