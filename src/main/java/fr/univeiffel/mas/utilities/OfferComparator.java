package fr.univeiffel.mas.utilities;

import fr.univeiffel.mas.interfaces.IOffer;

import java.util.Comparator;

public class OfferComparator implements Comparator<IOffer> {
	@Override
	public int compare(IOffer o1, IOffer o2) {
		return Double.compare(o1.getPrice(), o2.getPrice());
	}
}
