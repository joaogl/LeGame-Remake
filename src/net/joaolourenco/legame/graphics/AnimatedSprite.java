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

package net.joaolourenco.legame.graphics;

/**
 * @author Joao Lourenco
 * 
 */
public class AnimatedSprite {

	private int frame = 0;
	private int[] full_textures;
	private int texture;
	private int rate = 5;
	private int length = -1;
	private int time = 0;
	private int defaultFrame = 0;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public AnimatedSprite(int[] tex, int length, int defaultFrame) {
		this.full_textures = tex;
		this.length = length;
		this.defaultFrame = defaultFrame;
	}

	public void update() {
		time++;
		if (time % rate == 0) {
			if (frame >= length - 1) frame = 0;
			else frame++;
			texture = full_textures[frame];
		}
	}

	public void setFrameRate(int frames) {
		rate = frames;
	}

	public void setFrame(int index) {
		if (index > full_textures.length - 1) return;
		frame = index;
		texture = full_textures[frame];
	}

	public void resetAnimation() {
		frame = defaultFrame;
		texture = full_textures[frame];
	}

	public int getTexture() {
		return texture;
	}

}