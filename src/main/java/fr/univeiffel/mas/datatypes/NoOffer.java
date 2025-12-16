package fr.univeiffel.mas.datatypes;

import fr.univeiffel.mas.interfaces.IOffer;

public class NoOffer implements IOffer {
	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public void setValue(int value) {

	}
}
