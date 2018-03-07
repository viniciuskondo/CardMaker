package com.pokedyna.fele;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Renderer
{
	private HashMap<String, BufferedImage> images;
	private Graphics canvasGraphic;
	// private String defaultPath;
	private static final String canvasName = "__canvas";

	public Renderer()
	{
		images = new HashMap<>();
		createCanvas(100, 100);
	}

	public void createCanvas(int width, int height)
	{
		images.put(canvasName, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		canvasGraphic =  images.get(canvasName).getGraphics();
	}

	public void createCanvasFromFile(String filename)
	{
		try
		{
			images.put(canvasName, ImageIO.read(new File(filename)));
			canvasGraphic =  images.get(canvasName).getGraphics();
		}
		catch (IOException e)
		{
			System.err.println(e.getClass() + " " + e.getMessage());
		}
	}

	public boolean loadImageWithAlias(String filename, String alias)
	{
		try
		{
			images.put(alias, ImageIO.read(new File(filename)));
		}
		catch (IOException e)
		{
			System.err.println(e.getClass() + " " + e.getMessage());
			return false;
		}

		return true;
	}

	public boolean loadImage(String filename)
	{
		return loadImageWithAlias(filename, filename);
	}

	public void draw(String imageName, int x, int y)
	{
		BufferedImage img = images.get(imageName);

		canvasGraphic.drawImage(img, x, y, null);
	}

	public void saveFile(String filename)
	{
		try
		{
			ImageIO.write(images.get(canvasName), "png", new File(filename + ".png"));
		}
		catch(IOException e)
		{
			System.err.println(e.getClass() + " " + e.getMessage());
		}
	}
}
