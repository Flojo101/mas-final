package fr.univeiffel.mas.sim;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.*;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;
import fr.univeiffel.mas.utilities.OfferComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
	List<IAgent> agents = new ArrayList<>();
	private Event currentEvent;
	private int currentRound = 0;
	private List<Event> goodEvents = new ArrayList<>();
	private List<Event> badEvents = new ArrayList<>();
	private double currentPrice = Configuration.startingPrice;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void addAgent(IAgent agent) {
		agents.add(agent);
	}

	public void setup() {
		// Create events
		goodEvents.add(new Event(EventType.GOOD, "The company is recording record profits and is expected to continue " +
				"growing."));
		goodEvents.add(new Event(EventType.GOOD, "The company is expanding to the Asian market as part of growth " +
				"strategy that has already proven itself in the American market."));
		goodEvents.add(new Event(EventType.GOOD, "Amid unprecedented rises in consumer demand, the company is building" +
				" a new manufacturing facility. Forecasts indicate that another facility is already necessary to cover" +
				" the demand next year."));
		goodEvents.add(new Event(EventType.GOOD, "Several consumer protection agencies have praised the companies " +
				"products due to their high quality and excellent support, none of which is matched by any competitor" +
				"."));
		goodEvents.add(new Event(EventType.GOOD, "The company is expected to announce record profits in their earnings" +
				" report next Friday, which are likely not priced in yet."));
		goodEvents.add(new Event(EventType.GOOD, "The company has just closed several, multi-billion contracts with " +
				"European governments. Their execution will occur during the next five years, coinciding with the " +
				"creation of new production facilities. More contracts are expected in the near future."));

		badEvents.add(new Event(EventType.BAD, "The sole factory of the company has just exploded."));
		badEvents.add(new Event(EventType.BAD, "The C-Suite of the company was just arrested during a business trip " +
				"due to mass fraud."));
		badEvents.add(new Event(EventType.BAD, "The company has just filed for bankruptcy."));
		badEvents.add(new Event(EventType.BAD, "Several customers have gone bankrupt, and the company no longer has " +
				"the cash to keep all its manufacturing facilities operating."));
		badEvents.add(new Event(EventType.BAD, "Several customers have died due to lax security and safety practices " +
				"at the company."));
		badEvents.add(new Event(EventType.BAD, "The Russian government just took control of the companies " +
				"manufacturing facilities without compensating the company."));

		currentEvent = goodEvents.getFirst();

		// Create output files
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("balances.csv"));

			for (int i = 0; i < agents.size() - 1; i++) {
				writer.append(agents.get(i).getName());
				writer.append(',');
			}

			writer.append(agents.getLast().getName());
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("valuations.csv"));

			for (int i = 0; i < agents.size() - 1; i++) {
				writer.append(agents.get(i).getName());
				writer.append(',');
			}

			writer.append(agents.getLast().getName());
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("price.csv"));

			writer.append("MegaShare");
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("events.csv"));

			writer.append("Event");
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}

		// setup agents
		for (IAgent agent : agents) {
			agent.setup();
		}
	}

	public void run() {
		for (currentRound = 0; currentRound < Configuration.numRounds; currentRound++) {
			logger.info("Starting round {}", currentRound);
			updateEvent();
			List<IOffer> offers = getOffers();
			resolveTrades(offers);
			writeOutput();
			setPrice();
		}
	}

	public void writeOutput() {

		writeBalances();
		writeValuations();
		writePrice();
		writeEvent();
	}

	public void writeBalances() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("balances.csv", true));

			for (int i = 0; i < agents.size() - 1; i++) {
				writer.append(Double.toString(agents.get(i).getAccountBalance()));
				writer.append(',');
			}

			writer.append(Double.toString(agents.getLast().getAccountBalance()));
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}
	}

	public void writeValuations() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("valuations.csv", true));

			for (int i = 0; i < agents.size() - 1; i++) {
				IAgent agent = agents.get(i);
				double valuation = agent.getAccountBalance();

				for (Position p : agent.getPositions()) {
					valuation += (currentPrice - p.getPrice()) * p.getShares();
				}
				writer.append(Double.toString(valuation));
				writer.append(',');
			}

			IAgent agent = agents.getLast();
			double valuation = agent.getAccountBalance();

			for (Position p : agent.getPositions()) {
				valuation += p.getPrice() * p.getShares();
			}
			writer.append(Double.toString(valuation));
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}
	}

	public void writePrice() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("price.csv", true));

			writer.append(Double.toString(currentPrice));
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}
	}

	public void writeEvent() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("events.csv", true));

			writer.append(currentEvent.message());
			writer.append("\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing to file", e);
		}
	}

	public void updateEvent() {
		if (Math.random() < 1 - Configuration.eventOccurrenceProbability) {
			return;
		}

		if (Math.random() > 1 - Configuration.goodEventProbability) {
			int eventIndex = (int) Math.round(Math.floor(Math.random() * goodEvents.size()));

			currentEvent = goodEvents.get(eventIndex);
		} else {
			int eventIndex = (int) Math.round(Math.floor(Math.random() * badEvents.size()));

			currentEvent = badEvents.get(eventIndex);
		}
	}

	public void resolveTrades(List<IOffer> offers) {
		boolean tradeResolved = false;

		List<IOffer> saleOffers = new ArrayList<>(offers.stream().filter(o -> o instanceof SaleOffer).toList());
		List<IOffer> buyOffers = new ArrayList<>(offers.stream().filter(o -> o instanceof BuyOffer).toList());

		do {
			if (saleOffers.isEmpty() || buyOffers.isEmpty()) {
				return;
			}

			saleOffers.sort(new OfferComparator());
			buyOffers.sort(new OfferComparator());

			IOffer currentSaleOffer = saleOffers.getFirst();
			IOffer currentBuyOffer = buyOffers.getLast();

			if (currentSaleOffer.getPrice() > currentBuyOffer.getPrice()) {
				// at least one can trade can be (partially) resolved
				int tradeSize = Math.min(currentSaleOffer.getShares(), currentBuyOffer.getShares());
				double sharePrice = currentBuyOffer.getPrice();

				// Process seller-side
				if (currentSaleOffer.getShares() > tradeSize) {
					IOffer pOffer = new SaleOffer();
					pOffer.setPrice(currentSaleOffer.getPrice());
					pOffer.setShares(currentSaleOffer.getShares() - tradeSize);
					pOffer.setOfferer(currentSaleOffer.getOfferer());

					saleOffers.add(pOffer);
				}

				IAgent seller = currentSaleOffer.getOfferer();

				int soldShares = 0;

				do {
					Position p = seller.getPositions().getFirst();

					int posShares = p.getShares();

					if (posShares > tradeSize - soldShares) {
						seller.addToAccountBalance((tradeSize - soldShares) * currentPrice);

						// Add partial position
						seller.addPosition(new Position(posShares - (tradeSize - soldShares), p.getPrice()));

						soldShares = tradeSize;
					} else {
						seller.addToAccountBalance(p.getShares() * currentPrice);

						soldShares += p.getShares();
					}

					seller.getPositions().remove(p);
				} while (soldShares < tradeSize);

				saleOffers.remove(currentSaleOffer);

				// Process buyer-side
				if (currentBuyOffer.getShares() > tradeSize) {
					IOffer partOffer = new BuyOffer();
					partOffer.setShares(currentBuyOffer.getShares() - tradeSize);
					partOffer.setPrice(currentBuyOffer.getPrice());
					partOffer.setOfferer(currentBuyOffer.getOfferer());

					buyOffers.add(partOffer);
				}

				IAgent buyer = currentBuyOffer.getOfferer();

				buyer.subtractFromAccountBalance(tradeSize * currentPrice);
				buyer.addPosition(new Position(tradeSize, currentPrice));
				tradeResolved = true;
			} else {
				tradeResolved = false;
			}
		} while (tradeResolved);
	}

	public void setPrice() {
		double prob = Math.random();
		int mult = 1;

		if (prob > 1 - Configuration.eventNoEffectProbability) {
			return;
		} else if (prob > 1 - Configuration.eventOppositeEffectProbability) {
			mult = -1;
		}

		if (currentEvent.type() == EventType.BAD) {
			mult *= -1;
		}

		currentPrice += mult * (currentPrice / 100) * Math.random() * Configuration.eventMaxEffect;
	}

	public List<IOffer> getOffers() {
		List<IOffer> offers = new ArrayList<>();

		for (IAgent agent : agents) {
			offers.addAll(agent.getOffer(new MarketInformation(currentPrice, currentEvent.message())));
		}

		return offers;
	}
}
