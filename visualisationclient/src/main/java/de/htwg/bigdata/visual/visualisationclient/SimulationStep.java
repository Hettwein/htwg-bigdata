package de.htwg.bigdata.visual.visualisationclient;

public class SimulationStep {
	private int step;
	private int time;
	private double[][] fields;
	
	public SimulationStep(int id, int time, int fieldSize) {
		this.step = id;
		this.time = time;
		fields = new double[fieldSize][fieldSize];
	}
	
	public void setField(int x, int y, int concentration) {
		
		if (x > fields.length - 1 ) { throw new RuntimeException("x to big"); }
		if (y > fields[x].length - 1) { throw new RuntimeException("y to big"); }
		if (concentration < 0) { throw new RuntimeException("concentration should not be negative"); }
		
		fields[x][y] = (double) concentration;
		
	}
	
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public double[][] getFields() {
		return fields;
	}
	public void setFields(double[][] fields) {
		this.fields = fields;
	}
}
