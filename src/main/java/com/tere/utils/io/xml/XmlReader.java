package com.tere.utils.io.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tere.TereException;

public class XmlReader implements AutoCloseable
{
	private DocumentBuilderFactory factory = null;
	private DocumentBuilder builder = null;
	private Document doc = null;
	private XPath xPath = null;

	public interface ListFunction
	{
		public void list(XmlReader xmlReader, Node node, String value,
				Map<String, String> attributes) throws TereException;
	};

	public XmlReader(String path) throws TereException
	{

		try (FileInputStream fileInputStream = new FileInputStream(path))
		{
			read(fileInputStream);
		}
		catch (FileNotFoundException e)
		{
			throw new TereException(e);
		}
		catch (IOException e)
		{
			throw new TereException(e);
		}

	}

	public XmlReader(InputStream inputStream) throws TereException
	{
		read(inputStream);
	}

	protected void read(InputStream inputStream) throws TereException
	{
		xPath = XPathFactory.newInstance().newXPath();

		factory = DocumentBuilderFactory.newInstance();
		try
		{
			builder = factory.newDocumentBuilder();
			doc = builder.parse(inputStream);
		}
		catch (ParserConfigurationException ex)
		{
			throw new TereException(ex);
		}
		catch (SAXException ex)
		{
			throw new TereException(ex);
		}
		catch (IOException ex)
		{
			throw new TereException(ex);
		}
	}

	public String getXPath(Node node)
	{
		Node parent = node.getParentNode();
		if (parent == null)
		{
			return "/" + node.getNodeName();
		}

		return getXPath(parent) + "/" + node.getNodeName();// "[@id='" +
															// node.getAttributes().getNamedItem("id")

	}

	public XPathExpression expression(String expression)
			throws XPathExpressionException
	{
		return xPath.compile(expression);
	}

	public void list(String expression, ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		list(expression(expression), function);
	}

