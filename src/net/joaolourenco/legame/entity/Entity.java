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

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.world.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Abstract Class for all the Entity Types
 * 
 * @author Joao Lourenco
 * 
 */
public abstract class Entity {

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
	 */
	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Method called by the World Class 60 times per second.
	 */
	public abstract void update();

	/**
	 * Method called by the World Class once per second.
	 */
	public abstract void tick();

	/**
	 * Method called by the World Class to render the Entity.
	 * 
	 * @param ent
	 *            : List of entities that emit light.
	 */
	public void render(ArrayList<Entity> ent) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ZERO);

		// Binding the shader
		this.shade.bind();

		// Calculating the required light.
		float day_light = 1f;
		if (this.lightAffected) day_light = this.world.DAY_LIGHT;
		// Sending the required light to the shader.
		glUniform1f(glGetUniformLocation(shade.getShader(), "dayLight"), day_light * 2);

		// Updating the Entity coordinates.
		glTranslatef(x, y, 0);
		// Activating and Binding the Tile Texture.
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, this.texture);
		// Sending the texture to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "texture"), 0);

		// Drawing the QUAD.
		glBegin(GL_QUADS);
		{
			// Each vertice of the Quad
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			// Each vertice of the Quad
			glTexCoord2f(0, 1);
			glVertex2f(0, this.height);

			// Each vertice of the Quad
			glTexCoord2f(1, 1);
			glVertex2f(this.width, this.height);

			// Each vertice of the Quad
			glTexCoord2f(1, 0);
			glVertex2f(this.width, 0);
		}
		glEnd();
		// Releasing the Texture.
		glBindTexture(GL_TEXTURE_2D, 0);
		// Getting the location back to the inicial coordinates.
		glTranslatef(-x, -y, 0);

		// Disabling BLEND and releasing shader for next render.
		glDisable(GL_BLEND);
		shade.release();
		glClear(GL_STENCIL_BUFFER_BIT);
	}

	/**
	 * Method to return the Enity vertices.
	 * 
	 * @return Vector2f[] with the vertices.
	 */
	public Vector2f[] getVertices() {
		return new Vector2f[] { new Vector2f(this.x, this.y), new Vector2f(this.x, this.y + this.height), new Vector2f(this.x + this.width, this.y + this.height), new Vector2f(this.x + this.width, this.y) };
	}

	/**
	 * Method to check if the Entity has been removed or not.
	 * 
	 * @return boolean, true if has been removed, false if hasn't.
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * Method to remove an Entity from the world.
	 */
	public void remove() {
		removed = true;
	}

	/**
	 * Method to initialize a Entity on the world.
	 * 
	 * @param world
	 *            : Instance of the world class.
	 */
	public void init(World world) {
		this.world = world;
	}

	/**
	 * Method to get the moving speed of an Entity.
	 * 
	 * @param running
	 *            : Boolean true if they are running, false if they are not.
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
	 */
	public int getX() {
		return (int) this.x;
	}

	/**
	 * Method to get the current Entity y coordinates.
	 * 
	 * @return int with the position.
	 */
	public int getY() {
		return (int) this.y;
	}

	/**
	 * Method to change the Entity's x coordinates.
	 * 
	 * @param x
	 *            : float with the new coordinates.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Method to change the Entity's y coordinates.
	 * 
	 * @param y
	 *            : float with the new coordinates.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Method to get if the Entity is Light Collidable or not.
	 * 
	 * @return boolean, true if its collidable, false if its not.
	 */
	public boolean isLightCollidable() {
		return this.lightCollidable;
	}

	/**
	 * Method to define if the Light is Collidable or not.
	 * 
	 * @param a
	 *            : true if its collidable, false if its not.
	 */
	public void isLightCollidable(boolean a) {
		this.lightCollidable = a;
	}

	/**
	 * Method to get collision property.
	 * 
	 * @return boolean, true if they collide, false if they dont.
	 */
	public boolean isCollidable() {
		return this.collidable;
	}

	/**
	 * Method to define the collision property.
	 * 
	 * @param a
	 *            : Boolean true if they collide, false if they dont.
	 */
	public void isCollidable(boolean a) {
		this.collidable = a;
	}

}