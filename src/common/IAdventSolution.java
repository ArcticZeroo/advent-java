package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public interface IAdventSolution
{
	String getName();

	void solvePartOne();
	void solvePartTwo();

	default BufferedReader getInputReader()
	{
		File file = new File("src/input/" + getName() + ".txt");

		if (!file.exists() || !file.canRead())
		{
			return null;
		}

		try
		{
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e)
		{
			return null;
		}
	}

	default List<String> getInputLines()
	{
		return getInputReader().lines().collect(Collectors.toList());
	}
}
