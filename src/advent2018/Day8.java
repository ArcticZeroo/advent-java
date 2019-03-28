package advent2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import common.IAdventSolution;
import common.Runner;
import common.util.Pair;
import sun.plugin.dom.exception.InvalidStateException;

public class Day8 implements IAdventSolution
{
	private int totalMetadata = 0;

	class Node
	{
		private List<Node> _children;
		private List<Integer> _metadata;

		public Node()
		{
			_children = new ArrayList<>();
			_metadata = new ArrayList<>();
		}

		public List<Node> getChildren()
		{
			return _children;
		}

		public List<Integer> getMetadata()
		{
			return _metadata;
		}

		public void addChildren(Node ...children)
		{
			_children.addAll(Arrays.asList(children));
		}

		public void addMetadata(Integer ...values)
		{
			_metadata.addAll(Arrays.asList(values));
		}

		public int getValue()
		{
			if (_children.isEmpty())
			{
				return _metadata.stream().reduce(0, Integer::sum);
			}

			int value = 0;

			for (int metadata : _metadata)
			{
				int childIndex = metadata - 1;

				if (childIndex < 0 || childIndex >= _children.size())
				{
					continue;
				}

				value += _children.get(childIndex).getValue();
			}

			return value;
		}
	}

	private Pair<Integer, Node> parseNextNode(List<Integer> data, int dataIndex)
	{
		int childNodeCount = data.get(dataIndex++);
		int metadataCount = data.get(dataIndex++);

		Node node = new Node();

		for (int i = 0; i < childNodeCount; ++i)
		{
			Pair<Integer, Node> nextNodeData = parseNextNode(data, dataIndex);

			dataIndex = nextNodeData.getLeft();

			node.addChildren(nextNodeData.getRight());
		}

		for (int i = 0; i < metadataCount; ++i)
		{
			int metadata = data.get(dataIndex++);

			totalMetadata += metadata;

			node.addMetadata(metadata);
		}

		return new Pair<>(dataIndex, node);
	}

	private Node buildTreeFromInput()
	{
		BufferedReader inputReader = getInputReader();

		if (inputReader == null)
		{
			throw new InvalidStateException("Could not open input file");
		}

		List<Integer> nodeData;
		try
		{
			nodeData = Arrays.stream(inputReader.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		Pair<Integer, Node> parseTreeRoot = parseNextNode(nodeData, 0);

		return parseTreeRoot.getRight();
	}

	private Node root = buildTreeFromInput();

	@Override
	public String getName()
	{
		return "2018 Day 8";
	}

	@Override
	public void solvePartOne()
	{
		System.out.println(totalMetadata);
	}

	@Override
	public void solvePartTwo()
	{
		if (root == null)
		{
			return;
		}

		System.out.println(root.getValue());
	}

	public static void main(String[] args)
	{
		Runner.run(new Day8());
	}
}
