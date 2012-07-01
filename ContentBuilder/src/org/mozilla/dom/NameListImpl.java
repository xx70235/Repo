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

//XPCOM import
import org.mozilla.interfaces.*;


public class NameListImpl implements org.w3c.dom.NameList
{
    protected nsIDOMNameList moz;
    protected static WeakValueHashMap instances = new WeakValueHashMap();
    
    public NameListImpl(nsIDOMNameList mozInst)
    {
        moz = mozInst;
        instances.put(mozInst, this);
    }    

    public static NameListImpl getDOMInstance(nsIDOMNameList mozInst)
    {
        NameListImpl node = (NameListImpl) instances.get(mozInst);
        return node == null ? new NameListImpl(mozInst) : node;
    }
    
    public nsIDOMNameList getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return moz;
    }
    
    public int getLength()
    {
        long result = getInstance().getLength();
        return (int) result;        
    }
    
    public String getName(int index)
    {
        String result = getInstance().getName(index);
        return result;
    }
    
    public boolean contains(String str)
    {

        throw new UnsupportedException();
    }
    
    public boolean containsNS(String namespaceURI, String name)
    {

        throw new UnsupportedException();
    }
    
    public String getNamespaceURI(int index)
    {
        String result = getInstance().getNamespaceURI(index);
        return result;
    }
    
}
