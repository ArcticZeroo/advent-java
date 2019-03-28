package common.grid;

public enum Direction
{
	UP(Axis.Y, Orientation.POSITIVE), DOWN(Axis.Y, Orientation.NEGATIVE),
	LEFT(Axis.X, Orientation.POSITIVE), RIGHT(Axis.X, Orientation.NEGATIVE);

	private Axis _mainAxis;
	private Orientation _orientation;

	Direction(Axis mainAxis, Orientation orientation)
	{
		_mainAxis = mainAxis;
		_orientation = orientation;
	}

	public Axis getMainAxis()
	{
		return _mainAxis;
	}

	public Axis getCrossAxis()
	{
		return Axis.crossAxisOf(_mainAxis);
	}

	public Orientation getOrientation()
	{
		return _orientation;
	}
}