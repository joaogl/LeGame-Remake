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
	protected List<MenuButton> buttons = new ArrayList<MenuButton>();
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
		Registry.focusGame();
	}

	public boolean toRemove() {
		return this.toRemove;
	}

	public void remove() {
		this.toRemove = true;
	}

}