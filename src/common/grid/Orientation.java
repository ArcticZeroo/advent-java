package common.grid;

public enum Orientation
{
	POSITIVE, NEGATIVE, ZERO;

	public boolean isValueOrientedSame(int value)
	{
		if (this == Orientation.ZERO)
		{
			return value == 0;
		}

		if (this == Orientation.POSITIVE)
		{
			return value > 0;
		}

		return value < 0;
	}
}
