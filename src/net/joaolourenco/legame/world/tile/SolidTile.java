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

import net.joaolourenco.legame.entity.*;

/**
 * Class for all the Solid, this is, collidable Tiles.
 * 
 * @author Joao Lourenco
 * 
 */
public class SolidTile extends Tile {

	/**
	 * Constructor to create a square Tile.
	 * 
	 * @param size
	 *            : with and height for the Tile.
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @author Joao Lourenco
	 */
	public SolidTile(int size, int tex) {
		super(size, tex);
		setRotation(0);
	}

	/**
	 * Constructor to create a square Tile.
	 * 
	 * @param size
	 *            : with and height for the Tile.
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @author Joao Lourenco
	 */
	public SolidTile(int size, int tex, int rotation) {
		super(size, tex);
		setRotation(rotation);
	}

	/**
	 * Constructor to create a square Tile with Light.
	 * 
	 * @param size
	 *            : with and height for the Tile.
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @param light
	 *            : true to make the Light collide with the Tile, false to make the Light spread through the Tile.
	 * @author Joao Lourenco
	 */
	public SolidTile(int size, int tex, boolean light) {
		super(size, tex);
		this.isLightCollidable(light);
		setRotation(0);
	}

	/**
	 * Method update called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		super.update();
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void entityOver(Entity e) {
	}

}