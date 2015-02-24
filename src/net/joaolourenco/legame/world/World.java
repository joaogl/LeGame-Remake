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

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;

import net.joaolourenco.legame.Main;
import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.entity.light.Light;
import net.joaolourenco.legame.entity.light.PointLight;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.font.AnimatedText;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.Vector2f;
import net.joaolourenco.legame.world.tile.SolidTile;
import net.joaolourenco.legame.world.tile.Tile;

/**
 * A class that handles all the world stuff.
 * 
 * @author Joao Lourenco
 * 
 */
public class World {

	/**
	 * Variable to hold the current DAY_LIGHT Light value.
	 */
	public float DAY_LIGHT = 1f;
	/**
	 * Array that stores all the entities on the world.
	 */
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	/**
	 * Array that stores all the Tiles on the world.
	 */
	public Tile[] worldTiles;
	/**
	 * Map with, height and the offset's for the map moving.
	 */
	private int width, height, xOffset, yOffset;
	/**
	 * Variable to keep track of the day rizing.
	 */
	protected boolean goingUp = false;

	/**
	 * World constructor to generate a new world.
	 * 
	 * @param width
	 *            : width of the world.
	 * @param height
	 *            : height of the world.
	 */
	public World(int width, int height) {
		// Setting up the variables
		this.width = width;
		this.height = height;
		this.xOffset = 0;
		this.yOffset = 0;
		this.height = height;
		this.worldTiles = new Tile[this.width * this.height];

		generateLevel();

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				SolidTile ti = new SolidTile(GeneralSettings.TILE_SIZE, Texture.Dirt, true);
				ti.isLightCollidable(false);
				setTile(x, y, ti);
			}
		}

		// Add a normal Tile:
		// setTile(9, 9, new SolidTile(GeneralSettings.TILE_SIZE,
		// Texture.Dirt));
		// Add a fire Tile:
		// setTile(0, 2, new FireTile(GeneralSettings.TILE_SIZE,
		// Texture.Fire[0], this));
		// Add a light:
		Vector2f location = new Vector2f((0 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2, (3 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2);
		PointLight l2 = new PointLight(location, (float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10, 0.8f);
		l2.init(this);
		this.entities.add(l2);

		location = new Vector2f((3 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2, (3 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2);
		l2 = new PointLight(location, (float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10, 0.8f);
		l2.init(this);
		this.entities.add(l2);
		// Add an Entity:
		// Block b = new Block(x, y, GeneralSettings.TILE_SIZE,
		// GeneralSettings.TILE_SIZE, false);
		// b.init(this);
		// this.entities.add(b);
		// Add Animated Text:
		new AnimatedText("Ola :D", 50, 5, 5);
	}

	/**
	 * 
	 */
	public void generateLevel() {

	}

	/**
	 * Method to get the world Height
	 * 
	 * @return int with the world Height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Method to render the entire world called by the Main Class.
	 */
	public void render() {
		// Moving the Render to the right position to render.
		glTranslatef(-this.xOffset, -this.yOffset, 0f);
		// Clearing the colors.
		glColor3f(1f, 1f, 1f);

		// Getting the variables ready to check what tiles to render.
		int x0 = this.xOffset >> GeneralSettings.TILE_SIZE_MASK;
		int x1 = (this.xOffset >> GeneralSettings.TILE_SIZE_MASK) + 14;
		int y0 = this.yOffset >> GeneralSettings.TILE_SIZE_MASK;
		int y1 = (this.yOffset >> GeneralSettings.TILE_SIZE_MASK) + 11;
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
			if (e != null && getDistance(e, Main.player) < 800) {
				if (e instanceof Light) {
					// If its a Light render it and its shadows.
					((Light) e).renderShadows(entities, worldTiles);
					((Light) e).render();
					// If not just render the Entity.
				} else e.render(getNearByLights(e.getX(), e.getY()));
			}
		}
		// Moving the Render back to the default position.
		glTranslatef(this.xOffset, this.yOffset, 0f);
	}

	/**
	 * Method to update everything called by the Main class 60 times per second.
	 */
	public void update() {
		// Updating all the entities.
		for (Entity e : this.entities)
			if (e != null) e.update();

		// Updating all the world tiles.
		for (Tile t : this.worldTiles)
			if (t != null) t.update();

		// Keep increasing and decreasing the Day Light value.
		if (this.DAY_LIGHT <= 0.1f) this.goingUp = true;
		else if (this.DAY_LIGHT >= 1.0f) this.goingUp = false;

		if (this.goingUp) this.DAY_LIGHT += 0.001f;
		else this.DAY_LIGHT -= 0.001f;
		this.DAY_LIGHT = 1.0f;
	}

	/**
	 * Method to tick everything called by the Main class once per second.
	 */
	public void tick() {
		// If an entity is removed remove it from the Array.
		for (Entity e : this.entities)
			if (e != null && e.isRemoved()) this.entities.remove(e);

		// Tick the entities.
		for (Entity e : this.entities)
			if (e != null) e.tick();
	}

	/**
	 * Method to the the Offset's.
	 * 
	 * @param xOffset
	 *            : int with the right x offset.
	 * @param yOffset
	 *            : int with the right y offset.
	 */
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Method to get x Offset.
	 * 
	 * @return int with the x Offset.
	 */
	public int getXOffset() {
		return this.xOffset;
	}

	/**
	 * Method to get y Offset.
	 * 
	 * @return int with the y Offset.
	 */
	public int getYOffset() {
		return this.yOffset;
	}

	/**
	 * Method to get the world Width
	 * 
	 * @return int with the world Width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Method to get the distance between two entities.
	 * 
	 * @param a
	 *            : First Entity.
	 * @param b
	 *            : Second Entity.
	 * @return double with the distance.
	 */
	public double getDistance(Entity a, Entity b) {
		return Math.sqrt(Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2));
	}

	/**
	 * Method to get the distance between two entities.
	 * 
	 * @param a
	 *            : First Entity.
	 * @param x
	 *            : Second Entity X.
	 * @param y
	 *            : Second Entity Y.
	 * @return double with the distance.
	 */
	public double getDistance(Entity a, float x, float y) {
		return Math.sqrt(Math.pow((x - a.getX()), 2) + Math.pow((y - a.getY()), 2));
	}

	/**
	 * Method to get the near by lights.
	 * 
	 * @param x
	 *            : x coordinates from where to search.
	 * @param y
	 *            : y coordinates from where to search.
	 * @return ArrayList with all the near by lights.
	 */
	public ArrayList<Entity> getNearByLights(float x, float y) {
		ArrayList<Entity> ent = new ArrayList<Entity>();

		for (Entity e : entities) {
			if (e instanceof Light && getDistance(e, x, y) < 800) ent.add(e);

		}

		return ent;
	}

	/**
	 * Method to the a Tile to a certain position.
	 * 
	 * @param x
	 *            : x location for the new Tile.
	 * @param y
	 *            : y location for the new Tile.
	 * @param tile
	 *            : the Tile that you want to be added.
	 */
	public void setTile(int x, int y, Tile tile) {
		this.worldTiles[x + y * this.width] = tile;
	}

	/**
	 * Method to add a new entity to the world.
	 * 
	 * @param ent
	 *            : Which entity to add.
	 */
	public void addEntity(Entity ent) {
		// Initialize the entity.
		ent.init(this);
		// Add the entity to the world.
		this.entities.add(ent);
	}

	/**
	 * Method to get the Tile at a certain location.
	 * 
	 * @param x
	 *            : x location to get the Tile from.
	 * @param y
	 *            : y location to get the Tile from.
	 * @return Tile at the specified location.
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return null;
		return this.worldTiles[x + y * this.width];
	}

}