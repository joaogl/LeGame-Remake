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

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.menu.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MenuSlider extends MenuActionReader {

	private Shader shader = new Shader(GeneralSettings.menuBackFragPath, GeneralSettings.defaultVertexPath);
	private int xSlideMin, xSlideMax, xSlide, xSlideOffset;
	private int minVal, maxVal;
	private String postText;
	private VertexHandlers VertexID;

	/**
	 * @param text
	 * @param x
	 * @param y
	 * @param size
	 * @param spacing
	 * @param o
	 * @author Joao Lourenco
	 */
	public MenuSlider(String text, int x, int y, int width, int size, int spacing, Menu o, int minVal, int maxVal, String postText) {
		super(text, x, y, size, spacing, o);
		this.width = width;
		this.x = x - 70;
		this.xSlideMin = this.x + 5;
		this.xSlideMax = this.x - 25 + this.width;
		this.xSlide = xSlideMax;
		this.xSlideOffset = 10;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.postText = postText;

		this.VertexID = Registry.registerVertexHandler(width, 30);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void render() {
		render(this.x, this.y - 5, Texture.Menus[2], this.shader, this.VertexID);
		render(this.xSlide, this.y, Texture.Menus[3], this.shader, 20, 20, new Vector4f(ccolor.x, ccolor.y, ccolor.z, 1f));

		this.font.drawString(this.text, this.xOffseted - 140, this.y, this.size, spacing, color);
		this.font.drawString(this.getValue() + "", this.xOffseted + this.width, this.y - 6, this.size + 10, -15, color);
		this.font.drawString(" " + this.postText, this.xOffseted + this.width - 10 + this.font.getStringSize(this.getValue() + "", -15, this.size + 10), this.y, this.size + 5, spacing, color);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void update() {
		if (Mouse.getX() > this.x && Mouse.getX() < (this.x + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) this.ccolor = this.scolor;
		else this.ccolor = this.color;

		if (Mouse.isButtonDown(0) && Mouse.getX() > this.x && Mouse.getX() < (this.x + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			for (ClickAction a : this.DownCallbacks)
				a.onClick(this.menuOwner);
			this.mouse = true;
		} else if (!Mouse.isButtonDown(0) && this.mouse && Mouse.getX() > this.x && Mouse.getX() < (this.x + this.width) && (this.screenHeight - Mouse.getY()) > this.y && (this.screenHeight - Mouse.getY()) < (this.y + this.height)) {
			this.selected = !this.selected;
			for (ClickAction a : this.ClickCallbacks)
				a.onClick(this.menuOwner);
			this.mouse = false;
		} else this.mouse = false;

		if (this.mouse) this.xSlide = (Mouse.getX() - this.xSlideOffset);

		if (this.xSlide > this.xSlideMax) this.xSlide = this.xSlideMax;
		else if (this.xSlide < this.xSlideMin) this.xSlide = this.xSlideMin;
	}

	public int getValue() {
		double perc = (this.xSlide - this.xSlideMin) * 100.0f / (this.xSlideMax - this.xSlideMin);
		return (int) ((perc * (this.maxVal - this.minVal) / 100.0f) + this.minVal);
	}

	public void setPosition(int pos) {
		double perc = (pos - this.minVal) * 100.0f / (this.maxVal - this.minVal);
		double value = (perc * (this.xSlideMax - this.xSlideMin) / 100.0f);
		this.xSlide = (int) (value + this.xSlideMin) + 1;
	}

}