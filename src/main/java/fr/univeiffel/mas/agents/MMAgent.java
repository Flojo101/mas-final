package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.BuyOffer;
import fr.univeiffel.mas.datatypes.MarketInformation;
import fr.univeiffel.mas.datatypes.Position;
import fr.univeiffel.mas.datatypes.SaleOffer;
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
		// Always post a certain liquidity
		// Sell shares if profitable
		// keep minimum share level

		List<IOffer> offerList = new ArrayList<>();

		for (int i = 1; i < 3; i++) {
			int shareCount = Math.powExact(10, i);

			IOffer mmOffer = new SaleOffer();
			mmOffer.setPrice(marketInformation.price() + 0.01d * i);
			mmOffer.setShares(shareCount);
			mmOffer.setOfferer(this);

			offerList.add(mmOffer);
		}

		int heldShares = 0;

		for (Position p : positions) {
			heldShares += p.getShares();
		}

		int maxSaleableShares = heldShares - Configuration.MMMinimumShares;
		int sharesOnOffer = 0;

		for (Position p : positions) {
			if (sharesOnOffer >= maxSaleableShares) {
				break;
			}

			if (p.getPrice() * (1.0d + Configuration.marketMakerProfitMargin) <= marketInformation.price()) {
				IOffer mmOffer = new SaleOffer();
				mmOffer.setPrice(marketInformation.price() + 0.01d);
				if (p.getShares() > maxSaleableShares - sharesOnOffer) {
					mmOffer.setShares(maxSaleableShares - sharesOnOffer);
					sharesOnOffer += maxSaleableShares - sharesOnOffer;
				} else {
					mmOffer.setShares(p.getShares());
					sharesOnOffer += p.getShares();
				}
				mmOffer.setOfferer(this);

				offerList.add(mmOffer);
			}
		}

		int totalShares = 0;

		for (Position p : positions) {
			totalShares += p.getShares();
		}

		// Always keep at least 50% of the minimum obligated holdings
		if (totalShares < 0.5d * Configuration.MMMinimumShares) {
			offerList.clear();
		}

		if (totalShares < Configuration.MMMinimumShares) {
			IOffer mmOffer = new BuyOffer();
			mmOffer.setPrice(marketInformation.price() + 0.10d);
			mmOffer.setShares(Configuration.MMMinimumShares - totalShares);
			mmOffer.setOfferer(this);

			offerList.add(mmOffer);
		}

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