	public void list(XPathExpression expression, ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{

		NodeList nodeList = (NodeList) expression.evaluate(doc,
				XPathConstants.NODESET);

		for (int nodeNo = 0; nodeNo < nodeList.getLength(); nodeNo++)
		{
			Node nNode = nodeList.item(nodeNo);

			NamedNodeMap attribtesMap = nNode.getAttributes();

			if (0 != attribtesMap.getLength())
			{
				Map<String, String> map = new HashMap<String, String>();

				for (int attNo = 0; attNo < attribtesMap.getLength(); attNo++)
				{
					Node node = attribtesMap.item(attNo);

					map.put(node.getNodeName(), node.getNodeValue());
				}

				function.list(this, nNode, nNode.getNodeValue(), map);
			}
		}
	}

	public void list(Node parentNode, String expression, ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		list(parentNode, expression(expression), function);
	}

	public void list(Node parentNode, XPathExpression expression,
			ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		// List list = new ArrayList();

		NodeList nodeList = (NodeList) expression.evaluate(parentNode,
				XPathConstants.NODESET);

		for (int nodeNo = 0; nodeNo < nodeList.getLength(); nodeNo++)
		{
			Node nNode = nodeList.item(nodeNo);

			NamedNodeMap attribtesMap = nNode.getAttributes();

			if (0 != attribtesMap.getLength())
			{
				Map<String, String> map = new HashMap<String, String>();

				for (int attNo = 0; attNo < attribtesMap.getLength(); attNo++)
				{
					Node node = attribtesMap.item(attNo);

					map.put(node.getNodeName(), node.getNodeValue());
				}

				function.list(this, nNode, nNode.getNodeValue(), map);
			}
		}
	}

	public void one(String expression, ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		one(expression(expression), function);
	}

	public void one(XPathExpression expression, ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		Node node = (Node) expression.evaluate(doc, XPathConstants.NODE);

		if (null != node)
		{
			NamedNodeMap attribtesMap = node.getAttributes();

			if (0 != attribtesMap.getLength())
			{
				Map<String, String> map = new HashMap<String, String>();

				for (int attNo = 0; attNo < attribtesMap.getLength(); attNo++)
				{
					Node attribtesNode = attribtesMap.item(attNo);

					map.put(attribtesNode.getNodeName(),
							attribtesNode.getNodeValue());
				}

				function.list(this, node, node.getNodeValue(), map);
			}
		}
	}

	public Node node(XPathExpression expression)
			throws XPathExpressionException, DOMException, TereException
	{
		Node node = (Node) expression.evaluate(doc, XPathConstants.NODE);

		if (null != node)
		{
			return node;
		}
		return null;
	}

	public Node node(String expression)
			throws XPathExpressionException, DOMException, TereException
	{
		Node node = (Node) expression(expression).evaluate(doc,
				XPathConstants.NODE);

		if (null != node)
		{
			return node;
		}
		return null;
	}

	public String one(XPathExpression expression)
			throws XPathExpressionException, DOMException, TereException
	{
		Node node = (Node) expression.evaluate(doc, XPathConstants.NODE);

		if (null != node)
		{
			return node.getNodeValue();
		}
		return null;
	}

	public String one(String expression)
			throws XPathExpressionException, DOMException, TereException
	{
		Node node = (Node) expression(expression).evaluate(doc,
				XPathConstants.NODE);

		if (null != node)
		{
			return node.getNodeValue();
		}
		return null;
	}

	public String one(Node node, String expression)
			throws XPathExpressionException, DOMException, TereException
	{
		Node retNode = (Node) expression(expression).evaluate(node,
				XPathConstants.NODE);

		if (null != retNode)
		{
			return retNode.getNodeValue();
		}
		return null;
	}

	public boolean one(Node node, String expression, ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		return one(node, expression(expression), function);
	}

	public boolean one(Node node, XPathExpression relativeExpression,
			ListFunction function)
			throws XPathExpressionException, DOMException, TereException
	{
		Node foundNode = (Node) relativeExpression.evaluate(node,
				XPathConstants.NODE);

		if (null != foundNode)
		{
			NamedNodeMap attribtesMap = foundNode.getAttributes();

			if (0 != attribtesMap.getLength())
			{
				Map<String, String> map = new HashMap<String, String>();

				for (int attNo = 0; attNo < attribtesMap.getLength(); attNo++)
				{
					Node attribtesNode = attribtesMap.item(attNo);

					map.put(attribtesNode.getNodeName(),
							attribtesNode.getNodeValue());
				}

				function.list(this, foundNode, foundNode.getNodeValue(), map);
			}
			return true;
		}
		return false;
	}

	public boolean existsOne(Node node, String expression)
			throws XPathExpressionException, DOMException, TereException
	{
		return existsOne(node, expression(expression));
	}

	public boolean existsOne(Node node, XPathExpression expression)
			throws XPathExpressionException, DOMException, TereException
	{

		Node foundNode = (Node) expression.evaluate(node, XPathConstants.NODE);

		if (null != foundNode)
		{
			return true;
		}
		return false;
	}

	public boolean existsOne(String expression)
			throws XPathExpressionException, DOMException, TereException
	{
		return existsOne(expression(expression));
	}

	public boolean existsOne(XPathExpression expression)
			throws XPathExpressionException, DOMException, TereException
	{

		Node node = (Node) expression.evaluate(doc, XPathConstants.NODE);

		if (null != node)
		{
			return true;
		}
		return false;
	}

	public void children(String expression, ListFunction listFunction)
			throws XPathExpressionException, DOMException, TereException
	{
		Map<String, String> map = null;
		NodeList nodeList = null;
		NamedNodeMap attributesMap = null;
		Node attribtesNode = null;
		Node childNode = null;
		Node node = (Node) expression(expression).evaluate(doc,
				XPathConstants.NODE);

		if ((null != node) && node.hasChildNodes())
		{
			nodeList = node.getChildNodes();

			if (null != nodeList)
			{
				for (int childNo = 0; childNo < nodeList.getLength(); childNo++)
				{
					childNode = nodeList.item(childNo);

					attributesMap = childNode.getAttributes();

					if (null != attributesMap && 0 != attributesMap.getLength())
					{
						map = new HashMap<String, String>();

						for (int attNo = 0; attNo < attributesMap
								.getLength(); attNo++)
						{
							attribtesNode = attributesMap.item(attNo);

							map.put(attribtesNode.getNodeName(),
									attribtesNode.getNodeValue());
						}
					}

					listFunction.list(this, childNode, childNode.getNodeValue(),
							map);
				}
			}
		}

	}

	public void children(Node node, ListFunction listFunction)
			throws XPathExpressionException, DOMException, TereException
	{
		Map<String, String> map = null;
		NodeList nodeList = null;
		NamedNodeMap attributesMap = null;
		Node attribtesNode = null;
		Node childNode = null;

		if ((null != node) && node.hasChildNodes())
		{
			nodeList = node.getChildNodes();

			if (null != nodeList)
			{
				for (int childNo = 0; childNo < nodeList.getLength(); childNo++)
				{
					childNode = nodeList.item(childNo);

					attributesMap = childNode.getAttributes();

					if (null != attributesMap && 0 != attributesMap.getLength())
					{
						map = new HashMap<String, String>();

						for (int attNo = 0; attNo < attributesMap
								.getLength(); attNo++)
						{
							attribtesNode = attributesMap.item(attNo);

							map.put(attribtesNode.getNodeName(),
									attribtesNode.getNodeValue());
						}
					}

					listFunction.list(this, childNode, childNode.getNodeValue(),
							map);
				}
			}
		}
	}

	public void siblings(Node node, ListFunction listFunction)
			throws XPathExpressionException, DOMException, TereException
	{
		Map<String, String> map = null;
		NodeList nodeList = null;
		NamedNodeMap attributesMap = null;
		Node attribtesNode = null;
		Node childNode = null;

		if ((null != null) && node.hasChildNodes())
		{
			nodeList = node.getParentNode().getChildNodes();

			if (nodeList != null)
			{
				for (int childNo = 0; childNo < nodeList.getLength(); childNo++)
				{
					childNode = nodeList.item(childNo);

					attributesMap = childNode.getAttributes();

					if (null != attributesMap && 0 != attributesMap.getLength())
					{
						map = new HashMap<String, String>();

						for (int attNo = 0; attNo < attributesMap
								.getLength(); attNo++)
						{
							attribtesNode = attributesMap.item(attNo);

							map.put(attribtesNode.getNodeName(),
									attribtesNode.getNodeValue());
						}
					}

					listFunction.list(this, childNode, childNode.getNodeValue(),
							map);
				}
			}
		}
	}

	@Override
	public void close() throws TereException
	{

	}

}
