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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;

import net.joaolourenco.legame.Registry;
import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.entity.block.Door;
import net.joaolourenco.legame.entity.light.Light;
import net.joaolourenco.legame.entity.light.PointLight;
import net.joaolourenco.legame.entity.mob.Citizen;
import net.joaolourenco.legame.entity.mob.Skeleton;
import net.joaolourenco.legame.entity.mob.Spider;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.font.AnimatedText;
import net.joaolourenco.legame.items.DoorKey;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.KeyboardFilter;
import net.joaolourenco.legame.utils.Timer;
import net.joaolourenco.legame.utils.TimerResult;
import net.joaolourenco.legame.utils.TutorialText;
import net.joaolourenco.legame.utils.Vector2f;
import net.joaolourenco.legame.world.tile.FinishPoint;
import net.joaolourenco.legame.world.tile.SolidTile;
import net.joaolourenco.legame.world.tile.Tile;

import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * @author Joao Lourenco
 * 
 */
public class Tutorial extends World {

	boolean firstUpdate = false, needUpdates = false, readyToAdd = false;
	List<TutorialText> text = new ArrayList<TutorialText>();
	int step = -1;

	/**
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Tutorial() {
		super(100, 100);
	}

	public void preLoad() {
		super.preLoad();
		this.player.setRenderable(false);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void levelEnd() {
	}

	/**
	 * @see net.joaolourenco.legame.world.World#generateLevel()
	 * @author Joao Lourenco
	 */
	@Override
	public void generateLevel() {
		new FinishPoint(this, 0, 0, Texture.Tiles[2]);

		if (timerOver) super.generateLevel();
		finished = true;
	}

