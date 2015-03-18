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
import net.joaolourenco.legame.settings.GeneralSettings;
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

			setBorders();

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
		} else if (this.level == 2) {
			this.setSize(27, 15);

			this.player.setX(1 << GeneralSettings.TILE_SIZE_MASK);
			this.player.setY(1 << GeneralSettings.TILE_SIZE_MASK);

			setBorders();

			for (int x = 1; x < 25; x++)
				setTile(x, 2, corner);

			setTile(24, 3, corner);
			setTile(23, 3, corner);

			for (int x = 3; x < 20; x++)
				setTile(x, 5, corner);

			for (int x = 19; x < 26; x++)
				setTile(x, 6, corner);

			for (int y = 6; y < 12; y++)
				setTile(7, y, corner);

			for (int y = 6; y < 12; y++)
				setTile(10, y, corner);

			for (int y = 6; y < 9; y++)
				setTile(14, y, corner);

			for (int y = 8; y < 14; y++)
				setTile(21, y, corner);

			for (int y = 9; y < 14; y++)
				setTile(5, y, corner);

			for (int x = 7; x < 19; x++)
				setTile(x, 11, corner);

			for (int x = 13; x < 22; x++)
				setTile(x, 8, corner);

			setTile(5, 6, corner);
			setTile(6, 6, corner);
			setTile(6, 7, corner);
			setTile(1, 9, corner);
			setTile(11, 8, corner);

			// Left
			Tile ti5 = new SolidTile(64, Texture.Tiles[5]);
			Tile ti8 = new SolidTile(64, Texture.Tiles[8]);

			ti8.setRotation(-90);
			setTile(0, 2, corner);
			setTile(0, 1, ti8);
			setTile(0, 8, ti8);

			ti5.setRotation(-90);
			setTile(0, 9, corner);
			setTile(0, 3, ti5);
			setTile(0, 10, ti5);

			// Right
			Tile ti52 = new SolidTile(64, Texture.Tiles[5]);
			Tile ti82 = new SolidTile(64, Texture.Tiles[8]);

			ti52.setRotation(90);
			ti82.setRotation(90);
			setTile(26, 6, corner);
			setTile(26, 5, ti52);
			setTile(26, 7, ti82);

			// Bot
			Tile ti53 = new SolidTile(64, Texture.Tiles[5]);
			Tile ti83 = new SolidTile(64, Texture.Tiles[8]);

			ti53.setRotation(180);
			ti83.setRotation(180);
			setTile(5, 14, corner);
			setTile(21, 14, corner);
			setTile(4, 14, ti83);
			setTile(6, 14, ti53);
			setTile(20, 14, ti83);
			setTile(22, 14, ti53);

			new Citizen((12 * 64), (7 * 64), this);
			new Spider((17 * 64), (4 * 64), this, Registry.getPlayer());
			new Spider((6 * 64), (13 * 64), this, Registry.getPlayer());
			new Skeleton((3 * 64), (8 * 64), this, Registry.getPlayer());
			new Skeleton((17 * 64), (9 * 64), this, Registry.getPlayer());

			new FinishPoint(this, 11, 6, Texture.Tiles[2]);
		} else if (this.level == 3) {
			this.setSize(30, 27);

			this.player.setX(4 << GeneralSettings.TILE_SIZE_MASK);
			this.player.setY(4 << GeneralSettings.TILE_SIZE_MASK);

			setBorders();

			for (int x = 15; x < 19; x++)
				setTile(x, 1, corner);

			for (int x = 1; x < 8; x++)
				setTile(x, 2, corner);

			for (int x = 9; x < 16; x++)
				setTile(x, 2, corner);

			for (int x = 18; x < 28; x++)
				setTile(x, 2, corner);

			for (int y = 3; y < 7; y++)
				setTile(1, y, corner);

			for (int y = 6; y < 16; y++)
				setTile(2, y, corner);

			for (int y = 15; y < 26; y++)
				setTile(5, y, corner);

			for (int x = 2; x < 6; x++)
				setTile(x, 15, corner);

			for (int y = 3; y < 7; y++)
				setTile(7, y, corner);

			for (int y = 6; y < 13; y++)
				setTile(6, y, corner);

			for (int y = 3; y < 9; y++)
				setTile(9, y, corner);

			for (int y = 8; y < 13; y++)
				setTile(8, y, corner);

			for (int x = 6; x < 9; x++)
				setTile(x, 12, corner);

			for (int x = 6; x < 28; x++)
				setTile(x, 25, corner);

			for (int x = 16; x < 24; x++)
				setTile(x, 23, corner);

			for (int x = 23; x < 28; x++)
				setTile(x, 23, corner);

			for (int x = 8; x < 18; x++)
				setTile(x, 20, corner);

			for (int x = 17; x < 24; x++)
				setTile(x, 19, corner);

			for (int x = 11; x < 20; x++)
				setTile(x, 4, corner);

			for (int x = 21; x < 26; x++)
				setTile(x, 4, corner);

			for (int x = 7; x < 11; x++)
				setTile(x, 22, corner);

			for (int x = 8; x < 14; x++)
				setTile(x, 15, corner);

			for (int x = 16; x < 23; x++)
				setTile(x, 14, corner);

			for (int x = 17; x < 20; x++)
				setTile(x, 17, corner);

			for (int x = 19; x < 22; x++)
				setTile(x, 16, corner);

			for (int x = 11; x < 17; x++)
				setTile(x, 9, corner);

			for (int x = 25; x < 29; x++)
				setTile(x, 13, corner);

			for (int x = 13; x < 19; x++)
				setTile(x, 6, corner);

			for (int y = 16; y < 20; y++)
				setTile(8, y, corner);

			for (int y = 5; y < 9; y++)
				setTile(11, y, corner);

			for (int y = 10; y < 13; y++)
				setTile(11, y, corner);

			for (int y = 10; y < 15; y++)
				setTile(13, y, corner);

			for (int y = 22; y < 25; y++)
				setTile(13, y, corner);

			for (int y = 3; y < 10; y++)
				setTile(27, y, corner);

			for (int y = 9; y < 13; y++)
				setTile(28, y, corner);

			for (int y = 5; y < 10; y++)
				setTile(25, y, corner);

			for (int y = 14; y < 18; y++)
				setTile(25, y, corner);

			for (int y = 5; y < 9; y++)
				setTile(21, y, corner);

			for (int y = 20; y < 23; y++)
				setTile(23, y, corner);

			for (int y = 21; y < 23; y++)
				setTile(21, y, corner);

			for (int y = 21; y < 23; y++)
				setTile(16, y, corner);

			for (int x = 16; x < 19; x++)
				setTile(x, 8, corner);

			for (int x = 16; x < 20; x++)
				setTile(x, 10, corner);

			for (int x = 22; x < 24; x++)
				setTile(x, 10, corner);

			for (int x = 22; x < 24; x++)
				setTile(x, 18, corner);

			for (int x = 23; x < 25; x++)
				setTile(x, 9, corner);

			for (int x = 23; x < 25; x++)
				setTile(x, 17, corner);

			for (int y = 9; y < 13; y++)
				setTile(16, y, corner);

			for (int y = 11; y < 13; y++)
				setTile(19, y, corner);

			for (int y = 12; y < 14; y++)
				setTile(18, y, corner);

			setTile(7, 23, corner);
			setTile(10, 23, corner);
			setTile(27, 24, corner);
			setTile(9, 21, corner);
			setTile(10, 21, corner);

			setTile(16, 15, corner);
			setTile(17, 18, corner);
			setTile(22, 13, corner);
			setTile(18, 5, corner);

			SolidTile back = new SolidTile(64, Texture.Tiles[2]);
			setTile(4, 2, back);

			new Spider((6 * 64), (13 * 64), this, Registry.getPlayer());
			new Skeleton((24 * 64), (5 * 64), this, Registry.getPlayer());
			new Skeleton((15 * 64), (7 * 64), this, Registry.getPlayer());
			new Spider((25 * 64), (11 * 64), this, Registry.getPlayer());
			new Skeleton((26 * 64), (24 * 64), this, Registry.getPlayer());
			new Skeleton((14 * 64), (15 * 64), this, Registry.getPlayer());
			new Citizen((10 * 64), (18 * 64), this);

			new FinishPoint(this, 14, 13, Texture.Tiles[2]);
		}

		super.generateLevel();
	}

	private void setBorders() {
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
	}
}