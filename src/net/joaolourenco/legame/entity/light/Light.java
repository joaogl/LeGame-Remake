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

package net.joaolourenco.legame.entity.light;

import java.util.ArrayList;
import java.util.Random;

import net.joaolourenco.legame.Registry;
import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.graphics.Shader;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.Vector2f;
import net.joaolourenco.legame.world.tile.Tile;

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilOp;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;

/**
 * Abstract class for all the Light types.
 * 
 * @author Joao Lourenco
 * 
 */
public abstract class Light extends Entity {

	/**
	 * Light location.
	 */
	public Vector2f location;

	/**
	 * Red, Green and Blue is the light color. Intensity marks the lights intensity. Type 1 for PointLight. Type 2 for SpotLight. hasLightSpot is the setting that defines if the light has a strong center or a normal center effect. facing is the degrees to where the light is facing. size is the size of the light effect.
	 */
	public float red, green, blue, intensity, type, hasLightSpot, facing, size;

	/**
	 * Is the light on or off.
	 */
	protected boolean inAction = true;

	/**
	 * Shader ID for the Light.
	 */
	public Shader shade = new Shader(GeneralSettings.lightFragPath);

	/**
	 * Constructor for the normal light with color and location.
	 * 
	 * @param location
	 *            : Light location.
	 * @param red
	 *            : Light red color value.
	 * @param green
	 *            : Light green color value.
	 * @param blue
	 *            : Light blue color value.
	 * @author Joao Lourenco
	 */
	public Light(Vector2f location, float red, float green, float blue) {
		// Calling the super method with the location and size.
		super((int) location.x, (int) location.y, GeneralSettings.defaultLightPointSize, GeneralSettings.defaultLightPointSize);
		// Setting the variables
		this.location = location;
		this.red = red;
		this.green = green;
		this.blue = blue;
		// Because it doesnt have a intensity value on the constructor we are randomizing it.
		this.intensity = (float) (new Random().nextGaussian() / 5) * 3;
		// Setting the spot center to 1 which is the default, and also setting the type to 1 has the default light, Point Light.
		this.type = 1;
		this.hasLightSpot = 1;
		this.lightCollidable = false;
	}

	/**
	 * Constructor for the normal light with color, location and intensity.
	 * 
	 * @param location
	 *            : Light location.
	 * @param red
	 *            : Light red color value.
	 * @param green
	 *            : Light green color value.
	 * @param blue
	 *            : Light blue color value.
	 * @param inte
	 *            : Light Intensity
	 * @author Joao Lourenco
	 */
	public Light(Vector2f location, float red, float green, float blue, float inte) {
		super((int) location.x, (int) location.y, GeneralSettings.defaultLightPointSize, GeneralSettings.defaultLightPointSize);
		this.location = location;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.intensity = inte;
		this.hasLightSpot = 1;
		this.lightCollidable = false;
	}

	/**
	 * Method to update called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update(double delta) {
		if (this.facing <= 360) this.facing++;
		else this.facing = 0;
		this.size = 90;
	}

	/**
	 * Method to tick called by the World Class once per second.
	 * 
	 * @author Joao Lourenco
	 */
	public abstract void tick();

	/**
	 * Method to bind the Texture uniforms, this is what makes the shaders and stuff.
	 * 
	 * @author Joao Lourenco
	 */
	public void bindUniforms() {
		// Binding the shader program.
		this.shade.bind();
		// Getting the right light coordinates.S
		float xx = this.location.getX() - this.world.getXOffset();
		float yy = this.location.getY() - this.world.getYOffset();
		// Sending all the information from the light to the shader.
		glUniform1f(glGetUniformLocation(shade.getShader(), "lightInt"), this.intensity);
		glUniform2f(glGetUniformLocation(shade.getShader(), "lightLocation"), xx, Registry.getScreenHeight() - yy);
		glUniform3f(glGetUniformLocation(shade.getShader(), "lightColor"), this.red, this.green, this.blue);
		glUniform1f(glGetUniformLocation(shade.getShader(), "lightType"), this.type);
		glUniform1f(glGetUniformLocation(shade.getShader(), "lightCenter"), this.hasLightSpot);
		glUniform1f(glGetUniformLocation(shade.getShader(), "lightFacing"), this.facing);
		glUniform1f(glGetUniformLocation(shade.getShader(), "lightSize"), this.size);
	}

	/**
	 * Method called by the World Class to render the Light.
	 * 
	 * @author Joao Lourenco
	 */
	public void render() {
		// Binding the Uniforms to make the light effects.
		bindUniforms();

		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);

		// Drawing the QUAD.
		glBegin(GL_QUADS);
		{
			glVertex2f(this.world.getXOffset(), this.world.getYOffset());
			glVertex2f(this.world.getXOffset(), Registry.getScreenHeight() + this.world.getYOffset());
			glVertex2f(Registry.getScreenWidth() + this.world.getXOffset(), Registry.getScreenHeight() + this.world.getYOffset());
			glVertex2f(Registry.getScreenWidth() + this.world.getXOffset(), this.world.getYOffset());
		}
		glEnd();

