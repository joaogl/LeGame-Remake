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

import static org.lwjgl.opengl.GL11.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.entity.light.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.utils.Timer;
import net.joaolourenco.legame.world.tile.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Tutorial extends World {

	boolean firstUpdate = false, needUpdates = false;
	List<TutorialText> text = new ArrayList<TutorialText>();

	/**
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Tutorial() {
		super(100, 100);

		Registry.getPlayer().setRenderable(false);

		new Timer("Map Loading", 2000, 1, new TimerResult(this) {
			public void timerCall() {
				World obj = (World) this.object;
				if (obj.finished) obj.stopLoading();

				obj.timerOver = true;
			}
		});
	}

	/**
	 * @see net.joaolourenco.legame.world.World#generateLevel()
	 * @author Joao Lourenco
	 */
	@Override
	public void generateLevel() {
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 2; x++) {
				SolidTile ti = new SolidTile(GeneralSettings.TILE_SIZE, Texture.Dirt, true);
				ti.isLightCollidable(false);
				setTile(x, y, ti);
			}
		}

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

		for (TutorialText t : text)
			t.render();
	}

	/**
	 * Method to update everything called by the Main class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		if (!firstUpdate || needUpdates) {
			// Updating all the entities.
			for (Entity e : this.entities) {
				if (e != null && getDistance(this.player, e) <= Registry.getScreenWidth()) e.update();
			}
			firstUpdate = true;
		}
		// Updating all the world tiles.
		for (Tile t : this.worldTiles)
			if (t != null && getDistance(this.player, t.getX(), t.getY()) <= Registry.getScreenWidth()) t.update();
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

		AnimatedText a = new AnimatedText("This game has a simple objective.", Registry.getScreenWidth() / 2, 100, 25, 100, 200, -1);
		new AnimatedText("Get to the end of each level.", Registry.getScreenWidth() / 2, 150, 25, 100, 200, -5, a);

		this.text.add(new TutorialText("End Mark", Registry.getScreenWidth() / 2, 400, 25));
		this.text.add(new TutorialText("Hit enter to continue.", 10, Registry.getScreenHeight() - 25, 18, false));

		new Timer("Tutorial-Step-1", 10000, 1, new TimerResult(this) {
			public void timerCall() {
				Tutorial obj = (Tutorial) this.object;
				
				obj.text.clear();
				Registry.clearAnimatedTexts();
			}
		});
	}

}