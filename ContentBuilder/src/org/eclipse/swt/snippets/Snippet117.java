package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;  
import org.eclipse.swt.graphics.GC;  
import org.eclipse.swt.graphics.Image;  
import org.eclipse.swt.layout.FillLayout;  
import org.eclipse.swt.widgets.Display;  
import org.eclipse.swt.widgets.Shell;  
import org.eclipse.swt.widgets.Table;  
import org.eclipse.swt.widgets.TableItem;  
public class Snippet117 {  
      
public static void main (String [] args) {  
    final Display display = new Display ();  
    final Image image = new Image (display, 16, 16);  
    GC gc = new GC (image);  
    gc.setBackground (display.getSystemColor (SWT.COLOR_RED));  
    gc.fillRectangle (image.getBounds ());  
    gc.dispose ();  
    final Shell shell = new Shell (display);  
    shell.setText ("Lazy Table");  
    shell.setLayout (new FillLayout ());  
    final Table table = new Table (shell, SWT.BORDER | SWT.MULTI);  
    table.setSize (200, 200);  
    //����һ���߳�  
    Thread thread = new Thread () {  
        public void run () {  
            for (int i=0; i<20000; i++) {  
                try {  
                    sleep(3000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                if (table.isDisposed ()) return;  
                final int [] index = new int [] {i};  
                //����ʹ��display�� syncExec ���� asyncExec ����������  
                display.syncExec (new Runnable () {  
                    public void run () {  
                        if (table.isDisposed ()) return;  
                        TableItem item = new TableItem (table, SWT.NONE);  
                        item.setText ("Table Item " + index [0]);  
                        item.setImage (image);  
                    }  
                });  
                // ���ʹ�������ע�ͷ�������׳���org.eclipse.swt.SWTException: Invalid thread access  
//              if (table.isDisposed ()) return;  
//              TableItem item = new TableItem (table, SWT.NONE);  
//              item.setText ("Table Item " + index [0]);  
//              item.setImage (image);  
            }  
        }  
    };  
    thread.start ();  
    shell.setSize (200, 200);  
    shell.open ();  
    while (!shell.isDisposed ()) {  
        if (!display.readAndDispatch ()) display.sleep ();  
    }  
    image.dispose ();  
    display.dispose ();  
}  
}  
