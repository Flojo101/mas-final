package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.MarketInformation;
import fr.univeiffel.mas.datatypes.Position;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

import java.util.ArrayList;
import java.util.List;

public class MMAgent implements IAgent {
	private List<Position> positions = new ArrayList<>();
	private double accountBalance;
	private String name;

	@Override
	public void setup() {
		positions.add(new Position(Configuration.MMStartingShareCount, Configuration.startingPrice));
		accountBalance = Configuration.MMStartingBalance;
	}

	@Override
	public List<Position> getPositions() {
		return positions;
	}

	@Override
	public List<IOffer> getOffer(MarketInformation marketInformation) {
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

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
