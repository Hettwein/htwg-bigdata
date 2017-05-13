package de.htwg.bigdata.visual.visualisationclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import de.htwg.bigdata.visual.visualisationclient.heatmap.Gradient;
import de.htwg.bigdata.visual.visualisationclient.heatmap.HeatMap;

public class Gui extends JFrame {
	
	private HeatMap heatMapPanel;	
	private Controller controller;
	private final boolean useGraphicsYAxis = true;
	private final int defaultFieldSize = 500;
	
	public Gui(final Controller controller) throws Exception {

		super("Ant-Simulation");	    	   
	 
		Color[] gradientWhiteToBlack = Gradient.createGradient(Color.WHITE, Color.BLACK, 100);
		
//		heatMapPanel = new HeatMap(new double[defaultFieldSize][defaultFieldSize], useGraphicsYAxis, Gradient.GRADIENT_BLACK_TO_WHITE);
		heatMapPanel = new HeatMap(new double[defaultFieldSize][defaultFieldSize], useGraphicsYAxis, gradientWhiteToBlack);
	    heatMapPanel.setSize(defaultFieldSize, defaultFieldSize);
	    heatMapPanel.setColorForeground(Color.WHITE);
	    heatMapPanel.setColorBackground(Color.WHITE);
	    this.controller = controller;
	    	  	    
	    Container contentPane = this.getContentPane();	    	    
	    JPanel mainPanel = new JPanel();	
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//	    JPanel paramPanel = new JPanel(new GridLayout(4, 2));
	    JPanel paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    paramPanel.add(new JLabel("Simulation name"));
	    final JTextField simulationName = new JTextField(20);
	    paramPanel.add(simulationName);
	    paramPanel.add(new JLabel("Fieldsize"));
	    final JTextField fieldSize = new JTextField(10);
	    paramPanel.add(fieldSize);
	    paramPanel.add(new JLabel("Stepsize"));
	    final JTextField stepSize = new JTextField(10);
	    paramPanel.add(stepSize);
	    JButton go = new JButton("Go");
	    go.addActionListener(new ActionListener() {    	
			public void actionPerformed(ActionEvent e) {				
				new SimulationPerformer(
						simulationName.getText(), 
						Integer.valueOf(fieldSize.getText()), 
						Integer.valueOf(stepSize.getText())).execute();
													
			}    	
	    });
	    paramPanel.add(go);	    
	    mainPanel.add(paramPanel);	    	    
	    mainPanel.add(heatMapPanel);	   
	    contentPane.add(mainPanel);
	   
	}
	

	
	// this function will be run from the EDT
	    public static void createAndShowGUI(Controller controller) throws Exception {
	        Gui hmf = new Gui(controller);
	        hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        hmf.setSize(1920,1080);
	        hmf.setVisible(true);
	    }
	    
	    
	    class SimulationPerformer extends SwingWorker<Void,Void> {
	    	private String simulationName;
	    	private int fieldSize;
	    	private int stepSize;
	    	
	    	public SimulationPerformer(String simulationName, int fieldSize, int stepSize) {
	    		this.simulationName = simulationName;
	    		this.fieldSize = fieldSize;
	    		this.stepSize = stepSize;
	    	}
	    	
			@Override
			protected Void doInBackground() throws Exception {
				List<SimulationStep> steps = controller.getSimulationData(simulationName, fieldSize, stepSize);
				
				if (fieldSize < defaultFieldSize) {
					heatMapPanel.setSize(defaultFieldSize, defaultFieldSize);
				} else {
					heatMapPanel.setSize(fieldSize, fieldSize);
				}
								
				for (SimulationStep step : steps) {
					heatMapPanel.updateData(step.getFields(), useGraphicsYAxis);
					try {
						Thread.sleep(stepSize);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null; 
			}
	    	
	    }

	}

