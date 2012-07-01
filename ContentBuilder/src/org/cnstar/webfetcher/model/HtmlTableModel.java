package org.cnstar.webfetcher.model;

import org.cnstar.browser.LadyrBrowser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html2.HTMLTableCellElement;
import org.w3c.dom.html2.HTMLTableSectionElement;


public class HtmlTableModel {

    public static int findTrIndex(Node dataNode,Node headerNode , LadyrBrowser browser) {
	int index = -1;
	while (dataNode.getParentNode() != null
		&& !dataNode.getParentNode().getNodeName().equals("td")) {
	    dataNode = dataNode.getParentNode();
	}
	// 此时dataNode为td或null
	if (dataNode != null) {
	    if (dataNode instanceof HTMLTableCellElement) {
		index = ((HTMLTableCellElement) dataNode).getCellIndex();
	    }
	}
	
	while (headerNode.getParentNode() != null
		&& !headerNode.getParentNode().getNodeName().equals("td")) {
	    headerNode = headerNode.getParentNode();
	}
	// 此时headerNode为td或null
	if (headerNode != null) {
	    if (headerNode instanceof HTMLTableCellElement) {
		index = ((HTMLTableCellElement) headerNode).getCellIndex();
	    }
	}
	
	
	
	while(dataNode.getParentNode()!=null&& !dataNode.getParentNode().getNodeName().equals("tbody"))
	{
	    dataNode = dataNode.getParentNode();
	}
	// 此时dataNode为tbody或null


//	if (dataNode != null) {
//	    NodeList nodeList =	dataNode.getChildNodes();
//	    for(int i =0;i<nodeList.getLength();++i)
//	    {
//		if(nodeList.item(i))
//	    }
//	}
	
	return index;
    }
    
    public static boolean testIfHeaderCorrect(String headerXpath, int index, String text){
	
//	Node node = browser.xpathNode(xpath);
	return false;
    }

}
