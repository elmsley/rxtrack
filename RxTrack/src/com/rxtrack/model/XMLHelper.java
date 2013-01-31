package com.rxtrack.model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper {

	private static XMLHelper instance = null;
	
	private XMLHelper(){}
	
	public static synchronized XMLHelper getInstance(){ 
		if (instance == null){
			instance = new XMLHelper();
		}
		return instance; 
	}
	
	public int getAttributeIntValue(Element element, String attributeKey){
		String rawData = getAttributeValue(element, attributeKey);
		try {
			return Integer.parseInt(rawData);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public String getAttributeValue(Element element, String attributeKey){
		NamedNodeMap attributeMap = element.getAttributes();
		return attributeMap.getNamedItem(attributeKey).getNodeValue();
	}

	public String getElementValue(Element element, String tag) {
		Node aNode = element.getElementsByTagName(tag).item(0);
		if (aNode==null) return "";
		NodeList nodes = aNode.getChildNodes();
		Node node = (Node) nodes.item(0);
		if (node==null) return "";
		return node.getNodeValue();
	}

	public void addAttribute(Document doc, Element element, String key,
			String value) {
		Attr attr = doc.createAttribute(key);
		attr.setValue(value);
		element.setAttributeNode(attr);
	}

	public void addSubElement(Document doc, Element parent, String key,
			String value) {
		Element element = doc.createElement(key);
		element.setTextContent(value);
		parent.appendChild(element);
	}

}
