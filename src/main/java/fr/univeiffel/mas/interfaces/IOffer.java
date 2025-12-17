package fr.univeiffel.mas.interfaces;

public interface IOffer {
	public int getShares();
	public void setShares(int shares);
	public double getPrice();
	public void setPrice(double price);
	public IAgent getOfferer();
	public void setOfferer(IAgent offerer);
}
