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

package net.joaolourenco.legame.entity;

import java.util.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.entity.block.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.items.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.Vector2f;
import net.joaolourenco.legame.world.*;

import org.lwjgl.util.vector.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Abstract Class for all the Entity Types
 * 
 * @author Joao Lourenco
 * 
 */
public abstract class Entity extends RenderableComponent {

	/**
	 * Size and location of the Entity.
	 */
	protected float x, y, width, height;
	/**
	 * Texture ID for the Entity.
	 */
	protected int texture;
	/**
	 * Instance of the World class.
	 */
	protected World world;
	/**
	 * Boolean to remove the entity from the world.
	 */
	private boolean removed = false;
	/**
	 * Random is always needed in entities.
	 */
	protected Random random = new Random();
	/**
	 * Is the Entity collidable? Will the light end on the Entity or keep spreading?
	 */
	protected boolean collidable = true, lightCollidable = true, lightAffected = true;
	/**
	 * Shader ID for the entity.
	 */
	public Shader shade = new Shader(GeneralSettings.blockFragPath, GeneralSettings.defaultVertexPath);
	/**
	 * Array o store all the Items in the entityies inventory.
	 */
	public List<Item> inventory = new ArrayList<Item>();
	/**
	 * This tells the game if this entity is going to be rendered or not.
	 */
	public boolean renderable = true;
	protected boolean dying = false;
	protected int time, rate = 10;
	protected float life = 100;
	protected boolean renderHealthBar = false;

	/**
	 * Constructor for the Entities.
	 * 
	 * @param x
	 *            : coordinates to the entity position.
	 * @param y
	 *            : coordinates to the entity position.
	 * @param width
	 *            : entity width.
	 * @param height
	 *            : entity height.
	 * @author Joao Lourenco
	 */
	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Method called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public abstract void update(double delta);

