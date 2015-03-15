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

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.menu.*;

import org.lwjgl.input.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuCheckBox extends MenuActionReader {

	protected Shader shader;
	protected int unseletedTexture = Texture.MenuCheckBox[0];
	protected int seletedTexture = Texture.MenuCheckBox[1];
	protected int unseletedDisabledTexture = Texture.MenuCheckBox[2];
	protected int seletedDisabledTexture = Texture.MenuCheckBox[3];

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuCheckBox(String text, int x, int y, int size, int spacing, Menu o, Shader shader) {
		super(text, x, y, size, spacing, o);
		this.shader = shader;
	}

	public void render() {
		if (this.enabled) {
			if (this.selected) this.render(this.xOffseted - 32, this.y - (32 / 4), this.seletedTexture, this.shader, 32, 32);
			else this.render(this.xOffseted - 32, this.y - (32 / 4), this.unseletedTexture, this.shader, 32, 32);
		} else {
			if (this.selected) this.render(this.xOffseted - 32, this.y - (32 / 4), this.seletedDisabledTexture, this.shader, 32, 32);
			else this.render(this.xOffseted - 32, this.y - (32 / 4), this.unseletedDisabledTexture, this.shader, 32, 32);
		}

		if (this.enabled) this.font.drawString(this.text, this.xOffseted, this.y, this.size, spacing, ccolor);
		else this.font.drawString(this.text, this.xOffseted, this.y, this.size, spacing, this.dColor);
	}

	public void update() {
		if (!this.enabled) return;

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

}