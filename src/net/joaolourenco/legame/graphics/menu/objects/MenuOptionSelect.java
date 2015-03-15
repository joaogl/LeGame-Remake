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

import net.joaolourenco.legame.graphics.menu.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuOptionSelect extends MenuActionReader {

	protected List<String> Options = new ArrayList<String>();

	protected int ID = 0;

	protected MenuButton left, right;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public MenuOptionSelect(String text, int x, int y, int size, int spacing, Menu o) {
		super(text, x, y, size, spacing, o);
		this.width = 0;

		this.left = new MenuButton("<<", this.x - 20, this.y, this.size, spacing, o);
		this.left.addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				OptionsMenu menu = ((OptionsMenu) m);
				if (menu.resolution.ID > 0) menu.resolution.ID--;
				else menu.resolution.ID = menu.resolution.Options.size() - 1;
			}
		});
		this.right = new MenuButton(">>", this.x + 20, this.y, this.size, spacing, o);
		this.right.addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				OptionsMenu menu = ((OptionsMenu) m);
				if (menu.resolution.ID < (menu.resolution.Options.size() - 1)) menu.resolution.ID++;
				else menu.resolution.ID = 0;
			}
		});
	}

	public void render() {
		this.left.render();
		this.right.render();
		this.font.drawString(this.text, this.x - this.width - this.font.getStringSize(text, size, spacing), this.y, this.size, spacing, ccolor);
		this.font.drawString(this.Options.get(ID), this.x - (this.font.getStringSize(this.Options.get(ID), this.size, spacing) / 2), this.y, this.size, spacing, ccolor);
	}

	public void update() {
		this.left.update();
		this.right.update();
	}

	public void addOption(String add) {
		int local_size = this.font.getStringSize(add, this.size, spacing);
		if (local_size > this.width) {
			this.width = local_size;
			this.left.setX(this.x - 30 - (this.width / 2));
			this.right.setX(this.x + 40 + (this.width / 2));
		}
		this.Options.add(add);
	}

}