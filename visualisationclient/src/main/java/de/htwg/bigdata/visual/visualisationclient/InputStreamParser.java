package de.htwg.bigdata.visual.visualisationclient;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class InputStreamParser {
	private int fieldSize;
	
	public InputStreamParser(int fieldSize) {
		this.fieldSize = fieldSize;
	}
	
	public List<SimulationStep> getData(InputStream is) {
		
		List<SimulationStep> data = new LinkedList<SimulationStep>();
		
		JsonReader reader = Json.createReader(is);
		JsonObject obj = reader.readObject();
		JsonArray steps = obj.getJsonArray("steps");
		for (JsonObject step : steps.getValuesAs(JsonObject.class)) {
			int id = step.getInt("step");
			int time = step.getInt("time");
			SimulationStep simulationStep = new SimulationStep(id, time, fieldSize);
			JsonArray fields = step.getJsonArray("fields");
			for (JsonObject field : fields.getValuesAs(JsonObject.class)) {
				simulationStep.setField(
						field.getInt("x"), 
						field.getInt("y"), 
						field.getInt("cocentration")
				);
			}
			data.add(simulationStep);
		}
		
		return data;
		
	}

}
