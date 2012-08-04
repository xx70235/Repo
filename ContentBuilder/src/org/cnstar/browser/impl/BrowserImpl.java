package org.cnstar.browser.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.naming.spi.DirStateFactory.Result;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cnstar.browser.Configuration;
import org.cnstar.browser.CriticalTimeoutException;
import org.cnstar.browser.FilteredURLException;
import org.cnstar.browser.LadyrBrowser;
import org.cnstar.browser.LadyrBrowserException;
import org.cnstar.browser.LadyrXPathEvaluator;
import org.cnstar.browser.Resource;
import org.cnstar.browser.ResourceListener;
import org.cnstar.browser.URLLoadException;
import org.cnstar.browser.common.StringAnalyzer;
import org.cnstar.utils.XPCOMUtils;
import org.cnstar.webfetcher.model.PageModel;
import org.cnstar.webfetcher.model.ProxyModel;
import org.cnstar.webfetcher.model.RegularUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.mozilla.dom.NodeFactory;
import org.mozilla.dom.html.HTMLDocumentImpl;
import org.mozilla.interfaces.nsICacheService;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsICookieManager;
import org.mozilla.interfaces.nsIDOMAbstractView;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMDocumentEvent;
import org.mozilla.interfaces.nsIDOMDocumentView;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIIOService;
import org.mozilla.interfaces.nsILocalFile;
import org.mozilla.interfaces.nsIPrefBranch;
import org.mozilla.interfaces.nsIPrefService;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsIURI;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.interfaces.nsIWebBrowserPersist;
import org.mozilla.interfaces.nsIWebBrowserSetup;
import org.mozilla.interfaces.nsIWebNavigation;
import org.mozilla.xpcom.Mozilla;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.html2.HTMLIFrameElement;
import org.w3c.dom.html2.HTMLSelectElement;
import org.w3c.dom.html2.HTMLAnchorElement;
import org.w3c.dom.html2.HTMLBRElement;
import org.w3c.dom.html2.HTMLDocument;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLFormElement;
import org.w3c.dom.html2.HTMLInputElement;
import org.w3c.dom.html2.HTMLOptionElement;
import org.w3c.dom.html2.HTMLTextAreaElement;

public class BrowserImpl implements LadyrBrowser {

	private static final int MAX_NUM_RETRIES = 20;
	private static final int RETRY_POOLING_TIME = 500;
	private static final int CONCURRENT_OPERATION_TIMEOUT = 10000;
	private static final int MAX_NUM_RELOADS = 2;

	private CountDownLatch latch = new CountDownLatch(1);
	private LadyrXPathEvaluator xpathEval;
	private Browser browser;
	private Configuration configuration;
	private volatile boolean loading;
	private Display display;
	private ArrayList<ProxyModel> proxyList;
	private Iterator<ProxyModel> proxyIter;
	private ProxyModel currProxyModel;
	private TableItem statusItem;

	public BrowserImpl() {
		this(null, null, null);
	}

	public BrowserImpl(Browser b) {
		this(null, b, null);
	}

	public BrowserImpl(Display eclipseDisplay) {
		this(eclipseDisplay, null, null);
	}

	public BrowserImpl(Display eclipseDisplay, final Browser b, TableItem item) {
		this.display = eclipseDisplay;
		this.statusItem = item;
		if (b != null) {
			open(b);
		} else {
			open();
		}
	}

	public void open(Browser b) {
		this.browser = b;
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				// loading = true;
				// System.out.println("CHANGED "+event.current);
			}

