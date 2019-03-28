package advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import common.IAdventSolution;
import common.Runner;

public class Day5 implements IAdventSolution
{
	@Override
	public String getName()
	{
		return "2018-Day5";
	}

	private String getPolymer()
	{
		try
		{
			return getInputReader().readLine();
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private String reactPolymer(String polymer)
	{
		StringBuilder finalPolymer = new StringBuilder(polymer);

		for (int i = 0; i < finalPolymer.length() - 1; ++i)
		{
			char first = finalPolymer.charAt(i);
			char second = finalPolymer.charAt(i + 1);

			// Only care about matching types and differing polarities
			if (first == second || Character.toLowerCase(first) != Character.toLowerCase(second))
			{
				continue;
			}

			finalPolymer.delete(i, i + 2);

			// i is about to be incremented, so minimum value
			// is -1 so it gets pushed to 0
			i = Math.max(i - 1, 0) - 1;
		}

		return finalPolymer.toString();
	}

	private int findFirstNonIgnoreChar(String polymer, int startIndex, char ignoreCharLower)
	{
		ignoreCharLower = Character.toLowerCase(ignoreCharLower);

		for (int i = startIndex; i < polymer.length(); ++i)
		{
			char polymerChar = polymer.charAt(i);

			if (Character.toLowerCase(polymerChar) != ignoreCharLower)
			{
				return i;
			}
		}

		return -1;
	}

	private int reactPolymerIgnoreWithCount(String polymer, char ignoreChar)
	{
		StringBuilder finalPolymer = new StringBuilder(polymer);

		for (int i = 0; i < finalPolymer.length() - 1; ++i)
		{
			int firstIndex = findFirstNonIgnoreChar(polymer, i, ignoreChar);
			int secondIndex = findFirstNonIgnoreChar(polymer, firstIndex + 1, ignoreChar);

			if (firstIndex == -1 || secondIndex == -1)
			{
				break;
			}

			char first = finalPolymer.charAt(firstIndex);
			char second = finalPolymer.charAt(secondIndex);

			// Only care about matching types and differing polarities
			if (first == second || Character.toLowerCase(first) != Character.toLowerCase(second))
			{
				continue;
			}

			finalPolymer.delete(firstIndex, secondIndex + 1);

			// i is about to be incremented, so minimum value
			// is -1 so it gets pushed to 0
			i = Math.max(firstIndex - 1, 0) - 1;
		}

		return finalPolymer.length();
	}

	@Override
	public void solvePartOne()
	{
		String polymer = getPolymer();

		if (polymer == null)
		{
			System.out.println("Could not get polymer");
			return;
		}

		String reacted = reactPolymer(polymer);

		System.out.println("Final polymer: " + reacted);
		System.out.println("Length: " + reacted.length());
	}

	@Override
	public void solvePartTwo()
	{
		String polymer = getPolymer();

		if (polymer == null)
		{
			System.out.println("Could not get polymer");
			return;
		}

		Set<Character> chars = polymer.toLowerCase().chars().mapToObj(c -> (char) c).collect(Collectors.toSet());

		Map<Character, Boolean> distinctChars = new HashMap<>();
		int shortestPolymerLength = Integer.MAX_VALUE;

		for (char possibleBadChar : chars)
		{
			char lowerChar = Character.toLowerCase(possibleBadChar);

			if (distinctChars.containsKey(lowerChar))
			{
				continue;
			}

			shortestPolymerLength = Math.min(reactPolymerIgnoreWithCount(polymer, lowerChar), shortestPolymerLength);

			distinctChars.put(lowerChar, true);
		}

		System.out.println("Shortest polymer length: " + shortestPolymerLength);
	}

	public static void main(String[] args)
	{
		Runner.run(new Day5());
	}
}