		// Disabling BLEND and releasing shader for next render.
		glDisable(GL_BLEND);
		this.shade.release();
		glClear(GL_STENCIL_BUFFER_BIT);
	}

	/**
	 * The most important method for the Shadows.
	 * 
	 * @param entities
	 *            : all the entities to check for shadows cast.
	 * @param tiles
	 *            : all the tiles to check for shadows cast.
	 * @author Joao Lourenco
	 */
	public void renderShadows(ArrayList<Entity> entities, Tile[] tiles) {
		// Getting OpenGL ready for shadows render.
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

		// Run through all the entities to check if they need to cast shadows.
		for (Entity entity : entities) {
			// Is the entity light collidable.
			if (entity.isLightCollidable()) {
				// Get the entities vertices.
				Vector2f[][] vert = entity.getVertices();
				for (int n = 0; n < vert.length; n++) {
					Vector2f[] vertices = vert[n];
					// Go through all the vertices.
					for (int i = 0; i < vertices.length; i++) {
						// Setup the variables for the shaders calculations.
						Vector2f currentVertex = vertices[i];
						Vector2f nextVertex = vertices[(i + 1) % vertices.length];
						Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
						Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
						Vector2f lightToCurrent = Vector2f.sub(currentVertex, this.location, null);
						// Checking if there should be a cast.
						if (Vector2f.dot(normal, lightToCurrent) > 0) {
							// Adding two points for the cast.
							Vector2f point1 = Vector2f.add(currentVertex, (Vector2f) Vector2f.sub(currentVertex, this.location, null).scale(800), null);
							Vector2f point2 = Vector2f.add(nextVertex, (Vector2f) Vector2f.sub(nextVertex, this.location, null).scale(800), null);
							// Rendering the casts.
							glBegin(GL_QUADS);
							{
								glVertex2f(currentVertex.getX(), currentVertex.getY());
								glVertex2f(point1.getX(), point1.getY());
								glVertex2f(point2.getX(), point2.getY());
								glVertex2f(nextVertex.getX(), nextVertex.getY());
							}
							glEnd();
						}
					}
				}
			}
		}

		// Run through all the tiles to check if they need to cast shadows.
		for (Tile tile : tiles) {
			// Is the tile light collidable.
			if (tile != null && tile.isLightCollidable()) {
				// Get the tile vertices.
				Vector2f[] vertices = tile.getVertices();
				// Go through all the vertices.
				for (int i = 0; i < vertices.length; i++) {
					// Setup the variables for the shaders calculations.
					Vector2f currentVertex = vertices[i];
					Vector2f nextVertex = vertices[(i + 1) % vertices.length];
					Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
					Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
					Vector2f lightToCurrent = Vector2f.sub(currentVertex, this.location, null);
					// Checking if there should be a cast.
					if (Vector2f.dot(normal, lightToCurrent) > 0) {
						// Adding two points for the cast.
						Vector2f point1 = Vector2f.add(currentVertex, (Vector2f) Vector2f.sub(currentVertex, this.location, null).scale(800), null);
						Vector2f point2 = Vector2f.add(nextVertex, (Vector2f) Vector2f.sub(nextVertex, this.location, null).scale(800), null);
						// Rendering the casts.
						glBegin(GL_QUADS);
						{
							glVertex2f(currentVertex.getX(), currentVertex.getY());
							glVertex2f(point1.getX(), point1.getY());
							glVertex2f(point2.getX(), point2.getY());
							glVertex2f(nextVertex.getX(), nextVertex.getY());
						}
						glEnd();
					}
				}
			}
		}

		// Getting the OpenGL ready to go.
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		glStencilFunc(GL_EQUAL, 0, 1);
		glColorMask(true, true, true, true);
	}

	/**
	 * Method to set the Light x location.
	 * 
	 * @param x
	 *            : float with the x location.
	 * @author Joao Lourenco
	 */
	public void setX(float x) {
		this.location.x = x;
	}

	/**
	 * Method to set the Light y location.
	 * 
	 * @param y
	 *            : float with the y location.
	 * @author Joao Lourenco
	 */
	public void setY(float y) {
		this.location.y = y;
	}

	/**
	 * Method to get the Light x location.
	 * 
	 * @return int with the x coordinates.
	 * @author Joao Lourenco
	 */
	public int getX() {
		return (int) this.location.x;
	}

	/**
	 * Method to get the Light y location.
	 * 
	 * @return int with the y coordinates.
	 * @author Joao Lourenco
	 */
	public int getY() {
		return (int) this.location.y;
	}

	/**
	 * Method to get the Light Type.
	 * 
	 * @return float, 1 for PointLight, 2 for SpotLight.
	 * @author Joao Lourenco
	 */
	public float getType() {
		return this.type;
	}

	/**
	 * Method to get where's the Light facing.
	 * 
	 * @return float, degrees to where the Light is facing.
	 * @author Joao Lourenco
	 */
	public float getFacing() {
		return this.facing;
	}

	/**
	 * Method to get the Light size.
	 * 
	 * @return float, Light size.
	 * @author Joao Lourenco
	 */
	public float getSize() {
		return this.size;
	}

	/**
	 * Method to get the Light State.
	 * 
	 * @return boolean, Light state, on or off.
	 * @author Joao Lourenco
	 */
	public boolean getLightState() {
		return this.inAction;
	}

	/**
	 * Method to turn the Light off.
	 * 
	 * @author Joao Lourenco
	 */
	public void turnLightOff() {
		this.inAction = false;
	}

	/**
	 * Method to turn the Light on.
	 * 
	 * @author Joao Lourenco
	 */
	public void turnLightOn() {
		this.inAction = true;
	}

}