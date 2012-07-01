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


package org.mozilla.dom.events;

import org.w3c.dom.events.*;
//com.lixto imports
import org.mozilla.dom.*;
//XPCOM import
import org.mozilla.interfaces.*;


public class DocumentEventImpl implements org.w3c.dom.events.DocumentEvent
{
    protected nsIDOMDocumentEvent moz;
    protected static WeakValueHashMap instances = new WeakValueHashMap();
    
    public DocumentEventImpl(nsIDOMDocumentEvent mozInst)
    {
        moz = mozInst;
        instances.put(mozInst, this);
    }    

    public static DocumentEventImpl getDOMInstance(nsIDOMDocumentEvent mozInst)
    {
        DocumentEventImpl node = (DocumentEventImpl) instances.get(mozInst);
        return node == null ? new DocumentEventImpl(mozInst) : node;
    }
    
    public nsIDOMDocumentEvent getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return moz;
    }
    
    public Event createEvent(String eventType)
    {
        nsIDOMEvent result = getInstance().createEvent(eventType);
        return new EventImpl(result);
    }
    
}