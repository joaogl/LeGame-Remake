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

package net.joaolourenco.fallen.graphics.font;

import net.joaolourenco.fallen.settings.GeneralSettings;

/**
 * Class that processes Animated Text.
 * 
 * @author Joao Lourenco
 *
 */
public class AnimatedText {

	/**
	 * Font istance for the Font renders/loads etc's.
	 */
	private Font font;
	/**
	 * Text is the corrent text being rendered.
	 * finalText is the full text, rendered and not rendered.
	 */
	private String text, finalText;
	/**
	 * x and y is the position of the text.
	 * size is the size of the font.
	 */
	private int x, y, size;
	/**
	 * last Holds the time that the last letter was added.
	 */
	private long last = 0;
	/**
	 * finished holds the information on whether is the animation over or not.
	 * remove holds the information on whether the text is going to be removed or not.
	 */
	private boolean finished = false, remove = false;

	/**
	 * Constructor for AnimatedText.
	 * 
	 * @param text
	 *            : Text to be rendered after the animation is over.
	 * @param x
	 *            : x Location of the text.
	 * @param y
	 *            : y Location of the text.
	 * @param size
	 *            : Font size.
	 */
	public AnimatedText(String text, int x, int y, int size) {
		this.font = new Font();
		this.text = "";
		this.finalText = text;
		this.x = x;
		this.y = y;
		this.size = size;
		this.last = System.currentTimeMillis();
		GeneralSettings.animatedText.add(this);
	}

	/**
	 * Method called by the Main Class to render the text.
	 */
	public void render() {
		font.drawString(this.text, this.x, this.y, this.size, 5);
	}

	/**
	 * Method called by the Main Class to update the text.
	 */
	public void update() {
		// Current time of this update.
		long current = System.currentTimeMillis();
		// If another update is required.
		if (current - this.last >= 200 && !this.finished) {
			this.last += 200;
			// Adds the letter to the rendering text.
			addLetter();
			// If the time is passed and the animation is over, remove the text.
		} else if (this.finished && current - this.last >= (300 * this.text.length())) this.remove = true;
	}

	/**
	 * Method to add another letter to the rendering text.
	 */
	public void addLetter() {
		// if there is another letter to add add it if not finish animation.
		if (this.text.length() < this.finalText.length()) this.text += this.finalText.charAt(this.text.length());
		else this.finished = true;
	}

	/**
	 * Method to check if the text is ready to be removed.
	 * 
	 * @return boolean, is the text ready to be removed or not.
	 */
	public boolean isRemoved() {
		return this.remove;
	}

}