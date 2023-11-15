package cryptogramUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cryptogramPD.Cryptogram;

public class cryptogramFrame extends JFrame {

	// Method that is called to start the window
	public static void start(Cryptogram crypto) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cryptogramFrame frame = new cryptogramFrame(crypto);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Cryptogram constructor, sets the panel inside the frame
	public cryptogramFrame(Cryptogram crypto) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 350);
		setTitle("Cryptogram Solver");
		getContentPane().add(new cryptogramPanel(crypto));
	}

}
