import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOfLife extends JFrame {
	
	private static final int GRID_SIZE = 25;
	private static final int CELL_SIZE = 15;
	private boolean[][] grid;
	private JPanel gridPanel;
	private JButton startStopButton;
	private JButton clearButton;
	private Timer simulationTimer;
	private boolean isRunning = false;
	
	public GameOfLife() {
		grid = new boolean[GRID_SIZE][GRID_SIZE];
		
		setTitle("Conway's Game of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		gridPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				for (int row = 0; row < GRID_SIZE; row++) {
					for (int col = 0; col < GRID_SIZE; col++) {
						if (grid[row][col]) {
							g.setColor(Color.GREEN);
						} else {
							g.setColor(Color.DARK_GRAY);
						}
						g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
						}
					}
				}
			};
			gridPanel.setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE));
			gridPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int col = e.getX() / CELL_SIZE;
					int row = e.getY() / CELL_SIZE;
					
					if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
						grid[row][col] = !grid[row][col];
						gridPanel.repaint();
					}
				}
			});
			
			JPanel controlPanel = new JPanel();
			startStopButton = new JButton("Start");
			clearButton = new JButton("Clear");
			
			startStopButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!isRunning) {
						startSimulation();
					} else {
						stopSimulation();
					}
				}
			});
			
			clearButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed (ActionEvent e) {
					stopSimulation();
					clearGrid();
				}
			});
			
			controlPanel.add(startStopButton);
			controlPanel.add(clearButton);
			
			simulationTimer = new Timer(200, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					computeNextGeneration();
					gridPanel.repaint();
				}
			});
			
			add(gridPanel, BorderLayout.CENTER);
			add(controlPanel, BorderLayout.SOUTH);
			
			pack();
			setLocationRelativeTo(null);
	}
	
	private void startSimulation() {
		if (!isRunning) {
			simulationTimer.start();
			startStopButton.setText("Stop");
			isRunning = true;
		}
	}
	
	private void stopSimulation() {
		if (isRunning) {
			simulationTimer.stop();
			startStopButton.setText("Start");
			isRunning = false;
		}
	}
	
	private void clearGrid() {
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int col = 0; col < GRID_SIZE; col++) {
				grid[row][col] = false;
			}
		}
		gridPanel.repaint();
	}
	
	private void computeNextGeneration() {
		boolean[][] nextGrid = new boolean[GRID_SIZE][GRID_SIZE];
		
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int col = 0; col < GRID_SIZE; col++) {
				int liveNeighbors = countLiveNeighbors(row, col);
				
				if (grid[row][col]) {
					nextGrid[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
				} else {
					nextGrid[row][col] = liveNeighbors == 3;
				}
			}
		}
		
		grid = nextGrid;	
	}
	
	private int countLiveNeighbors(int row, int col) {
		int liveNeighbors = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				
				int newRow = row + i;
				int newCol = col + j;
				
				if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE) {
					if (grid[newRow][newCol]) {
						liveNeighbors++;
					}
				}
			}
		}
		return liveNeighbors;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GameOfLife().setVisible(true);
			}
		});
	}	
}