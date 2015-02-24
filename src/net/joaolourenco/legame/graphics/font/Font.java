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

import net.joaolourenco.legame.graphics.Shader;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.Buffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

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
	 * The shader for the letters
	 */
	private Shader shader;
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

	/**
	 * Buffer Data variables.
	 */
	protected int vao, vbo, vio, vto;

	/**
	 * Buffer Data variables.
	 */
	protected float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, //
			size, 0.0f, 0.0f, //
			size, size, 0.0f, //
			0.0f, size, 0.0f //
	};

	/**
	 * Buffer Data variables.
	 */
	protected byte[] indices = new byte[] { 0, 1, 2, //
			2, 3, 0 //
	};

	/**
	 * Buffer Data variables.
	 */
	protected byte[] texCoords = new byte[] { 0, 0, //
			1, 0, //
			1, 1, //
			1, 1, //
			0, 1, //
			0, 0 //
	};

	/**
	 * Constructor for the Font's class.
	 */
	public Font() {
		// Add this class to the font's to be disposed at the closing of the
		// game.
		GeneralSettings.font.add(this);
		// Loading of the fonts.
		texIDs = Texture.loadFont("res/font.png", 13, 7, size);
		// Compiling the buffer lists.
		compile();
		// Creating the shader.
		shader = new Shader("res/shaders/font.frag", "res/shaders/font.vert");

		// Activating the third texture
		glActiveTexture(GL_TEXTURE3);
		// Binding the shader
		shader.bind();
		// Getting OpenGL ready for the third texture
		int uniform = glGetUniformLocation(shader.getShader(), "texture");
		glUniform1i(uniform, 3);

		// Activating the forth texture
		glActiveTexture(GL_TEXTURE4);
		// Getting OpenGL ready for the forth texture
		uniform = glGetUniformLocation(shader.getShader(), "texture2");
		glUniform1i(uniform, 3);
		// Releasing the shader.
		shader.release();
	}

	/**
	 * Method to compile the Buffer lists.
	 */
	protected void compile() {
		// Generating Vertex Arrays
		vao = glGenVertexArrays();
		// Binding the vertex array
		glBindVertexArray(vao);
		{
			// Generating Buffers
			vbo = glGenBuffers();
			// Binding the buffer
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			{
				// Putting the vertices into the buffer
				glBufferData(GL_ARRAY_BUFFER, Buffer.createFloatBuffer(vertices), GL_STATIC_DRAW);
				glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			}
			// Releasing the buffer
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			// Generating Buffers
			vio = glGenBuffers();
			// Binding the buffer
			glBindBuffer(GL_ARRAY_BUFFER, vio);
			{
				// Putting the indices into the buffer
				glBufferData(GL_ARRAY_BUFFER, Buffer.createByteBuffer(indices), GL_STATIC_DRAW);
			}
			// Releasing the buffer
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			// Deleting the vto buffer to insure that is clean to be used.
			glDeleteBuffers(vto);

			// Generating Buffers
			vto = glGenBuffers();
			// Binding the buffer
			glBindBuffer(GL_ARRAY_BUFFER, vto);
			{
				// Putting the texture coordinates into the buffer
				glBufferData(GL_ARRAY_BUFFER, Buffer.createByteBuffer(texCoords), GL_STATIC_DRAW);
				glVertexAttribPointer(1, 3, GL_UNSIGNED_BYTE, false, 0, 1);
			}
			// Releasing the buffer
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		// Unbinding the vertex array
		glBindVertexArray(0);
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
		// Enabling the Blend for render
		glEnable(GL_BLEND);
		// Enabling Alpha chanel
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// Activating the third texture
		glActiveTexture(GL_TEXTURE3);
		// Binding the shader
		shader.bind();
		// Setting up some variables
		float scale = size / 20.0f;
		int xx = x;
		int yy = y;
		// Going through each letter of the text to render
		for (int i = 0; i < text.length(); i++) {
			// Getting the right offsets
			float xOffset = xx / scale;
			float yOffset = yy / scale;
			// Getting the char to render
			int currentChar = text.charAt(i);
			// Getting the right texture for the char
			int index = chars.indexOf(currentChar);
			// Checking if the char actually exists or if it isn't a space.
			if (index >= 0 && currentChar != ' ') {
				// If the char requires y offset add it.
				if (currentChar == 'p' || currentChar == 'g' || currentChar == 'j' || currentChar == 'q' || currentChar == 'y' || currentChar == ',') yOffset += 40;
				// Pushing the Matrix and loading the Identity.
				glPushMatrix();
				glLoadIdentity();
				// Scaling the view
				glScalef(scale, scale, 0);
				// Moving to the render position
				glTranslatef(xOffset, yOffset, 0);

				// Applying the letter texture.
				glActiveTexture(GL_TEXTURE3);
				glBindTexture(GL_TEXTURE_2D, texIDs[index]);

				// Binding the vertex array.
				glBindVertexArray(vao);
				// Enabling the vertex attributes
				glEnableVertexAttribArray(0);
				glEnableVertexAttribArray(1);
				{
					// Moving to the render position
					glTranslatef(x, y, 0);
					// Binding the Buffer
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio);
					// Drawing the elements
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
					// Unbinding the elements
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
				}
				// Disabling the vertex attributes
				glEnableVertexAttribArray(1);
				glEnableVertexAttribArray(0);
				// Unbinding the vertex array.
				glBindVertexArray(0);

				// Unbinding the Texture
				glBindTexture(GL_TEXTURE_2D, 0);
				// Poping the Matrix
				glPopMatrix();
			}
			// Moving the the next letter position
			xx += (this.size + spacing) * scale;
		}
		// Releasing the shader
		shader.release();
		// Deactivating the texture
		glActiveTexture(GL_TEXTURE0);
		// Disabling the Blend
		glDisable(GL_BLEND);
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
	 * @param texture
	 *            : Texture for the font to use.
	 */
	public void drawString(String text, int x, int y, int size, int spacing, int texture) {
		// Enabling the Blend for render
		glEnable(GL_BLEND);
		// Enabling Alpha chanel
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// Activating the third texture
		glActiveTexture(GL_TEXTURE3);
		// Binding the shader
		shader.bind();
		// Setting up some variables
		float scale = size / 20.0f;
		int xx = x;
		int yy = y;
		// Going through each letter of the text to render
		for (int i = 0; i < text.length(); i++) {
			// Getting the right offsets
			float xOffset = xx / scale;
			float yOffset = yy / scale;
			// Getting the char to render
			int currentChar = text.charAt(i);
			// Getting the right texture for the char
			int index = chars.indexOf(currentChar);
			// Checking if the char actually exists or if it isn't a space.
			if (index >= 0 && currentChar != ' ') {
				// If the char requires y offset add it.
				if (currentChar == 'p' || currentChar == 'g' || currentChar == 'j' || currentChar == 'q' || currentChar == 'y' || currentChar == ',') yOffset += 40;
				if (currentChar == ':') {
					vertices = new float[] { 0.0f, 0.0f, 0.0f, //
							32, 0.0f, 0.0f, //
							32, 32, 0.0f, //
							0.0f, 32, 0.0f //
					};
				}
				// Pushing the Matrix and loading the Identity.
				glPushMatrix();
				glLoadIdentity();
				// Scaling the view
				glScalef(scale, scale, 0);
				// Moving to the render position
				glTranslatef(xOffset, yOffset, 0);

				// Applying the texture
				glActiveTexture(GL_TEXTURE4);
				glBindTexture(GL_TEXTURE_2D, texture);

				// Applying the letter texture.
				glActiveTexture(GL_TEXTURE3);
				glBindTexture(GL_TEXTURE_2D, texIDs[index]);

				// Binding the vertex array.
				glBindVertexArray(vao);
				// Enabling the vertex attributes
				glEnableVertexAttribArray(0);
				glEnableVertexAttribArray(1);
				{
					// Moving to the render position
					glTranslatef(x, y, 0);
					// Binding the Buffer
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio);
					// Drawing the elements
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
					// Unbinding the elements
					glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
				}
				// Disabling the vertex attributes
				glEnableVertexAttribArray(1);
				glEnableVertexAttribArray(0);
				// Unbinding the vertex array.
				glBindVertexArray(0);

				// Unbinding the Texture
				glBindTexture(GL_TEXTURE_2D, 0);
				// Poping the Matrix
				glPopMatrix();
			}
			// Moving the the next letter position
			xx += (this.size + spacing) * scale;
		}
		// Releasing the shader
		shader.release();
		// Deactivating the texture
		glActiveTexture(GL_TEXTURE0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to clean up the vertex array and buffers.
	 */
	public void cleanup() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(vio);
		glDeleteBuffers(vto);
	}

}