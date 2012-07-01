package org.cnstar.webfetcher;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;

public class DataIntegrate {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DataIntegrate window = new DataIntegrate();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setImage(null);
		shell.setSize(673, 535);
		shell.setText("\u83B7\u53D6\u7ED3\u679C\u6570\u636E\u8868\u91CD\u6784");
		shell.setLayout(new BorderLayout(0, 0));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("\u6587\u4EF6");
		
		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.setText("\u64CD\u4F5C");
		
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(BorderLayout.NORTH);
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.setImage(SWTResourceManager.getImage(DataIntegrate.class, "/org/cnstar/images/Gnome-Go-Bottom-32.png"));
		toolItem.setText("\u8F7D\u5165\u83B7\u53D6\u7ED3\u679C\u8868");
		
		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.setImage(SWTResourceManager.getImage(DataIntegrate.class, "/org/cnstar/images/Add-Files-To-Archive-32.png"));
		toolItem_1.setText("\u751F\u6210\u805A\u5408\u8868");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		composite.setLayout(null);
		
		Composite composite_1 = new Composite(composite, SWT.BORDER | SWT.H_SCROLL);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setBounds(0, 28, 162, 405);
		
		Button btnWikipedia = new Button(composite_1, SWT.CHECK);
		btnWikipedia.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnWikipedia.setBounds(10, 10, 98, 17);
		btnWikipedia.setText("Wikipedia");
		
		Button btnCiaWorldFact = new Button(composite_1, SWT.CHECK);
		btnCiaWorldFact.setSelection(true);
		btnCiaWorldFact.setBounds(10, 33, 142, 17);
		btnCiaWorldFact.setText("CIA World Fact Book");
		
		Button btnUnWorldHeritage = new Button(composite_1, SWT.CHECK);
		btnUnWorldHeritage.setSelection(true);
		btnUnWorldHeritage.setBounds(10, 56, 142, 17);
		btnUnWorldHeritage.setText("UN World Heritage");
		
		Button btnWeather = new Button(composite_1, SWT.CHECK);
		btnWeather.setSelection(true);
		btnWeather.setBounds(10, 79, 98, 17);
		btnWeather.setText("Weather Info");
		
		Button button = new Button(composite, SWT.NONE);
		button.setBounds(383, 89, 48, 27);
		button.setText(">>");
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setText(">>");
		button_1.setBounds(383, 218, 48, 27);
		
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.setText("<<");
		button_2.setBounds(447, 406, 48, 27);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(160, 11, 2, 405);
		
		Group group = new Group(composite, SWT.NONE);
		group.setText("\u805A\u5408\u8868");
		group.setBounds(437, 10, 210, 390);
		
		List list_2 = new List(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list_2.setItems(new String[] {"\u56FD\u5BB6\u540D\u79F0", "\u9996\u90FD", "GeoLocation", "\u56FD\u571F\u9762\u79EF", "GDP", "\u6587\u5316\u9057\u4EA7\u603B\u91CF", "\u81EA\u7136\u9057\u4EA7\u603B\u91CF", "\u5929\u6C14", "\u6C14\u6E29", "\u6E7F\u5EA6"});
		list_2.setBounds(10, 21, 189, 369);
		
		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("\u83B7\u53D6\u7ED3\u679C\u8868");
		group_1.setBounds(168, 10, 210, 390);
		
		List list = new List(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.setItems(new String[] {"\u56FD\u5BB6\u540D\u79F0", "\u9996\u90FD", "GeoLocation", "\u603B\u4EBA\u53E3", "\u56FD\u571F\u9762\u79EF", "GDP", "\u4EBA\u5747GDP"});
		list.setBounds(10, 50, 187, 89);
		
		List list_1 = new List(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list_1.setItems(new String[] {"\u56FD\u5BB6\u540D\u79F0", "\u9996\u90FD", "GeoLocation", "\u6587\u5316\u9057\u4EA7\u603B\u91CF", "\u81EA\u7136\u9057\u4EA7\u603B\u91CF"});
		list_1.setBounds(10, 176, 187, 89);
		
		List list_3 = new List(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list_3.setItems(new String[] {"\u5730\u540D", "GeoLocation", "\u5929\u6C14", "\u6C14\u6E29", "\u6E7F\u5EA6"});
		list_3.setBounds(10, 304, 187, 76);
		
		Label lblCiaWorldFact = new Label(group_1, SWT.NONE);
		lblCiaWorldFact.setBounds(10, 27, 187, 17);
		lblCiaWorldFact.setText("CIA World Fact Book\uFF1A");
		
		Label lblUnWorldHeritage = new Label(group_1, SWT.NONE);
		lblUnWorldHeritage.setBounds(10, 153, 187, 17);
		lblUnWorldHeritage.setText("UN World Heritage\uFF1A");
		
		Label lblWeatherInfo = new Label(group_1, SWT.NONE);
		lblWeatherInfo.setBounds(10, 281, 187, 17);
		lblWeatherInfo.setText("Weather Info\uFF1A");
		
		Button button_3 = new Button(composite, SWT.NONE);
		button_3.setBounds(556, 406, 80, 27);
		button_3.setText("\u751F\u6210\u805A\u5408\u8868");
		
		Button button_4 = new Button(composite, SWT.NONE);
		button_4.setText(">>");
		button_4.setBounds(383, 335, 48, 27);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 11, 130, 17);
		label_1.setText("\u83B7\u53D6\u7ED3\u679C\u8868\u5217\u8868");

	}
}
