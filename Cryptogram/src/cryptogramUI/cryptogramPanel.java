package cryptogramUI;

import javax.swing.JPanel;
import javax.swing.JTextField;

import cryptogramPD.Cryptogram;

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;

// The Panel with the elements of the panel inside
public class cryptogramPanel extends JPanel {
	private JTextField textField;

	// Creates the panels with the elements inside
	public cryptogramPanel(Cryptogram crypto) {
		setLayout(null);
		
		// Text Color
		Color red = new Color(255, 0, 0); // Used for errors
		Color black = new Color (0, 0, 0); // Used for regular use
		
		// Textfield where users input information
		textField = new JTextField();
		textField.setBounds(10, 11, 430, 20);
		add(textField);
		textField.setColumns(10);
		
		// Text area that prints the results
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 78, 430, 211);
		add(textArea);
		
		// The button the user presses to decrypt cipher
		JButton btnNewButton = new JButton("Decrypt");
		// Listens for a user to press the button
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Prints error if the user presses the button without putting anything into the text field
				if (textField.getText().isEmpty()) {
					textArea.setText("No Cryptogram Entered");
					textArea.setForeground(red);
				}
				// Prints the deciphered text into the textArea
				else {
					//Cryptogram crypto = new Cryptogram(textField.getText());
					crypto.setCryptogram(textField.getText());
					crypto.decryptCipher();
					textArea.setText(crypto.getAnswer());
					textArea.setForeground(black);
				}
			}
		});
		btnNewButton.setBounds(184, 42, 89, 23);
		add(btnNewButton);
	}
}
