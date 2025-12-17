package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.BuyOffer;
import fr.univeiffel.mas.datatypes.MarketInformation;
import fr.univeiffel.mas.datatypes.Position;
import fr.univeiffel.mas.datatypes.SaleOffer;
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

	@Override
	public void setup() {
		accountBalance = Configuration.BasicAgentStartingBalance;

		for (int i = 0; i < pricingHistory.size(); i ++) {
			pricingHistory.add(Configuration.startingPrice);
		}
	}

	@Override
	public List<Position> getPositions() {
		return positions;
	}

	@Override
	public IOffer getOffer(MarketInformation marketInformation) {
		IOffer offer;

		if (pricingHistory.peek() < marketInformation.price()) {
			offer = new BuyOffer();
			offer.setShares((int) Math.round(Math.floor(accountBalance / marketInformation.price())));
			offer.setPrice(marketInformation.price());
		} else {
			offer = new SaleOffer();

			int numShares = 0;
			double saleprice = (1 - Configuration.minMaxProfitMargin) * marketInformation.price();

			for (Position p : positions) {
				if (p.getPrice() <= saleprice) {
					numShares += p.getShares();
				}
			}

			offer.setShares(numShares);
			offer.setPrice(saleprice);
		}

		try {
			pricingHistory.take();
			pricingHistory.add(marketInformation.price());
		} catch (InterruptedException e) {
			logger.error("Queue is blocked", e);
		}

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
