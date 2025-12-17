package fr.univeiffel.mas;


import fr.univeiffel.mas.agents.*;
import fr.univeiffel.mas.sim.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	static void main() {
		logger.info("Setting up simulation");

		Simulator sim = new Simulator();

		// Add agents
		logger.info("Adding agents");
		{
			MMAgent mmAgent = new MMAgent();
			mmAgent.setName("MarketMaker");
			sim.addAgent(mmAgent);
		}

		{
			HodlAgent hodlAgent = new HodlAgent();
			hodlAgent.setName("Hodler");
			sim.addAgent(hodlAgent);
		}

		{
			RandomAgent randomAgent = new RandomAgent();
			randomAgent.setName("Random");
			sim.addAgent(randomAgent);
		}

		{
			MinMaxAgent minMaxAgent = new MinMaxAgent();
			minMaxAgent.setName("MinMaxer");
			sim.addAgent(minMaxAgent);
		}

		{
			LLMAgent llmAgent = new LLMAgent();
			llmAgent.setName("LLM-powered");
			sim.addAgent(llmAgent);
		}

		sim.setup();

		logger.info("Starting simulation");
		sim.run();
		logger.info("Simulation finished successfully!");
	}
}
