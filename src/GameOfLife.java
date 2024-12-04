import java.util.Random;

public class GameOfLife {
	private int rows;
	private int cols;
	private boolean[][] grid;
	private boolean[][] nextGrid;
	
	public GameOfLife (int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		grid = new boolean[rows][cols];
		nextGrid = new boolean[rows][cols];
	}
	
	public void initRandomGrid() {
		Random random = new Random();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = random.nextBoolean();
			}
		}
	}
	
	private int countLiveNeighbors(int row, int col) {
		int liveNeighbors = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				
				int newRow = row + i;
				int newCol = col + j;
				
				if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
					if (grid[newRow][newCol]) {
						liveNeighbors++;
					}
				}
			}
		}
		return liveNeighbors;
	}
	
	public void nextGen() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int liveNeighbors = countLiveNeighbors(i, j);
				
				if(grid[i][j]) {
					nextGrid[i][j] = liveNeighbors == 2 || liveNeighbors == 3;
				} else {
					nextGrid[i][j] = liveNeighbors == 3;
				}
			}
		}
		
		boolean[][] temp = grid;
		grid = nextGrid;
		nextGrid = temp;
	}
	
	public void printGrid() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(grid[i][j] ? "■ " : "□ ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		GameOfLife game = new GameOfLife(20, 20);
		game.initRandomGrid();
		
		for (int gen = 0; gen < 10; gen++) {
			System.out.println("Generation " + gen + ":");
			game.printGrid();
			game.nextGen();
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
