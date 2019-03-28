package common.grid;

public class Position
{
	private int _x;
	private int _y;

	public Position(int x, int y)
	{
		_x = x;
		_y = y;
	}

	public int getX()
	{
		return _x;
	}

	public int getY()
	{
		return _y;
	}

	public int getAxisCoordinate(Axis axis)
	{
		return axis == Axis.X ? _x : _y;
	}

	public int getManhattanDistanceTo(Position position)
	{
		return Math.abs(position.getX() - getX()) + Math.abs(position.getY() - getY());
	}

	public boolean equals(Position other)
	{
		return other.getX() == getX() && other.getY() == getY();
	}

	@Override
	public String toString()
	{
		return String.format("Position(%d, %d)", getX(), getY());
	}
}