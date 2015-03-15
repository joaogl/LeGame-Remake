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

package net.joaolourenco.legame.graphics.menu.objects;

import java.util.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.*;

import org.lwjgl.util.vector.*;

/**
 * @author Joao Lourenco
 * 
 */
public abstract class MenuActionReader extends RenderableComponent {

	protected int x, y, xOffseted, width, height, spacing, size, screenHeight;
	protected String text;
	protected Menu menuOwner;
	protected Font font;
	protected Vector3f color = new Vector3f(1, 1, 1); // Default Color
	protected Vector3f scolor = new Vector3f(0.5f, 0, 0); // Over Color
	protected Vector3f pcolor = new Vector3f(0, 0.1f, 1); // Pressed Color
	protected Vector3f dColor = new Vector3f(0.6f, 0.6f, 0.6f); // Disabled Color
	protected Vector3f ccolor = color;

	protected List<ClickAction> DownCallbacks = new ArrayList<ClickAction>();
	protected List<ClickAction> ClickCallbacks = new ArrayList<ClickAction>();

	protected boolean selected = false;
	protected boolean mouse = false;
	protected boolean enabled = true;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuActionReader(String text, int x, int y, int size, int spacing, Menu o) {
		this.font = Registry.getFont();
		this.text = text;
		this.x = x;
		this.y = y;
		this.spacing = spacing;
		this.width = font.getStringSize(text, size, spacing);
		this.height = size + (size / 2);
		this.size = size;
		this.xOffseted = this.x - (this.width / 2);
		this.screenHeight = Registry.getScreenHeight();
		this.menuOwner = o;
	}

	public abstract void render();

	public abstract void update();

	public void addClickAction(ClickAction a) {
		this.ClickCallbacks.add(a);
	}

	public void addMouseDownAction(ClickAction a) {
		this.DownCallbacks.add(a);
	}

	public void setX(int x) {
		this.x = x;
		this.xOffseted = this.x - (this.width / 2);
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

}