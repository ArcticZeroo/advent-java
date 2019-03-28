package advent2018;

import java.util.function.Function;

import common.IAdventSolution;

public class Day9 implements IAdventSolution
{
	private static final int PLAYER_COUNT = 5;
	private static final int LAST_MARBLE_VALUE = 0;

	@Override
	public String getName()
	{
		return "2018 Day 9";
	}

	@Override
	public void solvePartOne()
	{
		Marble<Integer> current = new Marble<>(0);
		int turns = 10;

		for (int i = 0 ; i < turns; ++i)
		{
			Marble<Integer> nextMarble = new Marble<>(i);

			if (i % 23 == 0)
			{

			}
			else
			{

			}
		}
	}

	@Override
	public void solvePartTwo()
	{

	}

	class CircularMarbleList<T>
	{
		private Marble<T> _head;
		private Marble<T> _tail;

		void append(Marble<T> marble)
		{
			if (_head == null)
			{
				_head = marble;
			}

			if (_tail != null)
			{
				_tail.insertOtherAfter(marble);
			}

			_tail = marble;
		}

		private Marble<T> getMarbleInDirection(Marble<T> start, int count, Function<Marble<T>, Marble<T>> getNextInDirection, Marble<T> whenNull)
		{
			Marble<T> current = start;

			for (int i = 0; i < count; ++i)
			{
				Marble<T> next = getNextInDirection.apply(current);

				if (next == null)
				{
					next = whenNull;
				}

				current = next;
			}

			return current;
		}

		Marble<T> getMarbleClockwise(Marble<T> start, int count)
		{
			return getMarbleInDirection(start, count, Marble::getNext, _head);
		}

		Marble<T> getMarbleCounterClockwise(Marble<T> start, int count)
		{
			return getMarbleInDirection(start, count, Marble::getPrevious, _tail);
		}

		void insertBefore(Marble<T> source, Marble<T> insert)
		{
			if (source == _head)
			{
				_head = insert;
			}

			source.insertOtherBefore(insert);
		}
	}

	class Marble<T>
	{
		private T _value;
		private Marble<T> _previous;
		private Marble<T> _next;

		public Marble(T value)
		{
			_value = value;
		}

		public T getValue()
		{
			return _value;
		}

		public void setPrevious(Marble<T> previous)
		{
			_previous = previous;
			_previous._next = this;
		}

		public void setNext(Marble<T> next)
		{
			_next = next;
			_next._previous = this;
		}

		public void insertOtherAfter(Marble<T> marble)
		{
			if (_next != null)
			{
				_next._previous = marble;
				marble._next = _next;
			}

			setNext(marble);
		}

		public void insertOtherBefore(Marble<T> marble)
		{
			if (_previous != null)
			{
				_previous._next = marble;
				marble._previous = _previous;
			}

			setPrevious(marble);
		}

		public Marble<T> getPrevious()
		{
			return _previous;
		}

		public Marble<T> getNext()
		{
			return _next;
		}
	}


}
