package org.cnstar.webfetcher;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import org.cnstar.leademu.model.AdModel;
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
import org.cnstar.browser.impl.ConfigurationImpl;
import org.cnstar.browser.impl.BrowserImpl;
import org.cnstar.browser.impl.ObjectStore;
import org.cnstar.browser.impl.RunnableWithException;
import org.cnstar.browser.impl.RunnableWithReturn;
import org.cnstar.browser.impl.XPathEvaluatorImpl;
import org.cnstar.dbutil.DBQuery;
import org.cnstar.dbutil.Dbutil;
import org.cnstar.utils.XPCOMUtils;
import org.cnstar.webfetcher.model.AdStatus;
import org.cnstar.webfetcher.model.GenderModel;
import org.cnstar.webfetcher.model.GroupModel;
import org.cnstar.webfetcher.model.LeadProfileModel;
import org.cnstar.webfetcher.model.PageModel;
import org.cnstar.webfetcher.model.ProxyModel;
import org.cnstar.webfetcher.model.ProxyStatus;
import org.cnstar.webfetcher.model.RegularModel;
import org.cnstar.webfetcher.model.RegularUnit;
import org.cnstar.webfetcher.model.USStateName;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.ToolItem;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridData;
import org.mozilla.dom.ElementImpl;
import org.mozilla.dom.NodeFactory;
import org.mozilla.dom.html.HTMLDocumentImpl;
import org.mozilla.interfaces.nsIBoxObject;
import org.mozilla.interfaces.nsICacheService;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsICookieManager;
import org.mozilla.interfaces.nsIDOMClientRect;
import org.mozilla.interfaces.nsIDOMClientRectList;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMHTMLIFrameElement;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNSElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIHttpChannel;
import org.mozilla.interfaces.nsIHttpHeaderVisitor;
import org.mozilla.interfaces.nsIIOService;
import org.mozilla.interfaces.nsILocalFile;
import org.mozilla.interfaces.nsIObserver;
import org.mozilla.interfaces.nsIObserverService;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsIURI;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.interfaces.nsIWebBrowserPersist;
import org.mozilla.interfaces.nsIWebBrowserSetup;
import org.mozilla.interfaces.nsIWebNavigation;
import org.mozilla.xpcom.GREVersionRange;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.html2.HTMLSelectElement;
import org.w3c.dom.html2.HTMLAnchorElement;
import org.w3c.dom.html2.HTMLBRElement;
import org.w3c.dom.html2.HTMLDocument;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLFormElement;
import org.w3c.dom.html2.HTMLInputElement;
import org.w3c.dom.html2.HTMLOptionElement;
import org.w3c.dom.html2.HTMLTextAreaElement;
import org.xml.sax.SAXException;
import org.eclipse.swt.layout.FillLayout;
import swing2swt.layout.FlowLayout;

public class WebFetcher implements LadyrBrowser {

	private static final int MAX_NUM_RETRIES = 2;
	private static final int RETRY_POOLING_TIME = 500;
	private static final int CONCURRENT_OPERATION_TIMEOUT = 60000;
	private static final int MAX_NUM_RELOADS = 5;
	protected Shell shlWebContentWrapper;
	private Table tbProxyInfo;
	private List<String> adsList;
	private LeadPrepare leadPrepare;
	private Tree adsTree;
	private List<String> waitPageList;
	private List<String> keywordList;
	private Map<String, GroupModel> waitGroupList;
	private Map<Integer, PageModel> successAdList;
	private Map<Integer, PageModel> failedAdList;
	private Map<String, ProxyModel> proxyList;
	private Map<Integer, LeadProfileModel> profileListMap;
	private LeadProfileModel currProfileModel;
	private ProxyModel currProxyModel;
	private PageModel currPageModel;
	private Display display;
	private Browser browser;
	private Button btSaveRegular;
	private Button btDeleteSelected;
	private volatile boolean loading;
	private CountDownLatch latch = new CountDownLatch(1);
	private CountDownLatch leadActionLatch = new CountDownLatch(1);
	private LadyrXPathEvaluator xpathEval;
	private Configuration configuration;
	private static Iterator<String> currProxyIter;
	private Map<String, String> nodeVauleMap;//存放元素的映射
	private Map<String, String> operateMap;//存放元素的操作
	private Map<String, Boolean> repeatMap;//存放标记的重复状态（是否重复）
	private Table tbRegular;
	private TableEditor tableEditor;
	private Label lbBrowserStatus;
	private Label lbCurrProxy;
	private RegularModel currRegularModel;
	private boolean isRecordRegular;
	private Text txPageAddr;
	public static String projectName;
	private BulkPagesSetting bps;
	private int browserCount = 5;
	private BrowserImpl[] virtualBrowsers;
	private TabFolder browserTabFolder;
	private String filePathString;
	private nsIDOMElement highLightedElement;
	private String highLightedElementStyle;
	private HashMap<nsIDOMElement, String> elementStyleMap;

	private int editableColumn;
	public static CountDownLatch checkLatch = new CountDownLatch(1);
	private Table tbStatus;

	public WebFetcher() {
		leadPrepare = new LeadPrepare();
		waitGroupList = new LinkedHashMap<String, GroupModel>();
		successAdList = new LinkedHashMap<Integer, PageModel>();
		failedAdList = new LinkedHashMap<Integer, PageModel>();
		proxyList = new LinkedHashMap<String, ProxyModel>();
		profileListMap = new LinkedHashMap<Integer, LeadProfileModel>();
		currProfileModel = new LeadProfileModel();
		currProxyModel = new ProxyModel();
		currRegularModel = new RegularModel();
		currPageModel = new PageModel();
		nodeVauleMap = new LinkedHashMap<String, String>();
		operateMap = new LinkedHashMap<String, String>();
		repeatMap = new LinkedHashMap<String, Boolean>();
		waitPageList = new LinkedList<String>();
		keywordList = new LinkedList<String>();
		elementStyleMap = new HashMap<nsIDOMElement, String>();
		initialize(true);
	}

