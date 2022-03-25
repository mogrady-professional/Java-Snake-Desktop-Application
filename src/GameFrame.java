import javax.swing.JFrame;

/*
 * JFrame Abstraction 
 */
public class GameFrame extends JFrame{

	GameFrame(){
			
		this.add(new GamePanel());
		this.setTitle("Java Snake Desktop Application"); // Application Title
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit functionality
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null); // Center Frame on Display
		
	}
}