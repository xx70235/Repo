package org.cnstar.webfetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.*;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class DataIntegrate2 {

	protected Shell shell;
	
	java.util.List<String> tableList = null;
	public static int N=10;
	
	String[][] values = new String[N][];
	
	final Button[] jcb = new Button[N];
	final JPanel [] jpTest = new JPanel[N];
	
	final JLabel [] jlTest = new JLabel[N];
	final JScrollPane [] jsp=new JScrollPane[1]; 
//	final JList list = new JList();
	final JList listRight = new JList(new DefaultListModel());
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DataIntegrate2 window = new DataIntegrate2();
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
		shell.setSize(673, 535);
		shell.setText("\u805A\u5408\u83B7\u53D6\u7ED3\u679C");
		shell.setLayout(new BorderLayout(0, 0));
	
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("\u6587\u4EF6");
		
		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.setText("\u64CD\u4F5C");
		
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(BorderLayout.NORTH);
		
		final Composite composite = new Composite(shell, SWT.NONE);
		final Composite composite_1 = new Composite(composite, SWT.None);
//		SWT.BORDER | SWT.H_SCROLL
		composite.setLayoutData(BorderLayout.CENTER);
		composite.setLayout(null);
		
		
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setBounds(5, 28, 148, 400);
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.Selection);
		toolItem.setImage(SWTResourceManager.getImage(DataIntegrate2.class, "/org/cnstar/images/Gnome-Go-Bottom-32.png"));
		toolItem.setText("\u8F7D\u5165\u83B7\u53D6\u7ED3\u679C\u8868");
		
		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.setImage(SWTResourceManager.getImage(DataIntegrate2.class, "/org/cnstar/images/Add-Files-To-Archive-32.png"));
		toolItem_1.setText("\u751F\u6210\u805A\u5408\u8868");
		
		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("\u83B7\u53D6\u7ED3\u679C\u8868");
		group_1.setBounds(168, 10, 210, 417);
		
		final List list = new List(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		 final SelectionAdapter selectionAdapter=new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button item = (Button) e.widget;  
					 if (((item.getStyle() & SWT.CHECK) != 0))  {
						 System.out.println(item.getText());
	             	             for(int i = 0;i<tableList.size();i++){
		            	if(item.equals(jcb[i])) {
		            		if(jcb[i].getSelection()){
			                	for(String str:values[i])
			                		list.add(str+"("+jcb[i].getText()+")");
			                	list.setBounds(10, 20, 187, 390);
		            		}
		            		else {
		            			String[] field = list.getItems();
		            			for(String str:field){
		            				System.out.println(str);
		            				if(str.indexOf("("+jcb[i].getText()+")")>=0){
		            					System.out.println("ttt");
		            					list.remove(str);
		            				}
		            			}
		            		}
			            }
		            }
	         }
				}
			};
			
		Listener selectionListener = new Listener() {  
            public void handleEvent(Event event) {  
                ToolItem item = (ToolItem) event.widget;  
                System.out.println(item.getText() + " is selected");  
                if ((item.getStyle() & SWT.Selection) != 0  
                        )  {
                    System.out.println("Selection status: "  
                            + item.getSelection());  
                    System.out.println("dddddd");
    				ConnectmySQL connectmySQL = new ConnectmySQL();
    				try {
    					Connection conn = connectmySQL.getConn();
    					tableList = connectmySQL.gettableList(conn);
    					System.out.println(tableList.size());
    					for(int i=0;i<tableList.size();i++){
    						jcb[i]=new Button(composite_1, SWT.CHECK);
    						jcb[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    						jcb[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    						jcb[i].setBounds(10, 10+i*23, 142, 17);
    						jcb[i].setText(tableList.get(i));
    						jcb[i].addSelectionListener(selectionAdapter);
    						java.util.List<String> tablefield = connectmySQL.gettablefiled(conn, tableList.get(i));
    						int j=0;
    						values[i] = new String[tablefield.size()];
    						for(String field:tablefield){
    							values[i][j++]=field;
    						}
    					}
    					conn.close();
    				} catch (Exception e1) {
    					e1.printStackTrace();
    				}
    				composite_1.update();
    				composite_1.redraw();
    				composite.update();
    				composite.redraw();
                }
            }  
        };  
        toolItem.addListener(SWT.Selection, selectionListener);  
		
        Group group = new Group(composite, SWT.NONE);
		group.setText("\u805A\u5408\u8868");
		group.setBounds(437, 10, 210, 390);
		
        final List list_2 = new List(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list_2.setItems(new String[] {});
		list_2.setBounds(10, 21, 189, 362);
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String [] test=list.getSelection();
				for(int i=0;i<test.length;i++){
					list_2.add(test[i]);
					list.remove(test[i]);
				}
			}
		});
		button_1.setText(">>");
		button_1.setBounds(383, 218, 48, 27);
		
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String [] test=list_2.getSelection();
				for(int i=0;i<test.length;i++){
					list.add(test[i]);
					list_2.remove(test[i]);
				}
			}
		});
		button_2.setText("<<");
		button_2.setBounds(447, 406, 48, 27);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(160, 11, 2, 417);
		
		
		
		
		
//		Group group_1 = new Group(composite, SWT.NONE);
//		group_1.setText("\u83B7\u53D6\u7ED3\u679C\u8868");
//		group_1.setBounds(168, 10, 210, 423);
		
//		List list = new List(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		list.setItems(new String[] {"\u56FD\u5BB6\u540D\u79F0", "\u9996\u90FD", "GeoLocation", "\u603B\u4EBA\u53E3", "\u56FD\u571F\u9762\u79EF", "GDP", "\u4EBA\u5747GDP"});
//		list.setBounds(10, 23, 187, 390);
		
		Button button_3 = new Button(composite, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] result=list_2.getItems();
				if(result.length>0){
					ConnectmySQL connectmySQL = new ConnectmySQL();
					Connection conn;
					try {
						conn = connectmySQL.getConn();
						//判断表是否存在
//						String sql=null;
						for(String str:tableList){
							if("resulttable".equalsIgnoreCase(str))
							{
								String sql ="drop table ResultTable cascade" ;
								PreparedStatement pstmt = conn.prepareStatement(sql);
								pstmt.executeUpdate();
//								return;
							}
						}
						//如果不存在的话
						
						String sql = "CREATE TABLE ResultTable ( ";
						for(String str :result){
							str = str.substring(0,str.indexOf('('));
							sql+=str+" TEXT, ";
						}
						sql = sql.substring(0,sql.lastIndexOf(','));
						sql+=" );";
//						System.out.println(sql);
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();
						
						
					} 
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
		button_3.setBounds(556, 406, 80, 27);
		button_3.setText("\u751F\u6210\u805A\u5408\u8868");
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 11, 130, 17);
		label_1.setText("\u83B7\u53D6\u7ED3\u679C\u8868\u5217\u8868");

	}
}
