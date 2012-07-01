package org.cnstar.webfetcher.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RegularModel {

	ArrayList<RegularUnit> regularUnitList = new ArrayList<RegularUnit>();
	ArrayList<String> pageUrlList = new ArrayList<String>();
	String projectName;

	/**
	 * @return the regularUnitList
	 */
	public ArrayList<RegularUnit> getRegularUnitList() {
		return regularUnitList;
	}

	/**
	 * @param regularUnitList
	 *            the regularUnitList to set
	 */
	public void setRegularUnitList(ArrayList<RegularUnit> regularUnitList) {
		this.regularUnitList = regularUnitList;
	}

	public void generateRegularList(File regularFile) {

		try {
			regularUnitList.clear();
			pageUrlList.clear();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(regularFile);
			NodeList rootNode = doc.getElementsByTagName("Regulars");
			for (int index = 0; index < rootNode.getLength(); ++index) {
				Node fatherNode = rootNode.item(index);
				NamedNodeMap attributes = fatherNode.getAttributes();
				for (int j = 0; j < attributes.getLength(); ++j) {
					Node attribute = attributes.item(j);
					projectName = attribute.getNodeValue();
				}
			}

			NodeList nodeList = doc.getElementsByTagName("WrapUrls");
			for (int index = 0; index < nodeList.getLength(); ++index) {
				Node fatherNode = nodeList.item(index);
				NodeList childNodes = fatherNode.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);
					// 如果这个节点属于Element ,再进行取值
					if (childNode instanceof Element) {
						if (childNode.getNodeName().equals("url")) {
							pageUrlList.add(childNode.getFirstChild().getNodeValue());
						}
					}
				}
			}

			nodeList = doc.getElementsByTagName("RegularUnit");
			for (int index = 0; index < nodeList.getLength(); ++index) {
				Node fatherNode = nodeList.item(index);
				RegularUnit ru = new RegularUnit();
				// 把父节点的属性拿出来
				NamedNodeMap attributes = fatherNode.getAttributes();

				for (int i = 0; i < attributes.getLength(); i++) {
					Node attribute = attributes.item(i);

					ru.setStep(Integer.parseInt(attribute.getNodeValue()));
				}

				NodeList childNodes = fatherNode.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);
					// 如果这个节点属于Element ,再进行取值
					if (childNode instanceof Element) {
						if (childNode.getNodeName().equals("TargetMark")) {
							ru.setXpath(childNode.getFirstChild().getNodeValue());
						} else if (childNode.getNodeName().equals("Option")) {
							ru.setOption(childNode.getFirstChild().getNodeValue());
						} else if (childNode.getNodeName().equals("Mapping")) {
							ru.setMapping(childNode.getFirstChild().getNodeValue());
						} else if (childNode.getNodeName().equals("Repeat")) {
							if (childNode.getFirstChild().getNodeValue().equals("true"))
								ru.setRepeat(true);
							else
								ru.setRepeat(false);
						}
					}
				}
				regularUnitList.add(ru);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		}

	}

	/**
	 * @return the pageUrlList
	 */
	public ArrayList<String> getPageUrlList() {
		return pageUrlList;
	}

	/**
	 * @param pageUrlList
	 *            the pageUrlList to set
	 */
	public void setPageUrlList(ArrayList<String> pageUrlList) {
		this.pageUrlList = pageUrlList;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