	/**
	 * Method called by the World Class 10 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public abstract void tick();

	/**
	 * Method called by the World Class to render the Entity.
	 * 
	 * @author Joao Lourenco
	 */
	public void render() {
		if (this.renderable) {
			if (this.dying) {
				time++;
				if (time % rate != 0) return;
			}

			if (renderHealthBar && !this.dying) {
				render(x + (this.width / 4), y - 15, 0, shade, (this.width / 2), 5, new Vector4f(0.0f, 0.0f, 0.0f, 1f));

				float w = this.life * (this.width / 2) / 100;
				if (this.life <= 0) w = 0 * (this.width / 2) / 100;
				render(x + (this.width / 4), y - 15, 0, shade, w, 5, new Vector4f(0.0f, 0.6f, 0.0f, 0.5f));
			}

			// Setting up OpenGL for render
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ZERO);

			// Binding the shader
			this.shade.bind();

			// Calculating the required light.
			float day_light = 1f;
			if (lightAffected) day_light = world.DAY_LIGHT;
			// Sending the required light to the shader.
			glUniform1f(glGetUniformLocation(shade.getShader(), "dayLight"), day_light * 2);

			// Releasing the shader
			shade.release();
			if (renderHealthBar && !this.dying) {
				render(x + (this.width / 4), y - 15, 0, shade, (this.width / 2), 5, new Vector4f(0.0f, 0.0f, 0.0f, 1f));

				float w = this.life * (this.width / 2) / 100;
				if (this.life <= 0) w = 0 * (this.width / 2) / 100;
				render(x + (this.width / 4), y - 15, 0, shade, w, 5, new Vector4f(0.0f, 0.6f, 0.0f, 0.5f));
			}
			// Binding the shader
			this.shade.bind();

			// Rendering the Quad.
			render(x, y, texture, shade, width, height);

			// Disabling BLEND and releasing shader for next render.
			glDisable(GL_BLEND);
			shade.release();
			glClear(GL_STENCIL_BUFFER_BIT);
		}
	}

	/**
	 * Method to return the Enity vertices.
	 * 
	 * @return Vector2f[] with the vertices.
	 * @author Joao Lourenco
	 */
	public Vector2f[][] getVertices() {
		return new Vector2f[][] { { new Vector2f(this.x, this.y), new Vector2f(this.x, this.y + this.height), new Vector2f(this.x + this.width, this.y + this.height), new Vector2f(this.x + this.width, this.y) } };
	}

	/**
	 * Method to check if the Entity has been removed or not.
	 * 
	 * @return boolean, true if has been removed, false if hasn't.
	 * @author Joao Lourenco
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * Method to remove an Entity from the world.
	 * 
	 * @author Joao Lourenco
	 */
	public void remove() {
		removed = true;
	}

	/**
	 * Method to initialize a Entity on the world.
	 * 
	 * @param world
	 *            : Instance of the world class.
	 * @author Joao Lourenco
	 */
	public void init(World world) {
		this.world = world;
	}

	/**
	 * Method to get the moving speed of an Entity.
	 * 
	 * @param running
	 *            : Boolean true if they are running, false if they are not.
	 * @author Joao Lourenco
	 */
	public float getSpeed(boolean running) {
		float speed = 0;
		if (running) speed = GeneralSettings.defaultEntityRunning;
		else speed = GeneralSettings.defaultEntityWalking;
		return speed;
	}

	/**
	 * Method to get the current Entity x coordinates.
	 * 
	 * @return int with the position.
	 * @author Joao Lourenco
	 */
	public int getX() {
		return (int) this.x;
	}

	public int getTX() {
		return (int) this.x >> GeneralSettings.TILE_SIZE_MASK;
	}

	/**
	 * Method to get the current Entity y coordinates.
	 * 
	 * @return int with the position.
	 * @author Joao Lourenco
	 */
	public int getY() {
		return (int) this.y;
	}

	public int getTY() {
		return (int) this.y >> GeneralSettings.TILE_SIZE_MASK;
	}

	/**
	 * Method to change the Entity's x coordinates.
	 * 
	 * @param x
	 *            : float with the new coordinates.
	 * @author Joao Lourenco
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Method to change the Entity's y coordinates.
	 * 
	 * @param y
	 *            : float with the new coordinates.
	 * @author Joao Lourenco
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Method to get if the Entity is Light Collidable or not.
	 * 
	 * @return boolean, true if its collidable, false if its not.
	 * @author Joao Lourenco
	 */
	public boolean isLightCollidable() {
		return this.lightCollidable;
	}

	/**
	 * Method to define if the Light is Collidable or not.
	 * 
	 * @param a
	 *            : true if its collidable, false if its not.
	 * @author Joao Lourenco
	 */
	public void isLightCollidable(boolean a) {
		this.lightCollidable = a;
	}

	/**
	 * Method to get collision property.
	 * 
	 * @return boolean, true if they collide, false if they dont.
	 * @author Joao Lourenco
	 */
	public boolean isCollidable() {
		return this.collidable;
	}

	/**
	 * Method to define the collision property.
	 * 
	 * @param a
	 *            : Boolean true if they collide, false if they dont.
	 * @author Joao Lourenco
	 */
	public void isCollidable(boolean a) {
		this.collidable = a;
	}

	/**
	 * @param item
	 * @author Joao Lourenco
	 */
	public void removeItem(Item item) {
		inventory.remove(item);
	}

	public Item hasKeyForDoor(Door door) {
		for (Item i : inventory)
			if (i instanceof DoorKey && ((DoorKey) i).canOpenDoor(door)) return i;

		return null;
	}

	public void giveItem(Item i) {
		this.inventory.add(i);
	}

	public boolean isRenderable() {
		return renderable;
	}

	public void setRenderable(boolean r) {
		this.renderable = r;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setSize(float x, float y) {
		this.width = x;
		this.height = y;
	}

	public void setLocationAndCenter(int x, int y) {
		this.x = x;
		this.y = y;

		int xOff = (int) ((this.x + (this.width / 2)) - Registry.getScreenWidth() / 2);
		int yOff = (int) (this.y + (this.height / 2) - Registry.getScreenHeight() / 2);
		this.world.setOffset(xOff, yOff);
	}

	public World getWorld() {
		return this.world;
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
	 * @author Joao Lourenco
	 */
	public Object generateRandom(float min, float max, int type) {
		// This method accepts two types of returns, 0 for Ints and 1 for Floats.
		if (type == 0) {
			// Generate an int random.
			Random rand = new Random();
			int out = rand.nextInt((int) max);
			// if its out of the bounds, keep trying.
			while (out > max || out < min)
				out = rand.nextInt((int) max);
			// return the random value.
			return out;
		} else if (type == 1) {
			// Generate an float random.
			Random rand = new Random();
			double out = min + (max - min) * rand.nextDouble();
			// if its out of the bounds, keep trying.
			while (out > max || out < min)
				out = min + (max - min) * rand.nextDouble();
			// return the random value.
			return (float) out;
		}
		return 0f;
	}

}