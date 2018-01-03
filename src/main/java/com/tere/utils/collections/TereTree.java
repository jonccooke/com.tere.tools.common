package com.tere.utils.collections;

import java.util.ArrayList;
import java.util.List;

public class TereTree<T>
{
	private Node<T> root = null;

	public TereTree()
	{
	}

	public TereTree(Node<T> root)
	{
		this.root = root;
	}

	public void insertBefore(Node<T> source, Node<T> target)
	{
		Node<T> parent = source.getParent();
		
		target.setParent(parent);
		
		if (null != parent)
		{
			parent.addChild(target);
			parent.removeChild(source);
		}
		else
		{
			setRootNode(target);
		}
		target.addChild(source);
	}

//	public void insertAfter(Node<T> source, Node<T> target)
//	{
//		
//		target.setParent(source);
//
//		for ()
//	}

	public static class Node<T>
	{
		private T data;
		private Node<T> parent;
		private List<Node<T>> children = new ArrayList<Node<T>>();

		public Node(Node<T> parent, T t)
		{
			this.data = t;
			this.parent = parent;
		}

		public void addChild(Node<T> t)
		{
			children.add(t);
		}

		public void removeChild(Node<T> t)
		{
			children.remove(t);
		}

		public T getData()
		{
			return data;
		}

		public List<Node<T>> getChildren()
		{
			return children;
		}

		public void setParent(Node parentNode)
		{
			this.parent = parentNode;
		}

		public Node getParent()
		{
			return this.parent;
		}

		@Override
		public String toString()
		{
			if (null != data)
			{
				return data.toString();
			}
			return super.toString();
		}

	}

	public Node getRootNode()
	{
		return root;
	}

	public void setRootNode(Node rootNode)
	{
		this.root = rootNode;
	}

	protected void addNode(int pos, Node node, StringBuffer buffer)
	{
		if (null == node)
		{
			return;
		}
		for (int tab = 0; tab < pos; tab++)
		{
			buffer.append("-");
		}
		buffer.append(node.toString());
		buffer.append("\n");

		List<Node> children = node.getChildren();

		for (Node childNode : children)
		{
			addNode(pos + 1, childNode, buffer);
		}
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		addNode(0, this.getRootNode(), buffer);
		return buffer.toString();
	}
	
	
}