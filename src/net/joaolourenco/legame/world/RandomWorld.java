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

package net.joaolourenco.legame.world;

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.world.tile.*;

/**
 * @author Joao Lourenco
 * 
 */
public class RandomWorld extends World {

	/**
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public RandomWorld(int level) {
		super(10, 10);

	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void levelEnd() {

	}

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public void generateLevel() {
		this.player.setX(2);
		this.player.setY(2);

		SolidTile back = new SolidTile(64, Texture.Tiles[2]);
		SolidTile l = new SolidTile(64, Texture.Tiles[1], -90);
		SolidTile r = new SolidTile(64, Texture.Tiles[1], 90);

		SolidTile t = new SolidTile(64, Texture.Tiles[1], 0);
		SolidTile b = new SolidTile(64, Texture.Tiles[1], 180);

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (x == 0 || x == (width - 1)) {
					if (x == 0) this.setTile(x, y, l);
					else this.setTile(x, y, r);
				} else if (y == 0 || y == (height - 1)) {
					if (y == 0) this.setTile(x, y, t);
					else this.setTile(x, y, b);
				} else this.setTile(x, y, back);

		setTile(0, 0, new SolidTile(64, Texture.Tiles[0], 0));
		setTile(this.width - 1, 0, new SolidTile(64, Texture.Tiles[0], 90));
		setTile(0, this.height - 1, new SolidTile(64, Texture.Tiles[0], 270));
		setTile(this.width - 1, this.height - 1, new SolidTile(64, Texture.Tiles[0], 180));

		Entity cit = new Citizen((5 * 64), (5 * 64), this);
		this.addEntity(cit);

		super.generateLevel();
	}

}