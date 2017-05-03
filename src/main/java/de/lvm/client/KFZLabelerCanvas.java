package de.lvm.client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class KFZLabelerCanvas extends Canvas implements ImageObserver, KFZLabelerConstants {
	Image img;

	Dimension ds;

	HashMap<Integer, String> imgFiles;

	String filename;

	String workingDir;

	int curImgIndex;

	KFZLabelerCanvas(String workingDir) {
		this.workingDir = workingDir;
		ds = getToolkit().getScreenSize();
		loadImages();
		filename = null;
	}

	@Override
	public void paint(Graphics g) {
		if (filename != null) {
			img = getToolkit().getImage(filename);
			drawIt(g, img);
		}
	}

	public void drawIt(Graphics g, Image img) {
		Graphics2D g2d = (Graphics2D) g;
		int x = getWidth() / 2 - img.getWidth(this) / 2;
		int y = getHeight() - img.getHeight(this) - 10;
		int w = img.getWidth(this);
		int h = img.getHeight(this);
		g2d.drawImage(img, x, y, w, h, this);

	}

	public void storeImages() {
		imgFiles = new HashMap<Integer, String>();
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "gif", "png", "gif", "bmp");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] Files = chooser.getSelectedFiles();
			for (int i = 0; i < Files.length; i++) {
				imgFiles.put(i, Files[i].toString());
			}
		}
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
			filename = imgFiles.get(curImgIndex);
			repaint();
		}
	}

	public void moveNext() {
		if (!imgFiles.isEmpty() && curImgIndex < imgFiles.size() - 1) {
			curImgIndex++;
			filename = imgFiles.get(curImgIndex);
			repaint();
		}
	}

	public HashMap<Integer, String> getImgFiles() {
		return imgFiles;
	}

}