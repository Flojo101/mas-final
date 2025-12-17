package fr.univeiffel.mas.agents;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.BuyOffer;
import fr.univeiffel.mas.datatypes.MarketInformation;
import fr.univeiffel.mas.datatypes.NoOffer;
import fr.univeiffel.mas.datatypes.Position;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

import java.util.ArrayList;
import java.util.List;

public class HodlAgent implements IAgent {
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
		// Check how many shares can be bought from 10% of the available account balance
		// To spread buy-side demand, the agent doesnt just place a buy order for several hundred shares at the start
		long numSharesToBuy = Math.round(Math.floor(0.1 * accountBalance / marketInformation.price()));
		IOffer offer;
		List<IOffer> offerList = new ArrayList<>();

		// If no shares can be bought, don't order anything
		if (numSharesToBuy > 0) {
			offer = new BuyOffer();
			offer.setShares((int) numSharesToBuy);
		} else {
			offer = new NoOffer();
		}

		// For improved order resolution, the "standard" agents use the same margins
		offer.setPrice(marketInformation.price() + 0.1d);

		offer.setOfferer(this);

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
