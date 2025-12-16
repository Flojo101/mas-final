package fr.univeiffel.mas.datatypes;

import java.util.Comparator;

public class Position {

	private int shares;
	private double price;

	public Position(int shares, double price) {
		this.shares = shares;
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
