
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * JFrame Display & ActionListener Functionality
 * 
 * @author Michael
 *
 */
public class GamePanel extends JPanel implements ActionListener {

//	Display Sizing
	static final int SCREEN_WIDTH = 1300;
	static final int SCREEN_HEIGHT = 750;

	static final int UNIT_SIZE = 50; // Define the Size of the Objects in Game

	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE); // Calculate how many
																							// objects that can be fit
																							// on the screen

	static final int DELAY = 175; // Timer Delay -> higher = slower speed

//	Arrays to hold coordinates for snake length
	final int x[] = new int[GAME_UNITS]; // Array Size holds all x cordinates
	final int y[] = new int[GAME_UNITS]; // Array Size holds all y cordinates

//	Declare Instance Variables for Constructor
	int bodyParts = 6; // Snake begins at 6 squares
	int applesEaten; // Define object for snake to eat
	int appleX; // x coordinate of where apple is located -> appear randomly
	int appleY; // y coordinate of where apple is located -> appear randomly
	char direction = 'R'; // Have the snake begin going right at the start of the game [R, L, U, D]
	boolean running = false;
	Timer timer;
	Random random;

	/**
	 * Constructor for Game Panel
	 */
	GamePanel() {
		random = new Random();
//		Pass in sizing as Enums
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
//		Add key listener for control using myKeyAdaptor
		this.addKeyListener(new MyKeyAdapter());
//		Start Game
		startGame();
	}

	/**
	 * 
	 */
	public void startGame() {
//		Create new Apple object 
		newApple();
//		Start game
		running = true;
//		Initiate timer
		timer = new Timer(DELAY, this);
		timer.start();
	}

	/*
	 * Draw elements on frame
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	/**
	 * Generate Grid and Snake 
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {

		if (running) {
//			Grid for generation of Objects			
//			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//			}

//			Define Colours of Objects
			g.setColor(Color.red); // Apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // Apple Sizing

//			Draw head of snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.yellow);// Yellow head
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					// fill remaining parts if greater than 0
					g.setColor(new Color(45, 180, 0)); // Green body
					 g.setColor(new
					 Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); // Randomly generated integers -> multi colour snake
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());
		} else {
			gameOver(g);
		}

	}

	/*
	 * Generate random coordinates for apple object
	 */
	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // X Axis -> cast as integer
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // Y Axis -> cast as integer
	}

	/*
	 * Snake Movement across Grid
	 */
	public void move() {
//		Iterate all parts of the snake -> shifting the parts around
		for (int i = bodyParts; i > 0; i--) {
//			Move each part by one spot
			x[i] = x[i - 1];
//			Move each part by one spot
			y[i] = y[i - 1];
		}

//		Movement
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}

	/**
	 * Check coordinates of the snake and the apple
	 */
	public void checkApple() {
//		x[0] -> head of snake ; appleX -> position of the apple on the x axis ; y axis
		if ((x[0] == appleX) && (y[0] == appleY)) {
//			Increment length of snake and score
			bodyParts++;
			applesEaten++;
//			Generate new apple
			newApple();
		}
	}

	/**
	 * Check for head collides with body
	 */
	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
//				Trigger game over
				running = false;
			}
		}
		// check if head touches left border
		if (x[0] < 0) {
//			Trigger game over
			running = false;
		}
		// check if head touches right border
		if (x[0] > SCREEN_WIDTH) {
//			Trigger game over
			running = false;
		}
		// check if head touches top border
		if (y[0] < 0) {
//			Trigger game over
			running = false;
		}
		// check if head touches bottom border
		if (y[0] > SCREEN_HEIGHT) {
//			Trigger game over
			running = false;
		}

		if (!running) {
			timer.stop();
		}
	}

	/*
	 * End game and set game over messages
	 */
	public void gameOver(Graphics g) {
		// Score
		g.setColor(Color.green); // Font red for Game over
		g.setFont(new Font("Ink Free", Font.BOLD, 40)); // Font specifics
		FontMetrics metrics1 = getFontMetrics(g.getFont());// Line up text in center of screen
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());
		// Game Over text
		g.setColor(Color.green);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // Metrics to center Game over message
	}

	/*
	 * Main functionality while game running using ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkApple();
//			Check for head colision with body
			checkCollisions();
		}
		repaint();
	}

	/*
	 * Direction controller functionality
	 */
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
//				Limit user to 90 degree turns
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
//				Limit user to 90 degree turns
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
//				Limit user to 90 degree turns
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
//				Limit user to 90 degree turns
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}