package org.cnstar.webfetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class BulkPagesSetting extends Dialog {

    protected Object result;
    protected Shell shell;
    private Text txAddrRegular;
    private Text txRegularFile;
    private Tree addrTree;
    private Text txProjectName;
    private String projectName;
    private int fetchType=1;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public BulkPagesSetting(Shell parent, int style, Tree tree) {
	super(parent, style);
	setText("SWT Dialog");
	this.addrTree = tree;
	// this.projectName = projectName;
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
	createContents();
	shell.open();
	shell.layout();
	Display display = getParent().getDisplay();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}
	return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
	shell = new Shell(getParent(), getStyle());
	shell.setSize(394, 251);
	shell.setText("\u5185\u5BB9\u83B7\u53D6\u5DE5\u7A0B\u8BBE\u7F6E");

	txAddrRegular = new Text(shell, SWT.BORDER);
	txAddrRegular.setBounds(98, 60, 269, 23);
	txAddrRegular
		.setText("http://www.articlesbase.com/");

	Label label = new Label(shell, SWT.NONE);
	label.setText("\u5730\u5740\u8868\u8FBE\u5F0F\uFF1A");
	label.setBounds(20, 62, 72, 17);

	Label label_1 = new Label(shell, SWT.NONE);
	label_1.setBounds(20, 103, 61, 17);
	label_1.setText("\u5730\u5740\u53D8\u91CF\uFF1A");

	txRegularFile = new Text(shell, SWT.BORDER);
	txRegularFile.setBounds(98, 100, 244, 23);

	Button btOpenRegularFile = new Button(shell, SWT.NONE);
	btOpenRegularFile.setBounds(348, 98, 23, 27);
	btOpenRegularFile.setText("...");
	btOpenRegularFile.addSelectionListener(new OpenFileAction());

	Button btOK = new Button(shell, SWT.NONE);
	btOK.setBounds(98, 186, 80, 27);
	btOK.setText("\u786E\u5B9A");
	btOK.addSelectionListener(new AddTreeAction());

	Button btCancel = new Button(shell, SWT.NONE);
	btCancel.setBounds(287, 186, 80, 27);
	btCancel.setText("\u53D6\u6D88");
	shell.setLocation(Display.getCurrent().getClientArea().width / 2
		- shell.getShell().getSize().x / 2, Display.getCurrent()
		.getClientArea().height / 2 - shell.getSize().y / 2);

	Label label_2 = new Label(shell, SWT.NONE);
	label_2.setBounds(20, 23, 61, 17);
	label_2.setText("\u5DE5\u7A0B\u540D\u79F0\uFF1A");

	txProjectName = new Text(shell, SWT.BORDER);
	txProjectName.setBounds(97, 20, 269, 23);

	Label label_3 = new Label(shell, SWT.NONE);
	label_3.setBounds(20, 144, 61, 17);
	label_3.setText("\u83B7\u53D6\u7C7B\u578B\uFF1A");

	final Combo combo = new Combo(shell, SWT.NONE);
	combo.setItems(new String[] { "\u5355\u9875\u9762\u83B7\u53D6",
		"\u591A\u9875\u9762\u83B7\u53D6",
		"\u590D\u5408\u578B\u83B7\u53D6" });
	combo.setBounds(98, 141, 269, 25);
	combo.select(1);
	combo.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent arg0) {
		if (combo.getSelectionIndex() == 0) {
		    fetchType = 0;
		} else if (combo.getSelectionIndex() == 1) {
		    fetchType = 1;
		} else if (combo.getSelectionIndex() == 2) {
		    fetchType = 2;
		}
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {

	    }
	});
    }

    class OpenFileAction implements SelectionListener {

	@Override
	public void widgetSelected(SelectionEvent e) {
	    FileDialog fd = new FileDialog(shell, SWT.SAVE);
	    fd.setText("��");
	    String[] filterExt = { "*.txt" };
	    fd.setFilterExtensions(filterExt);
//	    if(fd.open()!=null&&fd.open().length()!=0)
	    txRegularFile.setText(fd.open());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	    // TODO Auto-generated method stub

	}

    }

    class AddTreeAction implements SelectionListener {

	@Override
	public void widgetSelected(SelectionEvent e) {
	    if (addrTree == null)
		return;
	    projectName = txProjectName.getText();
	    WebFetcher.projectName = projectName;
	    if (txRegularFile.getText().length() != 0) {
		BufferedReader bf;
		try {
		    bf = new BufferedReader(new FileReader(new File(
			    txRegularFile.getText())));

		    String tmp = "";
		    TreeItem root = new TreeItem(addrTree, 0);

		    if (txAddrRegular.getText().length() != 0) {
			root.setText(txAddrRegular.getText().replace(
				"[file_reg]", ""));
			root.setData(txAddrRegular.getText().replace(
				"[file_reg]", ""));
			while ((tmp = bf.readLine()) != null) {
			    TreeItem root1 = new TreeItem(root, 0);
			    root1.setText(txAddrRegular.getText().replace(
				    "[file_reg]", tmp));
			    root1.setData(txAddrRegular.getText().replace(
				    "[file_reg]", tmp));
			}
		    } else {
			root.setText(projectName);
			root.setData(projectName);
			while ((tmp = bf.readLine()) != null) {
			    TreeItem root1 = new TreeItem(root, 0);
			    root1.setText(tmp);
			    root1.setData(tmp);
			}
		    }

		} catch (FileNotFoundException e1) {
		    e1.printStackTrace();
		} catch (IOException e2) {
		    e2.printStackTrace();
		}
	    } else {
		TreeItem root = new TreeItem(addrTree, 0);
		root.setText(projectName);
		root.setData(projectName);

		TreeItem root1 = new TreeItem(root, 0);
		root1.setText(txAddrRegular.getText());
		root1.setData(txAddrRegular.getText());
	    }

	    shell.dispose();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	    // TODO Auto-generated method stub

	}

    }

    /**
     * @return the fetchType
     */
    public int getFetchType() {
        return fetchType;
    }

    /**
     * @param fetchType the fetchType to set
     */
    public void setFetchType(int fetchType) {
        this.fetchType = fetchType;
    }
}
