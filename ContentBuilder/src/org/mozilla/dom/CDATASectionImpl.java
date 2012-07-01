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


package org.mozilla.dom;

import org.mozilla.interfaces.*;

//XPCOM import


public class CDATASectionImpl extends TextImpl implements org.w3c.dom.CDATASection
{
    protected CDATASectionImpl(nsIDOMCDATASection mozInst)
    {
        super( mozInst );
    }

    public static CDATASectionImpl getDOMInstance(nsIDOMCDATASection mozInst)
    {
        CDATASectionImpl node = (CDATASectionImpl) instances.get(mozInst);
        return node == null ? new CDATASectionImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMCDATASection getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMCDATASection) moz;
    }
    
}
