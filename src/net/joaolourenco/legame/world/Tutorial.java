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

import java.util.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.entity.light.*;
import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.utils.Timer;
import net.joaolourenco.legame.world.tile.*;

import org.lwjgl.input.*;

import static org.lwjgl.opengl.GL11.*;

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
		Registry.getPlayer().setRenderable(false);
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
		// List<MultiData> list = new ArrayList<MultiData>();
		// list.add(new MultiData(0, 0, Texture.FinishPod[0], Texture.Tiles[3], true, 0));
		// list.add(new MultiData(1, 0, Texture.FinishPod[1], Texture.Tiles[3], true, 0));
		// list.add(new MultiData(0, 1, Texture.FinishPod[2], Texture.Tiles[3], true, 0));
		// list.add(new MultiData(1, 1, Texture.FinishPod[3], Texture.Tiles[3], true, 0));
		// setTile(0, 0, new MultiTile(list));

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
		int y0 = this.yOffset >> GeneralSettings.TILE_SIZE_MASK;
		int y1 = (this.yOffset >> GeneralSettings.TILE_SIZE_MASK) + (Registry.getScreenHeight() * 11 / 600);
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
			Registry.getPlayer().setX(5 * 64);
			Registry.getPlayer().setY(2 * 64);
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
		if (step >= 0 && step <= 3 && KeyboardFilter.isKeyDown(Keyboard.KEY_RETURN)) {
			if (step == 3) {
				Registry.getPlayer().setX(5 * 64);
				Registry.getPlayer().setY(2 * 64);
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

			AnimatedText a = new AnimatedText("You can move using the WASD", Registry.getScreenWidth() / 2, yPos, 25, 100, 5000, -1);
			new AnimatedText("keys or the arrow keys.", Registry.getScreenWidth() / 2, yPos + 50, 25, 100, 5000, -1, a);

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

			Registry.getPlayer().renderable = true;
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

			Registry.getPlayer().renderable = false;
			// Registry.getPlayer().freeze();
			this.needUpdates = true;
		} else if (step == 4) {
			Enemy e1 = new Enemy(0, (3 * 64), 64, 64);
			e1.setTextureAtlas(Texture.Citizen, 3, 4, 1);
			e1.init(this);
			this.entities.add(e1);

			Enemy e2 = new Enemy(2 * 64, (2 * 64), 64, 124);
			e2.setTextureAtlas(Texture.Ogre, 3, 4, 1);
			e2.init(this);
			this.entities.add(e2);

			Enemy e3 = new Enemy(4 * 64, (1 * 64), 184, 184);
			e3.setTextureAtlas(Texture.Dragon, 3, 4, 1);
			e3.init(this);
			this.entities.add(e3);

			Enemy e4 = new Enemy(8 * 64, (2 * 64), 64, 124);
			e4.setTextureAtlas(Texture.Ogre, 3, 4, 1);
			e4.init(this);
			this.entities.add(e4);

			Enemy e5 = new Enemy(10 * 64, (2 * 64), 64, 124);
			e5.setTextureAtlas(Texture.Ogre, 3, 4, 1);
			e5.init(this);
			this.entities.add(e5);
		}
	}

}