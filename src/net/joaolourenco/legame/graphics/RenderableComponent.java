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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Class to Render the quads, this can be a world tile or a simple letter.
 * 
 * @author Joao Lourenco
 * 
 */
public class RenderableComponent {

	/**
	 * Method to render a 64*64 quad on the desired location.
	 * 
	 * @param x
	 *            : x location of the quad.
	 * @param y
	 *            : y location of the quad.
	 * @param texture
	 *            : texture for the quad.
	 * @param shade
	 *            : shader the quad is going to use.
	 * @author Joao Lourenco
	 */
	public void render(int x, int y, int texture, Shader shade) {
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
			glVertex2f(0, 64);

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f(64, 64);

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f(64, 0);
		}
		glEnd();

		// Releasing the texture bank.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Getting the render position to the center.
		glTranslatef(-x, -y, 0);
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
	 * @param size
	 *            : the quad size.
	 * @author Joao Lourenco
	 */
	public void render(float x, float y, int texture, Shader shade, float size) {
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
	}

}