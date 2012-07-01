/*
+-----------------------------------------------+
| do NOT edit this file!                        |
+-----------------------------------------------+
| This file is an auto-generated implementation | 
| of the coresponding org.w3c.dom interface via |
| Java-XPCOM                                    |
| (c)2005, Peter Szinek [peter@rt.sk]           |
+-----------------------------------------------+
*/


package org.mozilla.dom.html;

import org.w3c.dom.html2.*;
//com.lixto imports
import org.eclipse.swt.widgets.Display;
//XPCOM import
import org.mozilla.interfaces.*;


public class HTMLFormElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLFormElement
{
    protected HTMLFormElementImpl(nsIDOMHTMLFormElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLFormElementImpl getDOMInstance(nsIDOMHTMLFormElement mozInst)
    {
        HTMLFormElementImpl node = (HTMLFormElementImpl) instances.get(mozInst);
        return node == null ? new HTMLFormElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLFormElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLFormElement) moz;
    }
    
    public String getAcceptCharset()
    {
        String result = getInstance().getAcceptCharset();
        return result;
    }
    
    public void reset()
    {
        getInstance().reset();
    }
    
    public String getAction()
    {
        String result = getInstance().getAction();
        return result;
    }
    
    public void setAcceptCharset(String acceptCharset)
    {
        getInstance().setAcceptCharset(acceptCharset);
    }
    
    public void setName(String name)
    {
        getInstance().setName(name);
    }
    
    public int getLength()
    {
        int result = getInstance().getLength();
        return result;
    }
    
    public String getName()
    {
        String result = getInstance().getName();
        return result;
    }
    
    public void setMethod(String method)
    {
        getInstance().setMethod(method);
    }
    
    public String getMethod()
    {
        String result = getInstance().getMethod();
        return result;
    }
    
    public void setEnctype(String enctype)
    {
        getInstance().setEnctype(enctype);
    }
    
    public String getTarget()
    {
        String result = getInstance().getTarget();
        return result;
    }
    
    public void setAction(String action)
    {
        getInstance().setAction(action);
    }
    
    public void submit()
    {
    	if(Display.getCurrent() != null) {
    		getInstance().submit();
    	}
    	else {
    		Display.getDefault().syncExec(new Runnable(){
				public void run() {
			    	getInstance().submit();										
				}
    		}); // notice SYNC exec
    	}
    }
    
    public HTMLCollection getElements()
    {
        nsIDOMHTMLCollection result = getInstance().getElements();
        return new HTMLCollectionImpl(result);
    }
    
    public void setTarget(String target)
    {
        getInstance().setTarget(target);
    }
    
    public String getEnctype()
    {
        String result = getInstance().getEnctype();
        return result;
    }
    
}
