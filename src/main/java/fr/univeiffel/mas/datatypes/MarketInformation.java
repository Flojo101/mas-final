package fr.univeiffel.mas.datatypes;

// All agents get the same information: price and market news, but only the LLM agent processes the news

public record MarketInformation(double price, String message) {
}
