package common.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositiveGrid<T>
{
	private Map<Integer, Map<Integer, T>> _grid;
	private int _maxIndexY = 0;
	private int _maxIndexX = 0;

	public PositiveGrid()
	{
		_grid = new HashMap<>();
	}

	private void ensurePositionExists(Position position)
	{
		_grid.putIfAbsent(position.getY(), new HashMap<>());

		_maxIndexY = Math.max(_maxIndexY, position.getY());
		_maxIndexX = Math.max(_maxIndexX, position.getX());
	}

	private Map<Integer, T> getRow(Position position)
	{
		ensurePositionExists(position);
		return _grid.get(position.getY());
	}

	public int getSizeX()
	{
		return _maxIndexX + 1;
	}

	public int getSizeY()
	{
		return _maxIndexY + 1;
	}

	public int getArea()
	{
		return getSizeX() * getSizeY();
	}

	public boolean hasValueAt(Position position)
	{
		return getRow(position).containsKey(position.getX());
	}

	public T get(Position position)
	{
		return getRow(position).get(position.getX());
	}

	public void set(Position position, T value)
	{
		ensurePositionExists(position);

		getRow(position).put(position.getX(), value);
	}

	public Map<Integer, Map<Integer, T>> getRawGrid()
	{
		return _grid;
	}

	@Override
	public String toString()
	{
		List<String> rows = new ArrayList<>();
		String emptyRowString = String.join("", Collections.nCopies(getSizeX(), "."));

		for (int y = 0; y < getSizeY(); ++y)
		{
			if (!_grid.containsKey(y))
			{
				rows.add(emptyRowString);
				continue;
			}

			StringBuilder rowString = new StringBuilder();
			Map<Integer, T> row = _grid.get(y);

			for (int x = 0; x < getSizeX(); ++x)
			{
				if (!row.containsKey(x))
				{
					rowString.append('.');
				}
				else
				{
					rowString.append(row.get(x));
				}
			}

			rows.add(rowString.toString());
		}

		return String.join("\n", rows);
	}
}
