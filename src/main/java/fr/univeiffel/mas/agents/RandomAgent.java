package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.*;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

import java.util.ArrayList;
import java.util.List;

public class RandomAgent implements IAgent {
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
	public IOffer getOffer(MarketInformation marketInformation) {
		double action = Math.random() - 0.5d;
		long numShares = Math.round(Math.floor(500 * Math.random()));
		double price = Math.random() + 0.5d;

		IOffer offer;

		if (action >= 0.5 - Configuration.randActionTakingRange) {
			offer = new BuyOffer();
		} else if (action <= -0.5 + Configuration.randActionTakingRange) {
			offer = new SaleOffer();
		} else {
			offer = new NoOffer();
		}

		offer.setShares((int) numShares);
		offer.setPrice(price);

		return offer;
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
