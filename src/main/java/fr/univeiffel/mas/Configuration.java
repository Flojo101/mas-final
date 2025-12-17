package fr.univeiffel.mas;

public class Configuration {
	public static double startingPrice = 20.0d;
	public static double MMStartingBalance = 1000000.0d;
	public static int MMStartingShareCount = 10000;
	public static double BasicAgentStartingBalance = 5000.0d;
	public static String LLMAddress = " http://localhost:11434/api/generate";	// default OLLAMA URL
	public static double randActionTakingRange = 0.2d;
	public static int priceHistoryLength = 2;
	public static double minMaxProfitMargin = 0.10d;
	public static double marketMakerProfitMargin = 0.01d;
	public static int MMMinimumShares = 5000;
	public static double LLMProfitMargin = 0.05d;
}
