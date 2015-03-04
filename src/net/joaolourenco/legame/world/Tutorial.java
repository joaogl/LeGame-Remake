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

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.world.tile.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Tutorial extends World {

	/**
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Tutorial() {
		super(10, 10);
	}

	/**
	 * @see net.joaolourenco.legame.world.World#generateLevel()
	 * @author Joao Lourenco
	 */
	@Override
	public void generateLevel() {
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				SolidTile ti = new SolidTile(GeneralSettings.TILE_SIZE, Texture.Dirt, true);
				ti.isLightCollidable(false);
				setTile(x, y, ti);
			}
		}

		super.generateLevel();
	}

}
