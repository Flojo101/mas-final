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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LLMAgent implements IAgent {
	private List<Position> positions = new ArrayList<>();
	private double accountBalance;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String name;

	private URL LLMURL;

	@Override
	public void setup() {
		accountBalance = Configuration.BasicAgentStartingBalance;

		try {
			LLMURL = new URL(Configuration.LLMAddress);
		} catch (MalformedURLException e) {
			logger.error("URL was malformed", e);
		}
	}

	@Override
	public List<Position> getPositions() {
				return positions;
	}

	@Override
	public List<IOffer> getOffer(MarketInformation marketInformation) {
		// Contact LLM for forecast interpretation
		// Buy/sell as necessary
		List<IOffer> offerList = new ArrayList<>();

		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) LLMURL.openConnection();
		} catch (IOException e) {
			logger.error("Exception on ocnnect", e);
		}

		if (conn == null) {
			return offerList;
		}

		try {
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		} catch (ProtocolException e) {
			throw new RuntimeException(e);
		}

		// Build message
		// Use granite3.1-moe:1b model for speed and accuracy
		String message = "{" +
				"   \"model\": \"granite3.1-moe:1b\",\n" +
				"   \"prompt\": \"I am an AI agent trading a fictional asset on a simulated stock market. The following event has just occured: ";
		message += marketInformation.message();
		message += " Is this good or bad for the stock price? Please answer with a single word (either Good or Bad)\",\n" +
				"   \"stream\": false\n" +
				" }";

		byte[] data = message.getBytes(StandardCharsets.UTF_8);

		try {
			conn.connect();
		} catch (IOException e) {
			logger.error("Error establishing connection", e);
		}

		try(OutputStream os = conn.getOutputStream()) {
			os.write(data);
		} catch (IOException e) {
			logger.error("Error sending request", e);
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader((conn.getInputStream())));
		} catch (IOException e) {
			logger.error("Error receiving response", e);
		}
		String output = "";
		String line;
		try {
			while ((line = br.readLine()) != null) {
				output += line;
			}
		} catch (IOException e) {
			logger.error("Error reading response", e);
		}

		if (output.contains("Good")) {
			// We should buy what we can get
			double shareprice = marketInformation.price() + 0.01d;
			int numShares = (int) Math.round(Math.floor(accountBalance / shareprice));

			IOffer offer = new BuyOffer();
			offer.setShares(numShares);
			offer.setPrice(shareprice);
			offer.setOfferer(this);

			offerList.add(offer);
		} else {
			// Dump everything thats profitable

			for (Position p : positions) {
				if (p.getPrice() * (1.0d + Configuration.LLMProfitMargin) < marketInformation.price()) {
					IOffer offer = new SaleOffer();
					offer.setShares(p.getShares());
					offer.setPrice(p.getPrice() * (1.0d + Configuration.LLMProfitMargin));
					offer.setOfferer(this);

					offerList.add(offer);
				}
			}
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
