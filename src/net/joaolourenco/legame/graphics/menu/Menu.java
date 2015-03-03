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

/**
 * @author Joao Lourenco
 * 
 */
public abstract class Menu extends RenderableComponent {

	protected int texture;
	protected int x, y;
	protected int width, height;
	protected List<String> buttons = new ArrayList<String>();
	protected boolean hasFocus = false, renderMe = false;

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
		this.width = width;
		this.height = height;
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
		Registry.focusGame();
	}

}