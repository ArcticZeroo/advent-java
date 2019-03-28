package common.util;

public class Pair<L, R>
{
	private L _left;
	private R _right;

	public Pair(L left, R right)
	{
		_left = left;
		_right = right;
	}

	public L getLeft()
	{
		return _left;
	}

	public R getRight()
	{
		return _right;
	}

	public boolean equals(Pair<L, R> other)
	{
		return other.getLeft() == getLeft() && other.getRight() == getRight();
	}
}
