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

//org.w3c imports
import org.w3c.dom.NamedNodeMap;
import org.mozilla.interfaces.*;

public class DocumentTypeImpl extends NodeImpl implements org.w3c.dom.DocumentType
{
    protected DocumentTypeImpl(nsIDOMDocumentType mozInst)
    {
        super( mozInst );
    }

    public static DocumentTypeImpl getDOMInstance(nsIDOMDocumentType mozInst)
    {
        DocumentTypeImpl node = (DocumentTypeImpl) instances.get(mozInst);
        return node == null ? new DocumentTypeImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMDocumentType getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMDocumentType) moz;
    }
    
    public String getInternalSubset()
    {
        String result = getInstance().getInternalSubset();
        return result;
    }
    
    public String getPublicId()
    {
        String result = getInstance().getPublicId();
        return result;
    }
    
    public String getName()
    {
        String result = getInstance().getName();
        return result;
    }
    
    public String getSystemId()
    {
        String result = getInstance().getSystemId();
        return result;
    }
    
    public NamedNodeMap getNotations()
    {
        nsIDOMNamedNodeMap result = getInstance().getNotations();
        return new NamedNodeMapImpl(result);
    }
    
    public NamedNodeMap getEntities()
    {
        nsIDOMNamedNodeMap result = getInstance().getEntities();
        return new NamedNodeMapImpl(result);
    }
    
}
