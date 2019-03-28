package advent2018;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import common.IAdventSolution;
import common.Runner;
import sun.plugin.dom.exception.InvalidStateException;

public class Day3 implements IAdventSolution
{
	class Claim
	{
		private int _id;
		private int _leftIndex;
		private int _topIndex;
		private int _width;
		private int _height;

		Claim(int id, int leftIndex, int topIndex, int width, int height)
		{
			_id = id;
			_leftIndex = leftIndex;
			_topIndex = topIndex;
			_width = width;
			_height = height;
		}

		public int getId()
		{
			return _id;
		}

		public int getLeftIndex()
		{
			return _leftIndex;
		}

		public int getTopIndex()
		{
			return _topIndex;
		}

		public int getWidth()
		{
			return _width;
		}

		public int getHeight()
		{
			return _height;
		}
	}

	class Fabric
	{
		private Map<Integer, Map<Integer, List<Claim>>> _fabric;
		private int _highestRowIndex = 0;
		private int _highestColumnIndex = 0;

		public Fabric()
		{
			_fabric = new HashMap<>();
		}

		private Map<Integer, List<Claim>> getRow(int topIndex)
		{
			_fabric.putIfAbsent(topIndex, new HashMap<>());
			_highestRowIndex = Math.max(_highestRowIndex, topIndex);

			return _fabric.get(topIndex);
		}

		private List<Claim> getPositionClaims(int topIndex, int leftIndex, boolean putIfAbsent)
		{
			Map<Integer, List<Claim>> row = getRow(topIndex);

			if (putIfAbsent)
			{
				row.putIfAbsent(leftIndex, new ArrayList<>());
				_highestColumnIndex = Math.max(_highestColumnIndex, leftIndex);
			}

			return row.getOrDefault(leftIndex, new ArrayList<>());
		}

		public List<Claim> getPositionClaims(int topIndex, int leftIndex)
		{
			return getPositionClaims(topIndex, leftIndex, false);
		}

		public int occupiedCount(int topIndex, int leftIndex)
		{
			return getRow(topIndex).getOrDefault(leftIndex, new ArrayList<>()).size();
		}

		public boolean isOccupied(int topIndex, int leftIndex)
		{
			return occupiedCount(topIndex, leftIndex) > 0;
		}

		public void addClaimForSingleSquare(Claim claim, int topIndex, int leftIndex)
		{
			List<Claim> existingClaims = getPositionClaims(topIndex, leftIndex, true);

			existingClaims.add(claim);
		}

		@Override
		public String toString()
		{
			List<String> rows = new ArrayList<>();
			final String emptyRow = String.join("", Collections.nCopies(_highestColumnIndex + 1, "."));

			for (int rowIndex = 0; rowIndex <= _highestRowIndex; ++rowIndex)
			{
				if (!_fabric.containsKey(rowIndex))
				{
					rows.add(emptyRow);
					continue;
				}

				Map<Integer, List<Claim>> row = _fabric.get(rowIndex);
				StringBuilder rowString = new StringBuilder();

				for (int column = 0; column <= _highestColumnIndex; ++column)
				{
					List<Claim> claims = row.get(column);

					if (claims == null || claims.size() < 1)
					{
						rowString.append(".");
					}
					else
					{
						if (claims.size() > 1)
						{
							rowString.append("X");
						}
						else
						{
							rowString.append("+");
						}
					}
				}

				rows.add(rowString.toString());
			}

			return String.join("\n", rows);
		}
	}

	private static final String claimIdGroup = "id";
	private static final String claimLeftGroup = "left";
	private static final String claimTopGroup = "top";
	private static final String claimWidthGroup = "width";
	private static final String claimHeightGroup = "height";
	private static final String claimPatternFormat = String.format(
			"#(?<%s>\\d+) @ (?<%s>\\d+),(?<%s>\\d+): (?<%s>\\d+)x(?<%s>\\d+)",
			claimIdGroup, claimLeftGroup, claimTopGroup, claimWidthGroup, claimHeightGroup
	);

	private static final Pattern claimPattern = Pattern.compile(claimPatternFormat);