	private void createBrowser() {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				org.eclipse.swt.widgets.Control[] control = browserTabFolder.getChildren();
				if (control != null) {
					for (int i = 1; i < control.length; ++i) {
						control[i].dispose();
					}
				}
				setStatusLabel("");
				tbStatus.setSelection(1);
				TabItem[] tabItem = new TabItem[browserCount];
				Browser[] nativeBrowser = new Browser[browserCount];
				TableItem[] statusItem = new TableItem[browserCount];
				virtualBrowsers = new BrowserImpl[browserCount];
				for (int i = 0; i < browserCount; ++i) {
					tabItem[i] = new TabItem(browserTabFolder, SWT.NONE);
					tabItem[i].setText("内容获取窗口" + (i + 1));
					statusItem[i] = new TableItem(tbStatus, SWT.NONE);
					statusItem[i].setText(0,String.valueOf(i+1));
					nativeBrowser[i] = new Browser(browserTabFolder, SWT.MOZILLA);
					tabItem[i].setControl(nativeBrowser[i]);
					virtualBrowsers[i] = new BrowserImpl(display, nativeBrowser[i],statusItem[i]);
				}
			}
		});
	}

	private void beginWrap() {

		Thread wrapThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					/*
					 * 将关键字列表分成浏览器的个数个 
					 */
					ArrayList<ArrayList<String>> dividedList = divideKeywordList();
					int i = 0;
					checkLatch = new CountDownLatch(browserCount);
					BlockingQueue<Element> queue = new LinkedBlockingDeque<Element>();
					for (ArrayList<String> keywordList : dividedList) {
						//通过浏览器开始抓取
						virtualBrowsers[i].wrap(queue, projectName,waitPageList.get(0), currRegularModel.getRegularUnitList(), keywordList, checkLatch);
						++i;
					}
					checkLatch.await();
					/*
					 * 
					 * 
					 */
//					ConnectmySQL connSql = new ConnectmySQL();
//					String sql = "create table " + projectName + "( ";
//					for (RegularUnit ruRegularUnit : currRegularModel.getRegularUnitList()) {
//						if (ruRegularUnit.getMapping().trim().length() != 0)
//							sql += ruRegularUnit.getMapping() + " TEXT , ";
//					}
//					sql = sql.subSequence(0, sql.length() - 2) + ")";
//					Connection conn = connSql.getConn();
//					connSql.excuteUpdate(conn, sql);

					File xmlFiles = new File(projectName);
					DirFilter filter = new DirFilter(".xml");
					File[] files = xmlFiles.listFiles(filter);
					String insertSql;
					for (int j = 0; j < files.length; ++j) {
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db;
						Document doc = null;
						try {
							db = dbf.newDocumentBuilder();
							doc = db.parse(files[j]);
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (doc == null)
							return;
						NodeList rootNode = doc.getElementsByTagName("ContentNode");

//						for (int index = 0; index < rootNode.getLength(); ++index) {
//
//							insertSql = "insert into " + projectName + " (";
//							Node fatherNode = rootNode.item(index);
//							NodeList childNodes = fatherNode.getChildNodes();
//							System.out.println(fatherNode.getNodeName());
//							HashMap<String, String> valueList = generateDBValues(childNodes);
//							for (String key : valueList.keySet()) {
//								insertSql += key + " ,";
//							}
//							insertSql = insertSql.substring(0, insertSql.length() - 2) + " ) values (";
//
//							for (String key : valueList.keySet()) {
//								insertSql += "'" + valueList.get(key).replace("'", "''") + "',";
//							}
//							insertSql = insertSql.substring(0, insertSql.length() - 1) + " )";
//							connSql.excuteUpdate(conn, insertSql);
//						}

					}
					// Element rootElement = doc.createElement("Project");
					// doc.appendChild(rootElement);
					// rootElement.setAttribute("Name", projectName);
					//
					// for (Element element : queue) {
					// rootElement.appendChild(element);
					// }
					// getDomXml(doc, projectName + "/" + projectName + ".xml");
					display.syncExec(new Runnable() {

						@Override
						public void run() {
							MessageBox mBox = new MessageBox(shlWebContentWrapper);
							mBox.setText("提示");
							mBox.setMessage("网页内容获取完毕");
							mBox.open();
						}
					});
				} catch (NullPointerException e) {
					e.printStackTrace();
					display.syncExec(new Runnable() {

						@Override
						public void run() {
							MessageBox mBox = new MessageBox(shlWebContentWrapper);
							mBox.setText("警告");
							mBox.setMessage("未载入资料文件");
							mBox.open();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		wrapThread.setDaemon(true);
		wrapThread.start();

	}

	// content
	private HashMap<String, String> generateDBValues(NodeList nodeList) {
		HashMap<String, String> valueList = new HashMap<String, String>();

		for (int index = 0; index < nodeList.getLength(); ++index) {
			Node node = nodeList.item(index);
			System.out.println(node.getNodeName());
			NamedNodeMap attributes = node.getAttributes();
			if (attributes == null)
				continue;
			for (int j = 0; j < attributes.getLength(); ++j) {
				Node attribute = attributes.item(j);
				valueList.put(attribute.getNodeValue(), node.getTextContent());
			}

		}
		return valueList;
	}

	private ArrayList<ArrayList<String>> dividePageList() {
		ArrayList<ArrayList<String>> dividedLists = new ArrayList<ArrayList<String>>();

		int groupCount = waitPageList.size() / browserCount;
		if (groupCount == 0) {
			groupCount = 1;
		}
		Iterator<String> pageListIter = waitPageList.iterator();
		for (int i = 0; i < browserCount; ++i) {
			ArrayList<String> pageList = new ArrayList<String>();
			int j = 0;
			while (pageListIter.hasNext() && j < groupCount) {
				pageList.add(pageListIter.next());
				++j;
			}
			if (i == (browserCount - 1)) {
				while (pageListIter.hasNext()) {
					pageList.add(pageListIter.next());
				}
			}
			dividedLists.add(pageList);
		}

		return dividedLists;
	}
	
	private ArrayList<ArrayList<String>> divideKeywordList() {
		ArrayList<ArrayList<String>> dividedLists = new ArrayList<ArrayList<String>>();

		int groupCount = keywordList.size() / browserCount;
		if (groupCount == 0) {
			groupCount = 1;
		}
		Iterator<String> keywordListIter = keywordList.iterator();
		for (int i = 0; i < browserCount; ++i) {
			ArrayList<String> tmpList = new ArrayList<String>();
			int j = 0;
			while (keywordListIter.hasNext() && j < groupCount) {
				tmpList.add(keywordListIter.next());
				++j;
			}
			if (i == (browserCount - 1)) {
				while (keywordListIter.hasNext()) {
					tmpList.add(keywordListIter.next());
				}
			}
			dividedLists.add(tmpList);
		}

		return dividedLists;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WebFetcher window = new WebFetcher();
			window.open();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testGo(String url) {
		go(url);
	}

	/**
	 * Open the window.
	 * 
	 */
	public void open() {
		final CountDownLatch initialLatch = new CountDownLatch(1);
		new Thread() {
			public void run() {
				display = Display.getDefault();
				createContents();
				readProxy();
				addObersever();
				shlWebContentWrapper.open();
				shlWebContentWrapper.layout();
				initialLatch.countDown();
				while (!shlWebContentWrapper.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			}
		}.start();
		try {
			initialLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Proxy列表并将迭代器指针指向第一个proxy
	 */
	private void readProxy() {
		int count = tbProxyInfo.getItemCount();
		if (count > 0) {
			for (int i = 0; i < count; ++i) {
				tbProxyInfo.remove(0);
			}
		}
		proxyList.clear();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("proxy.txt")));
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				ProxyModel proxyModel = new ProxyModel();
				proxyModel.generateProxyModel(tmp);
				proxyList.put(proxyModel.getPorxyIp(), proxyModel);
				TableItem item = new TableItem(tbProxyInfo, SWT.NONE);
				item.setText(0, tmp);
				item.setText(1, proxyModel.getProxyStatus().toString());
				currProxyIter = proxyList.keySet().iterator();// 在操作每一组时将proxy指针放至list最前端
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateProxyTable() {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				int count = tbProxyInfo.getItemCount();
				if (count > 0) {
					for (int i = 0; i < count; ++i) {
						tbProxyInfo.remove(0);
					}
				}
				for (String ip : proxyList.keySet()) {
					TableItem item = new TableItem(tbProxyInfo, SWT.NONE);
					item.setText(0, ip);
					item.setText(1, proxyList.get(ip).getProxyStatus().toString());
					if (proxyList.get(ip).getProxyStatus() == ProxyStatus.isUsing) {
						item.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
					}
				}
			}
		});

	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 * 
	 */
	protected void createContents() {
		shlWebContentWrapper = new Shell();
		shlWebContentWrapper.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Automator.png"));
		shlWebContentWrapper.setSize(1050, 730);
		shlWebContentWrapper.setText("Web Fetcher 2.0");
		shlWebContentWrapper.setLayout(new BorderLayout(0, 0));

		Menu menu = new Menu(shlWebContentWrapper, SWT.BAR);
		shlWebContentWrapper.setMenuBar(menu);

		MenuItem mbAccount = new MenuItem(menu, SWT.CASCADE);
		mbAccount.setText("\u5DE5\u7A0B");

		Menu menu_1 = new Menu(mbAccount);
		mbAccount.setMenu(menu_1);

		MenuItem mnSetLoginInfo = new MenuItem(menu_1, SWT.NONE);
		mnSetLoginInfo.setText("\u65B0\u5EFA\u5DE5\u7A0B");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mnExit = new MenuItem(menu_1, SWT.NONE);
		mnExit.setText("\u9000\u51FA\u7A0B\u5E8F");

		MenuItem mnProxy = new MenuItem(menu, SWT.CASCADE);
		mnProxy.setText("\u4EE3\u7406");

		Menu menu_3 = new Menu(mnProxy);
		mnProxy.setMenu(menu_3);

		MenuItem mnAddProxy = new MenuItem(menu_3, SWT.NONE);
		mnAddProxy.setText("\u5BFC\u5165\u4EE3\u7406");

		new MenuItem(menu_3, SWT.SEPARATOR);

		MenuItem menuItem = new MenuItem(menu_3, SWT.NONE);
		menuItem.setText("\u5BFC\u51FA\u4EE3\u7406");

		MenuItem mnOperation = new MenuItem(menu, SWT.CASCADE);
		mnOperation.setText("\u64CD\u4F5C");

		Menu menu_2 = new Menu(mnOperation);
		mnOperation.setMenu(menu_2);

		MenuItem mnBeginLead = new MenuItem(menu_2, SWT.NONE);
		mnBeginLead.setText("\u5F00\u59CB");

		MenuItem mnAbout = new MenuItem(menu, SWT.CASCADE);
		mnAbout.setText("\u5173\u4E8E");

		Menu menu_4 = new Menu(mnAbout);
		mnAbout.setMenu(menu_4);

		SashForm sashForm = new SashForm(shlWebContentWrapper, SWT.SMOOTH);
		sashForm.setDragDetect(false);
		sashForm.setLayoutData(BorderLayout.CENTER);

		TabFolder PageInfoTabFolder = new TabFolder(sashForm, SWT.NONE);

		TabItem tabItem_1 = new TabItem(PageInfoTabFolder, SWT.NONE);
		tabItem_1.setText("\u5730\u5740\u4FE1\u606F");

		adsTree = new Tree(PageInfoTabFolder, SWT.BORDER | SWT.CHECK);
		tabItem_1.setControl(adsTree);

		adsTree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {

				if (event.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) event.item;
					boolean checked = item.getChecked();
					checkItems(item, checked);
					checkPath(item.getParentItem(), checked, false);

					if (checked) {
						if (item.getParentItem() == null) {
							for (int i = 0; i < item.getItems().length; ++i) {
								waitPageList.add(item.getItem(i).getText());
							}
						} else {

							waitPageList.add(item.getText());

						}
					} else {
						if (item.getParentItem() == null) {
							for (int i = 0; i < item.getItems().length; ++i) {
								waitPageList.remove(item.getItem(i).getText());
							}
						} else {
							waitPageList.remove(item.getText());
						}

					}
					setStatusLabel("共选中"+waitPageList.size()+"个页面");
				}
			}

		});

		TabItem tabItem_3 = new TabItem(PageInfoTabFolder, SWT.NONE);
		tabItem_3.setText("\u4EE3\u7406\u4FE1\u606F");

		tbProxyInfo = new Table(PageInfoTabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabItem_3.setControl(tbProxyInfo);
		tbProxyInfo.setHeaderVisible(true);
		tbProxyInfo.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(tbProxyInfo, SWT.NONE);
		tableColumn.setWidth(140);
		tableColumn.setText("\u4EE3\u7406\u5730\u5740");

		TableColumn tableColumn_4 = new TableColumn(tbProxyInfo, SWT.CENTER);
		tableColumn_4.setWidth(89);
		tableColumn_4.setText("\u72B6\u6001");

		SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);
		sashForm_1.setOrientation(SWT.VERTICAL);
		try {
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			return;
		}

		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.MouseMove:
					if (isRecordRegular) {
						if (loading)
							return;
						try {
							nsIDOMElement element = getMouseClickXpath(e);
							String tmString = getClickElementXpath("//", (nsIDOMNode) element);
							tmString = tmString.substring(0, tmString.length() - 1);
							int index = elementIndex((nsIDOMNode) element, tmString);
							if (index != -1) {
								tmString += "%%" + index;
							}
							hightLight(tmString);
						} catch (NullPointerException e1) {
							e1.printStackTrace();
						}
					}
					break;

				case SWT.MouseDown:
					if (loading)
						return;
					if (isRecordRegular) {
						nsIDOMElement element = getMouseClickXpath(e);
						String tmString = getClickElementXpath("//", (nsIDOMNode) element);
						tmString = tmString.substring(0, tmString.length() - 1);
						int index = elementIndex((nsIDOMNode) element, tmString);
						if (bps.getFetchType() == 0) {
							tmString += "%%-1";
						} else {
							if (index != -1) {
								tmString += "%%" + index;
							}
						}
						hightLight(tmString);
						nodeVauleMap.put(tmString, " ");
						operateMap.put(tmString, " ");
						repeatMap.put(tmString,false);
						updateRegularTable(tmString);
					}
					break;

				}
			}

		};

		Group group = new Group(sashForm_1, SWT.NONE);
		group.setText("\u6D4F\u89C8\u5668");
		group.setLayout(new BorderLayout(0, 0));

		Composite composite_3 = new Composite(group, SWT.NONE);
		composite_3.setLayoutData(BorderLayout.SOUTH);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

		lbBrowserStatus = new Label(composite_3, SWT.NONE);

		lbCurrProxy = new Label(composite_3, SWT.NONE);

		Composite composite = new Composite(group, SWT.NONE);
		composite.setLayoutData(BorderLayout.NORTH);
		composite.setLayout(new GridLayout(4, false));

		Button btBack = new Button(composite, SWT.NONE);
		btBack.setText("\u540E\u9000");
		btBack.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				back();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Button btForward = new Button(composite, SWT.NONE);
		btForward.setText("\u524D\u8FDB");
		btForward.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				new Thread("Operation") {
					public void run() {
						forward();
					}
				}.start();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		txPageAddr = new Text(composite, SWT.BORDER);
		txPageAddr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button tbGoto = new Button(composite, SWT.NONE);
		tbGoto.setText("\u6253\u5F00");

		browserTabFolder = new TabFolder(group, SWT.NONE);
		browserTabFolder.setLayoutData(BorderLayout.CENTER);

		TabItem tabItem_4 = new TabItem(browserTabFolder, SWT.NONE);
		tabItem_4.setText("\u89C4\u5219\u5B9A\u5236\u7A97\u53E3");
		browser = new Browser(browserTabFolder, SWT.BORDER | SWT.MOZILLA);
		tabItem_4.setControl(browser);
		browser.setLayoutData(BorderLayout.CENTER);

		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				// loading = true;
				// lbBrowserStatus.setText("页面加载中");
				setStatusLabel("页面加载中");
			}

			public void completed(ProgressEvent event) {
				loading = false;
				System.out.println("Document Loaded");
				// lbBrowserStatus.setText("页面加载完成");
				setStatusLabel("页面加载完成");
				finishLoading();
			}
		});
		browser.addOpenWindowListener(new OpenWindowListener() {

			@Override
			public void open(WindowEvent event) {
				if (!event.required)
					return;
				event.browser = browser;

			}
		});
		browser.addListener(SWT.MouseMove, listener);
		browser.addListener(SWT.MouseDown, listener);

		TabFolder WrapStatusTabFolder = new TabFolder(sashForm_1, SWT.NONE);

		TabItem tabItem = new TabItem(WrapStatusTabFolder, SWT.NONE);
		tabItem.setText("\u89C4\u5219\u4FE1\u606F");

		Composite composite_1 = new Composite(WrapStatusTabFolder, SWT.NONE);
		tabItem.setControl(composite_1);
		composite_1.setLayout(new BorderLayout(0, 0));

		tbRegular = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tbRegular.setLayoutData(BorderLayout.CENTER);
		tbRegular.setHeaderVisible(true);
		tbRegular.setLinesVisible(true);
		tableEditor = new TableEditor(tbRegular);

		tbRegular.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				org.eclipse.swt.graphics.Rectangle clientArea = tbRegular.getClientArea();
				Point selectedPoint = new Point(event.x, event.y);
				int index = tbRegular.getTopIndex();
				while (index < tbRegular.getItemCount()) {
					boolean visible = false;
					TableItem item = tbRegular.getItem(index);
					for (int i = 0; i < tbRegular.getColumnCount(); i++) {
						org.eclipse.swt.graphics.Rectangle rect = item.getBounds(i);
						if (rect.contains(selectedPoint)) {
							// System.out.println ("Item " + index + "-" + i);
							editableColumn = i;
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});
		tbRegular.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				org.eclipse.swt.widgets.Control oldEditor = tableEditor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();
				final TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				if (editableColumn == 3) {
					final CCombo combo = new CCombo(tbRegular, SWT.NONE);
					combo.setText("请选择此元素的映射");
					combo.add("Title");
					combo.add("Description");
					combo.add("Option");
					try {
						final String xpath = item.getText(1);
						combo.addModifyListener(new ModifyListener() {

							@Override
							public void modifyText(ModifyEvent e) {
								try {
									if (!combo.getText().equals("")) {
										nodeVauleMap.put(xpath, combo.getText());
										item.setText(3, combo.getText());
									}
								} catch (LadyrBrowserException e1) {
									e1.printStackTrace();
								}
							}
						});

						combo.addSelectionListener(new SelectionListener() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								try {
									if (combo.getSelectionIndex() == 0) {
										nodeVauleMap.put(xpath, "Title");
									} else if (combo.getSelectionIndex() == 1) {
										nodeVauleMap.put(xpath, "Description");
									} else if (combo.getSelectionIndex() == 2) {
										nodeVauleMap.put(xpath, "Option");
									} 
								} catch (LadyrBrowserException e1) {
									e1.printStackTrace();
								}
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});
						tableEditor.grabHorizontal = true;
						tableEditor.setEditor(combo, item, 3);

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if (editableColumn == 2) {
					final CCombo combo = new CCombo(tbRegular, SWT.NONE);
					combo.setText("请选择针对此元素的操作");
					combo.add("ClickButton");
					combo.add("Input:");
					combo.add("Select:");
					combo.add("GetValue");
					combo.add("Download");
					combo.add("GetLink");
					try {
						
						final String xpath = item.getText(1);
						combo.addModifyListener(new ModifyListener() {

							@Override
							public void modifyText(ModifyEvent e) {
								try {
									if (!combo.getText().equals("")) {
										operateMap.put(xpath, combo.getText());
										item.setText(2, combo.getText());
									}
								} catch (LadyrBrowserException e1) {
									e1.printStackTrace();
								}
							}
						});

						combo.addSelectionListener(new SelectionListener() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								try {
									if (combo.getSelectionIndex() == 0) {
										operateMap.put(xpath, "ClickButton");
									} else if (combo.getSelectionIndex() == 1) {
										operateMap.put(xpath, "Input:");
									} else if (combo.getSelectionIndex() == 2) {
										operateMap.put(xpath, "Select:");
									} else if (combo.getSelectionIndex() == 3) {
										operateMap.put(xpath, "GetValue");
									} else if (combo.getSelectionIndex() == 4) {
										operateMap.put(xpath, "Download");
									} else if (combo.getSelectionIndex() == 5) {
										operateMap.put(xpath, "GetLink");
									} 
								} catch (LadyrBrowserException e1) {
									e1.printStackTrace();
								}
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});
						tableEditor.grabHorizontal = true;
						tableEditor.setEditor(combo, item, 2);

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else if (editableColumn == 4) {
					final CCombo combo = new CCombo(tbRegular, SWT.NONE);
					combo.setText("请选择此元素是否重复操作");
					combo.add("true");
					combo.add("false");

					try {
						
						final String xpath = item.getText(1);
						combo.addModifyListener(new ModifyListener() {

							@Override
							public void modifyText(ModifyEvent e) {
								try {
								if (!combo.getText().equals("")) {
									if(combo.getText().contains("t")||combo.getText().contains("T"))
									{repeatMap.put(xpath, true);
									item.setText(4, "true");
									}
									else
									{repeatMap.put(xpath, false);
									item.setText(4, "false");
										
									}
								}
							} catch (LadyrBrowserException e1) {
								e1.printStackTrace();
							}
							}
						});

						combo.addSelectionListener(new SelectionListener() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								try {
									if (combo.getSelectionIndex() == 0) {
										repeatMap.put(xpath,true);
									} else if (combo.getSelectionIndex() == 1) {
										repeatMap.put(xpath,false);
									} 
								} catch (LadyrBrowserException e1) {
									e1.printStackTrace();
								}
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});
						tableEditor.grabHorizontal = true;
						tableEditor.setEditor(combo, item, 4);

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		TableColumn tableColumn_5 = new TableColumn(tbRegular, SWT.NONE);
		tableColumn_5.setWidth(52);
		tableColumn_5.setText("\u6B65\u9AA4");

		TableColumn tblclmnxpath = new TableColumn(tbRegular, SWT.NONE);
		tblclmnxpath.setToolTipText("\u5143\u7D20\u5B9A\u4F4D");
		tblclmnxpath.setWidth(243);
		tblclmnxpath.setText("\u5143\u7D20\u5B9A\u4F4D\u6807\u8BB0");

		TableColumn tbcContent = new TableColumn(tbRegular, SWT.NONE);
		tbcContent.setToolTipText("\u9700\u8981\u5F80\u6B64\u5143\u7D20\u586B\u5199\u7684\u5185\u5BB9");
		tbcContent.setWidth(167);
		tbcContent.setText("\u5143\u7D20\u64CD\u4F5C");

		TableColumn tbclMap = new TableColumn(tbRegular, SWT.NONE);
		tbclMap.setWidth(129);
		tbclMap.setText("\u5143\u7D20\u6620\u5C04");
		
		TableColumn tblclReverse = new TableColumn(tbRegular, SWT.NONE);
		tblclReverse.setWidth(100);
		tblclReverse.setText("\u662F\u5426\u91CD\u590D");

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayoutData(BorderLayout.SOUTH);
		composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));

		btDeleteSelected = new Button(composite_2, SWT.NONE);
		btDeleteSelected.setText("\u5220\u9664\u6240\u9009\u89C4\u5219");
		btDeleteSelected.setEnabled(false);
		btDeleteSelected.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] tableItem = tbRegular.getSelection();
				for (int i = 0; i < tableItem.length; ++i) {
					nodeVauleMap.remove(tableItem[i].getText(1));
					operateMap.remove(tableItem[i].getText(1));
					repeatMap.remove(tableItem[i].getText(1));
				}
				updateRegularTable();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btSaveRegular = new Button(composite_2, SWT.NONE);
		btSaveRegular.setText("\u4FDD\u5B58\u89C4\u5219\u6587\u4EF6");
		btSaveRegular.setEnabled(false);
		
		TabItem tabItem_2 = new TabItem(WrapStatusTabFolder, 0);
		tabItem_2.setText("\u83B7\u53D6\u72B6\u6001");
		
		Composite composite_4 = new Composite(WrapStatusTabFolder, SWT.NONE);
		tabItem_2.setControl(composite_4);
		composite_4.setLayout(new BorderLayout(0, 0));
		
		tbStatus = new Table(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
		tbStatus.setLinesVisible(true);
		tbStatus.setHeaderVisible(true);
		tbStatus.setLayoutData(BorderLayout.CENTER);
		
		TableColumn tableColumn_1 = new TableColumn(tbStatus, SWT.NONE);
		tableColumn_1.setWidth(52);
		tableColumn_1.setText("\u7A97\u53E3");
		
		TableColumn tableColumn_2 = new TableColumn(tbStatus, SWT.NONE);
		tableColumn_2.setWidth(243);
		tableColumn_2.setToolTipText("\u5143\u7D20\u5B9A\u4F4D");
		tableColumn_2.setText("\u5F53\u524D\u9875\u9762\u5730\u5740");
		
		TableColumn tableColumn_3 = new TableColumn(tbStatus, SWT.NONE);
		tableColumn_3.setWidth(167);
		tableColumn_3.setToolTipText("\u9700\u8981\u5F80\u6B64\u5143\u7D20\u586B\u5199\u7684\u5185\u5BB9");
		tableColumn_3.setText("\u72B6\u6001");
		
		TableColumn tableColumn_6 = new TableColumn(tbStatus, SWT.NONE);
		tableColumn_6.setWidth(129);
		tableColumn_6.setText("\u5269\u4F59\u9875\u9762\u6570");
		btSaveRegular.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {

					btDeleteSelected.setEnabled(false);
					btSaveRegular.setEnabled(false);
					isRecordRegular = false;
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.newDocument();
					Element rootElement = doc.createElement("Regulars");
					rootElement.setAttribute("Project_Name", projectName);
					doc.appendChild(rootElement);
					Element urlsElement = doc.createElement("WrapUrls");
					for (String url : waitPageList) {
						Element urlElement = doc.createElement("url");
						urlElement.setTextContent(url);
						urlsElement.appendChild(urlElement);
					}
					rootElement.appendChild(urlsElement);
					int i = 1;
					for (String xpath : nodeVauleMap.keySet()) {
						Element regularEle = doc.createElement("RegularUnit");
						regularEle.setAttribute("step", Integer.toString(i));
						rootElement.appendChild(regularEle);

						Element xpathEle = doc.createElement("TargetMark");
						xpathEle.setTextContent(xpath);
						Element optionEle = doc.createElement("Option");
						optionEle.setTextContent(operateMap.get(xpath));
						Element valueEle = doc.createElement("Mapping");
						valueEle.setTextContent(nodeVauleMap.get(xpath));
						Element repeatEle = doc.createElement("Repeat");
						repeatEle.setTextContent(repeatMap.get(xpath).toString());
						regularEle.appendChild(xpathEle);
						regularEle.appendChild(optionEle);
						regularEle.appendChild(valueEle);
						regularEle.appendChild(repeatEle);
						i++;
					}
					getDomXml(doc, "regular_files/" + projectName.replace('/', ' ') + ".xml");
					currRegularModel.generateRegularList(new File("regular_files/" + projectName.replace('/', ' ') + ".xml"));
					MessageBox messageBox = new MessageBox(shlWebContentWrapper, SWT.OK);
					messageBox.setText("提示");
					messageBox.setMessage("文件已成功保存");
					messageBox.open();

				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				}
			}

		});
		sashForm_1.setWeights(new int[] { 403, 228 });
		tbGoto.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final String url = txPageAddr.getText();
				new Thread("Operation") {
					public void run() {
						go(url);
					}
				}.start();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		sashForm.setWeights(new int[] { 180, 538 });

		ToolBar toolBar = new ToolBar(shlWebContentWrapper, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(BorderLayout.NORTH);

		ToolItem tiNewProject = new ToolItem(toolBar, SWT.NONE);
		tiNewProject.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Gnome-Window-New-32.png"));
		tiNewProject.setText("\u65B0\u5EFA\u5DE5\u7A0B");
		tiNewProject.addSelectionListener(new NewProject());

		ToolItem tiImport = new ToolItem(toolBar, SWT.NONE);
		tiImport.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Mail-Import-32.png"));
		tiImport.addSelectionListener(new ImportProject());
		tiImport.setText("\u5BFC\u5165\u5DE5\u7A0B");

		ToolItem tIImportProxy = new ToolItem(toolBar, SWT.NONE);
		tIImportProxy.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Gnome-X-Office-Document-32.png"));
		tIImportProxy.setText("\u5BFC\u5165\u4EE3\u7406");

		ToolItem tIRegular = new ToolItem(toolBar, SWT.NONE);
		tIRegular.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Gnome-Accessories-Text-Editor-32.png"));
		tIRegular.addSelectionListener(new RecordRegularListener());
		tIRegular.setText("\u5F55\u5236\u89C4\u5219");

		ToolItem tiBeginWrap = new ToolItem(toolBar, SWT.NONE);
		tiBeginWrap.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Gnome-Go-Bottom-32.png"));
		tiBeginWrap.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File projectfile = new File(projectName);
				if (!projectfile.exists()) {
					projectfile.mkdir();
				}
				createBrowser();
				beginWrap();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		tiBeginWrap.setText("\u5F00\u59CB\u83B7\u53D6");

		ToolItem tIBeginLead = new ToolItem(toolBar, SWT.NONE);
		tIBeginLead.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Gnome-Go-Next-32.png"));
		tIBeginLead.addSelectionListener(new BeginLeadAction());
		tIBeginLead.setText("\u5185\u5BB9\u805A\u5408");

		ToolItem tIAbout = new ToolItem(toolBar, SWT.NONE);
		tIAbout.setImage(SWTResourceManager.getImage(WebFetcher.class, "/org/cnstar/images/Gnome-Dialog-Question-32.png"));
		tIAbout.setText("\u5173\u4E8E");

		// };
	}

	private void setStatusLabel(final String text) {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				lbBrowserStatus.setText(text);
			}
		});
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
			Result resultXml = new StreamResult(writerXml);
			// 实现此接口的对象包含构建转换结果树所需的信息
			// Result resultXml = new StreamResult(new
			// FileOutputStream("city-dom.xml"));

			// 用来得到XML字符串形式
			// 一个字符流，可以用其回收在字符串缓冲区中的输出来构造字符串。
			StringWriter writerStr = new StringWriter();
			Result resultStr = new StreamResult(writerStr);

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

	private void finishLoading() {
		if (this.xpathEval == null) {
			this.xpathEval = createXPathEval();
		}

		if (latch.getCount() == 1) {
			latch.countDown();
		}
	}

	/**
     
     */
	public void addItems(File file, TreeItem root) {

		File[] files = file.listFiles();
		if (files == null) {
			System.out.println("no files");
			return;
		}
		for (int j = 0; j < files.length; j++) {
			TreeItem item = new TreeItem(root, 0);
			item.setText(files[j].getName());
			item.setData(files[j]);
			if (files[j].isDirectory()) {
				addItems(files[j], item);
			}
		}
	}

	protected static void checkItems(TreeItem item, boolean checked) {
		item.setGrayed(false);
		item.setChecked(checked);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			checkItems(items[i], checked);
		}
	}

	/**
     
     */
	protected static void checkPath(TreeItem item, boolean checked, boolean grayed) {
		if (item == null)
			return;
		if (grayed) {
			checked = true;
		} else {
			int index = 0;
			TreeItem[] items = item.getItems();
			while (index < items.length) {
				TreeItem child = items[index];
				if (child.getGrayed() || checked != child.getChecked()) {
					checked = grayed = true;
					break;
				}
				index++;
			}
		}
		item.setChecked(checked);
		item.setGrayed(grayed);
		checkPath(item.getParentItem(), checked, grayed);
	}

	// private void operateLead() throws CriticalTimeoutException {
	// new Thread("LEAD") {
	// public void run() {
	// try {
	// boolean isSuccess = true;
	// // go(currAdModel.getAdvUrl());
	// System.out.println("睡3秒");
	// Thread.sleep(3000);
	// for (RegularUnit ru : currRegularModel.getRegularUnitList()) {
	// if (ru.getInputValue() == null
	// || ru.getInputValue().trim().length() == 0)
	// continue;
	// else {
	// Node node = null;
	// int retryFindNodeTime = 0;
	// while (node == null && retryFindNodeTime < 20) {
	// System.out.println("睡1秒");
	// Thread.sleep(1000);
	// if (ru.getXpath().contains("%%")) {
	// String[] tmp = ru.getXpath().split("%%");
	// node = xpathNodes(tmp[0]).get(
	// Integer.parseInt(tmp[1]));
	// } else {
	// node = xpathNode(ru.getXpath());
	// }
	// retryFindNodeTime++;
	// }
	// if (node == null) {
	// isSuccess = false;
	// break;
	// }
	//
	// String inputValue = ru.getInputValue();
	// }
	// }
	// if (isSuccess) {
	// } else {
	// }
	// leadActionLatch.countDown();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// } catch (LadyrBrowserException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }.start();
	// }

	/**
	 * 从代理列表中读取可用proxy
	 * 
	 * @param isLastABadIp
	 * @return
	 */

	private boolean getProxyModelFromList(boolean isLastABadIp) {
		if (isLastABadIp) {
			for (TableItem item : tbProxyInfo.getItems()) {
//				System.out.println(item.getText(0));
				if (item.getText(0).equals(currProxyModel.getPorxyIp() + ":" + currProxyModel.getProxyPort())) {
					item.setBackground(display.getSystemColor(SWT.COLOR_RED));
					item.setText(1, "BadID");
				}
			}
		}
		if (currProxyIter.hasNext()) {
			currProxyModel = proxyList.get(currProxyIter.next());
		} else {
			currProxyModel = null;
			MessageBox messageBox = new MessageBox(shlWebContentWrapper, SWT.OK);
			messageBox.setText("警告");
			messageBox.setMessage("对于此广告无可用代理");
			if (messageBox.open() == SWT.OK) {
				return false;
			}
		}

		for (TableItem item : tbProxyInfo.getItems()) {
			if (item.getText(0).equals(currProxyModel.getPorxyIp() + ":" + currProxyModel.getProxyPort())) {
				item.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
				item.setText(1, "正在使用");
			}
		}
		return true;
	}

	private nsIDOMElement getMouseClickXpath(Event e) {
		nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
		if (webBrowser == null) {
			System.out.println("Could not get the nsIWebBrowser from the Browser widget");
		}
		nsIDOMWindow window = webBrowser.getContentDOMWindow();
		nsIDOMNSDocument nsdoc = XPCOMUtils.qi(window.getDocument(), nsIDOMNSDocument.class);
		nsIDOMElement element = nsdoc.elementFromPoint(e.x, e.y);
		return element;
	}

	public void hightLight(String tmString) {
		if (loading)
			return;
		try {
			if (tmString.contains("%%")) {
				String[] tmp = tmString.split("%%");
				String xpath = tmp[0];
				int index = Integer.parseInt(tmp[1]);
				List<Node> xpathNodes = xpathNodes(xpath);
				removeHighLight();
				if (bps.getFetchType() == 1) {
					if (index == -1) {
						index = 0;
					}
					Node node = xpathNodes.get(index);
					nsIDOMNode element = NodeFactory.getnsIDOMNode(node);
					if (element instanceof nsIDOMElement)
						hightLight((nsIDOMElement) element);
				} else {
					for (Node node : xpathNodes) {
						nsIDOMNode element = NodeFactory.getnsIDOMNode(node);
						if (element instanceof nsIDOMElement)
							hightLight((nsIDOMElement) element);
					}
				}
			} else {
				List<Node> xpathNodes = xpathNodes(tmString);
				removeHighLight();
				for (Node node : xpathNodes) {
					nsIDOMNode element = NodeFactory.getnsIDOMNode(node);
					if (element instanceof nsIDOMElement)
						hightLight((nsIDOMElement) element);
				}
			}
		} catch (XPCOMException e) {
			e.printStackTrace();
		}
	}

	public void hightLight(nsIDOMElement element) {
		highLightedElement = element;
		if (element.hasAttribute("style")) {
			highLightedElementStyle = element.getAttribute("style");
			elementStyleMap.put(element, highLightedElementStyle);
//			System.out.println(highLightedElementStyle);
			StringBuilder sb = new StringBuilder();
			sb.append(highLightedElementStyle);
			sb.append(" outline:#f00 solid 2px;");
//			System.out.println(sb.toString());
			element.setAttribute("style", sb.toString());
		} else {
			elementStyleMap.put(element, "");
			element.setAttribute("style", "outline:#f00 solid 2px;");
		}
	}

	public void removeHighLight() {
		for (nsIDOMElement element : elementStyleMap.keySet()) {
			String style = elementStyleMap.get(element);
			if (style == null || style.length() == 0) {
				element.removeAttribute("style");
			} else {
				element.setAttribute("style", style);
			}
		}
	}

	public String getClickElementXpath(String xpath, nsIDOMNode childNode) {
		if (childNode != null) {
//			System.out.println(childNode.getParentNode().getLocalName());
			if (childNode.getParentNode().getLocalName() != null) {
				nsIDOMNode node = childNode.getParentNode();
				xpath = getClickElementXpath(xpath, node);
				xpath += childNode.getLocalName() + getNodeAttrXpath(childNode) + "/";
			} else {
				return xpath;
			}
			return xpath;
		} else
			return null;
	}

	public int elementIndex(nsIDOMNode childNode, String xpath) {
		List<Node> nodeList = xpathNodes(xpath);
		int i = 0;
		for (Node node : nodeList) {
			nsIDOMNode nsINode = NodeFactory.getnsIDOMNode(node);
			if (nsINode.equals(childNode)) {
				return i;
			}
			++i;
		}
		return -1;
	}



	private nsIDOMElement getElementInIframe(Event e, StringBuilder xpath, nsIDOMElement element) {
		nsIDOMNode node = element.getParentNode();
		if (node.getLocalName() != null) {
			xpath.append(node.getLocalName());
			xpath.append(getNodeAttrXpath(node));
			if ((!element.getLocalName().equals("span") && !element.getLocalName().equals("SPAN"))
					|| (!node.getLocalName().equals("a") && !node.getLocalName().equals("A"))) {

				xpath.append("/");
				xpath.append(element.getLocalName());
				xpath.append(getNodeAttrXpath(element));
				xpath.append("##//");
			}
		}

		nsIDOMHTMLIFrameElement iframeElement = XPCOMUtils.qi(element, nsIDOMHTMLIFrameElement.class);
		nsIDOMDocument contentDoc = iframeElement.getContentDocument();
		nsIDOMNSDocument nsContentdoc = XPCOMUtils.qi(contentDoc, nsIDOMNSDocument.class);
		element = nsContentdoc.elementFromPoint(e.x, e.y);
		return element;
	}

	private void updateRegularTable(String selectedXpath) {

		int count = tbRegular.getItemCount();
		if (count > 0) {
			for (int i = 0; i < count; ++i) {
				tbRegular.remove(0);
			}
		}
		org.eclipse.swt.widgets.Control control = tableEditor.getEditor();
		if (control != null) {
			control.dispose();
		}
		int i = 1;
		for (String xpath : nodeVauleMap.keySet()) {
			final TableItem item = new TableItem(tbRegular, SWT.NONE);
			item.setText(0, Integer.toString(i++));
			item.setText(1, xpath);
			item.setText(2, operateMap.get(xpath));
			item.setText(3, nodeVauleMap.get(xpath));
			item.setText(4, repeatMap.get(xpath).toString());
			if (xpath.equals(selectedXpath)) {
				tbRegular.setSelection(item);
			}
		}
	}

	private void updateRegularTable() {

		int count = tbRegular.getItemCount();
		if (count > 0) {
			for (int i = 0; i < count; ++i) {
				tbRegular.remove(0);
			}
		}
		org.eclipse.swt.widgets.Control control = tableEditor.getEditor();
		if (control != null) {
			control.dispose();
		}
		int i = 1;
		for (String xpath : nodeVauleMap.keySet()) {
			final TableItem item = new TableItem(tbRegular, SWT.NONE);
			item.setText(0, Integer.toString(i++));
			item.setText(1, xpath);
			item.setText(2, operateMap.get(xpath));
			item.setText(3, nodeVauleMap.get(xpath));
			item.setText(4, repeatMap.get(xpath).toString());
		}
		tbRegular.setSelection(i - 2);
	}

	private String getNodeAttrXpath(nsIDOMNode node) {
		String parentAttString = "";
		if (node.hasAttributes()) {
			int count = 0;
			for (long i = 0; i < node.getAttributes().getLength(); ++i) {

				if (node.getAttributes().item(i).getNodeName().equals("name") || node.getAttributes().item(i).getNodeName().equals("NAME")
						|| node.getAttributes().item(i).getNodeName().equals("id") || node.getAttributes().item(i).getNodeName().equals("ID")
						|| node.getAttributes().item(i).getNodeName().equals("type") || node.getAttributes().item(i).getNodeName().equals("TYPE")) {
					if (count == 0) {
						parentAttString = "[";
					}
					parentAttString += "@" + node.getAttributes().item(i).getNodeName() + "='" + node.getAttributes().item(i).getNodeValue()
							+ "' and ";
					count++;
				}
			}
			if (count != 0) {
				parentAttString = parentAttString.substring(0, parentAttString.length() - 4) + "]";
			}
			// System.out.println(parentAttString);
		}

		return parentAttString;
	}

	class BeginLeadAction implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {

			DataIntegrate2 window = new DataIntegrate2();
			window.open();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}
	}

	private void waitLoad() {
		System.out.println("等待页面载入");
		for (int i = 0; i < 2; i++) {
			boolean timeout = waitLoad(getConfiguration().getTimeout());
			if (!timeout) {
				System.out.println("载入完成");
				return;
			} else {
				try {
					System.out.println("重试");
					executeWithException(new RunnableWithException() {
						public void run() throws Exception {
							browser.stop();
							browser.refresh();
							loading = true;
							setStatusLabel("载入页面中...");
							// lbBrowserStatus.setText("载入页面中...");
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

			System.out.println("载入页面中....");
			latch = new CountDownLatch(1);
			try {

				boolean timeout = !latch.await(CONCURRENT_OPERATION_TIMEOUT, TimeUnit.MILLISECONDS);

				int numRetries = 0;
				while (timeout) {

					numRetries++;
					System.out.println("重试载入中");

					try {
						executeWithException(new RunnableWithException() {
							public void run() throws Exception {
								browser.stop();
								browser.refresh();
								loading = true;
								lbBrowserStatus.setText("载入页面中...");
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
				System.out.println("超时");
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
		this.shlWebContentWrapper.dispose();
	}

	private LadyrXPathEvaluator createXPathEval() {
		return new XPathEvaluatorImpl();
	}

	private void inputInfo(Node node, String input) throws LadyrBrowserException {
		if (node instanceof HTMLSelectElement) {
			select(node, input);
		} else if (input.contains("|")) {
			enterAlternate(node, input);
		}

	}

	private void enterAlternate(Node node, String inputAltiernate) throws LadyrBrowserException {
		String[] inputs = inputAltiernate.split("\\|");
		Random random = new Random();
		String input = inputs[random.nextInt(inputs.length)];
		enter(node, input);
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
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					browser.setUrl(url);
					loading = true;
					lbBrowserStatus.setText("载入页面中...");
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
					loading = true;
					setStatusLabel("载入页面中...");
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
					loading = true;
					setStatusLabel("载入页面中...");
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
					loading = true;
					setStatusLabel("载入页面中...");
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

	public Browser getBrowser() {
		return browser;
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
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPaths: " + Arrays.toString(xpaths)
							+ " in URL " + getCurrentURL());
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
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with regex: " + regex + " in URL "
							+ getCurrentURL());
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
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPath: " + xpath + " in URL "
							+ getCurrentURL());
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
							loading = true;
						}
					});
				} catch (Exception e) {
				}

				numRetries = 0;
				numReloads++;

				if (numReloads == MAX_NUM_RELOADS) {
					throw new LadyrBrowserException("MAX_NUM_RELOADS=" + MAX_NUM_RELOADS + " reached with XPath: " + xpath + " in URL "
							+ getCurrentURL());
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
		try {
			nsICacheService cacheService = (nsICacheService) Mozilla.getInstance().getServiceManager()
					.getServiceByContractID("@mozilla.org/network/cache-service;1", nsICacheService.NS_ICACHESERVICE_IID);

			/*
			 * for now since the NSI interface for nsICache in Java does not
			 * provide access to the nsICache.STORE_ON_DISK and
			 * nsICache.STORE_IN_MEMORY, need to pass the actual values. (Got
			 * values from LXR)
			 * 
			 * const nsCacheStoragePolicy STORE_IN_MEMORY = 1; const
			 * nsCacheStoragePolicy STORE_ON_DISK = 2;
			 */
			cacheService.evictEntries(1);
			cacheService.evictEntries(2);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clearCookies() {
		nsICookieManager cookieManager = XPCOMUtils.getService("@mozilla.org/cookiemanager;1", nsICookieManager.class); //$NON-NLS-1$
		cookieManager.removeAll();
	}

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
		} catch (LadyrBrowserException e) {
			e.printStackTrace();
		}
	}

	public void click(Node node, double waitSeconds) throws TimeoutException {
		checkIfDocumentLoaded();

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

			if (link.getHref() != null && !link.getHref().equals("")) {
				go(link.getHref(), waitSeconds);
			}

		} else if (!(node instanceof HTMLElement)) {
			throw new LadyrBrowserException("click only works with HTMLElements with onclick "
					+ " attribute or links (HTMLAnchorElement) or buttons (HTMLButtonElement)");
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

	}

	public void enter(final Node node, final String text) {

		if (node == null) {
			System.out.println("No such node");
			return;
		}
		if (node instanceof HTMLInputElement) {
			HTMLInputElement textComponent = (HTMLInputElement) node;
			enter(textComponent, text);
		} else if (node instanceof HTMLTextAreaElement) {
			HTMLTextAreaElement textComponent = (HTMLTextAreaElement) node;
			enter(textComponent, text);
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
					component.setValue(inputText);
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
			throw new LadyrBrowserException("Could not enter the text successfully.", e);
		}
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
					if (component.hasAttribute("onfocus")) {
						String tmp = component.getAttribute("onfocus");
						if (tmp.contains("this")) {
							tmp = tmp.replace("this", "document.getElementById('" + component.getAttribute("id") + "')");
						}
						executeJavascript(tmp);
					}

					NodeList nodeList = component.getChildNodes();
					for (int i = 0; i < nodeList.getLength(); i++) {
						component.removeChild(nodeList.item(i));
					}
					component.appendChild(getDocument().createTextNode(inputText));

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
			throw new LadyrBrowserException("Could not enter the text successfully.", e);
		}
	}

	public void select(Node node, String selection) {
		checkIfDocumentLoaded();
		if (node instanceof HTMLSelectElement) {

			final HTMLSelectElement component = (HTMLSelectElement) node;
			System.out.println(component.getChildNodes().getLength());

			if (selection == null) {
				selection = "0";
			}
			String tmp = "";
			int index = 0;
			if (selection.equals("State") || selection.equals("Sex")) {
				NodeList nodeList = component.getChildNodes();
				ArrayList<String> optionList = new ArrayList<String>();
				for (int i = 0; i < nodeList.getLength(); i++) {
//					System.out.println(nodeList.item(i).getNodeName());
					if (nodeList.item(i).getNodeName().equals("option")) {
						optionList.add(nodeList.item(i).getTextContent().toLowerCase());
					}
				}
				if (selection.equals("State")) {
					tmp = currProfileModel.getState();

					if ((index = optionList.indexOf(tmp.toLowerCase())) == -1) {
						tmp = USStateName.get(tmp);
						index = optionList.indexOf(tmp);
					}
				} else if (selection.equals("Sex")) {
					tmp = currProfileModel.getSex();
					if ((index = optionList.indexOf(tmp.toLowerCase())) == -1) {
						index = optionList.indexOf(GenderModel.get(tmp));
					}
				}
			} else {
				try {
					String[] options = selection.split("\\|");
					Random random = new Random(42);
					index = Integer.parseInt(options[random.nextInt(options.length)]);
					while (component.getChildNodes().getLength() < index) {
						System.out.println("睡3秒");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
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

	public nsIDOMNode xpathnsINode(String xpath) {
		checkIfDocumentLoaded();
		Node node = xpathEval.xpathNode(this, xpath);
		return NodeFactory.getnsIDOMNode(node);

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

	protected void initialize(boolean isThis) {
		GREVersionRange[] range = new GREVersionRange[1];
		range[0] = new GREVersionRange("1.8", true, "1.9+", true);
		File grePath = null;
		grePath = new File("component/bin").getAbsoluteFile();
		System.setProperty("org.eclipse.swt.browser.XULRunnerPath", grePath.getAbsolutePath());

		Mozilla mozilla = Mozilla.getInstance();
		mozilla.initialize(grePath);
		try {
			mozilla.initXPCOM(grePath, null);

		} catch (Throwable t) {
			// log.error("initXPCOM failed");
			return;
		}
		// From this line, we can use JavaXPCOM
		// log.info("--> initialized\n");
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
		// checkIfDocumentLoaded();
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

		} catch (IOException e) {
			throw new LadyrBrowserException("IOExcepion during access to resource", e);
		}
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub

	}

	class RecordRegularListener implements SelectionListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			clearBrowserInfo();
			System.out.println(getConfiguration().getUserAgent());
			isRecordRegular = true;
			btDeleteSelected.setEnabled(true);
			btSaveRegular.setEnabled(true);
			nodeVauleMap.clear();
			operateMap.clear();
			repeatMap.clear();
			updateRegularTable();

			readProxy();

			if (!(waitPageList.size() > 0))
				return;
			final String url = waitPageList.get(0);
			txPageAddr.setText(url);
			new Thread("LEAD_RECORD") {
				public void run() {
					go(url);
				}
			}.start();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 清理浏览器信息，清cache、cookies,换useragent
	 */
	private void clearBrowserInfo() {
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

	private void addObersever() {
		String contractID = "@mozilla.org/observer-service;1";

		nsIServiceManager serviceManager = Mozilla.getInstance().getServiceManager();
		nsIObserverService observerService = (nsIObserverService) serviceManager.getServiceByContractID(contractID,
				nsIObserverService.NS_IOBSERVERSERVICE_IID);
		SimpleHTTPObserver httpObserver = new SimpleHTTPObserver();
		observerService.addObserver(httpObserver, "http-on-modify-request", false);// 获取http-on-modify-request主题（类似与事件监听）
	}

	class SimpleHTTPObserver implements nsIObserver {
		private int nRequests = 0;
		private int nResponses = 0;

		public void observe(nsISupports aSubject, String aTopic, String aData)// 当http-on-modify-request事件发生时，运行此函数
		{
			nsIHttpChannel httpChannel = (nsIHttpChannel) aSubject.queryInterface(nsIHttpChannel.NS_IHTTPCHANNEL_IID);

			if (aTopic.equals("http-on-modify-request")) {
				nRequests++;
				try {
					httpChannel.setRequestHeader("Accept-Language", "us-en,us;q=0.8,en-us;q=0.5,en;q=0.3", false);
					if (nRequests == 1) {
					}
				} catch (Exception e) {
					System.out.println(e.toString());
				}

			} else if (aTopic.equals("http-on-examine-response")) {
				nResponses++;
				System.out.println("\n------BEGIN RESPONSE NUMBER " + nResponses + " ---------\n");
				httpChannel.visitResponseHeaders(new nsIHttpHeaderVisitor() {

					@Override
					public nsISupports queryInterface(String uuid) {
						return null;
					}

					@Override
					public void visitHeader(String header, String value) {
						System.out.println("Header: " + header + " -- Value: " + value);
					}

				});

			}
		}

		@Override
		public nsISupports queryInterface(String uuid) {
			return Mozilla.queryInterface(this, uuid);
		}

		private String getRequestHeader(nsIHttpChannel httpChannel, String header) {
			try {
				return httpChannel.getRequestHeader(header);
			} catch (Exception e) {
				return "Header Not Found";
			}
		}

		private String getResponseHeader(nsIHttpChannel httpChannel, String header) {
			try {
				return httpChannel.getRequestHeader(header);
			} catch (Exception e) {
				return "Header Not Found";
			}
		}
	}

	class NewProject implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			bps = new BulkPagesSetting(shlWebContentWrapper, SWT.DIALOG_TRIM, adsTree);
			bps.open();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class ImportProject implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			FileDialog fd = new FileDialog(shlWebContentWrapper, SWT.OPEN);
			fd.setText("打开");
			String[] filterExt = { "*.xml" };
			fd.setFilterExtensions(filterExt);
			filePathString = fd.open();
			waitPageList.clear();
			keywordList.clear();
			if (filePathString != null && filePathString.length() != 0) {
				currRegularModel.generateRegularList(new File(filePathString));
				projectName = currRegularModel.getProjectName();
				File file = new File(projectName);
				file.mkdir();
				adsTree.clearAll(true);
				TreeItem root = new TreeItem(adsTree, 0);
				root.setText(projectName);
				for (String url : currRegularModel.getPageUrlList()) {
					TreeItem item = new TreeItem(root, 0);
					item.setText(url);
				}
				
				/*
				 * 从数据库中获取关键字列表
				 */
				DBQuery dbQuery = new DBQuery();
				String sqlStr = "SELECT keyword FROM keywords where status <> 1 or status is null";
				ResultSet rs = dbQuery.getQueryResult(sqlStr);
				try {
					while(rs.next())
					{
						keywordList.add(rs.getString(1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			
			}
		}

	}

}
