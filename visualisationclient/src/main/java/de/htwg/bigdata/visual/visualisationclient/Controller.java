package de.htwg.bigdata.visual.visualisationclient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Controller {
	private Connector connector;
	private InputStreamParser parser;
	
	
	public Controller() {
		connector = new Connector();
	}
	
	public List<SimulationStep> getSimulationData(String simulationName, int fieldSize, int stepSize) {
		System.out.println(simulationName);
		System.out.println(fieldSize);
		System.out.println(stepSize);
		parser = new InputStreamParser(fieldSize);
		try {
			return parser.getData(connector.getSimulationData(simulationName, fieldSize, stepSize));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
