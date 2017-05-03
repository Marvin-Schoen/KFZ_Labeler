package de.lvm.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class KFZLabelerFrame extends JFrame implements KeyListener, KFZLabelerConstants {

	JPanel panel;

	KFZLabelerCanvas canvasCarImage;

	JLabel keyBindings;

	String workingDir = WORKING_DIR;

	private Map<Character, String> richtungen;



	private JTextField textField;

	public KFZLabelerFrame() {
		setTitle("KFZ Labeler");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Wählen Sie den Ordner mit den KFZ Bildern!");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int fcResult = fc.showOpenDialog(this);

		if (JFileChooser.CANCEL_OPTION == fcResult || JFileChooser.ERROR_OPTION == fcResult) {
			dispose();
			return;
		}

		workingDir = fc.getSelectedFile().getAbsolutePath();

		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(new Color(75, 168, 41));
		getContentPane().add(panel, BorderLayout.SOUTH);

		ImageIcon bindings = new ImageIcon(getClass().getClassLoader().getResource("KeyBindings.png").getPath());
		keyBindings = new JLabel(bindings);
		keyBindings.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		getContentPane().add(keyBindings, BorderLayout.NORTH);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(3);
		textField.addKeyListener(this);

		canvasCarImage = new KFZLabelerCanvas(workingDir);
		getContentPane().add(canvasCarImage, BorderLayout.CENTER);
		setVisible(true);
		canvasCarImage.setSize(800, 600);
		canvasCarImage.moveFirst();
		canvasCarImage.paint(getGraphics());
		this.pack();

	}



	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			showNextCar();
		}
	}

	/**
	 * Prüft die eingegebene Zahl, verschiebt das Bild in den entsprechenden Ordner und zeigt das nächste Auto
	 */
	private void showNextCar() {
		if (!checkInput()) {
			return;
		}

		String labelCode = textField.getText();
		int length = labelCode.length();
		char[] ziffern = labelCode.toCharArray();


		String fileName = canvasCarImage.filename.substring(canvasCarImage.filename.lastIndexOf("/") + 1);
		String oldFolderPath = workingDir + "/";
		//Blickrichtung
		String newFolderPath = oldFolderPath + getRichtungen().get(ziffern[0]) + "/";
		//Schaden vorhanden
		if (ziffern[1] == '1') {
			newFolderPath += "J/";
		} else {
			newFolderPath += "N/";
		}
		//Schaden Position
		if (length > 2) {
			newFolderPath += getRichtungen().get(ziffern[2]) + "/";
		}


		//Ordner erstellen (Wenn er bereits existiert passiert nix
		(new File(newFolderPath)).mkdirs();

		//Bild verschieben
		try {
			Files.copy(Paths.get(oldFolderPath + fileName), Paths
					.get(newFolderPath + fileName), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		textField.setText("");
		canvasCarImage.moveNext();
	}

	private boolean checkInput() {
		String labelCode = textField.getText();
		int length = labelCode.length();
		char[] ziffern = labelCode.toCharArray();

		if (length < 2){
			showUsage("Die Zahl muss mindestens 2 Stellen haben.");
			return false;
		}
		if (length > 3) {
			showUsage("Die Zahl darf Maximal 3 Stellen haben.");
			return false;
		}
		for (char c : ziffern) {
			if (c < '0' || c > '9') {
				showUsage("Als Eingabe sind nur ziffern erlaubt");
				return false;
			}
		}
		if (ziffern[0] == '5') {
			showUsage("5 ist für die erste Ziffer nicht gültig.");
			return false;
		}
		if (ziffern[length - 1] == '5') {
			showUsage(ziffern[length - 1] + " ist für die letzte Ziffer nicht gültig.");
			return false;
		}
		if (ziffern[1] > '1') {
			showUsage("Die 2. Ziffer darf nur 0 (Kein Schaden) oder 1 (Schaden) sein");
			return false;
		}
		if (length == 3) {
			if (ziffern[2] == '0') {
				showUsage("Die 3. Ziffer darf nicht 0 sein");
				return false;
			}
			if (ziffern[1] == '0') {
				showUsage("Wenn kein Schaden zu sehen ist, braucht man die 3. Ziffer nicht.");
				return false;
			}
		}
		if (length == 2 && ziffern[1] == '1') {
			showUsage("Wenn das Auto einen Schaden hat, muss der Ort angegeben werden");
			return false;
		}
		return true;
	}

	/**
	 * Zeigt wie das Tool zu nutzen ist. Gibt als zusätzlichen Hinweis die Headline an.
	 */
	private void showUsage(String headline) {
		if (headline != null){
			JOptionPane.showMessageDialog(this, headline
					+"\nBitte geben Sie eine 2 - 3 stellige Zahl ein:\n"
					+ "Erste Ziffer: Richtung der Aufnahme\n"
					+ "Zweite Ziffer: ist ein Schaden zu sehen (1:ja, 0:nein)\n"
					+ "Dritte Ziffer: Nur wenn ein Schaden zu sehen ist: Wo ist der Schaden.\n"
					+ "Für die erste und 3. Ziffer gilt (Immer aus Fahrer Perspektive):\n"
					+ "1:Hinten Links\n"
					+ "2:Hinten\n"
					+ "3:Hinten Rechts\n"
					+ "4:Rechts\n"
					+ "6:Links\n"
					+ "7:Vorne Links\n"
					+ "8:Vorne\n"
					+ "9:Vorne Rechts");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//Not used
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//not used
	}

	private Map<Character, String> getRichtungen() {
		if (richtungen == null) {
			richtungen = new HashMap<>();
			richtungen.put('1', "HL");
			richtungen.put('2', "H");
			richtungen.put('3', "HR");
			richtungen.put('4', "L");
			richtungen.put('6', "R");
			richtungen.put('7', "VL");
			richtungen.put('8', "V");
			richtungen.put('9', "VR");
		}
		return richtungen;
	}




}
