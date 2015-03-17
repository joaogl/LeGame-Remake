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

import net.joaolourenco.legame.Registry;
import net.joaolourenco.legame.entity.mob.Citizen;
import net.joaolourenco.legame.entity.mob.Skeleton;
import net.joaolourenco.legame.entity.mob.Spider;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.menu.MainMenu;
import net.joaolourenco.legame.world.tile.FinishPoint;
import net.joaolourenco.legame.world.tile.SolidTile;
import net.joaolourenco.legame.world.tile.Tile;

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
		super(10, 10, level);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void levelEnd() {
		this.gameOver = true;
		if (!this.lost) this.endMessage = "Level " + (this.level + 1);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void gameOver() {
		if (this.lost) {
			Registry.getMainClass().setWorld(null);
			Registry.registerMenu(new MainMenu());
		} else Registry.getMainClass().setWorld(new RandomWorld(this.level + 1));
	}

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public void generateLevel() {

		Tile corner = new SolidTile(64, Texture.Tiles[4]);
		corner.isCollidable(true);

		System.out.println("Level " + this.level);
		if (this.level == 1) {
			this.setSize(29, 14);

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

			for (int y = 1; y < 7; y++)
				setTile(8, y, corner);

			setTile(8, 0, corner);
			setTile(17, 13, corner);
			setTile(28, 6, corner);

			Tile ti = new SolidTile(64, Texture.Tiles[5]);
			ti.isCollidable(true);
			setTile(7, 0, ti);

			ti = new SolidTile(64, Texture.Tiles[8]);
			ti.isCollidable(true);
			setTile(9, 0, ti);

			ti = new SolidTile(64, Texture.Tiles[8]);
			ti.isCollidable(true);
			ti.setRotation(180);
			setTile(16, 13, ti);

			ti = new SolidTile(64, Texture.Tiles[5]);
			ti.isCollidable(true);
			ti.setRotation(180);
			setTile(18, 13, ti);

			ti = new SolidTile(64, Texture.Tiles[5]);
			ti.isCollidable(true);
			ti.setRotation(90);
			setTile(28, 5, ti);

			ti = new SolidTile(64, Texture.Tiles[8]);
			ti.isCollidable(true);
			ti.setRotation(90);
			setTile(28, 7, ti);

			setTile(7, 6, corner);
			setTile(7, 7, corner);
			setTile(6, 7, corner);
			setTile(6, 8, corner);
			setTile(5, 8, corner);
			setTile(5, 9, corner);
			setTile(4, 9, corner);

			setTile(9, 6, corner);
			setTile(9, 7, corner);
			setTile(9, 8, corner);
			setTile(10, 8, corner);
			setTile(11, 8, corner);
			setTile(12, 8, corner);
			setTile(12, 9, corner);
			setTile(13, 9, corner);

			for (int y = 6; y < 13; y++)
				setTile(17, y, corner);

			setTile(18, 6, corner);
			setTile(19, 6, corner);
			setTile(26, 6, corner);
			setTile(27, 6, corner);

			new FinishPoint(this, 22, 9, Texture.Tiles[2]);

			new Citizen((4 * 64), (8 * 64), this);
			new Spider((8 * 64), (9 * 64), this, Registry.getPlayer());
			new Skeleton((10 * 64), (2 * 64), this, Registry.getPlayer());
			new Skeleton((26 * 64), (2 * 64), this, Registry.getPlayer());
		}

		super.generateLevel();
	}
}