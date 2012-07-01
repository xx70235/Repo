import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.xml.crypto.NodeSetData;

import org.cnstar.utils.XPCOMUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.dom.NodeFactory;
import org.mozilla.dom.NodeImpl;
import org.mozilla.dom.events.EventListenerImpl;
import org.mozilla.dom.html.HTMLDocumentImpl;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMAbstractView;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMDocumentEvent;
import org.mozilla.interfaces.nsIDOMDocumentView;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMHTMLInputElement;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNSHTMLInputElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDOMWindow2;
import org.mozilla.interfaces.nsIDOMXPathEvaluator;
import org.mozilla.interfaces.nsIDOMXPathNSResolver;
import org.mozilla.interfaces.nsIDOMXPathResult;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.xpcom.Mozilla;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html2.*;

public class BrowserWithAutoGo {
    private Display display;
    private Browser browser;
    private CountDownLatch latch;
    private long defaultTimeout = 60000;
    private final static String NS_IDOMXPATHEVALUATOR_CONTRACTID = "@mozilla.org/dom/xpath-evaluator;1";
    private nsIDOMXPathEvaluator xpathEval;
    private Map<String, String> xpathMap = new HashMap<String, String>();

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String args[]) {
	BrowserWithAutoGo simpleBrowser = new BrowserWithAutoGo();

	try {
	    // Use the new functionality to load some URLs
	    // with our browser.
	    simpleBrowser.go("http://renren.com");

//	    Thread.sleep(1000);
//	    simpleBrowser.click(simpleBrowser
//		    .xpathNodes("//input[@id='login']").get(0));
//	    Thread.sleep(3000);
	    List<Node> nodes = simpleBrowser.xpathNodes("//p[@class='clearfix']/input[@type='password'and @tabindex='2'and @class='input-text'and @name='password'and @id='password']");
	    for (Node node : nodes) {

		System.out.println("Node Type: " + node.getNodeName()
			+ "--Content: " + node.getNodeValue());
		if (node instanceof nsIDOMHTMLInputElement) {
		    nsIDOMHTMLInputElement input = (nsIDOMHTMLInputElement) node;
		    System.out.println("Node Type: " + node.getNodeName()
			    + "--Content: " + node.getNodeValue());
		    simpleBrowser.enter(node,"test");
		}
	    }

	} catch (SimpleBrowserException e) {
	    System.err.println("Problems calling go method.");
	    e.printStackTrace();
	} catch (Exception e) {
	    System.err.println("Problems calling sleep.");
	    e.printStackTrace();
	    Thread.interrupted();
	}
    }

    /**
     * Create the shell.
     * 
     * @param display
     * @wbp.parser.entryPoint
     */
    public BrowserWithAutoGo() {
	// 在起始时将initLatch设为1
	final CountDownLatch initLatch = new CountDownLatch(1);
	new Thread("SWT-Event-Thread") {
	    @Override
	    public void run() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(800, 600);
		shell.open();
		shell.layout();
		try {
		    browser = new Browser(shell, SWT.MOZILLA);
		} catch (SWTError e) {
		    System.out.println("Could not instantiate Browser: "
			    + e.getMessage());
		    return;
		}

		browser.setBounds(shell.getClientArea());

		browser.addProgressListener(new ProgressListener() {

		    @Override
		    public void completed(ProgressEvent event) {
			latch.countDown();
			System.out.println("load finished");
		    }

		    @Override
		    public void changed(ProgressEvent event) {

		    }
		});
		browser.addMouseListener(new MouseListener() {

		    @Override
		    public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void mouseDown(MouseEvent e) {
			// TODO Auto-generated method stub
			mouseEventTest(e);
		    }

		    @Override
		    public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub

		    }
		});

		// 当浏览器初始化完成后将countDown设为0
		initLatch.countDown();
		while (!shell.isDisposed()) {
		    if (!display.readAndDispatch()) {
			display.sleep();
		    }
		}
		System.exit(0);
	    }
	}.start();

	try {
	    // 等待浏览器完成初始化
	    initLatch.await();
	} catch (InterruptedException e) {
	    Thread.interrupted();
	}
	Mozilla moz = Mozilla.getInstance();
	nsIComponentManager componentManager = moz.getComponentManager();
	xpathEval = (nsIDOMXPathEvaluator) componentManager
		.createInstanceByContractID(NS_IDOMXPATHEVALUATOR_CONTRACTID,
			null, nsIDOMXPathEvaluator.NS_IDOMXPATHEVALUATOR_IID);

    }

    public void go(final String url) throws SimpleBrowserException {

	latch = new CountDownLatch(1);
	display.syncExec(new Runnable() {
	    public void run() {
		browser.setUrl(url);
	    }
	});
	boolean timeout = waitLoad(defaultTimeout);
	if (timeout) {
	    throw new SimpleBrowserException("Time out waiting page loading.");
	}
    }

    private boolean waitLoad(long millis) {
	try {
	    boolean timeout;
	    timeout = !latch.await(millis, TimeUnit.MILLISECONDS);// 让当前线程等待
	    if (timeout) {
		display.syncExec(new Runnable() {
		    public void run() {
			browser.stop();
		    }
		});
		latch.await(millis, TimeUnit.MILLISECONDS);
	    }
	    return timeout;
	} catch (InterruptedException e) {
	    throw new Error(e);
	}
    }

    public HTMLDocument getW3CDocument() {
	class DocumentGetter implements Runnable {

	    private nsIDOMHTMLDocument htmldoc;

	    @Override
	    public void run() {
		nsIWebBrowser webBrowser = (nsIWebBrowser) browser
			.getWebBrowser();

		if (webBrowser == null) {
		    System.out
			    .println("Could not get the nsIWebBrowser form the Browser widget");
		}
		nsIDOMWindow dw = webBrowser.getContentDOMWindow();
		nsIDOMDocument nsDoc = dw.getDocument();
		htmldoc = (nsIDOMHTMLDocument) nsDoc
			.queryInterface(nsIDOMHTMLDocument.NS_IDOMHTMLDOCUMENT_IID);

	    }

	    public nsIDOMHTMLDocument getHtmldoc() {
		return htmldoc;
	    }
	}
	DocumentGetter dg = new DocumentGetter();
	display.syncExec(dg);
	return HTMLDocumentImpl.getDOMInstance(dg.getHtmldoc());
    }

    private List<Node> xpathNodes(String xpath) {
	return xPathNodes(xpath,
		((HTMLDocumentImpl) getW3CDocument()).getInstance());
    }

    @SuppressWarnings("unchecked")
    private <T extends Node> List<T> xpathNodes(String xpath, Class<T> nodeClass) {
	return (List<T>) xPathNodes(xpath,
		((HTMLDocumentImpl) getW3CDocument()).getInstance());
    }

    private List<Node> xPathNodes(String xpath, nsIDOMNode context) {
	HTMLDocumentImpl documentImpl = (HTMLDocumentImpl) getW3CDocument();
	nsIDOMHTMLDocument document = documentImpl.getInstance();

	nsIDOMXPathNSResolver res = xpathEval.createNSResolver(document);
	List<Node> resultNodes = null;

	nsISupports obj = xpathEval.evaluate(xpath, document.cloneNode(true),
		res, nsIDOMXPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
	nsIDOMXPathResult result = (nsIDOMXPathResult) obj
		.queryInterface(nsIDOMXPathResult.NS_IDOMXPATHRESULT_IID);
	try {
	    resultNodes = getNodes(result);
	} catch (org.mozilla.xpcom.XPCOMException e) {
	    throw e;
	}
	return resultNodes;
    }

    private void mouseEventTest(MouseEvent e) {
	nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
	if (webBrowser == null) {
	    System.out
		    .println("Could not get the nsIWebBrowser from the Browser widget");
	}
	nsIDOMWindow window = webBrowser.getContentDOMWindow();
	nsIDOMNSDocument nsdoc = XPCOMUtils.qi(window.getDocument(),
		nsIDOMNSDocument.class);
	nsIDOMElement element = nsdoc.elementFromPoint(e.x, e.y);
	System.out.println(element.getLocalName());
	if (element != null) {
	    String xpath = "//";
	    nsIDOMNode node = element.getParentNode();
	    xpath += node.getLocalName() + getNodeAttrXpath(node);
	    xpath += "/" + element.getLocalName() + getNodeAttrXpath(element);
	    System.out.println(xpath);
	    xpathMap.put(xpath, "test");
	    for(Node node1 : xpathNodes(xpath))
	    {
		try {
		    enter(node1, "test");
		    Thread.sleep(1000);
		} catch (SimpleBrowserException e1) {
		    e1.printStackTrace();
		} catch (InterruptedException e2) {
		    // TODO Auto-generated catch block
		    e2.printStackTrace();
		}
	    }
	    
	}
    }

    private String getNodeAttrXpath(nsIDOMNode node) {
	String parentAttString = "";
	if (node.hasAttributes()) {
	    parentAttString = "[";
	    for (long i = 0; i < node.getAttributes().getLength(); ++i) {
		parentAttString += "@"
			+ node.getAttributes().item(i).getNodeName() + "='"
			+ node.getAttributes().item(i).getNodeValue() + "'and ";
	    }
	    parentAttString = parentAttString.substring(0,
			parentAttString.length() - 4) + "]";
		System.out.println(parentAttString);
	}
	
	return parentAttString;
    }

    private <T> List<T> getNodes(nsIDOMXPathResult result) {
	List<T> nodes = new ArrayList<T>();
	nsIDOMNode node;
	while ((node = result.iterateNext()) != null) {
	    nsIDOMDocument doc = node.getOwnerDocument();
	    nsIDOMDocumentEvent evdoc = XPCOMUtils.qi(doc,
		    nsIDOMDocumentEvent.class);
	    nsIDOMEvent ev = evdoc.createEvent("MouseEvents"); //$NON-NLS-1$
	    nsIDOMMouseEvent mev = XPCOMUtils.qi(ev, nsIDOMMouseEvent.class);
	    nsIDOMDocumentView view = XPCOMUtils.qi(doc,
		    nsIDOMDocumentView.class);
	    nsIDOMAbstractView aview = view.getDefaultView();
	    mev.initMouseEvent(
		    "click", true, true, aview, 0, 0, 0, 0, 0, false, false, false, false, 0, null); //$NON-NLS-1$
	    nsIDOMEventTarget evt = XPCOMUtils
		    .qi(node, nsIDOMEventTarget.class);

	    nodes.add((T) NodeFactory.getNodeInstance(node));
	}
	return nodes;
    }

    public void enter(final Node node, final String text)
	    throws SimpleBrowserException {
	if (node instanceof HTMLInputElement) {
	    HTMLInputElement textComponent = (HTMLInputElement) node;
	    enter(textComponent, text);
	} else if (node instanceof HTMLTextAreaElement) {
	    HTMLTextAreaElement textComponent = (HTMLTextAreaElement) node;
	    enter(textComponent, text);
	} else {
	    throw new SimpleBrowserException("enter only works with textfield");
	}
    }

    public void enter(final HTMLInputElement inputElement, String text) {
	final String inputText;
	if (text == null) {
	    inputText = "";
	} else {
	    inputText = text;
	}
	display.syncExec(new Runnable() {
	    public void run() {
		inputElement.setValue(inputText);

	    }
	});
    }

    public void enter(final HTMLTextAreaElement textArea, String text) {
	final String inputText;
	if (text == null) {
	    inputText = "";
	} else {
	    inputText = text;
	}

	display.syncExec(new Runnable() {
	    public void run() {
		NodeList nodelist = textArea.getChildNodes();
		for (int i = 0; i < nodelist.getLength(); i++) {
		    textArea.removeChild(nodelist.item(i));
		}
		try {
		    textArea.appendChild(getW3CDocument().createTextNode(
			    inputText));
		} catch (DOMException e) {
		    System.err.println("Problems inserting the new Child node");
		    e.printStackTrace();
		}

	    }
	});
    }

    public void click(Node node) throws SimpleBrowserException {
	if (node instanceof HTMLElement) {
	    final HTMLElement ele = (HTMLElement) node;
	    display.syncExec(new Runnable() {
		public void run() {
		    String onclick = ele.getAttribute("onclick");
		    if (onclick != null && !onclick.equals("")) {
			browser.execute(onclick);
		    }
		}

	    });
	    if (node instanceof HTMLInputElement) {
		HTMLInputElement button = (HTMLInputElement) node;

		if (button.getType().equalsIgnoreCase("submit")
			|| button.getType().equals("image")) {
		    String formAction = button.getForm().getAction();
		    if (formAction != null && !formAction.equals("")) {
			submitForm(button.getForm());
		    }
		}
	    } else if (node instanceof HTMLAnchorElement) {
		HTMLAnchorElement link = (HTMLAnchorElement) node;
		if (link.getHref() != null && !link.getHref().equals("")) {
		    go(link.getHref());
		}
	    } else if (!(node instanceof HTMLElement)) {
		throw new SimpleBrowserException(
			"Click only works with HTMLElements with onclick attribute or links");
	    }

	}
    }

    private void submitForm(final HTMLFormElement form)
	    throws SimpleBrowserException {
	latch = new CountDownLatch(1);
	display.syncExec(new Runnable() {
	    public void run() {
		form.submit();
	    }
	});
	boolean timeout = waitLoad(defaultTimeout);
	if (timeout) {
	    throw new SimpleBrowserException("Timeout waiting page loading.");
	}
    }
}

class SimpleBrowserException extends Exception {
    public SimpleBrowserException(String exceptionStr) {

    }
}
