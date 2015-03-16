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

import org.lwjgl.util.vector.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

/**
 * Class to Render the quads, this can be a world tile or a simple letter.
 * 
 * @author Joao Lourenco
 * 
 */
public class RenderableComponent {

	/**
	 * Method to render a quad on the desired location with the desired size.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @param size
	 *            : the quad size.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float size) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Placing the quad in the right location
		glTranslatef(x, y, 0);
		// Activating the first texture bank.
		glActiveTexture(GL_TEXTURE0);
		// Storing the texture on the bank.
		glBindTexture(GL_TEXTURE_2D, texture);
		// Sending the texture location to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the Quad.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(0, size);

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f(size, size);

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f(size, 0);
		}
		glEnd();

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Getting the render position to the center.
		glTranslatef(-x, -y, 0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to render a quad on the desired location with the desired size.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @param width
	 *            : the quad width.
	 * @param height
	 *            : the quad height.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float width, float height) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Placing the quad in the right location
		glTranslatef(x, y, 0);
		// Activating the first texture bank.
		glActiveTexture(GL_TEXTURE0);
		// Storing the texture on the bank.
		glBindTexture(GL_TEXTURE_2D, texture);
		// Sending the texture location to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the Quad.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(0, height);

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f(width, height);

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f(width, 0);
		}
		glEnd();

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Getting the render position to the center.
		glTranslatef(-x, -y, 0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to render a quad on the desired location with the desired size.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @param width
	 *            : the quad width.
	 * @param height
	 *            : the quad height.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float width, float height, boolean noTranslation) {
		if (!noTranslation) glLoadIdentity();
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Placing the quad in the right location
		if (!noTranslation) glTranslatef(x, y, 0);
		// Activating the first texture bank.
		glActiveTexture(GL_TEXTURE0);
		// Storing the texture on the bank.
		glBindTexture(GL_TEXTURE_2D, texture);
		// Sending the texture location to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the Quad.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(0, height);

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f(width, height);

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f(width, 0);
		}
		glEnd();

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Getting the render position to the center.
		if (!noTranslation) glTranslatef(-x, -y, 0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to render a quad on the desired location with the desired size.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @param width
	 *            : the quad width.
	 * @param height
	 *            : the quad height.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float width, float height, Vector4f color) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Placing the quad in the right location
		glTranslatef(x, y, 0);
		// Activating the first texture bank.
		glActiveTexture(GL_TEXTURE0);

		glColor4f(color.x, color.y, color.z, color.w);

		// Storing the texture on the bank.
		glBindTexture(GL_TEXTURE_2D, texture);
		// Sending the texture location to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the Quad.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(0, height);

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f(width, height);

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f(width, 0);
		}
		glEnd();

		glColor4f(1f, 1f, 1f, 1f);

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Getting the render position to the center.
		glTranslatef(-x, -y, 0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to render a quad on the desired location with the desired size and with a specific rotation.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @param width
	 *            : the quad width.
	 * @param height
	 *            : the quad height.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float width, float height, float rotation) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Placing the quad in the right location
		glTranslatef(x, y, 0);
		glRotatef(rotation, 0, 0, 1);
		// Activating the first texture bank.
		glActiveTexture(GL_TEXTURE0);
		// Storing the texture on the bank.
		glBindTexture(GL_TEXTURE_2D, texture);
		// Sending the texture location to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the Quad.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(-(width / 2), -(height / 2));

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(-(width / 2), (height / 2));

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f((width / 2), (height / 2));

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f((width / 2), -(height / 2));
		}
		glEnd();

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);

		glRotatef(-rotation, 0, 0, 1);
		// Getting the render position to the center.
		glTranslatef(-x, -y, 0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * Method to render a quad on the desired location with the desired size and with a specific rotation.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @param width
	 *            : the quad width.
	 * @param height
	 *            : the quad height.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float rotation, boolean useRot) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		int width = 64;
		int height = 64;

		x = x + (width / 2);
		y = y + (height / 2);

		// Placing the quad in the right location
		glTranslatef(x, y, 0);
		glRotatef(rotation, 0, 0, 1);
		// Activating the first texture bank.
		glActiveTexture(GL_TEXTURE0);
		// Storing the texture on the bank.
		glBindTexture(GL_TEXTURE_2D, texture);
		// Sending the texture location to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the Quad.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(-(width / 2), -(height / 2));

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(-(width / 2), (height / 2));

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f((width / 2), (height / 2));

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f((width / 2), -(height / 2));
		}
		glEnd();

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);

		glRotatef(-rotation, 0, 0, 1);
		// Getting the render position to the center.
		glTranslatef(-x, -y, 0);
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

}