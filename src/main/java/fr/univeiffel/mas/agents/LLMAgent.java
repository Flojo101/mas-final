package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.datatypes.Position;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

import java.util.List;

public class LLMAgent implements IAgent {
	@Override
	public List<Position> getPositions() {
		return List.of();
	}

	@Override
	public IOffer getOffer(double currentPrice) {
		return null;
	}

	@Override
	public void removePosition(Position position) {

	}

	@Override
	public void addPosition(Position position) {

	}

	@Override
	public double getAccountBalance() {
		return 0;
	}

	@Override
	public void addToAccountBalance(double toAdd) {

	}

	@Override
	public void subtractFromAccountBalance(double toSubtract) {

	}
}
