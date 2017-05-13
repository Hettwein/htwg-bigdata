package de.htwg.bigdata.visual.visualisationclient;

import java.io.FileNotFoundException;
import java.util.List;

public class TestClass {

	public static void main(String[] args) throws FileNotFoundException {
		
		Connector c = new Connector();
		
		InputStreamParser i = new InputStreamParser(5);

		List<SimulationStep> data = i.getData(c.getSimulationDataFromFile());
		
		for (SimulationStep step : data) {
			System.out.println(step.getStep());
			System.out.println(step.getTime());
			double[][] fields = step.getFields();
			for (int x = 0; x < fields.length; x++) {
				for (int y = 0; y < fields[x].length; y++) {
					System.out.println("x=" + x + ", y=" + y + ": " + fields[x][y]);
				}
			}
		}
		
	}

}
