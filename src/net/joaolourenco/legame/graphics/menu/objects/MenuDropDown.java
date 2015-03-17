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

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.menu.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuDropDown extends MenuActionReader {

	protected List<String> Options = new ArrayList<String>();

	protected Shader shader;
	protected int unseletedTexture = Texture.MenuCheckBox[0];
	protected int seletedTexture = Texture.MenuCheckBox[1];

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuDropDown(int x, int y, int size, int spacing, Menu o, Shader shader) {
		super("", x, y, size, spacing, o);
		this.shader = shader;
		this.width = 0;
	}

	public void render() {
		this.render(this.xOffseted - 32, this.y - (32 / 4), 0, this.shader, 64, 64, new Vector4f(0.5f, 0.5f, 0.5f, 0.2f));

		// if (this.selected) this.render(this.xOffseted - 32, this.y - (32 / 4), this.seletedTexture, this.shader, 32, 32);
		// else this.render(this.xOffseted - 32, this.y - (32 / 4), this.unseletedTexture, this.shader, 32, 32);

		// this.font.drawString(this.text, this.xOffseted, this.y, this.size, spacing, ccolor);
	}

	public void update() {
		if (Mouse.getX() > this.xOffseted && Mouse.getX() < (this.xOffseted + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			this.ccolor = this.scolor;
			if (Mouse.isButtonDown(0)) {
				this.ccolor = this.pcolor;
				for (ClickAction a : this.DownCallbacks)
					a.onClick(this.menuOwner);
				this.mouse = true;
			} else if (!Mouse.isButtonDown(0) && this.mouse) {
				this.selected = !this.selected;
				for (ClickAction a : this.ClickCallbacks)
					a.onClick(this.menuOwner);
				this.mouse = false;
			}
		} else this.ccolor = this.color;
	}

	public void addOption(String string) {
		Options.add(string);
	}

}