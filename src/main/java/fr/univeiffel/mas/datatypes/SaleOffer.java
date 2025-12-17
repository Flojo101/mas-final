package fr.univeiffel.mas.datatypes;

import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

public class SaleOffer implements IOffer {
	private int shares;
	private double price;
	private IAgent offerer;

	@Override
	public int getShares() {
		return shares;
	}

	@Override
	public void setShares(int shares) {
		this.shares = shares;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public IAgent getOfferer() {
		return offerer;
	}

	@Override
	public void setOfferer(IAgent offerer) {
		this.offerer = offerer;
	}
}
