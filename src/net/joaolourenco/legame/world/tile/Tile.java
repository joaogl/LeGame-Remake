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

package net.joaolourenco.legame.world.tile;

import java.util.*;

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.world.*;

import org.lwjgl.input.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Abstract Class for all the Tile Types
 * 
 * @author Joao Lourenco
 * 
 */
public abstract class Tile extends RenderableComponent {

	/**
	 * Is the tile collidable? Will the light end on the tile or keep spreading?
	 */
	protected boolean collidable = false, lightCollidable = false, lightAffected = true;
	/**
	 * Size and location of the tile.
	 */
	protected int width, height, x = 9999999, y = 9999999;
	/**
	 * Texture ID for the main and second tile texture.
	 */
	protected int tex, stex;
	/**
	 * Shader ID for the tiles.
	 */
	public Shader shade = new Shader(GeneralSettings.blockFragPath, GeneralSettings.defaultVertexPath);
	/**
	 * Is this tile double? (Uses two textures)
	 */
	protected boolean doubleTile = false;
	protected int rotation = 0;

	public Tile() {
	}

	/**
	 * Constructor for tiles with a different with and height.
	 * 
	 * @param width
	 *            : Width of the Tile.
	 * @param height
	 *            : Height of the Tile.
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @author Joao Lourenco
	 */
	public Tile(int width, int height, int tex) {
		this.width = width;
		this.height = height;
		this.tex = tex;
	}

	/**
	 * Constructor for square tiles.
	 * 
	 * @param size
	 *            : width and height of the Tile.
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @author Joao Lourenco
	 */
	public Tile(int size, int tex) {
		this.width = size;
		this.height = size;
		this.tex = tex;
	}

	/**
	 * Constructor for square tiles which are not affected by environment light.
	 * 
	 * @param size
	 *            : width and height of the Tile.
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @param lightable
	 *            : Is tile affected by environment light.
	 * @author Joao Lourenco
	 */
	public Tile(int size, int tex, boolean lightable) {
		this.width = size;
		this.height = size;
		this.tex = tex;
		this.lightAffected = lightable;
	}

	/**
	 * Update Method, called by the World class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) shade.recompile();
	}

	/**
	 * Method called by the World Class to render the Tile.
	 * 
	 * @param x
	 *            : location to where the Tile is going to be rendered.
	 * @param y
	 *            : location to where the Tile is going to be rendered.
	 * @param w
	 *            : instance of the World Class
	 * @param ent
	 *            : List of entities that emit light.
	 * @author Joao Lourenco
	 */
	public void render(int x, int y, World w, ArrayList<Entity> ent) {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ZERO);

		// Updating the Tile coordinates.
		this.x = x;
		this.y = y;

		// Binding the shader
		this.shade.bind();

		// Calculating the required light.
		float day_light = 1f;
		if (lightAffected) day_light = w.DAY_LIGHT;
		// Sending the required light to the shader.
		glUniform1f(glGetUniformLocation(shade.getShader(), "dayLight"), day_light * 2);

		// Rendering the Quad.
		render(x, y, tex, shade, this.rotation, true);
		if (this.doubleTile) render(x, y, stex, shade, this.rotation, true);

		// Disabling BLEND and releasing shader for next render.
		shade.release();
		glClear(GL_STENCIL_BUFFER_BIT);
		glDisable(GL_BLEND);
	}

	/**
	 * Method to return the Tiles vertices.
	 * 
	 * @return Vector2f[] with the vertices.
	 * @author Joao Lourenco
	 */
	public Vector2f[] getVertices() {
		return new Vector2f[] { new Vector2f(this.x, this.y), new Vector2f(this.x, this.y + this.height), new Vector2f(this.x + this.width, this.y + this.height), new Vector2f(this.x + this.width, this.y) };
	}

	/**
	 * Method to get if the Tile is Light Collidable or not.
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
	 * Method to define if the Tile is affected by Environment Light or not.
	 * 
	 * @param a
	 *            : true if its affected, false if its not.
	 * @author Joao Lourenco
	 */
	public void isLightAffected(boolean a) {
		this.lightAffected = a;
	}

	/**
	 * Method to get if the Tile is affected by Environment Light or not.
	 * 
	 * @return boolean, true if its affected, false if its not.
	 * @author Joao Lourenco
	 */
	public boolean isLightAffected() {
		return this.lightAffected;
	}

	/**
	 * Method to get if Entities will collide with the Tile.
	 * 
	 * @return boolean, true if they collide, false if they dont.
	 * @author Joao Lourenco
	 */
	public boolean isCollidable() {
		return this.collidable;
	}

	/**
	 * Method to define if the Entities will collide with the Tile.
	 * 
	 * @param a
	 *            : Boolean true if they collide, false if they dont.
	 * @author Joao Lourenco
	 */
	public void isCollidable(boolean a) {
		this.collidable = a;
	}

	/**
	 * Method to get the Tile X location.
	 * 
	 * @return int with the x coordinates.
	 * @author Joao Lourenco
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Method to get the Tile Y location.
	 * 
	 * @return int with the y coordinates.
	 * @author Joao Lourenco
	 */
	public int getY() {
		return this.y;
	}

	public void setSecondTexture(int stexture) {
		this.doubleTile = true;
		this.stex = tex;
		this.tex = stexture;
	}

	public void setRotation(int x) {
		this.rotation = x;
	}

	public abstract void entityOver(Entity e);

}