			public void completed(ProgressEvent event) {
				loading = false;
				System.out.println("Document Loaded");
//				setStatus(2, "加载完成");
				finishLoading();
			}
		});

		nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
		String contractID = "@mozilla.org/preferences-service;1";

		nsIPrefService prefService = (nsIPrefService) serviceManager.getServiceByContractID(contractID, nsIPrefService.NS_IPREFSERVICE_IID);

		nsIPrefBranch branch = prefService.getBranch("");
		branch.setBoolPref("security.warn_submit_insecure", 0);
		branch.setBoolPref("javascript.enabled", 1);
	}

	public void setTitle(String title) {
		// TODO Auto-generated method stub

	}

	public void open() {

	}

	private Node getFirstNonNullNode(String... xpaths) {
		for (int i = 0; i < xpaths.length; i++) {
			Node node = xpathNode(xpaths[i]);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	public void waitClick(String... xpaths) {

		Node node;
		int numRetries = 0;
		int numReloads = 0;

		while ((node = getFirstNonNullNode(xpaths)) == null) {

			System.out.println("Retry waitClick() " + numRetries);

			try {
				Thread.sleep(RETRY_POOLING_TIME);
			} catch (InterruptedException e) {
			}

			numRetries++;
			if (numRetries == MAX_NUM_RETRIES) {
				System.out.println("Reload after " + MAX_NUM_RETRIES + " to URL: " + getCurrentURL() + " " + MAX_NUM_RELOADS);
				internalReload();
				numRetries = 0;
				numReloads++;

				if (numReloads == MAX_NUM_RELOADS) {
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPaths: " + Arrays.toString(xpaths) + " in URL "
							+ getCurrentURL());
				}
			}
		}
		click(node);
	}

	public String waitGetCurrentURL(String regex) {

		int numRetries = 0;
		int numReloads = 0;

		while (!getCurrentURL().matches(regex)) {
			System.out.println("Retry waitGetCurrentURL() " + numRetries);

			try {
				Thread.sleep(RETRY_POOLING_TIME);
			} catch (InterruptedException e) {
			}

			numRetries++;
			if (numRetries == MAX_NUM_RETRIES) {
				System.out.println("Reload after " + MAX_NUM_RETRIES + " to URL: " + getCurrentURL() + " " + MAX_NUM_RELOADS);
				internalReload();
				numRetries = 0;
				numReloads++;

				if (numReloads == MAX_NUM_RELOADS) {
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with regex: " + regex + " in URL " + getCurrentURL());
				}
			}
		}
		return getCurrentURL();
	}

	public String waitExtractText(String xpath, String regex) {

		int numRetries = 0;
		int numReloads = 0;

		List<String> results = extractText(xpath);
		while (results.size() == 0 || !results.get(0).matches(regex)) {
			System.out.println("Retry waitExtractText() " + numRetries);

			try {
				Thread.sleep(RETRY_POOLING_TIME);
			} catch (InterruptedException e) {
			}

			numRetries++;
			if (numRetries == MAX_NUM_RETRIES) {
				System.out.println("Reload after " + MAX_NUM_RETRIES + " to URL: " + getCurrentURL() + " " + MAX_NUM_RELOADS);
				internalReload();
				numRetries = 0;
				numReloads++;

				if (numReloads == MAX_NUM_RELOADS) {
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPath: " + xpath + " and regex: " + regex
							+ " in URL " + getCurrentURL());
				}
			}
			results = extractText(xpath);
		}
		return results.get(0);
	}

	public String waitExtractText(String xpath) {

		List<String> results = extractText(xpath);

		int numRetries = 0;
		int numReloads = 0;

		while (results.size() == 0 || results.get(0).equals("")) {
			System.out.println("Retry waitExtractText() " + numRetries);

			try {
				Thread.sleep(RETRY_POOLING_TIME);
			} catch (InterruptedException e) {
			}

			numRetries++;
			if (numRetries == MAX_NUM_RETRIES) {
				System.out.println("Reload after " + MAX_NUM_RETRIES + " to URL: " + getCurrentURL() + " " + MAX_NUM_RELOADS);
				internalReload();
				numRetries = 0;
				numReloads++;

				if (numReloads == MAX_NUM_RELOADS) {
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPath: " + xpath + " in URL " + getCurrentURL());
				}
			}
			results = extractText(xpath);
		}
		return results.get(0);
	}

	public List<String> waitExtractTextList(String xpath) {

		List<String> results = extractText(xpath);

		int numRetries = 0;
		int numReloads = 0;

		while (results.size() == 0) {
			System.out.println("Retry waitExtractTextList() " + numRetries);

			try {
				Thread.sleep(RETRY_POOLING_TIME);
			} catch (InterruptedException e) {
			}

			numRetries++;
			if (numRetries == MAX_NUM_RETRIES) {
				System.out.println("Reload after " + MAX_NUM_RETRIES + " to URL: " + getCurrentURL() + " " + MAX_NUM_RELOADS);

				try {
					executeWithException(new RunnableWithException() {
						public void run() throws Exception {
							browser.stop();
							browser.refresh();
						}
					});
				} catch (Exception e) {
				}

				numRetries = 0;
				numReloads++;

				if (numReloads == MAX_NUM_RELOADS) {
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPath: " + xpath + " in URL " + getCurrentURL());
				}
			}
			results = extractText(xpath);
		}
		return results;
	}

	/**
	 * Executes javascript code in the browser
	 * 
	 * @param javaScript
	 *            javascript code
	 */
	public void executeJavascript(final String javaScript) {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.execute(javaScript);
					// nsIWebNavigation nav =
					// XPCOMUtils.qi((nsIWebBrowser)browser.getWebBrowser(),
					// nsIWebNavigation.class);
					// nav.loadURI("javascript:void " + javaScript,
					// nsIWebNavigation.LOAD_FLAGS_NONE, null, null, null);
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during javascript execution.", e);
		}

		// if (Display.getCurrent() != null) {
		// browser.execute(javaScript);
		// } else {
		// display.asyncExec(new Runnable() {
		// public void run() {
		// browser.execute(javaScript);
		// }
		// });
		// }
	}

	// public void onStateChange(nsIWebProgress aWebProgress, nsIRequest
	// aRequest,
	// long aStateFlags, long aStatus) {
	//
	// boolean isDocument = (aStateFlags &
	// nsIWebProgressListener.STATE_IS_DOCUMENT) != 0;
	// boolean start = (aStateFlags & nsIWebProgressListener.STATE_START) != 0;
	// boolean stop = (aStateFlags & nsIWebProgressListener.STATE_STOP) != 0;
	//
	// if (isDocument) {
	// if (start) {
	// this.documentEvent(EventType.START);
	// } else if (stop) {
	// this.documentEvent(EventType.STOP);
	// }
	// }
	//
	// }

	// public Browser getMozillaBrowser() {
	// return browser;
	// }

	public Configuration getConfiguration() {
		if (this.configuration == null) {
			this.configuration = new ConfigurationImpl();
		}
		return this.configuration;
	}

	// @Override
	// protected void execute(final Runnable runnable) {
	// if (Display.getCurrent() != null) {
	// runnable.run();
	// } else {
	// display.syncExec(new Runnable() {
	// public void run() {
	// runnable.run();
	// }
	// });
	// }
	// }

	public void loadHtml(String html) {
		// Next, encode the data using the target encoding.
		// The String.getBytes() method does this.
		try {
			final int caseDiff = ('a' - 'A');
			StringBuilder out = new StringBuilder();
			byte[] ba = html.getBytes("utf16");

			for (int j = 0; j < ba.length; j++) {
				out.append('%');
				char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
				// converting to use uppercase letter as part of
				// the hex value if ch is a letter.
				if (Character.isLetter(ch)) {
					ch -= caseDiff;
				}
				out.append(ch);
				ch = Character.forDigit(ba[j] & 0xF, 16);
				if (Character.isLetter(ch)) {
					ch -= caseDiff;
				}
				out.append(ch);
			}

			html = out.toString();

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		final String content = html;

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.setText(content);
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during HTML loading.", e);
		}
	}

	public void saveURL(final String url, final File file) {
		System.out.println("     Saving file:" + url);

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					Mozilla Moz = Mozilla.getInstance();
					nsIServiceManager serviceManager = Moz.getServiceManager();
					nsIIOService IOService = (nsIIOService) serviceManager.getServiceByContractID("@mozilla.org/network/io-service;1",
							nsIIOService.NS_IIOSERVICE_IID);

					nsIURI source = IOService.newURI(url, null, null);

					nsIComponentManager componentManager = Moz.getComponentManager();
					nsILocalFile destination = (nsILocalFile) componentManager.createInstanceByContractID("@mozilla.org/file/local;1", null,
							nsILocalFile.NS_ILOCALFILE_IID);
					destination.initWithPath(file.getAbsolutePath());
					nsIWebBrowserPersist nsWebBrowserPersist = (nsIWebBrowserPersist) componentManager.createInstanceByContractID(
							"@mozilla.org/embedding/browser/nsWebBrowserPersist;1", null, nsIWebBrowserPersist.NS_IWEBBROWSERPERSIST_IID);
					nsWebBrowserPersist.setPersistFlags(nsIWebBrowserPersist.PERSIST_FLAGS_BYPASS_CACHE);
					nsWebBrowserPersist.saveURI(source, null, null, null, null, destination);
					System.out.println("     file " + file.getAbsolutePath() + " saved");
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("URL could not be saved successfully.", e);
		}
	}

	public void clearCache() {
		nsICacheService cacheService = (nsICacheService) Mozilla.getInstance().getServiceManager()
				.getServiceByContractID("@mozilla.org/network/cache-service;1", nsICacheService.NS_ICACHESERVICE_IID);

		/*
		 * for now since the NSI interface for nsICache in Java does not provide
		 * access to the nsICache.STORE_ON_DISK and nsICache.STORE_IN_MEMORY,
		 * need to pass the actual values. (Got values from LXR)
		 * 
		 * const nsCacheStoragePolicy STORE_IN_MEMORY = 1; const
		 * nsCacheStoragePolicy STORE_ON_DISK = 2;
		 */
		cacheService.evictEntries(1);
		// cacheService.evictEntries(2);

	}

	// public void setTitle(final String title) {
	// try {
	// executeWithException(new RunnableWithException() {
	// public void run() throws Exception {
	// display.getActiveShell().setText(title);
	// }
	// });
	// } catch (Exception e) {
	// throw new LadyrBrowserException("Cannot set the title", e);
	// }
	//
	// }

	public void submitForm(final HTMLFormElement form) {
		System.out.println("Entering submit form.");
		checkIfDocumentLoaded();
		latch = new CountDownLatch(1);
		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					form.submit();
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during form submiting.", e);
		}
		waitLoad();
		// System.out.println("Leaving submit form. NS:" + numStart);
	}

	public void submitForm(final HTMLFormElement form, final double waitSeconds) throws TimeoutException {

		if (waitSeconds == -1) {
			submitForm(form);

		} else {
			checkIfDocumentLoaded();
			latch = new CountDownLatch(1);

			try {
				executeWithException(new RunnableWithException() {
					public void run() throws Exception {
						form.submit();
						boolean timeout = waitLoad((long) (waitSeconds * 1000));
						if (timeout) {
							throw new TimeoutException();
						}
					}
				});
			} catch (Exception e) {
				throw (TimeoutException) e;
			}
		}

	}

	// public void go(String url) throws FilteredURLException {
	//
	// if (url.endsWith("#")) {
	// url = url.substring(0, url.length() - 1);
	// }
	//
	// url = parseURL(url);
	//
	// System.out.println("Go to " + url);
	// checkIfDocumentLoaded();
	//
	// // System.out.println("Creating lath");
	// latch = new CountDownLatch(1);
	// filteredGo(url);
	//
	// waitLoad();
	// // System.out.println("Leaving go. NS:" + numStart);
	// }

	public void go(String url) throws FilteredURLException, CriticalTimeoutException {

		if (url.endsWith("#")) {
			url = url.substring(0, url.length() - 1);
		}
		url = parseURL(url);
		System.out.println("前往 " + url);
		latch = new CountDownLatch(1);
		filteredGo(url);
		waitLoad();

	}

	public void go(String url, double secondsWait) throws TimeoutException, FilteredURLException {

		if (secondsWait == -1) {
			go(url);

		} else {
			url = parseURL(url);

			checkIfDocumentLoaded();

			latch = new CountDownLatch(1);
			filteredGo(url);

			boolean timeout = waitLoad((long) (secondsWait * 1000));
			// if (timeout) {
			// System.out.println("Timeout");
			// }
			if (timeout) {
				throw new TimeoutException();
			}

		}

	}

	public void go(String url, double secondsWait, int numRetries) throws TimeoutException {
		// System.out.println("Entering go with retries. NS:" + numStart);

		int cNumRetries = -1;
		boolean timeout = false;
		do {

			// if (cNumRetries > -1) {
			// System.out.println("Retring.... " + cNumRetries);
			// }
			try {
				go(url, secondsWait);
			} catch (TimeoutException e) {
				timeout = true;
			}
			cNumRetries++;

		} while (timeout && cNumRetries < numRetries);

		if (timeout) {
			throw new TimeoutException();
		}
		// System.out.println("Leaving go with retries. NS:" + numStart);
	}

	public void click(String xpathNode) {
		Node node = this.xpathNode(xpathNode);
		if (node == null) {
			throw new LadyrBrowserException("Trying clicking in XPath returning null");
		} else {
			click(node);
		}
	}

	public void click(Node node) {
		try {
			click(node, -1);
		} catch (TimeoutException te) {
		}
	}

	public void click(Node node, double waitSeconds) throws TimeoutException {
		checkIfDocumentLoaded();

		nsIDOMNode domNode = NodeFactory.getnsIDOMNode(node);
		if (domNode instanceof nsIDOMElement) {
			click((nsIDOMElement) domNode);
			return;
		}

		if (node instanceof HTMLElement) {
			final HTMLElement ele = (HTMLElement) node;

			try {
				executeWithException(new RunnableWithException() {
					public void run() {

						String tmp = ele.getAttribute("onclick");
						if (tmp != null && !tmp.equals("")) {
							if (tmp.contains("this")) {
								tmp = tmp.replace("this", "document.getElementById('" + ele.getAttribute("id") + "')");
							}
							executeJavascript(tmp);
						}
					}
				});
			} catch (Exception e) {
				throw new LadyrBrowserException("Exception during click.", e);
			}
		}

		if (node instanceof HTMLInputElement) {
			HTMLInputElement button = (HTMLInputElement) node;

			if (button.getType().equalsIgnoreCase("submit") || button.getType().equalsIgnoreCase("image")) {
				String formAction = button.getForm().getAction();
				if (formAction != null && !formAction.equals("")) {
					submitForm(button.getForm(), waitSeconds);
				}
			} else if (button.getType().equalsIgnoreCase("radio")) {
				button.setChecked(true);
			}

		} else if (node instanceof HTMLOptionElement) {

			HTMLOptionElement option = (HTMLOptionElement) node;

			option.setSelected(true);

		} else if (node instanceof HTMLAnchorElement) {

			HTMLAnchorElement link = (HTMLAnchorElement) node;

			if (link.getHref() != null && !link.getHref().equals("") && !link.getHref().equals(getCurrentURL() + "#")) {
				go(link.getHref(), waitSeconds);
			}

		} else if (!(node instanceof HTMLElement)) {
			throw new LadyrBrowserException("click only works with HTMLElements with onclick "
					+ " attribute or links (HTMLAnchorElement) or buttons (HTMLButtonElement)");
		}

	}

	public void click(final nsIDOMElement elem) {
		// assert isMozillaThread();

		// based on
		// http://developer.mozilla.org/en/docs/DOM:document.createEvent
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				nsIDOMDocument doc = elem.getOwnerDocument();
				nsIDOMDocumentEvent evdoc = XPCOMUtils.qi(doc, nsIDOMDocumentEvent.class);
				nsIDOMEvent ev = evdoc.createEvent("MouseEvents"); //$NON-NLS-1$
				nsIDOMMouseEvent mev = XPCOMUtils.qi(ev, nsIDOMMouseEvent.class);
				nsIDOMDocumentView view = XPCOMUtils.qi(doc, nsIDOMDocumentView.class);
				nsIDOMAbstractView aview = view.getDefaultView();
				mev.initMouseEvent("click", true, true, aview, 0, 0, 0, 0, 0, false, false, false, false, 0, null); //$NON-NLS-1$
				nsIDOMEventTarget evt = XPCOMUtils.qi(elem, nsIDOMEventTarget.class);
				evt.dispatchEvent(mev);

			}
		});
	}

	public void enter(final Node node, final String text) {
		if (node instanceof HTMLInputElement) {
			HTMLInputElement textComponent = (HTMLInputElement) node;
			enter(textComponent, text);
			// type((nsIDOMElement)NodeFactory.getnsIDOMNode(textComponent),
			// text);
		} else if (node instanceof HTMLTextAreaElement) {
			HTMLTextAreaElement textComponent = (HTMLTextAreaElement) node;
			enter(textComponent, text);
			// type((nsIDOMElement)NodeFactory.getnsIDOMNode(textComponent),
			// text);
		} else {
			throw new LadyrBrowserException("enter only works with textfield (HTMLInputElement) or textarea (HTMLTextAreaElement)");
		}

	}

	public void enter(final HTMLInputElement component, String text) {
		checkIfDocumentLoaded();

		final String inputText;
		if (text == null) {
			inputText = "";
		} else {
			inputText = text;
		}
		// nsIDOMNode domNode = NodeFactory.getnsIDOMNode(component);
		// type((nsIDOMElement) domNode, inputText);
		// if(domNode instanceof nsIDOMElement)
		// {
		// try {
		// executeWithException(new RunnableWithException() {
		// public void run() {
		// type((nsIDOMElement)domNode, inputText);
		//
		// }
		// });
		// } catch (Exception e) {
		// throw new LadyrBrowserException("Exception during enter.", e);
		// }
		//
		// }

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					if (component.hasAttribute("onfocus")) {
						String tmp = component.getAttribute("onfocus");
						if (tmp.contains("this")) {
							tmp = tmp.replace("this", "document.getElementById('" + component.getAttribute("id") + "')");
						}
						executeJavascript(tmp);
					}

				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during enter.", e);
		}

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					component.setValue(inputText);
				}
			});

		} catch (Exception e) {
			throw new LadyrBrowserException("Could not enter the text successfully.", e);
		}
		//
		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					if (component.hasAttribute("onchange")) {
						String tmp = component.getAttribute("onchange");
						if (tmp.contains("this")) {
							tmp = tmp.replace("this", "document.getElementById('" + component.getAttribute("id") + "')");
						}
						executeJavascript(tmp);
					}
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during enter.", e);
		}

	}

	public void type(final nsIDOMElement elem, final String text) {
		// assert isMozillaThread();

		// based on http://developer.mozilla.org/en/docs/DOM:event.initKeyEvent
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				nsIDOMDocument doc = elem.getOwnerDocument();
				nsIDOMDocumentEvent evdoc = XPCOMUtils.qi(doc, nsIDOMDocumentEvent.class);
				nsIDOMEvent ev = evdoc.createEvent("KeyboardEvent"); //$NON-NLS-1$
				nsIDOMKeyEvent mev = XPCOMUtils.qi(ev, nsIDOMKeyEvent.class);
				nsIDOMDocumentView view = XPCOMUtils.qi(doc, nsIDOMDocumentView.class);
				nsIDOMAbstractView aview = view.getDefaultView();
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					mev.initKeyEvent("keypress", true, true, aview, false, false, false, false, 0, c); //$NON-NLS-1$
					nsIDOMEventTarget evt = XPCOMUtils.qi(elem, nsIDOMEventTarget.class);
					evt.dispatchEvent(mev);
				}
			}
		});

	}

	public void enter(final HTMLTextAreaElement component, String text) {
		checkIfDocumentLoaded();

		final String inputText;
		if (text == null) {
			inputText = "";
		} else {
			inputText = text;
		}

		try {
			executeWithException(new RunnableWithException() {
				public void run() {

					NodeList nodeList = component.getChildNodes();
					for (int i = 0; i < nodeList.getLength(); i++) {
						component.removeChild(nodeList.item(i));
					}
					component.appendChild(getDocument().createTextNode(inputText));

				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Could not enter the text successfully.", e);
		}
	}

	public Document getDocument() {
		checkIfDocumentLoaded();
		return internalGetDocument();
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression.
	 * @return the node corresponding to the given xpath expression. If more
	 *         than one node is found for that xpath, then the first node found
	 *         is returned. The whole document is taken as context for the
	 *         evaluation.
	 **/
	public Node xpathNode(String xpath) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNode(this, xpath);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the node corresponding to the given xpath expression within the
	 *         given context. If more than one node is found for that xpath,
	 *         then the first node found is returned.
	 **/
	public Node xpathNode(String xpath, Node context) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNode(this, xpath, context);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted node
	 * @return the node corresponding to the given xpath expression. If more
	 *         than one node is found for that xpath, then the first node found
	 *         is returns. The whole document is taken as context for the
	 *         evaluation.
	 **/
	public <T extends Node> T xpathNode(String xpath, Class<T> nodeClass) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNode(this, xpath, nodeClass);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted node
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the node corresponding to the given xpath expression within the
	 *         given context. If more than one node is found for that xpath,
	 *         then the first node found is returns.
	 **/
	public <T extends Node> T xpathNode(String xpath, Class<T> nodeClass, Node context) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNode(this, xpath, nodeClass, context);
	}

	/**
	 * @param xpath
	 *            xpath expression
	 * @return the nodes corresponding to the given xpath expression. The whole
	 *         document is taken as context for the evaluation.
	 */
	public List<Node> xpathNodes(String xpath) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNodes(this, xpath);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted nodes.
	 * @return the nodes corresponding to the given xpath expression.
	 **/
	public <T extends Node> List<T> xpathNodes(String xpath, Class<T> nodeClass) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNodes(this, xpath, nodeClass);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the nodes corresponding to the given xpath expression within the
	 *         given context.
	 **/
	public List<Node> xpathNodes(String xpath, Node context) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNodes(this, xpath, context);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted nodes
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the nodes corresponding to the given xpath expression within the
	 *         given context.
	 **/
	public <T extends Node> List<T> xpathNodes(String xpath, Node context, Class<T> nodeClass) {
		checkIfDocumentLoaded();
		return xpathEval.xpathNodes(this, xpath, nodeClass, context);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return the text content of a node and its childrens using spaces to
	 *         merge the text of a node and its child. The root node for the
	 *         extraction is represented by the <code>xpath</code>, taking the
	 *         whole document as context. If the xpath matches with more than
	 *         one node, the result for every node will be stored in a different
	 *         position in the results list.
	 */
	public List<String> extractText(String xpath) {
		return this.extractText(xpath, null, null);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text. If that parameter is <code>null</code>, then the whole
	 *            text is taken, and no regular expression applied. The text we
	 *            want to extract must correspond to group number 1 within the
	 *            regular expression.
	 * @return the text content of a node and its childrens using spaces to
	 *         merge the text of a node and its child and applying the
	 *         <code>regExp</code> to the resulting text. The root node for the
	 *         extraction is represented by the <code>xpath</code>, taking the
	 *         whole document as context. If the xpath matches with more than
	 *         one node, the result for every node will be stored in a different
	 *         position in the results list.
	 */
	public List<String> extractText(String xpath, String regExp) {
		return this.extractText(xpath, regExp, null);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the text content of a node and its childrens using spaces to
	 *         merge the text of a node and its child. The root node for the
	 *         extraction is represented by the <code>xpath</code>, resolved
	 *         from the given <code>context</code>. If the xpath matches with
	 *         more than one node, the result for every node will be stored in a
	 *         different position in the results list.
	 */
	public List<String> extractText(String xpath, Node context) {
		return this.extractText(xpath, null, context);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text. If that parameter is <code>null</code>, then the whole
	 *            text is taken, and no regular expression applied. The text we
	 *            want to extract must correspond to group number 1 within the
	 *            regular expression.
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the text content of a node and its childrens using spaces to
	 *         merge the text of a node and its child and applying the
	 *         <code>regExp</code> to the resulting text. The root node for the
	 *         extraction is represented by the <code>xpath</code>, resolved
	 *         from the given <code>context</code>. If the xpath matches with
	 *         more than one node, the result for every node will be stored in a
	 *         different position in the results list.
	 */
	public List<String> extractText(String xpath, String regExp, Node context) {
		return this.extractText(xpath, regExp, context, " ");
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text. If that parameter is <code>null</code>, then the whole
	 *            text is taken, and no regular expression applied. The text we
	 *            want to extract must correspond to group number 1 within the
	 *            regular expression.
	 * @param nodeSeparator
	 * @return the text content of a node and its childrens using the given
	 *         <code>nodeSeparator</code> to merge the text of a node and its
	 *         child and applying the <code>regExp</code> to the resulting text.
	 *         The root node for the extraction is represented by the
	 *         <code>xpath</code>, resolved from the given <code>context</code>.
	 *         If the xpath matches with more than one node, the result for
	 *         every node will be stored in a different position in the results
	 *         list.
	 */
	public List<String> extractText(String xpath, String regExp, Node context, String nodeSeparator) {

		List<String> results = new ArrayList<String>();

		List<Node> nodes = this.xpathNodes(xpath, context);
		for (Node node : nodes) {
			results.add(StringAnalyzer.extract(getTextContent(node, nodeSeparator), regExp));
		}

		return results;

	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return normalized text corresponding to a node and its childrens, given
	 *         its xpath. If the xpath matches with more than one node, the
	 *         result for every node will be stored in a different position in
	 *         the results list.
	 * @see es.ladyr.common.StringAnalyzer.normalize(String text)
	 */
	public List<String> extractNormText(String xpath) {
		return StringAnalyzer.normalize(this.extractText(xpath));
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * 
	 * @return normalized text corresponding to a node and its childrens,
	 *         resolving its xpath from the given context. If the xpath matches
	 *         with more than one node, the result for every node will be stored
	 *         in a different position in the results list.
	 * @see es.ladyr.common.StringAnalyzer.normalize(String text)
	 */
	public List<String> extractNormText(String xpath, Node context) {
		return StringAnalyzer.normalize(this.extractText(xpath, context));
	}

	/**
	 * First, the text is extracted as is. Second, a regular expression is
	 * applied to that text. And finally, the resulted text is normalized.
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text. If that parameter is <code>null</code>, then the whole
	 *            text is taken, and no regular expression applied. The text we
	 *            want to extract must correspond to group number 1 within the
	 *            regular expression.
	 * @return normalized text corresponding to a node and its children after
	 *         applying the given regular expression. If the xpath matches with
	 *         more than one node, the result for every node will be stored in a
	 *         different position in the results list.
	 * @see es.ladyr.common.StringAnalyzer.normalize(String text)
	 */
	public List<String> extractNormText(String xpath, String regExp) {
		return StringAnalyzer.normalize(this.extractText(xpath, regExp));
	}

	/**
	 * First, the text is extracted in the given context. Second, a regular
	 * expression is applied to that text. And finally, the resulted text is
	 * normalized.
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text. If that parameter is <code>null</code>, then the whole
	 *            text is taken, and no regular expression applied. The text we
	 *            want to extract must correspond to group number 1 within the
	 *            regular expression.
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return normalized text corresponding to a node and its childrens,
	 *         resolving its xpath from the given context, after applying the
	 *         given regular expression. If the xpath matches with more than one
	 *         node, the result for every node will be stored in a different
	 *         position in the results list.
	 * @see es.ladyr.common.StringAnalyzer.normalize(String text)
	 */
	public List<String> extractNormText(String xpath, String regExp, Node context) {
		return StringAnalyzer.normalize(this.extractText(xpath, regExp, context));
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return the Integer value resulted from parsing the text corresponding to
	 *         the given <code>xpath</code> taking the whole document as
	 *         context. If that text does not correspond to an
	 *         <code>Integer</code>, then returns <code>null</code>. If the
	 *         xpath matches with more than one node, the result for every node
	 *         will be stored in a different position in the results list.
	 */
	public List<Integer> extractInt(String xpath) {
		return StringAnalyzer.parseInt(this.extractText(xpath));
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the Integer value resulted from parsing the text corresponding to
	 *         the given <code>xpath</code> in the given context. If that text
	 *         does not correspond to an <code>Integer</code>, then returns
	 *         <code>null</code>. If the xpath matches with more than one node,
	 *         the result for every node will be stored in a different position
	 *         in the results list.
	 */
	public List<Integer> extractInt(String xpath, Node context) {
		return StringAnalyzer.parseInt(this.extractText(xpath, context));
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text to obtain the <code>Integer</code>. If that parameter is
	 *            <code>null</code>, then the whole text is taken, and no
	 *            regular expression applied. The text we want to extract must
	 *            correspond to group number 1 within the regular expression.
	 * @return the Integer value resulted from parsing the text corresponding to
	 *         the given <code>xpath</code> once the regular expression has been
	 *         applied. If that text does not correspond to an
	 *         <code>Integer</code>, then returns <code>null</code>. If the
	 *         xpath matches with more than one node, the result for every node
	 *         will be stored in a different position in the results list.
	 */
	public List<Integer> extractInt(String xpath, String regExp) {
		return StringAnalyzer.extractInt(this.extractText(xpath), regExp);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param regExp
	 *            the regular expression that will be applied to the extracted
	 *            text to obtain the <code>Integer</code>. If that parameter is
	 *            <code>null</code>, then the whole text is taken, and no
	 *            regular expression applied. The text we want to extract must
	 *            correspond to group number 1 within the regular expression.
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the Integer value resulted from parsing the text corresponding to
	 *         the given <code>xpath</code> in the given context once the
	 *         regular expression has been applied. If that text does not
	 *         correspond to an <code>Integer</code>, then returns
	 *         <code>null</code>. If the xpath matches with more than one node,
	 *         the result for every node will be stored in a different position
	 *         in the results list.
	 */
	public List<Integer> extractInt(String xpath, String regExp, Node context) {
		return StringAnalyzer.extractInt(this.extractText(xpath, context), regExp);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return the <code>String</code> corresponding to the given
	 *         <code>xpath</code> taking the whole document as context.
	 */
	public String xpathString(String xpath) {
		checkIfDocumentLoaded();
		return xpathEval.xpathString(this, xpath);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the <code>String</code> value corresponding to the given
	 *         <code>xpath</code> in the given context.
	 */
	public String xpathString(String xpath, Node context) {
		checkIfDocumentLoaded();
		return xpathEval.xpathString(this, xpath, context);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return the <code>boolean</code> value corresponding to the given
	 *         <code>xpath</code> taking the whole document as context.
	 */
	public boolean xpathBoolean(String xpath) {
		checkIfDocumentLoaded();
		return xpathEval.xpathBoolean(this, xpath);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the <code>boolean</code> value corresponding to the given
	 *         <code>xpath</code> in the given context.
	 */
	public boolean xpathBoolean(String xpath, Node context) {
		checkIfDocumentLoaded();
		return xpathEval.xpathBoolean(this, xpath, context);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return the <code>Double</code> value corresponding to the given
	 *         <code>xpath</code> taking the whole document as context.
	 */
	public double xpathDouble(String xpath) {
		checkIfDocumentLoaded();
		return xpathEval.xpathDouble(this, xpath);
	}

	/**
	 * 
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the <code>Double</code> value corresponding to the given
	 *         <code>xpath</code> in the given context.
	 */
	public double xpathDouble(String xpath, Node context) {
		checkIfDocumentLoaded();
		return xpathEval.xpathDouble(this, xpath, context);
	}

	public boolean back() {
		boolean result = internalBack();
		waitLoad();
		return result;
	}

	public boolean forward() {
		boolean result = internalGoForward();
		waitLoad();
		return result;
	}

	public void reload() {
		internalReload();
		waitLoad();
	}

	public String getCurrentURL() {
		checkIfDocumentLoaded();
		return internalGetCurrentURL();
	}

	public String getReferringURL() {
		checkIfDocumentLoaded();
		return internalGetReferringURL();
	}

	public Resource downloadResource(String url) {

		final Resource resource = new Resource();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		downloadResource(url, new ResourceListener() {
			public void setContentType(String contentType) {
				resource.setContentType(contentType);
			}

			public OutputStream getOutputStream() {
				return os;
			}

			public void setURL(String url) {
				resource.setURL(url);
			}
		});
		resource.setData(os.toByteArray());
		return resource;
	}

	public void downloadResource(String url, ResourceListener listener) {

		String currentURL = getCurrentURL();

		URL rUrl;
		try {
			if (currentURL.equals("")) {
				if (!url.startsWith("https://") && !url.startsWith("http://")) {
					url = "http://" + url;
				}
				rUrl = new URL(url);
			} else {
				rUrl = new URL(new URL(getCurrentURL()), url);
			}

		} catch (MalformedURLException e) {
			throw new LadyrBrowserException("Invalid URL: " + url, e);
		}

		listener.setURL(rUrl.toString());

		try {

			HttpURLConnection connection = (HttpURLConnection) rUrl.openConnection();

			connection.setDoInput(true);
			connection.setUseCaches(true);
			connection.setInstanceFollowRedirects(true);

			connection.setAllowUserInteraction(false);

			connection.setRequestProperty("User-Agent", this.getConfiguration().getUserAgent());
			connection.setConnectTimeout((int) this.getConfiguration().getTimeout());
			connection.setReadTimeout((int) this.getConfiguration().getTimeout());

			int responseCode = connection.getResponseCode();

			if (responseCode == 200) {

				InputStream is = connection.getInputStream();

				OutputStream os = listener.getOutputStream();

				byte b[] = new byte[1024];
				int numRead;
				while ((numRead = is.read(b)) != -1) {
					os.write(b, 0, numRead);
				}

				os.close();
				connection.disconnect();
				listener.setContentType(connection.getContentType());
			} else {
				connection.disconnect();
				throw new URLLoadException(rUrl, responseCode);
			}

		} catch (ConnectException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// throw new
			// LadyrBrowserException("IOExcepion during access to resource", e);
			e.printStackTrace();
		}

	}

	// private void documentEvent(EventType eventType) {
	//
	// switch (eventType) {
	//
	// case START:
	// numStart++;
	// break;
	// case STOP:
	// numStart--;
	// if (numStart == 0) {
	// finishLoading();
	// }
	// break;
	// }
	// }

	private void finishLoading() {
		if (this.xpathEval == null) {
			this.xpathEval = createXPathEval();
		}

		if (latch.getCount() == 1) {
			latch.countDown();
		}
	}

	private void waitLoad() {
		System.out.println("Wait load");
		setStatus(2, "等待加载");
		for (int i = 0; i < 5; i++) {
			boolean timeout = waitLoad(getConfiguration().getTimeout());
			if (!timeout) {
				// System.out.println("Return");
				return;
			} else {
				try {
					System.out.println("Retry...");
					executeWithException(new RunnableWithException() {
						public void run() throws Exception {
							browser.stop();
							browser.refresh();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		throw new CriticalTimeoutException();
	}

	private void checkIfDocumentLoaded() {
		// if (numStart > 0) {
		if (loading) {

			System.out.println("LOADING....");
			setStatus(2, "页面加载中");
			latch = new CountDownLatch(1);
			try {

				boolean timeout = !latch.await(CONCURRENT_OPERATION_TIMEOUT, TimeUnit.MILLISECONDS);

				int numRetries = 0;
				while (timeout) {

					numRetries++;
					System.out.println("RETRY DOCUMENT LOADED");
					setStatus(2, "RETRY DOCUMENT LOADED");
					try {
						executeWithException(new RunnableWithException() {
							public void run() throws Exception {
								browser.stop();
								browser.refresh();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}

					timeout = !latch.await(CONCURRENT_OPERATION_TIMEOUT, TimeUnit.MILLISECONDS);

					if (numRetries == MAX_NUM_RETRIES) {
						throw new LadyrBrowserException("Accessing to document while loading");
					}
				}

			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	}

	public String getAbsoluteURL(String url) {

		String currentURL = this.getCurrentURL();
		if (currentURL == null || currentURL.equals("")) {

			if (!url.startsWith("http://") || !url.startsWith("https://")) {
				url = "http://" + url;
			}
			try {
				return new URL(url).toString();
			} catch (MalformedURLException e) {
				return null;
			}
		} else {
			try {
				return new URL(new URL(this.getCurrentURL()), url).toString();
			} catch (MalformedURLException e) {
				return null;
			}
		}

	}

	private boolean waitLoad(long millis) {
		try {
			boolean timeout;
			timeout = !latch.await(millis, TimeUnit.MILLISECONDS);

			if (timeout) {
				internalStop();
				latch.await(millis, TimeUnit.MILLISECONDS);
			}
			return timeout;
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

	private void filteredGo(String url) throws FilteredURLException {
		internalGo(url);
	}

	private String parseURL(String url) {
		if (url == null) {
			throw new LadyrBrowserException("The url cannot be null");
		}

		// String current = this.getCurrentURL();
		// if (current != null && !current.equals("")) {
		// URL rURL;
		// try {
		// rURL = new URL(new URL(current), url);
		// url = rURL.toString();
		// } catch (MalformedURLException e) {
		// }
		// }
		return url;
	}

	private String getTextContent(Node node, String nodeSeparator) {
		StringBuilder sb = new StringBuilder();
		fillTextContent(sb, node, nodeSeparator);
		String text = sb.toString();
		return text;
	}

	private void fillTextContent(StringBuilder sb, Node node, String nodeSeparator) {
		if (node instanceof Text) {
			Text t = (Text) node;
			sb.append(t.getData());

		} else {

			NodeList nList = node.getChildNodes();
			for (int i = 0; i < nList.getLength(); i++) {
				Node child = nList.item(i);
				if (child instanceof Comment || child instanceof ProcessingInstruction) {
					continue;
				} else if (child instanceof Text) {
					Text t = (Text) child;
					sb.append(t.getData());
					if (sb.length() > 0) {
						sb.append(nodeSeparator);
					}
				} else if (child instanceof HTMLBRElement) {
					if (sb.length() > 0) {
						sb.append(nodeSeparator);
					}
				} else {
					fillTextContent(sb, child, nodeSeparator);
				}
			}
		}

	}

	public void close() {
		this.display.dispose();
	}

	private LadyrXPathEvaluator createXPathEval() {
		return new XPathEvaluatorImpl();
	}

	private void internalStop() throws LadyrBrowserException {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.stop();
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during javascript execution.", e);
		}

	}

	private void internalGo(final String url) {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.setUrl(url);
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during javascript execution.", e);
		}

	}

	private HTMLDocument internalGetDocument() {

		return HTMLDocumentImpl.getDOMInstance(getDOMHTMLDocument());
	}

	private nsIDOMHTMLDocument getDOMHTMLDocument() {

		try {
			return (nsIDOMHTMLDocument) executeWithException(new RunnableWithReturn() {
				public Object run() {

					nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
					if (webBrowser == null) {
						System.out.println("Could not get the nsIWebBrowser from the Browser widget");
						return null;
					}

					nsIDOMWindow window = webBrowser.getContentDOMWindow();
					nsIDOMDocument nsDoc = window.getDocument();

					nsIDOMHTMLDocument htmldoc = (nsIDOMHTMLDocument) nsDoc.queryInterface(nsIDOMHTMLDocument.NS_IDOMHTMLDOCUMENT_IID);

					return htmldoc;
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception getting current URL.", e);
		}

	}

	private boolean internalBack() {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.back();
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during javascript execution.", e);
		}

		return false;
	}

	private boolean internalGoForward() {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.forward();
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during javascript execution.", e);
		}

		return false;
	}

	private void internalReload() {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {
					browser.refresh();
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception during javascript execution.", e);
		}

	}

	private String internalGetCurrentURL() {

		try {
			return (String) executeWithException(new RunnableWithReturn() {
				public Object run() {
					return browser.getUrl();
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception getting current URL.", e);
		}

	}

	private String internalGetReferringURL() {

		try {
			return (String) executeWithException(new RunnableWithReturn() {
				public Object run() {

					nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();

					nsIWebNavigation webNavigation = (nsIWebNavigation) webBrowser.queryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID);
					nsIURI uri = webNavigation.getReferringURI();
					String spec = "";
					if (uri != null) {
						spec = uri.getSpec();
					}
					return spec;
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception getting current URL.", e);
		}

	}

	public void disableImageLoading() {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {

					nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();

					nsIWebBrowserSetup wbSetup = (nsIWebBrowserSetup) webBrowser.queryInterface(nsIWebBrowserSetup.NS_IWEBBROWSERSETUP_IID);

					wbSetup.setProperty(nsIWebBrowserSetup.SETUP_ALLOW_IMAGES, 0);
					wbSetup.setProperty(nsIWebBrowserSetup.SETUP_ALLOW_PLUGINS, 0);
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception disabling image loading.", e);
		}

	}

	public void enableImageLoading() {

		try {
			executeWithException(new RunnableWithException() {
				public void run() {

					nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();

					nsIWebBrowserSetup wbSetup = (nsIWebBrowserSetup) webBrowser.queryInterface(nsIWebBrowserSetup.NS_IWEBBROWSERSETUP_IID);

					wbSetup.setProperty(nsIWebBrowserSetup.SETUP_ALLOW_IMAGES, 1);
				}
			});
		} catch (Exception e) {
			throw new LadyrBrowserException("Exception enabling image loading.", e);
		}

	}

	private void executeWithException(final RunnableWithException runnable) throws Exception {

		if (Display.getCurrent() != null) {
			runnable.run();
		} else {
			final ObjectStore exceptionStore = new ObjectStore();

			display.syncExec(new Runnable() {
				public void run() {
					try {
						runnable.run();
					} catch (Exception e) {
						exceptionStore.setObject(e);
					}
				}
			});

			Exception e = (Exception) exceptionStore.getObject();
			if (e != null) {
				throw e;
			}
		}
	}

	private Object executeWithException(final RunnableWithReturn runnable) throws Exception {

		if (Display.getCurrent() != null) {
			return runnable.run();
		} else {
			final ObjectStore exceptionStore = new ObjectStore();
			final ObjectStore stringStore = new ObjectStore();

			display.syncExec(new Runnable() {
				public void run() {
					try {
						stringStore.setObject(runnable.run());
					} catch (Exception e) {
						exceptionStore.setObject(e);
					}
				}
			});

			Exception e = (Exception) exceptionStore.getObject();
			if (e != null) {
				throw e;
			} else {
				return stringStore.getObject();
			}

		}

	}

	public void clearBrowserInfo() {
		clearCache();
		clearCookies();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("useragent.txt")));
			String tmp = "";
			ArrayList<String> userAgentList = new ArrayList<String>();
			while ((tmp = reader.readLine()) != null) {
				userAgentList.add(tmp);
			}
			Random random = new Random();
			int index = random.nextInt(userAgentList.size());
			getConfiguration().setUserAgent(userAgentList.get(index));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void clearCookies() {
		nsICookieManager cookieManager = XPCOMUtils.getService("@mozilla.org/cookiemanager;1", nsICookieManager.class); //$NON-NLS-1$
		cookieManager.removeAll();
	}

	public void select(Node node, String selection) {
		checkIfDocumentLoaded();
		if (node instanceof HTMLSelectElement) {

			final HTMLSelectElement component = (HTMLSelectElement) node;
			// System.out.println(component.getChildNodes().getLength());

			if (selection == null) {
				selection = "0";
			}
			// String tmp = "";
			int index = 0;
			// if (selection.equals("State") || selection.equals("Sex")) {
			// NodeList nodeList = component.getChildNodes();
			// ArrayList<String> optionList = new ArrayList<String>();
			// for (int i = 0; i < nodeList.getLength(); i++) {
			// System.out.println(nodeList.item(i).getNodeName());
			// if (nodeList.item(i).getNodeName().equals("option")) {
			// optionList.add(nodeList.item(i).getTextContent()
			// .toLowerCase());
			// }
			// }
			// if (selection.equals("State")) {
			// tmp = currProfileModel.getState();
			//
			// if ((index = optionList.indexOf(tmp.toLowerCase())) == -1) {
			// tmp = USStateName.get(tmp);
			// index = optionList.indexOf(tmp);
			// }
			// } else if (selection.equals("Sex")) {
			// tmp = currProfileModel.getSex();
			// if ((index = optionList.indexOf(tmp.toLowerCase())) == -1) {
			// index = optionList.indexOf(GenderModel.get(tmp));
			// }
			// }
			// } else {
			try {
				String[] options = selection.split("\\|");
				Random random = new Random(42);
				index = Integer.parseInt(options[random.nextInt(options.length)]);
				while (component.getChildNodes().getLength() < index) {
					// System.out.println("睡3秒");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// }
			final int option = index;
			try {
				executeWithException(new RunnableWithException() {
					public void run() {
						try {
							component.setSelectedIndex(option);
							if (component.hasAttribute("onchange")) {
								String tmp = component.getAttribute("onchange");
								if (tmp.contains("this")) {
									tmp = tmp.replace("this", "document.getElementById('" + component.getAttribute("id") + "')");
								}
								executeJavascript(tmp);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				throw new LadyrBrowserException("Could not enter the text successfully.", e);
			}
		}
	}

	/**
	 * @return the proxyList
	 */
	public ArrayList<ProxyModel> getProxyList() {
		return proxyList;
	}

	/**
	 * @param proxyList
	 *            the proxyList to set
	 */
	public void setProxyList(ArrayList<ProxyModel> proxyList) {
		this.proxyList = proxyList;
		this.proxyIter = proxyList.iterator();
		getNextProxy();
	}

	public void getNextProxy() {
		if (proxyIter.hasNext()) {
			currProxyModel = proxyIter.next();
		}
		System.out.println("当前使用ip：" + currProxyModel.getPorxyIp());
		this.getConfiguration().setSocksProxy(currProxyModel.getPorxyIp(), currProxyModel.getProxyPort());
		// this.getConfiguration().setHttpProxy(currProxyModel.getPorxyIp(),
		// currProxyModel.getProxyPort());
	}

	public void wrap(final BlockingQueue<Element> queue, final String projectName, final String url, final ArrayList<RegularUnit> regularUnitList,
			final ArrayList<String> keywordList, final CountDownLatch latch) {
		Thread wrapThread = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean isSuccess = true;
				try {
					int count = 0;
					for (String keyword : keywordList) {
						count++;
						setStatus(1, "正在获取关键字：" + keyword + "的结果");
						setStatus(3, String.valueOf(keywordList.size() - count + 1));
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						Document doc = db.newDocument();
						String title = "";
						go(url);
						LinkedHashMap<String, String> contentMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, ArrayList<String>> mutiContentMap = new LinkedHashMap<String, ArrayList<String>>();
						Element rootElement = doc.createElement("Project");
						doc.appendChild(rootElement);
						rootElement.setAttribute("Name", projectName);
						Element pageContentEle = doc.createElement("Page_Content");
						rootElement.appendChild(pageContentEle);

						boolean inNext = false;// 判断是否有下一页
						boolean hasNext = false;
						int pageIndex=0;
						do {
							if (hasNext)
								inNext = true;// 判断当前是否在next页面中。逻辑：当hasNext为真时，说明此循环进入了Next页面
							pageIndex++;
							hasNext = false; // 将hasNext归位
							for (RegularUnit ru : regularUnitList) {

								if (ru.getMapping() == null || ru.getMapping().trim().length() == 0)
									continue;
								else {

									String xpath = ru.getXpath();
									String xpathSearch = "";
									Node node = null;
									Node contextNode = null;
									List<String> xpaths = null;
									// 将TargetMark语句中的iframe与常规Xpath分开
									if (xpath.contains("##")) {
										xpaths = new ArrayList<String>();
										xpaths = Arrays.asList(xpath.split("##"));
									}
									if (xpaths != null) {
										for (int i = 0; i < xpaths.size() - 1; ++i) {
											if (i == 0) {
												contextNode = getDocument();
											}
											int retryFindNodeTime = 0;
											do {
												contextNode = xpathNode(xpaths.get(i), contextNode);
												retryFindNodeTime++;
											} while (contextNode == null && retryFindNodeTime < 10);

											if (contextNode instanceof HTMLIFrameElement) {
												HTMLIFrameElement element = (HTMLIFrameElement) contextNode;
												contextNode = element.getContentDocument();
											}
										}
										xpathSearch = xpaths.get(xpaths.size() - 1);
									} else {
										contextNode = getDocument();
										xpathSearch = xpath;
									}
									if (contextNode == null) {
										isSuccess = false;
										break;
									}
									// 注释结束
									List<Node> xpathNodes = null;
									if (xpathSearch.contains("%%")) {
										String[] tmp = xpathSearch.split("%%");
										String xpathtmp = tmp[0];
										int index = Integer.parseInt(tmp[1]);

										int retryFindNodeTime = 0;

										do {
											Thread.sleep(1000);
											xpathNodes = xpathNodes(xpathtmp);
											// 如果index不是-1
											if (xpathNodes.size() > index && index != -1) {
												node = xpathNodes.get(index);

												if (node == null) {
													isSuccess = false;
													continue;
												}
												isSuccess = true;
												String operation = ru.getOption();
												if (operation.contains("GetValue")) {
													if (!inNext || ru.isRepeat()) {
														String mapping = ru.getMapping();
														if (!mapping.isEmpty()) {
															// System.out.println(node.getTextContent().trim());
															contentMap.put(mapping, node.getTextContent().trim());
														}
													}
												} else if (operation.contains("Click")) {
													if (!inNext || ru.isRepeat()) {
														// TODO：加入是否重复标记,现在已经可以判断是否在next页面中
														if (ru.getMapping().equals("next"))
														{
															hasNext = true;
															System.out.println(node.getTextContent());
														}
														click(node);
														Thread.sleep(2000);
														waitLoad();
													}
												} else if (operation.contains("Input")) {
													if (!inNext || ru.isRepeat()) {
														Thread.sleep(2000);
														String[] tmps = operation.split(":");
														if (tmps[1].equals("keyword")) {
															enter(node, keyword);
														} else {
															enter(node, tmps[1]);
														}
													}
												} else if (operation.contains("Select")) {
													if (!inNext || ru.isRepeat()) {
														String[] tmps = operation.split(":");
														select(node, tmps[1]);
													}
												} else if (operation.contains("Download")) {
													if (!inNext || ru.isRepeat()) {
														if (node.getLocalName().equals("img")) {
															for (int i = 0; i < node.getAttributes().getLength(); ++i) {
																// System.out.println(node.getAttributes().item(i).getNodeName());
																if (node.getAttributes().item(i).getNodeName().equals("src")) {
																	String src = node.getAttributes().item(i).getNodeValue();

																	String currUrl = getCurrentURL();
																	String[] tmps = currUrl.split("/");
																	StringBuilder sb = new StringBuilder();

																	if (src.contains("../")) {
																		for (int j = 0; j < tmps.length - 2; ++j) {
																			sb.append(tmps[j]);
																			sb.append('/');
																		}
																		sb.append(src.substring(3, src.length()));
																	} else if (src.contains("./")) {
																		for (int j = 0; j < tmps.length - 1; ++j) {
																			sb.append(tmps[j]);
																			sb.append('/');
																		}
																		sb.append(src.substring(3, src.length()));
																	}
																	src = sb.toString();
																	File file = new File(projectName + "/" + URLEncoder.encode(url, "UTF-8"));
																	file.mkdir();
																	Resource imgResource = downloadResource(src);
																	String suffix = src.substring(src.lastIndexOf('.'), src.length());
																	try {

																		imgResource.saveToFile(new File(projectName + "/" + URLEncoder.encode(url, "UTF-8")
																				+ "/" + ru.getMapping() + suffix));
																	} catch (IOException e) {
																		e.printStackTrace();
																	}
																	String mapping = ru.getMapping();
																	if (mapping.length() != 0) {
																		contentMap.put(mapping, "<![CDATA[" + src + "]]>");
																	}
																}
															}
														}
													}
												} else if (operation.contains("GetLink")) {
													if (!inNext || ru.isRepeat()) {
														for (int i = 0; i < node.getAttributes().getLength(); ++i) {
															if (node.getAttributes().item(i).getNodeName().equals("href")) {
																String href = node.getAttributes().item(i).getNodeValue();

																String currUrl = getCurrentURL();
																String[] tmps = currUrl.split("/");
																StringBuilder sb = new StringBuilder();

																if (href.contains("../")) {
																	for (int j = 0; j < tmps.length - 2; ++j) {
																		sb.append(tmps[j]);
																		sb.append('/');
																	}
																	sb.append(href.substring(3, href.length()));
																} else if (href.contains("./")) {
																	for (int j = 0; j < tmps.length - 1; ++j) {
																		sb.append(tmps[j]);
																		sb.append('/');
																	}
																	sb.append(href.substring(3, href.length()));
																}
																href = sb.toString();
																String mapping = ru.getMapping();
																if (mapping.length() != 0) {
																	contentMap.put(mapping, href);
																}
																break;
															}
														}
													}
												}
											}// end if index不是-1
												// if index是-1
											else if (index == -1) {
												String operation = ru.getOption();
												for (Node node1 : xpathNodes) {
													if (operation.contains("Click")) {
														if (!inNext || ru.isRepeat()) {
															if (ru.getMapping().equals("next"))
															{
																hasNext = true;
																System.out.println(node1.getTextContent());
															}
															click(node1);
															Thread.sleep(2000);
															waitLoad();
														}

													} else if (operation.contains("Input")) {
														if (!inNext || ru.isRepeat()) {
															Thread.sleep(2000);
															String[] tmps = operation.split(":");
															if (tmps[1].equals("keyword")) {
																enter(node, keyword);
															} else {
																enter(node, tmps[1]);
															}
														}

													} else if (operation.contains("Select")) {
														if (!inNext || ru.isRepeat()) {
															String[] tmps = operation.split(":");
															select(node1, tmps[1]);
														}
													} else if (operation.contains("Download")) {
														if (!inNext || ru.isRepeat()) {
															if (node1.getLocalName().equals("img")) {
																for (int i = 0; i < node1.getAttributes().getLength(); ++i) {
																	// System.out.println(node1.getAttributes().item(i).getNodeName());
																	if (node1.getAttributes().item(i).getNodeName().equals("src")) {
																		String src = node1.getAttributes().item(i).getNodeValue();

																		String currUrl = getCurrentURL();
																		String[] tmps = currUrl.split("/");
																		StringBuilder sb = new StringBuilder();

																		if (src.contains("../")) {
																			for (int j = 0; j < tmps.length - 2; ++j) {
																				sb.append(tmps[j]);
																				sb.append('/');
																			}
																			sb.append(src.substring(3, src.length()));
																		} else if (src.contains("./")) {
																			for (int j = 0; j < tmps.length - 1; ++j) {
																				sb.append(tmps[j]);
																				sb.append('/');
																			}
																			sb.append(src.substring(3, src.length()));
																		}
																		src = sb.toString();
																		File file = new File(projectName + "/" + URLEncoder.encode(url, "UTF-8"));
																		file.mkdir();
																		Resource imgResource = downloadResource(src);
																		String suffix = src.substring(src.lastIndexOf('.'), src.length());
																		try {

																			imgResource.saveToFile(new File(projectName + "/" + URLEncoder.encode(url, "UTF-8")
																					+ "/" + ru.getMapping() + suffix));
																		} catch (IOException e) {
																			e.printStackTrace();
																		}
																		String mapping = ru.getMapping();
																		if (mapping.length() != 0) {
																			contentMap.put(mapping, "<![CDATA[" + src + "]]>");
																		}
																	}
																}
															}
														}
													} else if (operation.contains("GetLink")) {
														if (!inNext || ru.isRepeat()) {
															for (int i = 0; i < node1.getAttributes().getLength(); ++i) {
																if (node1.getAttributes().item(i).getNodeName().equals("href")) {
																	String href = node1.getAttributes().item(i).getNodeValue();

																	String currUrl = getCurrentURL();
																	String[] tmps = currUrl.split("/");
																	StringBuilder sb = new StringBuilder();

																	if (href.contains("../")) {
																		for (int j = 0; j < tmps.length - 2; ++j) {
																			sb.append(tmps[j]);
																			sb.append('/');
																		}
																		sb.append(href.substring(3, href.length()));
																	} else if (href.contains("./")) {
																		for (int j = 0; j < tmps.length - 1; ++j) {
																			sb.append(tmps[j]);
																			sb.append('/');
																		}
																		sb.append(href.substring(3, href.length()));
																	}
																	href = sb.toString();
																	String mapping = ru.getMapping();
																	if (mapping.length() != 0) {
																		contentMap.put(mapping, href);
																	}
																	break;
																}
															}
														}
													}
												}

												if (operation.contains("GetValue")) {
													if (!inNext || ru.isRepeat()) {
														if (ru.getMapping().length() != 0) {
															ArrayList<String> contentList = new ArrayList<String>();
															for (Node tmpNode : xpathNodes) {
																contentList.add(tmpNode.getTextContent().trim());
															}
															
															//中间结果不完整的问题在这里！
															mutiContentMap.put(ru.getMapping()+pageIndex, contentList);
														}
													}
												}

											}

											retryFindNodeTime++;
										} while (xpathNodes == null || xpathNodes.size() == 0 && retryFindNodeTime < 10);

									}

									if (xpathNodes == null || xpathNodes.size() == 0) {
										isSuccess = false;
										break;
									}
								}
							}
						} while (hasNext&&pageIndex<10);
						// TODO
						if (!isSuccess)
							continue;
						int contentMapCount = 0;
						Element contentNodeEle = null;
						for (String key : contentMap.keySet()) {
							if (contentMapCount == 0) {
								contentNodeEle = doc.createElement("ContentNode");
								pageContentEle.appendChild(contentNodeEle);
							}

							Element contentEle = doc.createElement("Content");
							contentEle.setAttribute("Mapping", key);
							contentEle.setTextContent(contentMap.get(key));
							contentNodeEle.appendChild(contentEle);
							contentMapCount++;
						}
						contentMapCount = 0;
						ArrayList<Element> contentNodeList = new ArrayList<Element>();
						for (String key : mutiContentMap.keySet()) {
							if (contentMapCount == 0) {

								for (int i = 0; i < mutiContentMap.get(key).size(); ++i) {
									contentNodeEle = doc.createElement("ContentNode");
									pageContentEle.appendChild(contentNodeEle);
									contentNodeList.add(contentNodeEle);
								}
							}
							ArrayList<String> tmpList = mutiContentMap.get(key);
							for (int i = 0; i < contentNodeList.size(); ++i) {
								if (i < tmpList.size()) {
									Element childEle = doc.createElement("Content");
									childEle.setAttribute("Mapping", key);
									childEle.setTextContent(tmpList.get(i));
									contentNodeList.get(i).appendChild(childEle);
								}
							}
							contentMapCount++;
						}

						getDomXml(doc, projectName + "/" + projectName + "_" + keyword + ".xml");
					}

					latch.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		wrapThread.setDaemon(true);
		wrapThread.setName("Wrapper");
		wrapThread.start();
	}

	/**
	 * 将org.w3c.dom.Document内容转化成String
	 * 
	 * @param doc
	 * @return
	 */
	private String getDomXml(Node doc, String filePath) {
		String xmlStr = null;
		try {
			// 以 Document Object Model（DOM）树的形式充当转换 Source 树的持有者
			DOMSource source = new DOMSource(doc);

			// 用来生成XML文件
			// 要生成文件需构造PrintWriter的writer,
			// DOM中这种方式name的值没有写进去,由于编码的问题
			// PrintWriter writerXml = new PrintWriter(new FileOutputStream
			// ("city-dom.xml"));
			// 用OutputStreamWriter加了编码就OK了
			PrintWriter writerXml = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
			javax.xml.transform.Result resultXml = new StreamResult(writerXml);
			// 实现此接口的对象包含构建转换结果树所需的信息
			// Result resultXml = new StreamResult(new
			// FileOutputStream("city-dom.xml"));

			// 用来得到XML字符串形式
			// 一个字符流，可以用其回收在字符串缓冲区中的输出来构造字符串。
			StringWriter writerStr = new StringWriter();
			javax.xml.transform.Result resultStr = new StreamResult(writerStr);

			// 此抽象类的实例能够将源树转换为结果树。
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			// 设置编码
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// 是否缩进
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// 将 XML Source 转换为 Result
			transformer.transform(source, resultXml);
			transformer.transform(source, resultStr);

			// 获取XML字符串
			xmlStr = writerStr.getBuffer().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlStr;
	}

	private void setStatus(final int i, final String status) {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				statusItem.setText(i, status);

			}
		});
	}

}
