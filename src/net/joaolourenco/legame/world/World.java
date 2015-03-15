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

import javax.xml.parsers.*;
import javax.xml.transform.dom.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.entity.block.*;
import net.joaolourenco.legame.entity.light.*;
import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.utils.Timer;
import net.joaolourenco.legame.world.tile.*;

import org.w3c.dom.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * A class that handles all the world stuff.
 * 
 * @author Joao Lourenco
 * 
 */
public abstract class World {

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
	protected int width, height, xOffset, yOffset;
	/**
	 * Variable to keep track of the day rizing.
	 */
	protected boolean goingUp = false, gameOver = false, stopLoading = false;

	public boolean levelOver = false, levelEndable = false, updatesReady = false, ready = false;

	protected Player player;

	protected Menu loading;

	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return +1;
			if (n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};

	/**
	 * World constructor to generate a new world.
	 * 
	 * @param width
	 *            : width of the world.
	 * @param height
	 *            : height of the world.
	 * @author Joao Lourenco
	 */
	public World(int width, int height) {
		loading = new Loading();
		Registry.registerMenu(loading);
		preLoad();
		this.player = new Player(32, 32, 64, 64);
		Registry.registerPlayer(this.player);

		// Setting up the variables
		this.width = width;
		this.height = height;
		this.xOffset = 0;
		this.yOffset = 0;
		this.worldTiles = new Tile[this.width * this.height];

		this.player.init(this);
		this.addEntity(this.player);

		generateLevel();

		/*
		 * for (int y = 0; y < this.height; y++) { for (int x = 0; x < this.width; x++) { SolidTile ti = new SolidTile(GeneralSettings.TILE_SIZE, Texture.Dirt, true); ti.isLightCollidable(false); setTile(x, y, ti); } }
		 */

		// Add a normal Tile:
		// setTile(9, 9, new SolidTile(GeneralSettings.TILE_SIZE,
		// Texture.Dirt));
		// Add a fire Tile:
		// setTile(0, 2, new FireTile(GeneralSettings.TILE_SIZE,
		// Texture.Fire[0], this));
		// Add a light:
		/*
		 * Vector2f location = new Vector2f((0 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2, (3 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2); SpotLight l2 = new SpotLight(location, (float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10, 0.8f); l2.init(this);
		 */
		// this.entities.add(l2);

		/*
		 * location = new Vector2f((3 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2, (3 << GeneralSettings.TILE_SIZE_MASK) + GeneralSettings.TILE_SIZE / 2); PointLight l3 = new PointLight(location, (float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10, 0.8f); l3.init(this); this.entities.add(l3);
		 */

		// Add an Entity:
		// Block b = new Block(x, y, GeneralSettings.TILE_SIZE,
		// GeneralSettings.TILE_SIZE, false);
		// b.init(this);
		// this.entities.add(b);
		// Add Animated Text:
		// new AnimatedText("Ola :D td bem?", 50, 5, 18);
		/*
		 * Door door = new Door(2, 2, 128, 64); door.setTexture(Texture.Player); door.init(this); this.entities.add(door);
		 * 
		 * DoorKey key = new DoorKey(1, door.getKey()); Main.player.giveItem(key);
		 */
	}

	public void preLoad() {
		Texture.load();
	}

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public void generateLevel() {
		new Timer("Map Loading", 2000, 1, new TimerResult(this) {
			public void timerCall(String caller) {
				World obj = (World) this.object;
				obj.stopLoading = true;
			}
		});

		new Timer("Map Loading - Updates startup", 1500, 1, new TimerResult(this) {
			public void timerCall(String caller) {
				World obj = (World) this.object;
				obj.updatesReady = true;
			}
		});
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
		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
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
	}

	/**
	 * Method to update everything called by the Main class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update(double delta) {
		if (this.stopLoading) stopLoading();
		if (this.gameOver && this.ready) this.gameOver();
		if (!this.updatesReady) return;

		// Updating all the entities.
		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e != null && getDistance(this.player, e) <= Registry.getScreenWidth()) {
				e.update(delta);
				Tile tileOver = getTile(e.getTX(true), e.getTY(true));
				if (tileOver != null) tileOver.entityOver(e);
			}
		}

		if (!gameOver) {
			// Updating all the world tiles.
			for (Tile t : this.worldTiles)
				if (t != null && getDistance(this.player, t.getX(), t.getY()) <= Registry.getScreenWidth()) t.update();

			if (this.levelOver && this.levelEndable) {
				this.levelOver = false;
				this.levelEnd();
			}

			// Keep increasing and decreasing the Day Light value.
			if (this.DAY_LIGHT <= 0.1f) this.goingUp = true;
			else if (this.DAY_LIGHT >= 1.0f) this.goingUp = false;

			if (this.goingUp) this.DAY_LIGHT += 0.001f;
			else this.DAY_LIGHT -= 0.001f;
		} else {
			if (this.DAY_LIGHT <= 0.1f) {
				this.updatesReady = false;
				for (int i = 0; i < this.entities.size(); i++) {
					Entity e = this.entities.get(i);
					e.renderable = false;
				}

				AnimatedText a = new AnimatedText("Game Over!", Registry.getScreenWidth() / 2, Registry.getScreenHeight() / 2, 40);
				new Timer("World-GameOver", (int) (250 + a.getTotalTiming()), 1, new TimerResult(this) {
					public void timerCall(String caller) {
						World obj = (World) this.object;
						obj.ready = true;
					}
				});
			} else this.DAY_LIGHT -= 0.01f;
		}
	}

	public abstract void levelEnd();

	public abstract void gameOver();

	/**
	 * Method to tick everything called by the Main class 10 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void tick() {
		// If an entity is removed remove it from the Array.
		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e != null && e.isRemoved()) this.entities.remove(e);
		}

		// Tick the entities.
		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e != null) e.tick();
		}
	}

	/**
	 * Method to the the Offset's.
	 * 
	 * @param xOffset
	 *            : int with the right x offset.
	 * @param yOffset
	 *            : int with the right y offset.
	 * @author Joao Lourenco
	 */
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Method to get x Offset.
	 * 
	 * @return int with the x Offset.
	 * @author Joao Lourenco
	 */
	public int getXOffset() {
		return this.xOffset;
	}

