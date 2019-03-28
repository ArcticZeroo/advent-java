package common.grid;

public enum Axis
{
	X, Y;

	public static Axis crossAxisOf(Axis axis)
	{
		return axis == Axis.X ? Axis.Y : Axis.X;
	}
}