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

//XPCOM import
import org.mozilla.interfaces.*;


public class HTMLMetaElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLMetaElement
{
    protected HTMLMetaElementImpl(nsIDOMHTMLMetaElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLMetaElementImpl getDOMInstance(nsIDOMHTMLMetaElement mozInst)
    {
        HTMLMetaElementImpl node = (HTMLMetaElementImpl) instances.get(mozInst);
        return node == null ? new HTMLMetaElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLMetaElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLMetaElement) moz;
    }
    
    public String getHttpEquiv()
    {
        String result = getInstance().getHttpEquiv();
        return result;
    }
    
    public void setName(String name)
    {
        getInstance().setName(name);
    }
    
    public String getName()
    {
        String result = getInstance().getName();
        return result;
    }
    
    public String getScheme()
    {
        String result = getInstance().getScheme();
        return result;
    }
    
    public void setScheme(String scheme)
    {
        getInstance().setScheme(scheme);
    }
    
    public void setHttpEquiv(String httpEquiv)
    {
        getInstance().setHttpEquiv(httpEquiv);
    }
    
    public String getContent()
    {
        String result = getInstance().getContent();
        return result;
    }
    
    public void setContent(String content)
    {
        getInstance().setContent(content);
    }
    
}
