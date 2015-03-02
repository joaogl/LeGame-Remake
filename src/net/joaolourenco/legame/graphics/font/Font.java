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

package net.joaolourenco.legame.graphics.font;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import net.joaolourenco.legame.graphics.QuadRender;
import net.joaolourenco.legame.graphics.Shader;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.settings.GeneralSettings;

/**
 * Class to Manage Fonts.
 * 
 * @author Joao Lourenco
 * 
 */
public class Font {

	/**
	 * Array to store the font textures ID's.
	 */
	private int[] texIDs;
	/**
	 * The size of each letter
	 */
	private int size = 128;
	/**
	 * Array to store the chars used by the font in order.
	 */
	private String chars = "ABCDEFGHIJKLM" + //
			"NOPQRSTUVWXYZ" + //
			"abcdefghijklm" + //
			"nopqrstuvwxyz" + //
			"0123456789?!." + //
			"-,_% #$&'[]*+" + //
			":;<=>/^�`";

	public Shader shader = new Shader("res/shaders/font.frag", "res/shaders/font.vert");

	public QuadRender quad;

	/**
	 * Constructor for the Font's class.
	 */
	public Font() {
		// Add this class to the font's to be disposed at the closing of the game.
		GeneralSettings.fonts.add(this);

		quad = new QuadRender(size);

		// Loading of the fonts.
		texIDs = Texture.loadFont("/textures/util/font.png", 13, 7, size);
	}

	/**
	 * Method to draw a string to the screen with a standard texture.
	 * 
	 * @param text
	 *            : Text to be rendered.
	 * @param x
	 *            : x Position of the text.
	 * @param y
	 *            : y Position of the text.
	 * @param size
	 *            : Size of the font.
	 * @param spacing
	 *            : Spacing between letters.
	 */
	public void drawString(String text, int x, int y, int size, int spacing) {

		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Enabling Alpha chanel
		glBlendFunc(GL_SRC_ALPHA, GL_ZERO);
		// Binding the shader
		this.shader.bind();

		// Setting up the variables for the letter positions.
		int xx = x;
		// Running through all the letters
		for (int i = 0; i < text.length(); i++) {
			// Getting some variables ready.
			int s = size;
			int yOffset = y;
			// What is the letter to be rendered.
			int currentChar = text.charAt(i);
			int index = chars.indexOf(currentChar);

			// If its not a space, render the letter.
			if (index >= 0 && currentChar != ' ') {
				// if the letter needs offsetting , offset it.
				if (currentChar == 'p' || currentChar == 'g' || currentChar == 'j' || currentChar == 'q' || currentChar == 'y' || currentChar == ',') yOffset += s / 2;
				if (currentChar == ':') s = size / 2;

				// render it.
				quad.render(xx, yOffset, texIDs[index], this.shader, size);
			}

			// Apply the spacing for the letter.
			xx += s + spacing;
		}

		// Releasing the shader
		this.shader.release();
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to cleanup the memory by removing all the shaders and programs.
	 * 
	 * @author Joao Lourenco
	 */
	public void cleanup() {
		this.shader.cleanUp();
	}

}