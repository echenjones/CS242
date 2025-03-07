/*
 * File: XMLBIFParser.java
 * Creator: George Ferguson
 * Created: Sun Mar 25 15:38:48 2012
 * Time-stamp: <Sat Mar 26 08:52:13 EDT 2016 ferguson>
 */

//package bn.parser;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

//import bn.core.*;

/**
 * DocumentBuilder-based DOM parser for
 * <a href="http://sites.poli.usp.br/p/fabio.cozman/Research/InterchangeFormat/index.html">XMLBIF</a>
 * files.
 * <p>
 * Note that XMLBIF explicitly states that <q>There is no mandatory
 * order of variable and probability blocks.</q> This means that we
 * have to read the DOM, then create nodes for all the variables using
 * the {@code variable} elements, then hook them up and add the CPTs
 * using the {@code definition} blocks. A good reason to use a DOM
 * parser rather than a SAX parser.
 * <p>
 * Also XMLBIF appears to use uppercase tag names, perhaps thinking they
 * really ought to be case-insensitive.
 * <p>
 * I have implemented minimal sanity checking and error handling.
 * You could do better. Caveat codor.
 */
public class XMLBIFParser {

	public BayesianNet readNetworkFromFile(String filename) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(filename));
		return processDocument(doc);
	}

	protected BayesianNet processDocument(Document doc) {
		BayesianNet network = new BayesianNet();
		doForEachElement(doc, "VARIABLE", new ElementTaker() {
			public void element(Element e) {
				processVariableElement(e, network);
			}
		});
		doForEachElement(doc, "DEFINITION", new ElementTaker() {
			public void element(Element e) {
				processDefinitionElement(e, network);
			}
		});
		network.topologicalSort();
		return network;
	}

	protected void doForEachElement(Document doc, String tagname, ElementTaker taker) {
		NodeList nodes = doc.getElementsByTagName(tagname);
		if (nodes != null && nodes.getLength() > 0) {
			for (int i=0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				taker.element((Element)node);
			}
		}
	}

	protected void processVariableElement(Element e, BayesianNet network) {
		Element nameElt = getChildWithTagName(e, "NAME");
		String name = getChildText(nameElt);
		ArrayList<String> values = new ArrayList<String>();
		doForEachChild(e, "OUTCOME", new ElementTaker() {
			public void element(Element e) {
				String value = getChildText(e);
				values.add(value);
			}
		});
		Variable var = new Variable(name, values);
		BNNode n = new BNNode(var, new ArrayList<BNNode>(), new ArrayList<Double>());
		network.addNode(n);
	}

	protected void processDefinitionElement(Element e, final BayesianNet network) {
		Element forElt = getChildWithTagName(e, "FOR");
		String forName = getChildText(forElt);
		BNNode forNode = network.getNode(forName);
		ArrayList<BNNode> givens = new ArrayList<BNNode>();
		doForEachChild(e, "GIVEN", new ElementTaker() {
			public void element(Element e) {
				String value = getChildText(e);
				givens.add(network.getNode(value));
			}
		});
		Element tableElt = getChildWithTagName(e, "TABLE");
		String tableStr = getChildText(tableElt);
		String[] tableArr = tableStr.split("\\s+");
		ArrayList<Double> cptTable = new ArrayList<Double>();
		for (String s : tableArr) {
			if (!s.isEmpty()) cptTable.add(Double.parseDouble(s));
		}
		forNode.setParents(givens);
		forNode.setTable(cptTable);
	}

	protected Element getChildWithTagName(Element elt, String tagname) {
		NodeList children = elt.getChildNodes();
		if (children != null && children.getLength() > 0) {
			for (int i=0; i < children.getLength(); i++) {
				Node node = children.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element childElt = (Element)node;
					if (childElt.getTagName().equals(tagname)) {
						return childElt;
					}
				}
			}
		}
		throw new NoSuchElementException(tagname);
	}

	protected void doForEachChild(Element elt, String tagname, ElementTaker taker) {
		NodeList children = elt.getChildNodes();
		if (children != null && children.getLength() > 0) {
			for (int i=0; i < children.getLength(); i++) {
				Node node = children.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element childElt = (Element)node;
					if (childElt.getTagName().equals(tagname)) {
						taker.element(childElt);
					}
				}
			}
		}
	}

	/**
	 * Returns the concatenated child text of the specified node.
	 * This method only looks at the immediate children of type
	 * Node.TEXT_NODE or the children of any child node that is of
	 * type Node.CDATA_SECTION_NODE for the concatenation.
	 */
	public String getChildText(Node node) {
		if (node == null) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		Node child = node.getFirstChild();
		while (child != null) {
			short type = child.getNodeType();
			if (type == Node.TEXT_NODE) {
				buf.append(child.getNodeValue());
			}
			else if (type == Node.CDATA_SECTION_NODE) {
				buf.append(getChildText(child));
			}
			child = child.getNextSibling();
		}
		return buf.toString();
	}

	protected void trace(String msg) {
		System.err.println(msg);
	}

	/**
	 * Parse an XMLBIF file and print out the resulting BayesianNetwork.
	 * <p>
	 * Usage: java bn.parser.XMLBIFParser FILE
	 * <p>
	 * With no arguments: reads aima-alarm.xml in the src tree 
	 */
	public static void main(String[] argv) throws IOException, ParserConfigurationException, SAXException {
		String filename = "insurance.xml";
		if (argv.length > 0) {
			filename = argv[0];
		}
		XMLBIFParser parser = new XMLBIFParser();
		BayesianNet network = parser.readNetworkFromFile(filename);
		network.printNetwork();
	}

}

interface ElementTaker {
	public void element(Element e);
}