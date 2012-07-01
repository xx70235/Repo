import java.io.File;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIHttpChannel;
import org.mozilla.interfaces.nsIHttpHeaderVisitor;
import org.mozilla.interfaces.nsIObserver;
import org.mozilla.interfaces.nsIObserverService;
import org.mozilla.interfaces.nsIPrefBranch;
import org.mozilla.interfaces.nsIProxyAutoConfig;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.xpcom.GREVersionRange;
import org.mozilla.xpcom.Mozilla;

public class SimpleBrowserWithObserver
{
	public SimpleBrowserWithObserver(String xulrunnerPath)
	{
	    initialize(true);
		final Browser browser;
		Display display = new Display();
		Shell shell = new Shell(display);
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
		String contractID = "@mozilla.org/observer-service;1";
		
		nsIServiceManager serviceManager = Mozilla.getInstance()
				.getServiceManager();
		nsIObserverService observerService = (nsIObserverService) serviceManager
				.getServiceByContractID(contractID,
						nsIObserverService.NS_IOBSERVERSERVICE_IID);
		nsIPrefBranch pref = (nsIPrefBranch) serviceManager.getServiceByContractID("@mozilla.org/preferences-service;1", nsIPrefBranch.NS_IPREFBRANCH_IID);
		pref.setIntPref("network.proxy.type", 1); 
		String socksHost = "174.98.17.165";
		int socksPort = 26497;
		boolean validConfig = false;
		if (socksHost!=null && socksHost.length()>0 && socksPort>0) {
//            pref.setCharPref("network.proxy.socks", socksHost); //$NON-NLS-1$
//            pref.setIntPref("network.proxy.socks_port", socksPort); //$NON-NLS-1$
            validConfig = true;
        } else {
            pref.setCharPref("network.proxy.socks", ""); //$NON-NLS-1$ //$NON-NLS-2$
            pref.setIntPref("network.proxy.socks_port", 0); //$NON-NLS-1$
        }
		if(!validConfig)
		{
			pref.setIntPref("network.proxy.type", 0); 
		}
		 
		SimpleHTTPObserver httpObserver = new SimpleHTTPObserver();
		observerService.addObserver(httpObserver, "http-on-modify-request",
				false);// 获取http-on-modify-request主题（类似与事件监听）
		observerService.addObserver(httpObserver, "http-on-examine-response",
				false);
		browser.setBounds(shell.getClientArea());
		browser.setUrl("http:ip38.com");
		browser.addProgressListener(new ProgressListener()
		{

			@Override
			public void completed(ProgressEvent event)
			{
				nsIWebBrowser webBrowser = (nsIWebBrowser) browser
						.getWebBrowser();
				if (webBrowser == null)
				{
					System.out
							.println("Cound not get the nsIWebBrowser from the Browser widget");

				}
				nsIDOMWindow window = webBrowser.getContentDOMWindow();
				nsIDOMDocument doc = window.getDocument();
				System.out.println(doc);
			}

			@Override
			public void changed(ProgressEvent event)
			{

			}
		});

		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}

