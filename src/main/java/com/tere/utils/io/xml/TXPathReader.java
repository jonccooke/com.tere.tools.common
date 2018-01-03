package com.tere.utils.io.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.io.TReader;

public abstract class TXPathReader<T> implements TReader<T>
{
	private static Logger log = LogManager.getLogger(TXPathReader.class);

	private DocumentBuilderFactory factory = null;
	private DocumentBuilder builder = null;
	private Document doc = null;
	private XPathFactory xPathfactory = null;
	private Map<String, XPathFieldMapping> fieldToMappingMap = new HashMap<String, XPathFieldMapping>();
	private Map<String, XPathExpression> fieldToXPathMap = new HashMap<String, XPathExpression>();
	private XPath xpath = null;
	private XPathExpression objectListXpath;

	public TXPathReader(String xPathObjectMapping) throws TereException
	{
		try
		{
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			xPathfactory = XPathFactory.newInstance();
			xpath = xPathfactory.newXPath();
			objectListXpath = xpath.compile(xPathObjectMapping);
		}
		catch (XPathExpressionException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (ParserConfigurationException e)
		{
			throw new TereException(e.getMessage(), e);
		}

	}

	public void putPrimativeFieldMapping(String fieldName, String xPath)
			throws XPathExpressionException
	{
		XPathFieldMapping<T> attMapping = new PrimativeFieldMapping<T>();

		fieldToMappingMap.put(fieldName, attMapping);
		fieldToXPathMap.put(fieldName, xpath.compile(xPath));
	}

	public void putClassFieldMapping(String fieldName, String xPath)
			throws XPathExpressionException
	{
		XPathExpression xPathExpression = xpath.compile(xPath);
		XPathFieldMapping<T> attMapping = new PrimativeFieldMapping<T>();

		fieldToMappingMap.put(fieldName, attMapping);
		fieldToXPathMap.put(fieldName, xpath.compile(xPath));
	}
	protected abstract T createNewObject(Node node) throws TereException;

	protected String getAttributeValue(Node node, String fieldName) throws XPathExpressionException
	{
		XPathExpression xPathExpression = fieldToXPathMap.get(fieldName);
		Node attrNode = (Node) xPathExpression.evaluate(node,
				XPathConstants.NODE);
		String value = attrNode.getNodeValue();
		return value;
	}

	@Override
	public T create(Object readObject) throws TereException
	{
		Node node = (Node) readObject;
		String name = node.getNodeName();
		String value;
		log.info("node name:%s", name);
		T newObject = createNewObject(node);
		try
		{
			for (String fieldName : fieldToMappingMap.keySet())
			{
				XPathFieldMapping<T> fieldMapping = fieldToMappingMap.get(fieldName);
				value = getAttributeValue(node, fieldName);
				fieldMapping.map(newObject, fieldName, value);
			}
		}
		catch (XPathExpressionException e)
		{
			throw new TereException(e);
		}
		log.info("Done");
		return newObject;
	}

	@Override
	public Collection<T> read(InputStream inputStream, Properties properties)
			throws TereException
	{
		Collection<T> returnObjects = new ArrayList<T>();
		NodeList nodeList = null;
		try
		{
			doc = builder.parse(inputStream);
			nodeList = (NodeList) objectListXpath.evaluate(doc,
					XPathConstants.NODESET);
			for (int nodeNo = 0; nodeNo < nodeList.getLength(); nodeNo++)
			{
				Node node = nodeList.item(nodeNo);
				T newObject = create(node);
				returnObjects.add(newObject);
			}
		}
		catch (SAXException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (IOException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (XPathExpressionException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		return returnObjects;
	}

}
