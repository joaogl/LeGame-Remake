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

package net.joaolourenco.fallen.world.tile;

import java.util.Random;

import net.joaolourenco.fallen.entity.light.specific.FireLight;
import net.joaolourenco.fallen.graphics.Texture;
import net.joaolourenco.fallen.utils.Vector2f;
import net.joaolourenco.fallen.world.World;

/**
 * Class for Fire effect Tiles.
 * 
 * @author Joao Lourenco
 *
 */
public class FireTile extends Tile {

	/**
	 * time with the timming for the effect.
	 * texture with the texture ID for the final Fire Texture from Texture Class.
	 */
	protected int time = 0, texture = 0;
	/**
	 * Instance of the World class.
	 */
	protected World w;
	/**
	 * ID of Fire Light to apply the effect.
	 */
	protected int lightID;

	/**
	 * Constructor to create the FireTile.
	 * 
	 * @param size
	 *            : with and height of the Tile.
	 * @param tex
	 *            : Texture ID from the Texture Class.
	 * @param w
	 *            : Instance of the World Class.
	 */
	public FireTile(int size, int tex, World w) {
		// Calling the main method of the tile.
		super(size, tex, false);
		// Applying the settings.
		this.w = w;
		this.isLightCollidable(false);

		// Creating a Light on the center of the Fire.
		Vector2f location = new Vector2f(0f, 0f);
		FireLight l = new FireLight(location, 1f, 0f, 0f);
		l.init(w);
		// Getting the light ID.
		this.lightID = w.entities.size();
		// Adding the light to the world to be rendered.
		w.entities.add(l);
	}

	/**
	 * Method called by the World Class every 60 seconds.
	 */
	public void update() {
		// Calling the main method of the tile.
		super.update();

		// If the x is defined, set the light to the right position. 
		if (this.x != 9999999) {
			w.entities.get(this.lightID).setX(this.x + (this.width / 2));
			w.entities.get(this.lightID).setY(this.y + (this.height / 2));
		}
		// Texture Animation.
		time++;
		if (time % (Integer) generateRandom(6, 7, 0) == 0) {
			if (texture > 3) texture = 0;
			else texture++;
		}
		this.tex = Texture.Fire[texture];
	}

	/**
	 * Method to generate a random value.
	 * 
	 * @param min
	 *            : from
	 * @param max
	 *            : to
	 * @param type
	 *            : 0 for Integers, 1 for Floats
	 * @return Object, if type is 0 will return integer, if its 1 will return float.
	 */
	public Object generateRandom(float min, float max, int type) {
		if (type == 0) {
			// Generate Random
			Random rand = new Random();
			int out = rand.nextInt((int) max);
			// Loop until the requirements are correct
			while (out > max || out < min)
				out = rand.nextInt((int) max);
			// Return the value
			return out;
		} else if (type == 1) {
			// Generate Random
			Random rand = new Random();
			double out = min + (max - min) * rand.nextDouble();
			// Loop until the requirements are correct
			while (out > max || out < min)
				out = min + (max - min) * rand.nextDouble();
			// Return the value
			return (float) out;
		}
		return 0f;
	}

}