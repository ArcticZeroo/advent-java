package advent2018;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import common.IAdventSolution;
import common.Runner;
import common.grid.PositiveGrid;
import common.grid.Position;

public class Day6 implements IAdventSolution
{
	private static int _currentPointId = 0;

	class Point
	{
		private Position _position;
		private int _id;

		Point(Position position)
		{
			_position = position;
			_id = _currentPointId++;
		}

		public Position getPosition()
		{
			return _position;
		}

		public int getId()
		{
			return _id;
		}
	}

	private List<Point> getPoints()
	{
		BufferedReader reader = getInputReader();

		if (reader == null)
		{
			return new ArrayList<>();
		}

		return reader.lines()
				.map((line) -> {
					String[] pieces = line.split(", ");

					return new Point(new Position(Integer.parseInt(pieces[0]), Integer.parseInt(pieces[1])));
				})
				.collect(Collectors.toList());
	}

	private Point getClosestPoint(Position position, List<Point> points)
	{
		List<Point> shortestDistancePoints = new ArrayList<>();
		int shortestDistance = Integer.MAX_VALUE;

		for (Point point : points)
		{
			int distance = point.getPosition().getManhattanDistanceTo(position);

			// Cannot be a position closer than itself
			if (distance == 0)
			{
				return point;
			}

			if (distance == shortestDistance)
			{
				shortestDistancePoints.add(point);
			}
			else if (distance < shortestDistance)
			{
				shortestDistance = distance;

				shortestDistancePoints.clear();
				shortestDistancePoints.add(point);
			}
		}

		if (shortestDistancePoints.isEmpty() || shortestDistancePoints.size() > 1)
		{
			return null;
		}

		return shortestDistancePoints.get(0);
	}

	@Override
	public String getName()
	{
		return "2018 Day 6";
	}

	@Override
	public void solvePartOne()
	{
		PositiveGrid<Integer> pointGrid = new PositiveGrid<>();

		List<Point> points = getPoints();

		for (Point point : points)
		{
			pointGrid.set(point.getPosition(), point.getId());
		}

		System.out.println(pointGrid);

		Set<Integer> infiniteIds = new HashSet<>();
		Map<Integer, Integer> totalAreasById = new HashMap<>();

		for (int x = 0; x < pointGrid.getSizeX(); ++x)
		{
			for (int y = 0; y < pointGrid.getSizeY(); ++y)
			{
				Point closest = getClosestPoint(new Position(x, y), points);

				if (closest == null)
				{
					continue;
				}

				if (x == 0 || y == 0 || x == pointGrid.getSizeX() - 1 || y == pointGrid.getSizeY() - 1)
				{
					infiniteIds.add(closest.getId());
				}
				else
				{
					totalAreasById.merge(closest.getId(), 1, Integer::sum);
				}
			}
		}

		System.out.println(totalAreasById.entrySet()
				.stream()
				.filter(entry -> !infiniteIds.contains(entry.getKey()))
				.map(Map.Entry::getValue)
				.max(Integer::compare)
				.orElse(0)
		);
	}

	@Override
	public void solvePartTwo()
	{
		final int maximumManhattan = 10000;
		PositiveGrid<Integer> pointGrid = new PositiveGrid<>();

		List<Point> points = getPoints();

		for (Point point : points)
		{
			pointGrid.set(point.getPosition(), point.getId());
		}

		int totalBelowMaximumArea = 0;

		for (int x = 0; x < pointGrid.getSizeX(); ++x)
		{
			for (int y = 0; y < pointGrid.getSizeY(); ++y)
			{
				Position position = new Position(x, y);
				int totalManhattan = 0;

				for (Point point : points)
				{
					totalManhattan += point.getPosition().getManhattanDistanceTo(position);
				}

				if (totalManhattan < maximumManhattan)
				{
					totalBelowMaximumArea++;
					pointGrid.set(position, 0);
				}
			}
		}

		System.out.println(pointGrid);
		System.out.println(totalBelowMaximumArea);
	}

	public static void main(String[] args)
	{
		Runner.run(new Day6());
	}
}
