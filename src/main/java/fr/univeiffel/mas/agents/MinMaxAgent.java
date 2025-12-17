package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.*;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class MinMaxAgent implements IAgent {
	private List<Position> positions = new ArrayList<>();
	private double accountBalance;
	private ArrayBlockingQueue<Double> pricingHistory = new ArrayBlockingQueue<>(Configuration.priceHistoryLength);
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String name;

	@Override
	public void setup() {
		accountBalance = Configuration.BasicAgentStartingBalance;

		for (int i = 0; i < Configuration.priceHistoryLength; i ++) {
			pricingHistory.add(Configuration.startingPrice);
		}
	}

	@Override
	public List<Position> getPositions() {
		return positions;
	}

	@Override
	public List<IOffer> getOffer(MarketInformation marketInformation) {
		IOffer offer;
		List<IOffer> offerList = new ArrayList<>();

		// Check if the market has gone up
		// Instead of looking for peaks, this agent is intended to look for long-term trends by comparing the current
		// market value with the value n moments ago (n can be configured by adjusting the length of the pricing history
		// this agent remembers)
		if (pricingHistory.peek() < marketInformation.price()) {
			// When the agent decides to buy shares, it goes all in

			offer = new BuyOffer();
			offer.setShares((int) Math.round(Math.floor(accountBalance / marketInformation.price())));

			double predictedPrice = marketInformation.price() + 0.1d;
			offer.setPrice(predictedPrice);
		} else if (!positions.isEmpty()) {

			// If the market is trending downwards, the agent tries to dump all the shares it can
			offer = new SaleOffer();

			int numShares = 0;
			double saleprice = marketInformation.price() - 0.5d;

			for (Position p : positions) {
				if (p.getPrice() <= saleprice) {
					numShares += p.getShares();
				}
			}

			offer.setShares(numShares);
			offer.setPrice(saleprice);
		} else {
			offer = new NoOffer();
		}

		if (offer.getShares() == 0) {
			offer = new NoOffer();
		}

		offer.setOfferer(this);

		// Only after deciding on the action is the pricing history updated
		try {
			pricingHistory.take();
			pricingHistory.add(marketInformation.price());
		} catch (InterruptedException e) {
			logger.error("Queue is blocked", e);
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
