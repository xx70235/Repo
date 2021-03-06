/*
 * Copyright (c) 2003 World Wide Web Consortium,
 * (Massachusetts Institute of Technology, Institut National de
 * Recherche en Informatique et en Automatique, Keio University). All
 * Rights Reserved. This program is distributed under the W3C's Software
 * Intellectual Property License. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.
 * See W3C License http://www.w3.org/Consortium/Legal/ for more details.
 */

package org.w3c.dom.html2;

/**
 * Client-side image map area definition. See the AREA element definition in 
 * HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLAreaElement extends HTMLElement {
    /**
     * A single character access key to give access to the form control. See 
     * the accesskey attribute definition in HTML 4.01.
     */
    public String getAccessKey();
    /**
     * A single character access key to give access to the form control. See 
     * the accesskey attribute definition in HTML 4.01.
     */
    public void setAccessKey(String accessKey);

    /**
     * Alternate text for user agents not rendering the normal content of this 
     * element. See the alt attribute definition in HTML 4.01.
     */
    public String getAlt();
    /**
     * Alternate text for user agents not rendering the normal content of this 
     * element. See the alt attribute definition in HTML 4.01.
     */
    public void setAlt(String alt);

    /**
     * Comma-separated list of lengths, defining an active region geometry. 
     * See also <code>shape</code> for the shape of the region. See the 
     * coords attribute definition in HTML 4.01.
     */
    public String getCoords();
    /**
     * Comma-separated list of lengths, defining an active region geometry. 
     * See also <code>shape</code> for the shape of the region. See the 
     * coords attribute definition in HTML 4.01.
     */
    public void setCoords(String coords);

    /**
     * The URI [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>] of the linked resource. See the href attribute definition in 
     * HTML 4.01.
     */
    public String getHref();
    /**
     * The URI [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>] of the linked resource. See the href attribute definition in 
     * HTML 4.01.
     */
    public void setHref(String href);

    /**
     * Specifies that this area is inactive, i.e., has no associated action. 
     * See the nohref attribute definition in HTML 4.01.
     */
    public boolean getNoHref();
    /**
     * Specifies that this area is inactive, i.e., has no associated action. 
     * See the nohref attribute definition in HTML 4.01.
     */
    public void setNoHref(boolean noHref);

    /**
     * The shape of the active area. The coordinates are given by 
     * <code>coords</code>. See the shape attribute definition in HTML 4.01.
     */
    public String getShape();
    /**
     * The shape of the active area. The coordinates are given by 
     * <code>coords</code>. See the shape attribute definition in HTML 4.01.
     */
    public void setShape(String shape);

    /**
     * Index that represents the element's position in the tabbing order. See 
     * the tabindex attribute definition in HTML 4.01.
     */
    public int getTabIndex();
    /**
     * Index that represents the element's position in the tabbing order. See 
     * the tabindex attribute definition in HTML 4.01.
     */
    public void setTabIndex(int tabIndex);

    /**
     * Frame to render the resource in. See the target attribute definition in 
     * HTML 4.01.
     */
    public String getTarget();
    /**
     * Frame to render the resource in. See the target attribute definition in 
     * HTML 4.01.
     */
    public void setTarget(String target);

}
