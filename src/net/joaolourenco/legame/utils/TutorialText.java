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

package net.joaolourenco.legame.utils;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.graphics.font.*;

/**
 * @author Joao Lourenco
 * 
 */
public class TutorialText {

	protected String text;
	protected Font font;
	protected int x, y, size;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public TutorialText(String text, int x, int y, int size) {
		this.text = text;
		this.font = Registry.getFont();
		this.y = y;
		this.size = size;
		this.x = x - (this.font.getStringSize(this.text, this.size, -5) / 2);
	}

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public TutorialText(String text, int x, int y, int size, boolean offset) {
		this.text = text;
		this.font = Registry.getFont();
		this.y = y;
		this.size = size;
		if (offset) this.x = x - (this.font.getStringSize(this.text, this.size, -5) / 2);
		else this.x = x;
	}

	public void render() {
		this.font.drawString(this.text, this.x, this.y, this.size, -5);
	}

}
