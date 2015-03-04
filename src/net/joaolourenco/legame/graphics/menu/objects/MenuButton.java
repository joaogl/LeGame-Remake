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
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuButton {

	protected int x, y, xOffseted, width, height, spacing, size, screenHeight;
	protected String text;
	protected Menu menuOwner;
	protected Font font;
	protected Vector3f color = new Vector3f(1, 1, 1);
	protected Vector3f scolor = new Vector3f(0.5f, 0, 0);
	protected Vector3f pcolor = new Vector3f(0, 0.1f, 1);
	protected Vector3f ccolor = color;

	protected List<ClickAction> callbacks = new ArrayList<ClickAction>();

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuButton(String text, int x, int y, int size, int spacing, Menu o) {
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

	public void render() {
		this.font.drawString(this.text, this.xOffseted, this.y, this.size, spacing, ccolor);
	}

	public void update() {
		if (Mouse.getX() > this.xOffseted && Mouse.getX() < (this.xOffseted + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			this.ccolor = this.scolor;
			if (Mouse.isButtonDown(0)) {
				this.ccolor = this.pcolor;
				for (ClickAction a : this.callbacks)
					a.onClick(this.menuOwner);
			}
		} else this.ccolor = this.color;
	}

	public void addClickAction(ClickAction a) {
		this.callbacks.add(a);
	}

}