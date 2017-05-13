package de.htwg.bigdata.visual.visualisationclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Connector {
	private final String hostUrl = "http://127.0.0.1:9200";
	
	public Connector () {}

	public InputStream getSimulationData(String simulationName, int fieldSize, int stepSize) throws IOException {
		
		return this.getSimulationDataFromFile();
		
//		URL url = new URL(hostUrl);
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("POST");
//		connection.setDoInput(true);
//		connection.setDoOutput(true);
//		connection.setUseCaches(false);
//		connection.setRequestProperty("Content-Type", "application/json");
//		
//		String param = 
//			"collection=" + URLEncoder.encode(simulationName, "UTF-8") + "&"
//		    + "x=" + URLEncoder.encode(Integer.toString(fieldSize), "UTF-8") + "&"
//		    + "y=" + URLEncoder.encode(Integer.toString(fieldSize), "UTF-8") + "&"
//		    + "timestep=" + URLEncoder.encode(Integer.toString(stepSize), "UTF-8");
//		
//		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//		writer.write(param);
//		writer.flush();
//		
//		return connection.getInputStream();
		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		
//		String line = reader.readLine();
//		while (line != null) {
//			System.out.println(line);
//			line = reader.readLine();
//		}
		
	}
	
	
	public InputStream getSimulationDataFromFile() throws FileNotFoundException {
		
		File f = new File("D:/Projekte/bigdata/visualisationclient/sim.json");
		InputStream in = new FileInputStream(f);
		return in;
		
	}
	
}
