package fr.univeiffel.mas.sim;

import fr.univeiffel.mas.Configuration;
import fr.univeiffel.mas.datatypes.Event;
import fr.univeiffel.mas.datatypes.EventType;
import fr.univeiffel.mas.datatypes.MarketInformation;
import fr.univeiffel.mas.interfaces.IAgent;
import fr.univeiffel.mas.interfaces.IOffer;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
	List<IAgent> agents = new ArrayList<>();
	private Event currentEvent;
	private int currentRound = 0;
	private List<Event> goodEvents = new ArrayList<>();
	private List<Event> badEvents = new ArrayList<>();
	private double currentPrice = Configuration.startingPrice;

	public void addAgent(IAgent agent) {
		agents.add(agent);
	}

	public void setup() {
		// Create events
		goodEvents.add(new Event(EventType.GOOD, "The company is recording record profits and is expected to continue growing."));
		goodEvents.add(new Event(EventType.GOOD, "The company is expanding to the Asian market as part of growth strategy that has already proven itself in the American market."));
		goodEvents.add(new Event(EventType.GOOD, "Amid unprecedented rises in consumer demand, the company is building a new manufacturing facility. Forecasts indicate that another facility is already necessary to cover the demand next year."));
		goodEvents.add(new Event(EventType.GOOD, "Several consumer protection agencies have praised the companies products due to their high quality and excellent support, none of which is matched by any competitor."));
		goodEvents.add(new Event(EventType.GOOD, "The company is expected to announce record profits in their earnings report next Friday, which are likely not priced in yet."));
		goodEvents.add(new Event(EventType.GOOD, "The company has just closed several, multi-billion contracts with European governments. Their execution will occur during the next five years, coinciding with the creation of new production facilities. More contracts are expected in the near future."));

		badEvents.add(new Event(EventType.BAD, "The sole factory of the company has just exploded."));
		badEvents.add(new Event(EventType.BAD, "The C-Suite of the company was just arrested during a business trip due to mass fraud."));
		badEvents.add(new Event(EventType.BAD, "The company has just filed for bankruptcy."));
		badEvents.add(new Event(EventType.BAD, "Several customers have gone bankrupt, and the company no longer has the cash to keep all its manufacturing facilities operating."));
		badEvents.add(new Event(EventType.BAD, "Several customers have died due to lax security and safety practices at the company."));
		badEvents.add(new Event(EventType.BAD, "The Russian government just took control of the companies manufacturing facilities without compensating the company."));

	}

	public void run() {
		updateEvent();
		List<IOffer> offers = getOffers();
		resolveTrades(offers);
		writeOutput();
		setPrice();
	}

	public void writeOutput() {
		writeBalances();
		writeValuations();
		writePrice();
		writeEvent();
	}

	public void writeBalances() {

	}

	public void writeValuations() {

	}

	public void writePrice() {

	}

	public void writeEvent() {

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
