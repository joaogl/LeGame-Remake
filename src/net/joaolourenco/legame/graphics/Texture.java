/*
 * Copyright 2014 Joao Lourenco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.joaolourenco.legame.graphics;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.joaolourenco.legame.utils.Buffer;

import org.lwjgl.BufferUtils;

/**
 * Texture holder and loader class.
 * 
 * @author Joao Lourenco
 * 
 */
public class Texture {

	// All the textures
	public static int Grass = 0;
	public static int Dirt = 0;
	public static int Mob = 0;
	public static int Player = 0;
	public static int[] Fire = new int[5];
	public static int[] Menus = new int[5];

	/**
	 * All the font textures are stored here.
	 * 
	 * @author Joao Lourenco
	 */
	private static List<Integer> fontTextures = new ArrayList<Integer>();

	/**
	 * Function to load some early needed resorces.
	 * 
	 * @author Joao Lourenco
	 */
	public static void preload() {

	}

	/**
	 * Function to load all the resorces.
	 * 
	 * @author Joao Lourenco
	 */
	public static void load() {
		Grass = loadTexture("/textures/grass.png", false);
		Dirt = loadTexture("/textures/dirt.png", false);
		Mob = loadTexture("/textures/mob.png", false);
		Player = loadTexture("/textures/player.png", false);
		Fire[0] = loadTexture("/textures/fire1.png", false);
		Fire[1] = loadTexture("/textures/fire2.png", false);
		Fire[2] = loadTexture("/textures/fire3.png", false);
		Fire[3] = loadTexture("/textures/fire4.png", false);
		Fire[4] = loadTexture("/textures/fire5.png", false);
	}

	/**
	 * Method used to load textures.
	 * 
	 * @param path
	 *            : String to where the texture is.
	 * @param antialiase
	 *            : boolean does it need antialiase
	 * @return int Texture ID.
	 * @author Joao Lourenco
	 */
	private static int loadTexture(String path, boolean antialiase) {
		// Setting up all the variables
		BufferedImage image;
		int width = 0;
		int height = 0;
		int[] pixels = null;
		try {
			// Loading the image
			image = ImageIO.read(Texture.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			// Moving the RGB data to the pixels array
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Processing the pixels array for OpenGL
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			pixels[i] = a << 24 | b << 16 | g << 8 | r;
		}

		// Creating the buffer
		IntBuffer buffer = Buffer.createIntBuffer(pixels);

		// Creating the texture
		int texture = glGenTextures();
		// Activating and Binding the new texture
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		// Loading the new texture to the graphics card.
		glTexImage2D(GL_TEXTURE_2D, 0, 3, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		// Setting the Texture settings.
		int ps = GL_NEAREST;
		if (antialiase) ps = GL_LINEAR;
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, ps);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, ps);
		// Unbinding the texture.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Returning the texture ID.
		return texture;
	}

	/**
	 * Method to load the font from file to the system.
	 * 
	 * @param path
	 *            : Path of the font texture.
	 * @param hLength
	 *            : height of the file.
	 * @param vLength
	 *            : width of the file.
	 * @param size
	 *            : size of each letter.
	 * @return int[], all the font letters ID's.
	 * @author Joao Lourenco
	 */
	public static int[] loadFont(String path, int hLength, int vLength, int size) {
		// Setting up some variables
		int width = 0;
		int height = 0;
		int index = 0;
		int[] ids = new int[hLength * vLength];
		int[] sheet = null;
		BufferedImage image;
		try {
			// Loading the image
			image = ImageIO.read(Texture.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			sheet = new int[width * height];
			// Moving the RGB data to the sheet array
			image.getRGB(0, 0, width, height, sheet, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Going through each line.
		for (int y0 = 0; y0 < vLength; y0++) {
			// Going through each column.
			for (int x0 = 0; x0 < hLength; x0++) {
				// Creating the letter pixel array to store the letter pixels.
				int[] letter = new int[size * size];
				// Going through each pixel of the letter
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						// Getting the color of each pixel.
						letter[x + y * size] = sheet[(x + x0 * size) + (y + y0 * size) * width];
					}
				}

				// Processing the letter array for OpenGL
				ByteBuffer buffer = BufferUtils.createByteBuffer(size * size * 4);
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						byte a = (byte) ((letter[x + y * size] & 0xff000000) >> 24);
						byte r = (byte) ((letter[x + y * size] & 0xff0000) >> 16);
						byte g = (byte) ((letter[x + y * size] & 0xff00) >> 8);
						byte b = (byte) (letter[x + y * size] & 0xff);
						buffer.put(r).put(g).put(b).put(a);
					}
				}
				buffer.flip();
				// Generating the texture.
				int texID = glGenTextures();
				// Binding the texture in order to define it.
				glBindTexture(GL_TEXTURE_2D, texID);
				// Defining the texture.
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
				// Defining the texture parameters.
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				// Adding the font to the font array to be used later.
				fontTextures.add(texID);
				ids[index++] = texID;
				// Unbinding the texture.
				glBindTexture(GL_TEXTURE_2D, 0);
			}
		}

		// Returning the array with all the texture ID's.
		return ids;
	}

	/**
	 * Method to get the texture.
	 * 
	 * @param texture
	 *            : id of the texture to get.
	 * @return int, the texture.
	 * @author Joao Lourenco
	 */
	public static int get(int texture) {
		if (texture < 0 || texture >= fontTextures.size()) return 0;
		return fontTextures.get(texture);
	}

}