	/**
	 * Method to render the entire world called by the Main Class.
	 * 
	 * @author Joao Lourenco
	 */
	public void render() {
		// Moving the Render to the right position to render.
		glTranslatef(-this.xOffset, -this.yOffset, 0f);
		// Clearing the colors.
		glColor3f(1f, 1f, 1f);

		// Getting the variables ready to check what tiles to render.
		int x0 = this.xOffset >> GeneralSettings.TILE_SIZE_MASK;
		int x1 = (this.xOffset >> GeneralSettings.TILE_SIZE_MASK) + (Registry.getScreenWidth() * 14 / 800);
		int y0 = this.yOffset >> GeneralSettings.TILE_SIZE_MASK + 1;
		int y1 = (this.yOffset >> GeneralSettings.TILE_SIZE_MASK) + (Registry.getScreenHeight() * 12 / 600);
		// Going through all the tiles to render.
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				// Getting and Rendering all the tiles.S
				Tile tile = getTile(x, y);
				if (tile != null) tile.render(x << GeneralSettings.TILE_SIZE_MASK, y << GeneralSettings.TILE_SIZE_MASK, this, getNearByLights(x << GeneralSettings.TILE_SIZE_MASK, y << GeneralSettings.TILE_SIZE_MASK));
			}
		}
		// Clearing the colors once more.
		glColor3f(1f, 1f, 1f);
		// Going througth all the entities
		for (Entity e : this.entities) {
			// Checking if they are close to the player.
			if (e != null && getDistance(e, this.player) < 800) {
				if (e instanceof Light) {
					// If its a Light render it and its shadows.
					((Light) e).renderShadows(entities, worldTiles);
					((Light) e).render();
					// If not just render the Entity.
				} else e.render();
			}
		}
		// Moving the Render back to the default position.
		glTranslatef(this.xOffset, this.yOffset, 0f);

		try {
			for (TutorialText t : text)
				t.render();
		} catch (ConcurrentModificationException e) {
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * Method to update everything called by the Main class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		if (this.levelOver && step >= 2) {
			this.levelOver = false;
			this.player.setX(5 * 64);
			this.player.setY(2 * 64);
			this.changeStep(true);
		}

		if (!firstUpdate || needUpdates) {
			// Updating all the entities.
			for (Entity e : this.entities) {
				if (e != null && getDistance(this.player, e) <= Registry.getScreenWidth()) e.update();
			}
			firstUpdate = true;
		}
		// Updating all the world tiles.
		for (Tile t : this.worldTiles)
			if (t != null && getDistance(this.player, t.getX(), t.getY()) <= Registry.getScreenWidth()) {
				t.update();
				for (Entity e : this.entities)
					if (getDistance(e, t.getX(), t.getY()) <= 32) t.entityOver(e);
			}

		if (readyToAdd) {
			changeStep(true);
			readyToAdd = false;
		}

		if (step == 1) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) changeStep(false);
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) changeStep(false);
			else if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) changeStep(false);
			else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) changeStep(false);
		}
		if (step >= 0 && step <= 4 && KeyboardFilter.isKeyDown(Keyboard.KEY_RETURN)) {
			if (step == 3) {
				this.player.setX(5 * 64);
				this.player.setY(2 * 64);
			}
			changeStep(true);
		}
	}

	/**
	 * Method to tick everything called by the Main class 10 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void tick() {
		// If an entity is removed remove it from the Array.
		for (Entity e : this.entities)
			if (e != null && e.isRemoved()) this.entities.remove(e);

		// Tick the entities.
		for (Entity e : this.entities)
			if (e != null) e.tick();
	}

	public void stopLoading() {
		this.loading.remove();
		changeStep(true);
		this.levelEndable = false;
	}

	public void changeStep(boolean removeText) {
		step++;
		if (removeText && step != 4) text.clear();
		if (step != 4) Registry.clearAnimatedTexts();
		if (step == 6) Registry.getMainClass().setWorld(new RandomWorld(1));

		if (step == 0) {
			int yPos = (Registry.getScreenHeight() / 4);

			AnimatedText a = new AnimatedText("This game has a simple objective.", Registry.getScreenWidth() / 2, yPos, 25, 100, 200, -1);
			new AnimatedText("Get to the end of each level. ALIVE!", Registry.getScreenWidth() / 2, yPos + 50, 25, 100, 200, -5, a);

			this.text.add(new TutorialText("End Mark", Registry.getScreenWidth() / 2, (Registry.getScreenHeight() / 8) * 5, 25));
			this.text.add(new TutorialText("Hit enter to continue.", 10, Registry.getScreenHeight() - 25, 18, false));

			new Timer("Tutorial-Step-" + step, 10000, 1, new TimerResult(this) {
				public void timerCall(String caller) {
					Tutorial obj = (Tutorial) this.object;
					if (obj.step == 0) obj.readyToAdd = true;
				}
			});
		} else if (step == 1) {
			int yPos = (Registry.getScreenHeight() / 4);

			AnimatedText a = new AnimatedText("Use the WASD or the", Registry.getScreenWidth() / 2, yPos, 25, 100, 5000, -1);
			new AnimatedText("arrow keys to move.", Registry.getScreenWidth() / 2, yPos + 50, 25, 100, 5000, -1, a);

			this.text.add(new TutorialText("Hit enter to continue.", 10, Registry.getScreenHeight() - 25, 18, false));

			this.setSize(6, 4);

			new FinishPoint(this, 3, 1, Texture.Tiles[2]);

			setTile(0, 0, new SolidTile(64, Texture.Tiles[0]));
			for (int i = 1; i < 5; i++) {
				setTile(i, 0, new SolidTile(64, Texture.Tiles[1]));
				setTile(i, 3, new SolidTile(64, Texture.Tiles[1], 180));
			}
			for (int i = 1; i < 3; i++) {
				setTile(0, i, new SolidTile(64, Texture.Tiles[1], -90));
				setTile(5, i, new SolidTile(64, Texture.Tiles[1], 90));
			}
			for (int y = 1; y < 3; y++)
				for (int x = 1; x < 3; x++)
					setTile(x, y, new SolidTile(64, Texture.Tiles[2], 0));

			setTile(5, 3, new SolidTile(64, Texture.Tiles[0], 180));
			setTile(5, 0, new SolidTile(64, Texture.Tiles[0], 90));
			setTile(0, 3, new SolidTile(64, Texture.Tiles[0], -90));

			this.player.renderable = true;
			this.needUpdates = true;
		} else if (step == 2) {
			int yPos = (Registry.getScreenHeight() / 4);

			this.levelEndable = true;

			new AnimatedText("Go to the End Mark", Registry.getScreenWidth() / 2, yPos, 25, 100, 5000, -1);
		} else if (step == 3) {
			this.levelEndable = false;
			this.setSize(20, 20);

			AnimatedText a = new AnimatedText("There are some Enemies that", Registry.getScreenWidth() / 2, 30, 25, 100, 5000, -1);
			AnimatedText b = new AnimatedText("you will face, some will shoot", Registry.getScreenWidth() / 2, 80, 25, 100, 5000, -1, a);
			AnimatedText c = new AnimatedText("you, some will follow you untill", Registry.getScreenWidth() / 2, 130, 25, 100, 5000, -1, b);
			AnimatedText d = new AnimatedText("you die.", Registry.getScreenWidth() / 2, 180, 25, 100, 5000, -1, c);

			new Timer("Tutorial-Step-" + step, (int) d.getDelayTime(), 1, new TimerResult(this) {
				public void timerCall(String caller) {
					Tutorial obj = (Tutorial) this.object;
					if (obj.step == 3) obj.readyToAdd = true;
				}
			});

			this.text.add(new TutorialText("Hit enter to continue.", 10, Registry.getScreenHeight() - 25, 18, false));

			this.player.renderable = false;
			this.player.freeze();
			this.needUpdates = true;
		} else if (step == 4) {
			new Citizen(0, 3 * 64, this);
			new Skeleton(2 * 64, 2 * 64, this);
			new Spider(5 * 64, 3 * 64, this);

			new AnimatedText("Press enter to continue", Registry.getScreenWidth() / 2, (Registry.getScreenHeight() / 6) * 5, 25, 100, 5000, -1);

			this.text.add(new TutorialText("Hit enter to continue.", 10, Registry.getScreenHeight() - 25, 18, false));
		} else if (step == 5) {
			this.levelEndable = true;
			this.player.renderable = true;
			this.player.unFreeze();
			this.needUpdates = true;

			this.entities.clear();
			this.setSize(14, 13);
			this.player.setX(3);
			this.player.setY(2);
			this.player.init(this);
			this.needUpdates = true;
			this.player.renderable = true;
			this.player.unFreeze();
			this.player.setX((2 * 64));
			this.player.setY((2 * 64));

			for (int y = 1; y < (this.height - 1); y++)
				for (int x = 1; x < (this.width - 1); x++)
					setTile(x, y, new SolidTile(64, Texture.Tiles[2], 0));

			for (int x = 1; x < (this.width - 1); x++) {
				setTile(x, 0, new SolidTile(64, Texture.Tiles[1], 0));
				setTile(x, this.height - 1, new SolidTile(64, Texture.Tiles[1], 180));
			}
			for (int y = 1; y < (this.height - 1); y++) {
				setTile(0, y, new SolidTile(64, Texture.Tiles[1], -90));
				setTile(this.width - 1, y, new SolidTile(64, Texture.Tiles[1], 90));
			}

			setTile(0, 0, new SolidTile(64, Texture.Tiles[0], 0));
			setTile(this.width - 1, 0, new SolidTile(64, Texture.Tiles[0], 90));
			setTile(0, this.height - 1, new SolidTile(64, Texture.Tiles[0], 270));
			setTile(this.width - 1, this.height - 1, new SolidTile(64, Texture.Tiles[0], 180));

			setTile(5, 0, new SolidTile(64, Texture.Tiles[5], 0));
			setTile(6, 0, new SolidTile(64, Texture.Tiles[7], 0));
			getTile(6, 0).isCollidable(true);
			setTile(7, 0, new SolidTile(64, Texture.Tiles[8], 0));

			for (int y = 1; y < 9; y++) {
				setTile(6, y, new SolidTile(64, Texture.Tiles[6], 0));
				getTile(6, y).isCollidable(true);
			}

			setTile(6, 9, new SolidTile(64, Texture.Tiles[4], 0));
			getTile(6, 9).isCollidable(true);

			for (int x = 7; x < 13; x++) {
				setTile(x, 9, new SolidTile(64, Texture.Tiles[6], 90));
				getTile(x, 9).isCollidable(true);
			}
			setTile(10, 9, new SolidTile(64, Texture.Tiles[2], 90));

			setTile(13, 8, new SolidTile(64, Texture.Tiles[5], 90));
			setTile(13, 9, new SolidTile(64, Texture.Tiles[7], 90));
			getTile(13, 9).isCollidable(true);
			setTile(13, 10, new SolidTile(64, Texture.Tiles[8], 90));

			new FinishPoint(this, 9, 2, Texture.Tiles[2]);

			Door door = new Door((9 * 64), (9 * 64), 124, 64);
			door.locked = true;
			door.setTexture(Texture.Door);
			door.init(this);
			this.player.giveItem(new DoorKey(door.getKey()));
			this.entities.add(door);

			Vector2f location = new Vector2f((6 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2, (6 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2);
			PointLight l2 = new PointLight(location, 10f, 10f, 10f, 0.5f);
			l2.init(this);
			l2.isCollidable(false);
			this.entities.add(l2);

			this.DAY_LIGHT = 0.3f;

			this.entities.add(this.player);

			AnimatedText a = new AnimatedText("To open doors,press ", Registry.getScreenWidth() / 2, 30, 25, 100, 5000, -1);
			AnimatedText b = new AnimatedText("ENTER near the door.", Registry.getScreenWidth() / 2, 80, 25, 100, 5000, -1, a);
			new AnimatedText("Try to finish the level.", Registry.getScreenWidth() / 2, 130, 25, 100, 5000, -1, b);
		}
	}
}