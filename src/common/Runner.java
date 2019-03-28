package common;

import java.util.Collections;
import java.util.function.Function;

public class Runner
{
	interface Command
	{
		void invoke();
	}

	private static final String LINE_SEPARATOR = String.join("", Collections.nCopies(16, "-"));
	private static final String PART_MESSAGE_FORMAT = "Running solution for part %d\n";
	private static final String RUN_TIME_FORMAT = "\nSolution ran in %d ms";

	private static void runTimedSolution(Command method, int part)
	{
		System.out.println(LINE_SEPARATOR);
		System.out.println(String.format(PART_MESSAGE_FORMAT, part));

		long start = System.currentTimeMillis();
		method.invoke();
		long end = System.currentTimeMillis();

		System.out.println(String.format(RUN_TIME_FORMAT, end - start));
	}

	public static void run(IAdventSolution solution)
	{
		System.out.println("Running advent solution " + solution.getName());

		runTimedSolution(solution::solvePartOne, 1);
		runTimedSolution(solution::solvePartTwo, 2);
	}
}
