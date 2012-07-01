import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMHTMLAnchorElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.dom.*;
import org.mozilla.dom.html.HTMLAnchorElementImpl;
import org.w3c.dom.html.HTMLAnchorElement;
import com.swtdesigner.SWTResourceManager;

public class LadyrBrowser
{
	private static final int WIDTH = 900;
	private static final int HEIGHT = 600;

	Browser browser;
	private Combo uriCombo;
	private final ToolItem backItem;
	private final ToolItem forwardItem;

	/**
	 * @wbp.parser.entryPoint
	 */
	public LadyrBrowser(String xulrunnerPath)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(WIDTH, HEIGHT);

		GridLayout gridLayout = new GridLayout(1, true);
		shell.setLayout(gridLayout);

		Menu menuBar = new Menu(shell, SWT.BAR);
		final MenuItem file = new MenuItem(menuBar, SWT.CASCADE);
		file.setText("&File");
		final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(filemenu);
		final MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
		exitMenuItem.setText("&Eixt\tCTRL+E");
		exitMenuItem.setAccelerator(SWT.CTRL + 'E');
		exitMenuItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				System.exit(0);
			}
		});

		final MenuItem helpMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuItem.setText("&Help");
		final Menu helpmenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuItem.setMenu(helpmenu);
		final MenuItem aboutMenuItem = new MenuItem(helpmenu, SWT.PUSH);
		aboutMenuItem.setText("&About");
		shell.setMenuBar(menuBar);
		ToolBar toolbar = new ToolBar(shell, SWT.WRAP);
		backItem = new ToolItem(toolbar, SWT.PUSH);
		backItem.setImage(SWTResourceManager.getImage(LadyrBrowser.class, "/org/cnstar/images/Gnome-Go-Previous-32.png"));
		backItem.setEnabled(false);
		forwardItem = new ToolItem(toolbar, SWT.PUSH);
		forwardItem.setImage(SWTResourceManager.getImage(LadyrBrowser.class, "/org/cnstar/images/Gnome-Go-Next-32.png"));
		forwardItem.setEnabled(false);
		backItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				if (browser.isBackEnabled())
				{
					browser.back();

				}
			}
		});
		forwardItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				if (browser.isBackEnabled())
				{
					browser.back();
				}
			}
		});

		final ToolItem refreshItem = new ToolItem(toolbar, SWT.PUSH);
		refreshItem.setImage(SWTResourceManager.getImage(LadyrBrowser.class, "/org/cnstar/images/Gnome-View-Refresh-32.png"));
		refreshItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				browser.refresh();
			}
		});
		final ToolItem stopItem = new ToolItem(toolbar, SWT.PUSH);
		stopItem.setImage(SWTResourceManager.getImage(LadyrBrowser.class, "/org/cnstar/images/Gnome-Go-Top-32.png"));
		stopItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				browser.stop();
			}
		});

		uriCombo = new Combo(toolbar, SWT.NONE);
		uriCombo.setItems(new String[] { "www.baidu.com", "www.google.com",
				"www.cj.com" });
		uriCombo.setText("www.renren.com");
		// GridData data1 = new GridData();
		// data1.horizontalAlignment = GridData.FILL;
		// uriCombo.setLayoutData(data1);
		// uriCombo.setBounds(400, 0, 2 * (WIDTH / 6), 0);
		// uriCombo.setBounds(0, 0, 2 * (WIDTH / 6), 32);
		final ToolItem goItem = new ToolItem(toolbar, SWT.PUSH);
		goItem.setImage(SWTResourceManager.getImage(LadyrBrowser.class, "/org/cnstar/images/Gnome-Emblem-Default-32.png"));
		goItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				browser.setUrl(uriCombo.getText());
			}
		});

		GridData data = new GridData();
		data.horizontalAlignment = SWT.CENTER;

		toolbar.setLayoutData(data);
		toolbar.pack();
		Canvas canvas = new Canvas(shell, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		data.widthHint = WIDTH;
		data.heightHint = HEIGHT;
		canvas.setLayoutData(data);
		shell.pack();
		shell.open();
		initMozillaBrowser(uriCombo.getText(), canvas, xulrunnerPath);
		final ToolItem anchorItem = new ToolItem(toolbar, SWT.PUSH);
		anchorItem.setImage(SWTResourceManager.getImage(LadyrBrowser.class, "/org/cnstar/images/Application-Default-Icon-32.png"));
		anchorItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{

				nsIWebBrowser webBrowser = (nsIWebBrowser) browser
						.getWebBrowser();
				if (webBrowser == null)
				{
					System.out
							.println("Could not get the nsIWebBrowser from the Browser widget");
				}
				nsIDOMWindow window = webBrowser.getContentDOMWindow();
				nsIDOMDocument doc = window.getDocument();
				nsIDOMNodeList nodelist = doc.getElementsByTagName("a");
				for (int i = 0; i < nodelist.getLength(); i++)
				{
					nsIDOMNode mozNode = nodelist.item(i);//首先得到一个mozilla的dom node
					//再获取到其相应的接口，使这个node转换为它的真正类型，在这里是Anchor node
					nsIDOMHTMLAnchorElement mozAnchor = (nsIDOMHTMLAnchorElement)mozNode.queryInterface(nsIDOMHTMLAnchorElement.NS_IDOMHTMLANCHORELEMENT_IID);
					
					//最后获取到相应的w3c的dom node
					HTMLAnchorElement a = (HTMLAnchorElement)HTMLAnchorElementImpl.getDOMInstance(mozAnchor);
					System.out.println("Tag Name: "+a.getNodeName()+" -- TEXT: " + a.getTextContent() +" -- Href: " + a.getHref());
				}

			}
		});
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}

	}

	private void initMozillaBrowser(String uri, Canvas canvas,
			String xulrunnerPath)
	{
		browser = new Browser(canvas, SWT.MOZILLA);
		browser.setBounds(canvas.getClientArea());

		browser.setUrl(uri);
		browser.addProgressListener(new ProgressListener()
		{
			@Override
			public void completed(ProgressEvent event)
			{
				updateToolItemsState();
			}

			@Override
			public void changed(ProgressEvent event)
			{
				uriCombo.setText(browser.getUrl());
				updateToolItemsState();
			}
		});

	}

	private Image getImage(String path)
	{
		InputStream sourceStream = this.getClass().getResourceAsStream(path);
		ImageData source = new ImageData(sourceStream);
		Image image = new Image(null, source);
		try
		{
			sourceStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}

	private void updateToolItemsState()
	{
		if (!backItem.isEnabled() && browser.isBackEnabled())
		{
			backItem.setEnabled(true);

		}
		else if (backItem.isEnabled() && !browser.isBackEnabled())
		{
			backItem.setEnabled(false);
		}
		if (!forwardItem.isEnabled() && browser.isForwardEnabled())
		{
			forwardItem.setEnabled(true);
		}
		else if (forwardItem.isEnabled() && !browser.isForwardEnabled())
		{
			forwardItem.setEnabled(false);
		}
	}

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			new LadyrBrowser(args[0]);

		}
		else
		{
			new LadyrBrowser(null);
		}
	}
}
