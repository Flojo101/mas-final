package fr.univeiffel.mas.utilities;

import fr.univeiffel.mas.datatypes.Position;

import java.util.Comparator;

public class PositionComparator implements Comparator<Position> {

	@Override
	public int compare(Position p1, Position p2) {
		return Double.compare(p1.getPrice(), p2.getPrice());
	}
}
