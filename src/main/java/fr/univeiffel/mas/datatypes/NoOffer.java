package fr.univeiffel.mas.datatypes;

import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

public class NoOffer implements IOffer {
	@Override
	public int getShares() {
		return 0;
	}

	@Override
	public void setShares(int shares) {

	}

	@Override
	public double getPrice() {
		return 0;
	}

	@Override
	public void setPrice(double price) {

	}

	@Override
	public IAgent getOfferer() {
		return null;
	}

	@Override
	public void setOfferer(IAgent offerer) {

	}
}
