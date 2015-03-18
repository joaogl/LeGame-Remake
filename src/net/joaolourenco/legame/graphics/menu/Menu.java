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
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.objects.*;

/**
 * @author Joao Lourenco
 * 
 */
public abstract class Menu extends RenderableComponent {

	protected int texture;
	protected int x, y;
	protected int xMax, yMax;
	protected int width, height;
	protected List<MenuActionReader> buttons = new ArrayList<MenuActionReader>();
	protected boolean hasFocus = false, renderMe = false, toRemove = false;
	protected Font font;

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Menu(int texture, int x, int y, int width, int height) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.xMax = Registry.getScreenWidth();
		this.yMax = Registry.getScreenHeight();
		this.width = width;
		this.height = height;
		this.font = Registry.getFont();
		this.open();
	}

	public abstract void render();

	public abstract void update();

	public abstract void tick();

	public void open() {
		this.hasFocus = true;
		this.renderMe = true;
		Registry.unFocusGame();
	}

	public void close() {
		this.hasFocus = false;
		this.renderMe = false;
		this.toRemove = true;
		Registry.clearAnimatedTexts();
		Registry.clearStaticTexts();
		Registry.focusGame();
	}

	public boolean toRemove() {
		return this.toRemove;
	}

	public void remove() {
		this.toRemove = true;
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