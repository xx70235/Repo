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


public class DocumentFragmentImpl extends NodeImpl implements org.w3c.dom.DocumentFragment
{
    protected DocumentFragmentImpl(nsIDOMDocumentFragment mozInst)
    {
        super( mozInst );
    }

    public static DocumentFragmentImpl getDOMInstance(nsIDOMDocumentFragment mozInst)
    {
        DocumentFragmentImpl node = (DocumentFragmentImpl) instances.get(mozInst);
        return node == null ? new DocumentFragmentImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMDocumentFragment getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMDocumentFragment) moz;
    }
    
}
