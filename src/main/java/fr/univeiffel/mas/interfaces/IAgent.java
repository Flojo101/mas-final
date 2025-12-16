package fr.univeiffel.mas.interfaces;

import fr.univeiffel.mas.datatypes.Position;

import java.util.List;

public interface IAgent {
	public void setup();
	public List<Position> getPositions();
	public IOffer getOffer(double currentPrice);
	public void removePosition(Position position);
	public void addPosition(Position position);
	public double getAccountBalance();
	public void addToAccountBalance(double toAdd);
	public void subtractFromAccountBalance(double toSubtract);
}