		observerService.removeObserver(httpObserver, "http-on-modify-request");

	}
	
	 protected void initialize(boolean isThis) {
		GREVersionRange[] range = new GREVersionRange[1];
		range[0] = new GREVersionRange("1.8", true, "1.9+", true);
		Properties props = null;
		File grePath = null;
		grePath = new File("xulrunner-sdk/bin").getAbsoluteFile();
		System.setProperty("org.eclipse.swt.browser.XULRunnerPath",
			grePath.getAbsolutePath());

		Mozilla mozilla = Mozilla.getInstance();
		mozilla.initialize(grePath);
		try {
		    mozilla.initXPCOM(grePath, null);

		} catch (Throwable t) {
		    System.out.println("initXPCOM failed");
		    t.printStackTrace();
		    return;
		}
		// From this line, we can use JavaXPCOM
		System.out.println("\n--> initialized\n");
	    }

	public static void main(String args[])
	{
		String xulrunnerPath = null;
		if (args.length > 0)
		{
			xulrunnerPath = args[0];

		}
		
		new SimpleBrowserWithObserver(xulrunnerPath);
	}

	class SimpleHTTPObserver implements nsIObserver
	{
		private int nRequests = 0;
		private int nResponses = 0;

		public void observe(nsISupports aSubject, String aTopic, String aData)// 当http-on-modify-request事件发生时，运行此函数
		{
			nsIHttpChannel httpChannel = (nsIHttpChannel) aSubject
					.queryInterface(nsIHttpChannel.NS_IHTTPCHANNEL_IID);

			if (aTopic.equals("http-on-modify-request"))
			{
				nRequests++;
				System.out.println("\n----BEGIN REQUEST NUMBER " + nRequests
						+ " ----\n");
				try
				{
					httpChannel.setRequestHeader("Accept-Language",
							"us-en,us;q=0.8,en-us;q=0.5,en;q=0.3", false);
					httpChannel
							.setRequestHeader(
									"User-Agent",
									"Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405.",
									false);
					if (nRequests == 1)
					{
						httpChannel.setRequestHeader("Referer",
								"http://www.google.com/", false);
					}
				}
				catch (Exception e)
				{
					System.out.println(e.toString());
				}
				// httpChannel.visitRequestHeaders(new nsIHttpHeaderVisitor()
				// {
				// @Override
				// public void visitHeader(String header, String value)
				// {
				// System.out.println("Header: " + header + "-- Value: "
				// + value);
				//
				// }
				//
				// @Override
				// public nsISupports queryInterface(String uuid)
				// {
				// return null;
				// }
				//
				// });
				System.out
						.println(" Method: " + httpChannel.getRequestMethod());
				System.out.println(" Name: " + httpChannel.getName());

				System.out.println(" Host: "
						+ getRequestHeader(httpChannel, "host"));
				System.out.println("  User Agent: "
						+ getRequestHeader(httpChannel, "user-agent"));
				System.out.println("  Accept: "
						+ httpChannel.getRequestHeader("accept"));
				System.out.println("  Accept Language: "
						+ getRequestHeader(httpChannel, "accept-language"));
				System.out.println("  Accept Encoding: "
						+ getRequestHeader(httpChannel, "accept-encoding"));
				System.out.println("  Accept Charset: "
						+ getRequestHeader(httpChannel, "accept-charset"));
				System.out.println("  Keep Alive: "
						+ getRequestHeader(httpChannel, "keep-alive"));
				System.out.println("  Connection: "
						+ getRequestHeader(httpChannel, "connection"));
				System.out.println("  Cookie: "
						+ getRequestHeader(httpChannel, "cookie"));
				System.out.println("\n---- END REQUEST NUMBER " + nRequests
						+ " ----\n");

			}
			else if (aTopic.equals("http-on-examine-response"))
			{
				nResponses++;
				System.out.println("\n------BEGIN RESPONSE NUMBER "
						+ nResponses + " ---------\n");
				httpChannel.visitResponseHeaders(new nsIHttpHeaderVisitor()
				{

					@Override
					public nsISupports queryInterface(String uuid)
					{
						return null;
					}

					@Override
					public void visitHeader(String header, String value)
					{
						System.out.println("Header: " + header + " -- Value: "
								+ value);
					}

				});

				System.out.println(" Status: "
						+ httpChannel.getResponseStatus());
				System.out.println(" Status Text: "
						+ httpChannel.getResponseStatusText());
				System.out.println(" Content Type: "
						+ httpChannel.getContentType());
				System.out.println(" Content Length: "
						+ httpChannel.getContentLength());
				System.out.println(" Content Encoding: "
						+ getResponseHeader(httpChannel, "content-encoding"));
				System.out.println("  Server: "
						+ getResponseHeader(httpChannel, "server"));
				System.out.println("\n---- END RESPONSE NUMBER " + nResponses
						+ " ----\n");
			}
		}

		@Override
		public nsISupports queryInterface(String uuid)
		{
			return Mozilla.queryInterface(this, uuid);
		}

		private String getRequestHeader(nsIHttpChannel httpChannel,
				String header)
		{
			try
			{
				return httpChannel.getRequestHeader(header);

			}
			catch (Exception e)
			{
				return "Header Not Found";
			}
		}

		private String getResponseHeader(nsIHttpChannel httpChannel,
				String header)
		{
			try
			{
				return httpChannel.getRequestHeader(header);
			}
			catch (Exception e)
			{
				return "Header Not Found";
			}
		}
	}
}
