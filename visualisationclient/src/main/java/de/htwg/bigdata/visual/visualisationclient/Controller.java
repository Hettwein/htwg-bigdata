package de.htwg.bigdata.visual.visualisationclient;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class Controller {
	private Connector connector;
	private InputStreamParser parser;
	private List<SimulationStep> steps;
	private ListIterator<SimulationStep> iterator;
	private int stepSize;
	
	
	public Controller() {
		connector = new Connector();
	}
	
	public void loadSimulationData(String simulationName, int fieldSize, int stepSize) {
		this.stepSize = stepSize;
		parser = new InputStreamParser(fieldSize);
		try {
			steps = parser.getData(connector.getSimulationData(simulationName, fieldSize, stepSize));
			iterator = steps.listIterator();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getStepSize() {
		return stepSize;
	}
	
	public SimulationStep getNextStep() {
		if (iterator.hasNext()) { return iterator.next(); }
		return null;
	}
	
	public SimulationStep getPrevStep() {
		if (iterator.hasPrevious()) { return iterator.previous(); }
		return null;
	}


}
