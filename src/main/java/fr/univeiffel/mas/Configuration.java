package fr.univeiffel.mas;

public class Configuration {
	public static double startingPrice = 20.0d;
	public static double MMStartingBalance = 1000000.0d;	// Market maker starting balance
	public static int MMStartingShareCount = 10000;			// Market maker starting share count
	public static double BasicAgentStartingBalance = 5000.0d;	// Starting balance for all non-Market-Maker agents
	public static String LLMAddress = " http://localhost:11434/api/generate";	// default OLLAMA URL
	public static double randActionTakingRange = 0.2d;	// Range outside which the random agent takes action (centered around 0)
	public static int priceHistoryLength = 2;			// Number of historical price points the MinMaxAgent can remember
	public static double marketMakerProfitMargin = 0.01d;	// Minimum profit for arbitrage trading by the market maker
	public static int MMMinimumShares = 5000;				// Market maker minimum shares that need to be held at all times
	public static double LLMProfitMargin = 0.05d;			// Minimum profit marking on all sales the LLM-enhanced agent expects

	public static double goodEventProbability = 0.55d;
	public static double eventOccurrenceProbability = 0.10d;	// Chance that an event happens
	public static double eventNoEffectProbability = 0.05d;	// Chance that an event has no effect for the current round
	public static double eventOppositeEffectProbability = 0.01d;	// Chance that an event has the opposite effect
	public static double eventMaxEffect = 5.0d;						// The maximum effect an event can have on share price in percentage points
	public static long numRounds = 10000;							// Number of simulation rounds
}
