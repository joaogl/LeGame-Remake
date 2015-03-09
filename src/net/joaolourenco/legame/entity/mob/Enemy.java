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

package net.joaolourenco.legame.entity.mob;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import net.joaolourenco.legame.graphics.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Enemy extends Mob {

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void update() {
		this.side = 2;
		this.updateTexture(0, 0);
		for (AnimatedSprite s : this.textures)
			s.setFrameRate(5);
	}

	/**
	 * Method called by the World Class to render the Entity.
	 * 
	 * @author Joao Lourenco
	 */
	public void render() {
		if (this.renderable) {
			// Setting up OpenGL for render
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ZERO);

			// Binding the shader
			this.shade.bind();

			// Calculating the required light.
			float day_light = 1f;
			if (lightAffected) day_light = world.DAY_LIGHT;
			// Sending the required light to the shader.
			glUniform1f(glGetUniformLocation(shade.getShader(), "dayLight"), day_light * 2);

			// Rendering the Quad.
			render(x, y, texture, shade, width, height);

			// Disabling BLEND and releasing shader for next render.
			glDisable(GL_BLEND);
			shade.release();
			glClear(GL_STENCIL_BUFFER_BIT);
		}
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {
	}

}
