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

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuCheckBox extends MenuActionReader {

	protected int x, y, xOffseted, width, height, spacing, size, screenHeight;
	protected String text;
	protected Menu menuOwner;
	protected Font font;
	protected Vector3f color = new Vector3f(1, 1, 1);
	protected Vector3f scolor = new Vector3f(0.5f, 0, 0);
	protected Vector3f pcolor = new Vector3f(0, 0.1f, 1);
	protected Vector3f ccolor = color;

	protected List<ClickAction> DownCallbacks = new ArrayList<ClickAction>();
	protected List<ClickAction> ClickCallbacks = new ArrayList<ClickAction>();

	protected Shader shader;
	protected int unseletedTexture = Texture.MenuCheckBox[0];
	protected int seletedTexture = Texture.MenuCheckBox[1];

	protected boolean selected = false;
	protected boolean mouse = false;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuCheckBox(String text, int x, int y, int size, int spacing, Menu o, Shader shader) {
		super(text, x, y, size, spacing, o);
		this.shader = shader;
		this.font = Registry.getFont();
		this.text = text;
		this.x = x;
		this.y = y;
		this.spacing = spacing;
		this.width = font.getStringSize(text, size, spacing);
		this.height = size + (size / 2);
		this.size = size;
		this.xOffseted = this.x - (this.width / 2) + 32;
		this.screenHeight = Registry.getScreenHeight();
		this.menuOwner = o;
	}

	public void render() {
		if (this.selected) this.render(this.xOffseted - 32, this.y - (32 / 4), this.seletedTexture, this.shader, 32, 32);
		else this.render(this.xOffseted - 32, this.y - (32 / 4), this.unseletedTexture, this.shader, 32, 32);

		this.font.drawString(this.text, this.xOffseted, this.y, this.size, spacing, ccolor);
	}

	public void update() {
		if (Mouse.getX() > this.xOffseted && Mouse.getX() < (this.xOffseted + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) this.ccolor = this.scolor;
		else this.ccolor = this.color;

		if (Mouse.isButtonDown(0) && Mouse.getX() > this.xOffseted && Mouse.getX() < (this.xOffseted + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			this.ccolor = this.pcolor;
			for (ClickAction a : this.DownCallbacks)
				a.onClick(this.menuOwner);
			this.mouse = true;
		} else if (!Mouse.isButtonDown(0) && this.mouse && Mouse.getX() > this.xOffseted && Mouse.getX() < (this.xOffseted + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			this.selected = !this.selected;
			for (ClickAction a : this.ClickCallbacks)
				a.onClick(this.menuOwner);
			this.mouse = false;
		} else this.mouse = false;
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void addClickAction(ClickAction a) {
		this.ClickCallbacks.add(a);
	}

	public void addMouseDownAction(ClickAction a) {
		this.DownCallbacks.add(a);
	}

}