	/**
	 * Method to get y Offset.
	 * 
	 * @return int with the y Offset.
	 * @author Joao Lourenco
	 */
	public int getYOffset() {
		return this.yOffset;
	}

	/**
	 * Method to get the world Width
	 * 
	 * @return int with the world Width
	 * @author Joao Lourenco
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Method to get the world Height
	 * 
	 * @return int with the world Height
	 * @author Joao Lourenco
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Method to get the distance between two entities.
	 * 
	 * @param a
	 *            : First Entity.
	 * @param b
	 *            : Second Entity.
	 * @return double with the distance.
	 * @author Joao Lourenco
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
	 * @author Joao Lourenco
	 */
	public double getDistance(Entity a, float x, float y) {
		return Math.sqrt(Math.pow((x - (a.getX() + (a.getWidth() / 2))), 2) + Math.pow((y - (a.getY() + (a.getHeight() / 2))), 2));
	}

	/**
	 * Method to get the near by lights.
	 * 
	 * @param x
	 *            : x coordinates from where to search.
	 * @param y
	 *            : y coordinates from where to search.
	 * @return ArrayList with all the near by lights.
	 * @author Joao Lourenco
	 */
	public ArrayList<Entity> getNearByLights(float x, float y) {
		ArrayList<Entity> ent = new ArrayList<Entity>();

		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e instanceof Light && getDistance(e, x, y) < 800) ent.add(e);
		}

		return ent;
	}

	/**
	 * Method to get the near by entities.
	 * 
	 * @param x
	 *            : x coordinates from where to search.
	 * @param y
	 *            : y coordinates from where to search.
	 * @return ArrayList with all the near by entities.
	 * @author Joao Lourenco
	 */
	public ArrayList<Entity> getNearByEntities(float x, float y, float radius) {
		ArrayList<Entity> ent = new ArrayList<Entity>();

		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e instanceof Entity && getDistance(e, x, y) < radius) ent.add(e);
		}

		return ent;
	}

	/**
	 * Method to get the nearest door.
	 * 
	 * @param x
	 *            : x coordinates from where to search.
	 * @param y
	 *            : y coordinates from where to search.
	 * @return The nearest door.
	 * @author Joao Lourenco
	 */
	public Door getNearByDoor(float x, float y, int radius) {
		Door door = null;
		double distance = 999999;

		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			double d = getDistance(e, x, y);
			if (e instanceof Door && d < radius && d < distance) {
				door = (Door) e;
				distance = d;
			}
		}

		return door;
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
	 * @author Joao Lourenco
	 */
	public void setTile(int x, int y, Tile tile) {
		if (x + y * this.width < this.worldTiles.length) this.worldTiles[x + y * this.width] = tile;
	}

	/**
	 * Method to add a new entity to the world.
	 * 
	 * @param ent
	 *            : Which entity to add.
	 * @author Joao Lourenco
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
	 * @author Joao Lourenco
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return null;
		return this.worldTiles[x + y * this.width];
	}

	public void stopLoading() {
		this.stopLoading = false;
		this.loading.remove();
		Registry.focusGame();
		this.levelEndable = true;
		if (Registry.isGameReloading()) {
			Registry.registerFinishedGameReload();
			Registry.registerMenu(new MainMenu());
		}
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		this.worldTiles = new Tile[this.width * this.height];
	}

	public List<Node> findPath(Vector2f start, Vector2f goal) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, getDistance(start, goal));
		openList.add(current);
		while (openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(goal)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				float x = current.tile.getX();
				float y = current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile((int) x + xi, (int) y + yi);
				if (at == null) continue;
				if (at.isCollidable()) continue;
				Vector2f a = new Vector2f(x + xi, y + yi);
				double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95);
				double hCost = getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= node.gCost) continue;
				if (!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}

	private boolean vecInList(List<Node> list, Vector2f vector) {
		for (Node n : list) {
			if (n.tile.equals(vector)) return true;
		}
		return false;
	}

	public double getDistance(Vector2f t1, Vector2f t2) {
		float dx = t1.x - t2.x;
		float dy = t1.y - t2.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public DOMSource saveWorld() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// Root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("world");
			doc.appendChild(rootElement);

			// Tiles
			for (Tile t : worldTiles) {
				Element entityElement = doc.createElement("tile");
				entityElement.setAttribute("x", String.valueOf(t.getX()));
				entityElement.setAttribute("y", String.valueOf(t.getY()));
				if (t instanceof SolidTile) entityElement.setAttribute("type", "solidtile");
				else if (t instanceof SolidTile) entityElement.setAttribute("type", "solidtile");
				rootElement.appendChild(entityElement);
			}

			// Entities
			for (Entity e : entities) {
				Element entityElement = doc.createElement("entity");
				if (e == Registry.getPlayer()) entityElement = doc.createElement("entityPlayer");
				entityElement.setAttribute("x", String.valueOf(e.getX()));
				entityElement.setAttribute("y", String.valueOf(e.getY()));
				rootElement.appendChild(entityElement);
			}

			System.out.println("DOM stored!");

			return new DOMSource(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

}