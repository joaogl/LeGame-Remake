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

package net.joaolourenco.legame.world.tile;

/**
 * @author Joao Lourenco
 * 
 */
public class MultiData {

	int xPos = 0;
	int yPos = 0;
	int fTextures = 0;
	int sTextures = 0;
	boolean doubleTiles = false;
	int rotations = 0;

	/**
	 * @param xPos
	 * @param yPos
	 * @param fTextures
	 * @param sTextures
	 * @param doubleTiles
	 * @param rotations
	 * @author Joao Lourenco
	 */
	public MultiData(int xPos, int yPos, int fTextures, int sTextures, boolean doubleTiles, int rotations) {
		super();
		this.xPos = xPos;
		this.yPos = yPos;
		this.fTextures = fTextures;
		this.sTextures = sTextures;
		this.doubleTiles = doubleTiles;
		this.rotations = rotations;
	}

}