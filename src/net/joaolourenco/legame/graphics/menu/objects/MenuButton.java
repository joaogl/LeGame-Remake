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

import net.joaolourenco.legame.graphics.menu.*;

import org.lwjgl.input.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuButton extends MenuActionReader {

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuButton(String text, int x, int y, int size, int spacing, Menu o) {
		super(text, x, y, size, spacing, o);
	}

	public void render() {
		this.font.drawString(this.text, this.xOffseted, this.y, this.size, spacing, ccolor);
	}

	public void update() {
		if (Mouse.getX() > this.xOffseted && Mouse.getX() < (this.xOffseted + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			this.ccolor = this.scolor;
			if (Mouse.isButtonDown(0)) {
				this.ccolor = this.pcolor;
				for (ClickAction a : this.DownCallbacks)
					a.onClick(this.menuOwner);
				mouse = true;
			} else if (!Mouse.isButtonDown(0) && mouse) {
				for (ClickAction a : this.ClickCallbacks)
					a.onClick(this.menuOwner);
				mouse = false;
			}
		} else this.ccolor = this.color;
	}

}