	private Claim claimFromString(String string)
	{
		Matcher match = claimPattern.matcher(string);

		if (!match.find())
		{
			return null;
		}

		int id = Integer.parseInt(match.group(claimIdGroup));
		int leftIndex = Integer.parseInt(match.group(claimLeftGroup));
		int topIndex = Integer.parseInt(match.group(claimTopGroup));
		int width = Integer.parseInt(match.group(claimWidthGroup));
		int height = Integer.parseInt(match.group(claimHeightGroup));

		return new Claim(id, leftIndex, topIndex, width, height);
	}

	private List<Claim> claimsFromInput(String[] entries)
	{
		return Arrays.stream(entries)
				.map(this::claimFromString)
				.collect(Collectors.toList());
	}

	@Override
	public String getName()
	{
		return "Day 3";
	}

	@Override
	public void solvePartOne()
	{
		BufferedReader inputReader = getInputReader();

		if (inputReader == null)
		{
			throw new InvalidStateException("Could not open input file");
		}

		Fabric fabric = new Fabric();
		AtomicInteger overlappingCount = new AtomicInteger(0);

		inputReader
			.lines()
			.forEach((line) -> {
				Claim claim = claimFromString(line);

				if (claim == null)
				{
					throw new InvalidStateException("Could not convert line " + line + " into claim");
				}

				int xEnd = claim.getLeftIndex() + claim.getWidth();
				int yEnd = claim.getTopIndex() + claim.getHeight();
				for (int leftIndex = claim.getLeftIndex(); leftIndex < xEnd; ++leftIndex)
				{
					for (int topIndex = claim.getTopIndex(); topIndex < yEnd; ++topIndex)
					{
						int existingClaimsAtPosition = fabric.occupiedCount(topIndex, leftIndex);

						// If existing claims is >1, we would be double counting overlaps
						if (existingClaimsAtPosition == 1)
						{
							overlappingCount.incrementAndGet();
						}

						fabric.addClaimForSingleSquare(claim, topIndex, leftIndex);
					}
				}
			});

		// System.out.println(fabric.toString());
		System.out.println(overlappingCount.get());
	}

	@Override
	public void solvePartTwo()
	{
		BufferedReader inputReader = getInputReader();

		if (inputReader == null)
		{
			throw new InvalidStateException("Could not open input file");
		}

		Fabric fabric = new Fabric();
		Map<Integer, Boolean> nonOverlappingIds = new HashMap<>();

		inputReader
				.lines()
				.forEach((line) -> {
					Claim claim = claimFromString(line);

					if (claim == null)
					{
						throw new InvalidStateException("Could not convert line " + line + " into claim");
					}

					int xEnd = claim.getLeftIndex() + claim.getWidth();
					int yEnd = claim.getTopIndex() + claim.getHeight();
					boolean hasOverlap = false;
					for (int leftIndex = claim.getLeftIndex(); leftIndex < xEnd; ++leftIndex)
					{
						for (int topIndex = claim.getTopIndex(); topIndex < yEnd; ++topIndex)
						{
							List<Claim> existingClaims = fabric.getPositionClaims(topIndex, leftIndex);

							// If existing claims is >1, we would be double counting overlaps
							if (existingClaims.size() >= 1)
							{
								hasOverlap = true;

								// In case any of these guys thought they were not overlapping,
								// remove them from the map so we know they are not!
								for (Claim existingClaim : existingClaims)
								{
									nonOverlappingIds.remove(existingClaim.getId());
								}
							}

							fabric.addClaimForSingleSquare(claim, topIndex, leftIndex);
						}
					}

					if (!hasOverlap)
					{
						nonOverlappingIds.putIfAbsent(claim.getId(), true);
					}
				});

		// System.out.println(fabric.toString());
		System.out.println("Count of non overlapping IDs: " + nonOverlappingIds.size());
		System.out.println("Their keys: " + nonOverlappingIds.keySet().stream().map(Object::toString).collect(Collectors.joining(", ")));
	}

	public static void main(String[] args)
	{
		Runner.run(new Day3());
	}
}
