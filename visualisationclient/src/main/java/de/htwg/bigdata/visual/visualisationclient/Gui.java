package de.htwg.bigdata.visual.visualisationclient;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htwg.bigdata.visual.visualisationclient.heatmap.Gradient;
import de.htwg.bigdata.visual.visualisationclient.heatmap.HeatMap;

public class Gui extends JFrame {
	
	private HeatMap panel;	
	private Controller controller;
	
	public Gui(final Controller controller) throws Exception {
	    super("Ant-Simulation");
	    double[][] data = HeatMap.generateRampTestData();

	    
	    double[][] data2 = new double[500][500];
	    for (int x = 0; x < 500; x++) {
	    	for (int y = 0; y < 500; y++){
	    		data2[x][y] = Math.random() * 100;	    		
	    	}
	    }
	    
	    boolean useGraphicsYAxis = true;
	    
	    // you can use a pre-defined gradient:
//	    panel = new HeatMap(data, useGraphicsYAxis, Gradient.GRADIENT_BLUE_TO_RED);
	    panel = new HeatMap(data2, useGraphicsYAxis, Gradient.GRADIENT_BLACK_TO_WHITE);
	    
	    // or you can also make a custom gradient:
//	    Color[] gradientColors = new Color[]{Color.blue, Color.green, Color.yellow};
//	    Color[] customGradient = Gradient.createMultiGradient(gradientColors, 500);
//	    panel.updateGradient(customGradient);
	    
	    // set miscelaneous settings
//	    panel.setDrawLegend(true);
//	
//	    panel.setTitle("Height (m)");
//	    panel.setDrawTitle(true);
//	
//	    panel.setXAxisTitle("X-Distance (m)");
//	    panel.setDrawXAxisTitle(true);
//	
//	    panel.setYAxisTitle("Y-Distance (m)");
//	    panel.setDrawYAxisTitle(true);
//	
//	    panel.setCoordinateBounds(0, 6.28, 0, 6.28);
//	    panel.setDrawXTicks(true);
//	    panel.setDrawYTicks(true);
//	
//	    panel.setColorForeground(Color.black);
//	    panel.setColorBackground(Color.white);
	
	   //	JTextField
	    //	JButton
	    //	JLabel
	    
	    Container mainframe = this.getContentPane();
	    
	    JPanel mainPanel = new JPanel();
	    
	    JPanel param = new JPanel(new GridLayout(4, 2));
	    param.add(new JLabel("Simulation name"));
	    final JTextField simulationName = new JTextField();
	    param.add(simulationName);
	    param.add(new JLabel("Fieldsize"));
	    final JTextField fieldSize = new JTextField();
	    param.add(fieldSize);
	    param.add(new JLabel("Stepsize"));
	    final JTextField stepSize = new JTextField();
	    param.add(stepSize);
	    JButton go = new JButton("Go");
	    go.addActionListener(new ActionListener() {    	
			public void actionPerformed(ActionEvent e) {
				Gui.this.replaySimulation(
						simulationName.getText(),
						Integer.valueOf(fieldSize.getText()),
						Integer.valueOf(stepSize.getText())
				);
			}    	
	    });
	    param.add(go);
	    
	    mainPanel.add(param);
//	    this.getContentPane().add(param);
	    
	    
	    panel.setSize(500, 500);
	    mainPanel.add(panel);
	   
	    mainframe.add(mainPanel);
	   
	    //this.getContentPane().add(panel);
	}
	
	private void replaySimulation(String simulationName, int fieldSize, int stepSize) {
		List<SimulationStep> steps = controller.getSimulationData(simulationName, fieldSize, stepSize);
		panel.setSize(fieldSize, fieldSize);
		for (SimulationStep step : steps) {
			panel.updateData(step.getFields(), true);
			try {
				Thread.sleep(stepSize);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// this function will be run from the EDT
	    public static void createAndShowGUI(Controller controller) throws Exception {
	        Gui hmf = new Gui(controller);
	        hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        hmf.setSize(1920,1080);
	        hmf.setVisible(true);
	    }

	}

