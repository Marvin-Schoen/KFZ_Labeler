package de.lvm.client;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

public class KFZLabelerCanvas extends Canvas implements ImageObserver, KFZLabelerConstants {
	Image img;



	HashMap<Integer, String> imgFiles;

	private String currentFile;

	String workingDir;

	int curImgIndex;

	KFZLabelerCanvas(String workingDir) {
		this.workingDir = workingDir;
		loadImages();
		currentFile = null;
	}

	/**
	 * @return currentFile
	 */
	public String getCurrentFile() {
		return currentFile;
	}

	@Override
	public void paint(Graphics g) {
		if (currentFile != null) {
			img = getToolkit().getImage(currentFile);
			drawIt(g, img);
		} else {
			img = getToolkit().getImage(getClass().getClassLoader().getResource("Ende.png").getPath());
			drawIt(g, img);
		}
	}

	public void updateCurrentFile(String newPath) {
		currentFile = newPath;
		imgFiles.put(curImgIndex, newPath);
	}

	public void drawIt(Graphics g, Image img) {
		Graphics2D g2d = (Graphics2D) g;

		//Wir skalieren das Bild damit es in die Canvas passt, dazu 
		//Schauen wir welche Dimension die Skalierung bestimmt: Höhe oder Breite
		//Diese Größe gibt den Scale Faktor for, damit das Bild sein
		//Seitenverhältnis behält
		int diffWidth = img.getWidth(this) - getWidth();
		int diffHeight = img.getHeight(this) - getHeight();
		float scaleFactor = 1f;
		if (diffHeight > 0 || diffWidth > 0) {
			if (diffHeight > diffWidth){
				scaleFactor = (float) getHeight() / img.getHeight(this);
			} else {
				scaleFactor = (float) getWidth() / img.getWidth(this);
			}
		}
		int h = Math.round(img.getHeight(this) * scaleFactor);
		int w = Math.round(img.getWidth(this) * scaleFactor);
		int x = getWidth() / 2 - w / 2;
		int y = getHeight() - h;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(img, x, y, w, h, this);
		g2d.dispose();
	}

	public void drawText(Graphics g, String text) {
		Graphics2D g2d = (Graphics2D) g;
		FontMetrics fm = g2d.getFontMetrics();
		g2d.clearRect(0, 0, getWidth(), getHeight());
		Font font = new Font("Arial", Font.BOLD, 64);
		g2d.setFont(font);
		int w = fm.stringWidth(text);
		int h = fm.getAscent();

		g2d.drawString(text, getWidth() / 2 - 2 * w, getHeight() / 2 + h / 2);

		g2d.dispose();
	}

	private void loadImages() {
		File folder = new File(workingDir);
		imgFiles = new HashMap<Integer, String>();
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("jpg") 
						|| pathname.getName().endsWith("jpeg")
						|| pathname.getName().endsWith("gif")
						|| pathname.getName().endsWith("png")
						|| pathname.getName().endsWith("bmp"))
					return true;
				else
					return false;
			}
		};

		File[] listOfFiles = {};
		listOfFiles = folder.listFiles(filter);

		int i = 0;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				imgFiles.put(i++, file.getAbsolutePath());
			} else if (file.isDirectory()) {
				System.out.println("Directory " + file.getAbsolutePath());
			}
		}
	}

	public void moveFirst() {
		if (!imgFiles.isEmpty()) {
			curImgIndex = 0;
			currentFile = imgFiles.get(curImgIndex);
			repaint();
		}
	}

	public void moveNext() {
		if (!imgFiles.isEmpty() && curImgIndex < imgFiles.size() - 1) {
			curImgIndex++;
			currentFile = imgFiles.get(curImgIndex);
			repaint();
		} else {
			currentFile = null;
			repaint();
		}
	}

	public void movePrevious() {
		if (!imgFiles.isEmpty() && curImgIndex > 0) {
			curImgIndex--;
			currentFile = imgFiles.get(curImgIndex);
			repaint();
		} else {
			currentFile = null;
			repaint();
		}
	}

	public HashMap<Integer, String> getImgFiles() {
		return imgFiles;
	}

}