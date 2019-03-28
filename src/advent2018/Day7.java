package advent2018;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import common.IAdventSolution;
import common.Runner;
import sun.plugin.dom.exception.InvalidStateException;

public class Day7 implements IAdventSolution
{
	private static final String stepPatternRequirementGroup = "requirement";
	private static final String stepPatternStepGroup = "step";
	private static final Pattern stepPattern = Pattern.compile(String.format("Step (?<%s>\\w) .+? step (?<%s>\\w) can", stepPatternRequirementGroup, stepPatternStepGroup));

	private static final int BASE_COST = 60;
	private static final int WORKER_COUNT = 5;

	class Step
	{

		private char _id;
		private List<Step> _requirements;
		private List<Step> _dependents;

		public Step(char id)
		{
			_id = id;
			_requirements = new ArrayList<>();
			_dependents = new ArrayList<>();
		}

		public char getId()
		{
			return _id;
		}

		public List<Step> getRequirements()
		{
			return _requirements;
		}

		public List<Step> getDependents()
		{
			return _dependents;
		}

		public void addRequirement(Step step)
		{
			_requirements.add(step);
		}

		public void addDependent(Step step)
		{
			_dependents.add(step);
		}

		public boolean hasNoRequirements()
		{
			return _requirements.isEmpty();
		}

		public int getCost()
		{
			return (getId() - 'A') + BASE_COST + 1;
		}

		public boolean hasNoDependents()
		{
			return _dependents.isEmpty();
		}

		@Override
		public String toString()
		{
			return String.format("Step(%c)[Dependents=%d, Requirements=%d]", _id, _dependents.size(), _requirements.size());
		}
	}

	class WorkerJob
	{
		private Step _step;
		private int _secondStarted;

		WorkerJob(Step step, int timeStarted)
		{
			_step = step;
			_secondStarted = timeStarted;
		}

		public Step getStep()
		{
			return _step;
		}

		public int getSecondStarted()
		{
			return _secondStarted;
		}

		public boolean isCompleteAtTime(int second)
		{
			return (second - _secondStarted) >= _step.getCost();
		}
	}

	private Step getNextStep(Set<Step> unorderedSteps)
	{
		return unorderedSteps.stream()
				.min(Comparator.comparingInt(Step::getId))
				.orElse(null);
	}

	private Step getStepById(Map<Character, Step> steps, char id)
	{
		return steps.compute(id, (cId, step) -> step == null ? new Step(id) : step);
	}

	private List<Step> getSteps()
	{
		BufferedReader inputReader = getInputReader();

		if (inputReader == null)
		{
			return new ArrayList<>();
		}

		Map<Character, Step> stepsById = new HashMap<>();

		inputReader.lines().forEach((line) -> {
			Matcher matcher = stepPattern.matcher(line);

			if (!matcher.find())
			{
				throw new InvalidStateException("Could not parse data from step instruction");
			}

			char requirementId = matcher.group(stepPatternRequirementGroup).charAt(0);
			char stepId = matcher.group(stepPatternStepGroup).charAt(0);

			Step requirementStep = getStepById(stepsById, requirementId);
			Step step = getStepById(stepsById, stepId);

			step.addRequirement(requirementStep);
			requirementStep.addDependent(step);
		});

		return new ArrayList<>(stepsById.values());
	}

	@Override
	public String getName()
	{
		return "2018 Day 7";
	}

	@Override
	public void solvePartOne()
	{
		Set<Step> readySteps = getSteps().stream().filter(Step::hasNoRequirements).collect(Collectors.toSet());
		Set<Step> completeSteps = new HashSet<>();
		StringBuilder instructions = new StringBuilder();

		Step nextStep;
		while (!readySteps.isEmpty() && (nextStep = getNextStep(readySteps)) != null)
		{
			readySteps.remove(nextStep);

			if (completeSteps.contains(nextStep))
			{
				continue;
			}

			completeSteps.add(nextStep);

			instructions.append(nextStep.getId());

			// Ready step can only be added if all its requirements are met,
			// not all dependents have a single requirement (see E in the example)
			nextStep.getDependents()
					.stream()
					.filter(dependent -> completeSteps.containsAll(dependent.getRequirements()))
					.forEach(readySteps::add);
		}

		System.out.println(instructions.toString());
	}

	@Override
	public void solvePartTwo()
	{
		List<Step> steps = getSteps();
		Set<Step> readySteps = steps.stream().filter(Step::hasNoRequirements).collect(Collectors.toSet());
		Set<Step> completeSteps = new HashSet<>();

		StringBuilder instructions = new StringBuilder();

		Map<Integer, WorkerJob> workerJobs = new HashMap<>();

		int second = 0;
		while (true)
		{
			Set<Integer> completedJobs = new HashSet<>();
			for (Map.Entry<Integer, WorkerJob> entry : workerJobs.entrySet())
			{
				WorkerJob job = entry.getValue();
				if (job != null && job.isCompleteAtTime(second))
				{
					completeSteps.add(job.getStep());
					job.getStep().getDependents()
							.stream()
							.filter(dependent -> completeSteps.containsAll(dependent.getRequirements()))
							.forEach(readySteps::add);

					completedJobs.add(entry.getKey());
				}
			}

			completedJobs.forEach(workerJobs::remove);

			LinkedList<Step> nextSteps = new LinkedList<>();
			readySteps.stream()
					.sorted(Comparator.comparingInt(Step::getId))
					.forEach(nextSteps::add);

			for (int workerId = 0; !nextSteps.isEmpty() && workerId < WORKER_COUNT; ++workerId)
			{
				if (workerJobs.containsKey(workerId))
				{
					continue;
				}

				Step nextStep = nextSteps.remove();

				readySteps.remove(nextStep);

				workerJobs.put(workerId, new WorkerJob(nextStep, second));
			}

			if (completeSteps.size() == steps.size())
			{
				break;
			}

			second++;
		}

		System.out.println(second);
	}

	public static void main(String[] args)
	{
		Runner.run(new Day7());
	}
}
