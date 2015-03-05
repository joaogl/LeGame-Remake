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
import net.joaolourenco.legame.world.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Class for all the Solid, this is, collidable Tiles.
 * 
 * @author Joao Lourenco
 * 
 */
public class MultiTile extends Tile {

	MultiData[] tiles;

	/**
	 * Constructor to create a square Tile.
	 * 
	 * @param tex
	 *            : Texture ID from the Texture class for the Tile.
	 * @author Joao Lourenco
	 */
	public MultiTile(List<MultiData> dat) {
		super();
		int s = dat.size();
		tiles = new MultiData[s];
		int i = 0;
		for (MultiData d : dat)
			tiles[i++] = d;
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
		for (int i = 0; i < this.tiles.length; i++) {
			render(this.tiles[i].xPos, this.tiles[i].yPos, this.tiles[i].fTextures, shade, this.tiles[i].rotations, true);
			if (this.tiles[i].doubleTiles) render(this.tiles[i].xPos, this.tiles[i].yPos, this.tiles[i].sTextures, shade, this.tiles[i].rotations, true);
		}

		// Disabling BLEND and releasing shader for next render.
		glDisable(GL_BLEND);
		shade.release();
		glClear(GL_STENCIL_BUFFER_BIT);
	}

	/**
	 * Method update called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		super.update();
	}

	public void setSecondTexture(int id, int stexture) {
		if (id >= 0 && id < this.tiles.length) {
			this.tiles[id].doubleTiles = true;
			this.tiles[id].sTextures = this.tiles[id].fTextures;
			this.tiles[id].fTextures = stexture;
		}
	}

}