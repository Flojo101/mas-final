package fr.univeiffel.mas.datatypes;

import fr.univeiffel.mas.interfaces.IOffer;

public class BuyOffer implements IOffer {
	private int value;

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int value) {
		this.value = value;
	}
}
