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
import org.mozilla.dom.*;
//XPCOM import
import org.mozilla.interfaces.*;


public class HTMLLegendElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLLegendElement
{
    protected HTMLLegendElementImpl(nsIDOMHTMLLegendElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLLegendElementImpl getDOMInstance(nsIDOMHTMLLegendElement mozInst)
    {
        HTMLLegendElementImpl node = (HTMLLegendElementImpl) instances.get(mozInst);
        return node == null ? new HTMLLegendElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLLegendElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLLegendElement) moz;
    }
    
    public void setAlign(String align)
    {
        getInstance().setAlign(align);
    }
    
    public HTMLFormElement getForm()
    {
        nsIDOMHTMLFormElement result = getInstance().getForm();
        return (HTMLFormElement) NodeFactory.getNodeInstance(result);
    }
    
    public String getAlign()
    {
        String result = getInstance().getAlign();
        return result;
    }
    
    public String getAccessKey()
    {
        String result = getInstance().getAccessKey();
        return result;
    }
    
    public void setAccessKey(String accessKey)
    {
        getInstance().setAccessKey(accessKey);
    }
    
}
