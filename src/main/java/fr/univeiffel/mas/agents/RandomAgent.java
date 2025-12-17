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
	private String name;

	@Override
	public void setup() {
		accountBalance = Configuration.BasicAgentStartingBalance;
	}

	@Override
	public List<Position> getPositions() {
		return positions;
	}

	@Override
	public List<IOffer> getOffer(MarketInformation marketInformation) {
		// This agent acts as randomly as possible. It was inspired by the monkeys writing Shakespeare.
		// Whether it buys or sells is random, the amount of shares traded is random, and the price is random
		// It is intended to create more trading activity in the market, until it inevitably goes bankrupt

		double action = Math.random() - 0.5d;
		long numShares = Math.round(Math.floor(500 * Math.random()));
		double price = (Math.random() + 0.5d) * marketInformation.price();

		IOffer offer;
		List<IOffer> offerList = new ArrayList<>();

		if (action >= 0.5 - Configuration.randActionTakingRange) {
			offer = new BuyOffer();
		} else if (action <= -0.5 + Configuration.randActionTakingRange && !positions.isEmpty()) {
			offer = new SaleOffer();

			int totalShares = 0;

			for (Position p : positions) {
				totalShares += p.getShares();
			}

			if (numShares < totalShares) {
				numShares = totalShares;
			}

			if (numShares == 0) {
				offer = new NoOffer();
			}

		} else {
			offer = new NoOffer();
		}

		// Due to its random nature, a lot of checks had to be implemented to prevent it from submitting sales it cannot
		// fulfill due to lack of the underlying asset. Since the simulator does not support the trading of derivatives,
		// such trades are not possible

		int ownedShares = 0;

		for (Position p : positions) {
			ownedShares += p.getShares();
		}

		if (accountBalance < 0) {
			offer = new NoOffer();
		} else if (price * numShares > accountBalance) {
			numShares = Math.round(Math.floor(accountBalance / marketInformation.price()));
		} else if (offer instanceof SaleOffer && (offer.getShares() == 0 || positions.isEmpty())) {
			offer = new NoOffer();
		}

		offer.setShares((int) numShares);
		offer.setPrice(price);
		offer.setOfferer(this);

		if (offer instanceof SaleOffer && offer.getShares() > ownedShares) {
			offer.setShares(ownedShares);
		}

		offerList.add(offer);

		return offerList;
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
