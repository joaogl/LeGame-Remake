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

package net.joaolourenco.legame.graphics.font;

import net.joaolourenco.legame.*;

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
	 * Text is the corrent text being rendered. finalText is the full text, rendered and not rendered.
	 */
	private String text, finalText;
	/**
	 * x and y is the position of the text. size is the size of the font.
	 */
	private int x, y, size;
	/**
	 * last Holds the time that the last letter was added.
	 */
	private long last = 0;
	/**
	 * finished holds the information on whether is the animation over or not. remove holds the information on whether the text is going to be removed or not.
	 */
	private boolean finished = false, remove = false;
	/**
	 * Time between each letter.
	 */
	private int time = 200;
	/**
	 * Time per letter.
	 */
	private int timeperletter = 200;
	/**
	 * The spacing between each letter.
	 */
	private int spacing;

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
	 * @author Joao Lourenco
	 */
	public AnimatedText(String text, int x, int y, int size) {
		this.font = Registry.getFont();
		this.text = "";
		this.finalText = text;
		this.y = y;
		this.size = size;
		this.last = System.currentTimeMillis();
		this.time = 200;
		this.timeperletter = 300;
		this.spacing = 5;
		this.x = x - (this.font.getStringSize(this.finalText, this.size, this.spacing) / 2);
		Registry.registerAnimatedText(this);
	}

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
	 * @param spacing
	 *            : The spacing between each letter.
	 * @author Joao Lourenco
	 */
	public AnimatedText(String text, int x, int y, int size, int spacing) {
		this.font = Registry.getFont();
		this.text = "";
		this.finalText = text;
		this.y = y;
		this.size = size;
		this.last = System.currentTimeMillis();
		this.time = 200;
		this.timeperletter = 300;
		this.spacing = spacing;
		this.x = x - (this.font.getStringSize(this.finalText, this.size, this.spacing) / 2);
		Registry.registerAnimatedText(this);
	}

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
	 * @param time
	 *            : Time between each letter.
	 * @param tp
	 *            : Time per letter.
	 * @author Joao Lourenco
	 */
	public AnimatedText(String text, int x, int y, int size, int time, int tp) {
		this.font = Registry.getFont();
		this.text = "";
		this.finalText = text;
		this.y = y;
		this.size = size;
		this.last = System.currentTimeMillis();
		this.time = time;
		this.timeperletter = tp;
		this.spacing = 5;
		this.x = x - (this.font.getStringSize(this.finalText, this.size, this.spacing) / 2);
		Registry.registerAnimatedText(this);
	}

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
	 * @param time
	 *            : Time between each letter.
	 * @param tp
	 *            : Time per letter.
	 * @param spacing
	 *            : The spacing between each letter.
	 * @author Joao Lourenco
	 */
	public AnimatedText(String text, int x, int y, int size, int time, int tp, int spacing) {
		this.font = Registry.getFont();
		this.text = "";
		this.finalText = text;
		this.y = y;
		this.size = size;
		this.last = System.currentTimeMillis();
		this.time = time;
		this.timeperletter = tp;
		this.spacing = spacing;
		this.x = x - (this.font.getStringSize(this.finalText, this.size, this.spacing) / 2);
		Registry.registerAnimatedText(this);
	}

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
	 * @param time
	 *            : Time between each letter.
	 * @param tp
	 *            : Time per letter.
	 * @param spacing
	 *            : The spacing between each letter.
	 * @param off
	 *            : The timing offset desired for the text animation.
	 * @author Joao Lourenco
	 */
	public AnimatedText(String text, int x, int y, int size, int time, int tp, int spacing, int off) {
		this.font = Registry.getFont();
		this.text = "";
		this.finalText = text;
		this.y = y;
		this.size = size;
		this.time = time;
		this.timeperletter = tp;
		this.spacing = spacing;
		this.x = x - (this.font.getStringSize(this.finalText, this.size, this.spacing) / 2);
		this.last = System.currentTimeMillis() + off;
		Registry.registerAnimatedText(this);
	}

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
	 * @param time
	 *            : Time between each letter.
	 * @param tp
	 *            : Time per letter.
	 * @param spacing
	 *            : The spacing between each letter.
	 * @param off
	 *            : Which AnimatedText is being rendered before the new one.
	 * @author Joao Lourenco
	 */
	public AnimatedText(String text, int x, int y, int size, int time, int tp, int spacing, AnimatedText off) {
		this.font = Registry.getFont();
		this.text = "";
		this.finalText = text;
		this.y = y;
		this.size = size;
		this.time = time;
		this.timeperletter = tp;
		this.spacing = spacing;
		this.x = x - (this.font.getStringSize(this.finalText, this.size, this.spacing) / 2);
		this.last = System.currentTimeMillis() + off.getRenderTiming();
		Registry.registerAnimatedText(this);
	}

	/**
	 * Method called by the Main Class to render the text.
	 * 
	 * @author Joao Lourenco
	 */
	public void render() {
		font.drawString(this.text, this.x, this.y, this.size, this.spacing);
	}

	/**
	 * Method called by the Main Class to update the text.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		// Current time of this update.
		long current = System.currentTimeMillis();
		// If another update is required.
		if (current - this.last >= this.time && !this.finished) {
			this.last += this.time;
			// Adds the letter to the rendering text.
			addLetter();
			// If the time is passed and the animation is over, remove the text.
		} else if (this.finished && current - this.last >= (timeperletter * this.text.length())) this.remove = true;
	}

	/**
	 * Method to add another letter to the rendering text.
	 * 
	 * @author Joao Lourenco
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
	 * @author Joao Lourenco
	 */
	public boolean isRemoved() {
		return this.remove;
	}

	public int getRenderTiming() {
		return this.finalText.length() * this.time;
	}

	public int getTotalTiming() {
		return (this.finalText.length() * this.time) + (this.finalText.length() * this.timeperletter);
	}

}