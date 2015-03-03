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

package net.joaolourenco.legame.graphics.menu;

import java.util.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.graphics.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuCloud extends RenderableComponent {

	private float x, y;
	private int texture, width, height;
	private float xOffset;
	private Shader shade;
	private boolean remove = false;
	private int xMax, yMax;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuCloud(Shader shade) {
		this.shade = shade;

		width = 128; // (Integer) generateRandom(150, 200, 0);
		xMax = Registry.getScreenWidth();
		xOffset = (Float) generateRandom(0.3f, 0.5f, 1);
		if ((Integer) generateRandom(0, 100, 0) > 50) {
			xOffset = -xOffset;
			x = xMax;
		} else x = -width;

		height = 128; // (Integer) generateRandom(100, 150, 0);
		yMax = Registry.getScreenHeight();
		y = (Integer) generateRandom(0, 200, 0);

		this.texture = Texture.Clouds[(Integer) generateRandom(0, Texture.Clouds.length, 0)];
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void render() {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Enabling Alpha chanel
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// Binding the shader
		this.shade.bind();

		// Render it.
		render(this.x, this.y, this.texture, this.shade, this.width, this.height);

		// Releasing the shader
		this.shade.release();
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	public void update() {
		this.x += this.xOffset;
		if (this.x > this.xMax || this.y > this.yMax || (this.x + this.width) < 0 || (this.y + this.height) < 0) this.remove = true;
	}

	public void tick() {
	}

	public boolean toRemove() {
		return this.remove;
	}

	/**
	 * Method to generate a random value.
	 * 
	 * @param min
	 *            : from
	 * @param max
	 *            : to
	 * @param type
	 *            : 0 for Integers, 1 for Floats
	 * @return Object, if type is 0 will return integer, if its 1 will return float.
	 * @author Joao Lourenco
	 */
	public Object generateRandom(float min, float max, int type) {
		// This method accepts two types of returns, 0 for Ints and 1 for Floats.
		if (type == 0) {
			// Generate an int random.
			Random rand = new Random();
			int out = rand.nextInt((int) max);
			// if its out of the bounds, keep trying.
			while (out > max || out < min)
				out = rand.nextInt((int) max);
			// return the random value.
			return out;
		} else if (type == 1) {
			// Generate an float random.
			Random rand = new Random();
			double out = min + (max - min) * rand.nextDouble();
			// if its out of the bounds, keep trying.
			while (out > max || out < min)
				out = min + (max - min) * rand.nextDouble();
			// return the random value.
			return (float) out;
		}
		return 0f;
	}

}