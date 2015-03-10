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

import java.awt.image.*;
import java.nio.*;

import javax.imageio.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.utils.Buffer;

import org.lwjgl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 * Texture holder and loader class.
 * 
 * @author Joao Lourenco
 * 
 */
public class Texture {

	// All the textures
	public static int[] Tiles;
	public static int loading = 0;
	public static int[] FinishPod;
	public static int[] Fire = new int[5];
	public static int[] Menus = new int[5];
	public static int[] Clouds;
	public static int[] Door;

	public static int[] PlayerWalking;
	public static int[] PlayerDying;
	public static int[] Citizen;
	public static int[] SkeletonAttacking;
	public static int[] SkeletonWalking;
	public static int[] SkeletonDying;
	public static int[] SpiderAttacking;
	public static int[] SpiderWalking;
	public static int[] SpiderDying;
	public static int[] Dragon;
	public static int[] Ogre;

	/**
	 * Function to load some early needed resorces.
	 * 
	 * @author Joao Lourenco
	 */
	public static void preload() {
		Menus[0] = loadTexture("/textures/menus/sky.png", false);
		Clouds = loadAtlas("/textures/menus/clouds2.png", 4, 4);
		loading = loadAtlas("/textures/menus/loading.png", 1, 1)[0];
	}

	/**
	 * Function to load all the resorces.
	 * 
	 * @author Joao Lourenco
	 */
	public static void load() {
		FinishPod = loadAtlas("/textures/FinishPod.png", 2, 2);
		Tiles = loadAtlas("/textures/GroundTiles.png", 3, 3);
		Door = loadAtlas("/textures/Door.png", 2, 1);
		Fire[0] = loadTexture("/textures/fire1.png", false);
		Fire[1] = loadTexture("/textures/fire2.png", false);
		Fire[2] = loadTexture("/textures/fire3.png", false);
		Fire[3] = loadTexture("/textures/fire4.png", false);
		Fire[4] = loadTexture("/textures/fire5.png", false);

		PlayerWalking = loadAtlas("/textures/mobs/Player-Walking.png", 3, 4);
		PlayerDying = loadAtlas("/textures/mobs/Player-Dying.png", 3, 3);
		Registry.getPlayer().setTextureAtlas(PlayerWalking, 3, 4, 1);
		Registry.getPlayer().setDyingTextureAtlas(PlayerDying, 3, 3, 1);

		Citizen = loadAtlas("/textures/mobs/Citizen.png", 3, 4);

		SkeletonAttacking = loadAtlas("/textures/mobs/Skeleton-Attacking.png", 3, 3);
		SkeletonWalking = loadAtlas("/textures/mobs/Skeleton-Walking.png", 3, 4);
		SkeletonDying = loadAtlas("/textures/mobs/Skeleton-Dying.png", 3, 3);

		SpiderAttacking = loadAtlas("/textures/mobs/Spider-Attacking.png", 2, 4);
		SpiderWalking = loadAtlas("/textures/mobs/Spider-Walking.png", 4, 4);
		SpiderDying = loadAtlas("/textures/mobs/Spider-Dying.png", 2, 2);

		Dragon = loadAtlas("/textures/mobs/Dragon.png", 3, 4);
		Ogre = loadAtlas("/textures/mobs/Ogre.png", 3, 4);
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
		} catch (Exception e) {
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
	 * Method to load a full Texture Atlas from file to the system.
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
	public static int[] loadAtlas(String path, int hLength, int vLength) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		int wsize = width / hLength;
		int hsize = height / vLength;

		// Going through each line.
		for (int y0 = 0; y0 < vLength; y0++) {
			// Going through each column.
			for (int x0 = 0; x0 < hLength; x0++) {
				// Creating the Texture pixel array to store the letter pixels.
				int[] tex = new int[wsize * hsize];
				// Going through each pixel of the letter
				for (int y = 0; y < hsize; y++) {
					for (int x = 0; x < wsize; x++) {
						// Getting the color of each pixel.
						tex[x + y * wsize] = sheet[(x + x0 * wsize) + (y + y0 * hsize) * width];
					}
				}

				// Processing the letter array for OpenGL
				ByteBuffer buffer = BufferUtils.createByteBuffer(wsize * hsize * 4);
				for (int y = 0; y < hsize; y++) {
					for (int x = 0; x < wsize; x++) {
						byte a = (byte) ((tex[x + y * wsize] & 0xff000000) >> 24);
						byte r = (byte) ((tex[x + y * wsize] & 0xff0000) >> 16);
						byte g = (byte) ((tex[x + y * wsize] & 0xff00) >> 8);
						byte b = (byte) (tex[x + y * wsize] & 0xff);
						buffer.put(r).put(g).put(b).put(a);
					}
				}
				buffer.flip();
				// Generating the texture.
				int texID = glGenTextures();
				// Binding the texture in order to define it.
				glBindTexture(GL_TEXTURE_2D, texID);
				// Defining the texture.
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, wsize, hsize, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
				// Defining the texture parameters.
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				// Adding the texture to the texture array to be stored.
				ids[index++] = texID;
				// Unbinding the texture.
				glBindTexture(GL_TEXTURE_2D, 0);
			}
		}

		// Returning the array with all the texture ID's.
		return ids;
	}

}