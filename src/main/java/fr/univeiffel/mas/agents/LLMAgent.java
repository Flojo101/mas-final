package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.Position;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

import java.util.ArrayList;
import java.util.List;

public class LLMAgent implements IAgent {
	private List<Position> positions = new ArrayList<>();
	private double accountBalance;

	@Override
	public void setup() {
		accountBalance = Configuration.BasicAgentStartingBalance;
	}

	@Override
	public List<Position> getPositions() {
		return positions;
	}

	@Override
	public IOffer getOffer(double currentPrice) {
		return null;
	}

	@Override
	public void removePosition(Position position) {
		positions.remove(position);
	}

	@Override
	public void addPosition(Position position) {
		positions.add(position);
	}

	@Override
	public double getAccountBalance() {
		return accountBalance;
	}

	@Override
	public void addToAccountBalance(double toAdd) {
		accountBalance += toAdd;
	}

	@Override
	public void subtractFromAccountBalance(double toSubtract) {
		accountBalance -= toSubtract;
	}
}
