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

package net.joaolourenco.legame.entity.block;

import java.util.*;

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.items.*;
import net.joaolourenco.legame.utils.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Door extends Entity {

	public static enum States {
		CLOSED, CLOSING, OPEN, OPENING
	}

	public States state;
	public String key;
	public boolean usesKey, locked = false, jammed;
	public int DoorSize = 64;
	public float DoorGap = 0, MaxDoorGap = 70, DoorRadius = 300;
	public boolean alongXAxis = true, autoOpen = true, autoClose = true, OverrideStatus = false;

	public int texture;

	/**
	 * Method to create a new Door.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Door(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.key = "DOOR(" + x + "," + y + ")";
		this.state = States.CLOSED;
		usesKey = false;
		this.collidable = true;
		this.locked = true;
	}

	/**
	 * Method to get the current door state.
	 * 
	 * @return The door state in States format.
	 * @author Joao Lourenco
	 */
	public States getState() {
		return state;
	}

	/**
	 * Method to set the current door state.
	 * 
	 * @param _state
	 *            : Door state in States format.
	 * @author Joao Lourenco
	 */
	public void setState(States _state) {
		state = _state;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param key
	 *            The key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The usesKey
	 */
	public boolean isUsesKey() {
		return usesKey;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param usesKey
	 *            The usesKey to set
	 */
	public void setUsesKey(boolean usesKey) {
		this.usesKey = usesKey;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param locked
	 *            The locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The jammed
	 */
	public boolean isJammed() {
		return jammed;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The doorSize
	 */
	public int getDoorSize() {
		return DoorSize;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param doorSize
	 *            The doorSize to set
	 */
	public void setDoorSize(int doorSize) {
		DoorSize = doorSize;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The alongXAxis
	 */
	public boolean isAlongXAxis() {
		return alongXAxis;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param alongXAxis
	 *            The alongXAxis to set
	 */
	public void setAlongXAxis(boolean alongXAxis) {
		this.alongXAxis = alongXAxis;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The texture
	 */
	public int getTexture() {
		return texture;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param texture
	 *            The texture to set
	 */
	public void setTexture(int texture) {
		this.texture = texture;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param jammed
	 *            The jammed to set
	 */
	public void setJammed(boolean jammed) {
		this.jammed = jammed;
	}

	public DoorKey createKey(int uses) {
		if (uses == 0) return new DoorKey(key);
		else return new DoorKey(key);
	}

	public boolean addDoorToKey(DoorKey key) {
		if (key == null) return false;
		key.addDoor(this.key);
		return true;
	}

	public void render() {
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

		// Rendering the Quad.
		if (this.alongXAxis) {
			render(x - (DoorGap / 2), y, texture, shade, DoorSize);
			render(x + (DoorGap / 2) + DoorSize, y, texture, shade, DoorSize);
		} else {
			render(x, y + (DoorGap / 2) + DoorSize, texture, shade, DoorSize);
			render(x, y - (DoorGap / 2), texture, shade, DoorSize);
		}

		// Disabling BLEND and releasing shader for next render.
		glDisable(GL_BLEND);
		shade.release();
		glClear(GL_STENCIL_BUFFER_BIT);
	}

	/**
	 * Method called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void update() {
		if (this.state == States.OPENING) {
			if (DoorGap < MaxDoorGap) DoorGap += 1f;
			else this.state = States.OPEN;
		} else if (this.state == States.CLOSING) {
			if (DoorGap > 0) DoorGap -= 1f;
			else this.state = States.CLOSED;
		}
	}

	public void closeDoor() {
		if (this.state == States.OPEN && (this.state != States.CLOSED && this.state != States.CLOSING) || this.state == States.OPENING && (this.state != States.CLOSED && this.state != States.CLOSING)) this.state = States.CLOSING;
		OverrideStatus = true;
	}

	public void openDoor() {
		if (this.state == States.CLOSED && (this.state != States.OPEN && this.state != States.OPENING) || this.state == States.CLOSING && (this.state != States.OPEN && this.state != States.OPENING)) this.state = States.OPENING;
		OverrideStatus = true;
	}

	public void ativateDoor(Entity activator) {
		Item i = activator.hasKeyForDoor(this);
		if (i != null || !this.locked) {
			if (this.state == States.CLOSED || this.state == States.CLOSING) openDoor();
			else if (this.state == States.OPEN || this.state == States.OPENING) closeDoor();
			if (i != null) i.use(activator);
		} else failedToOpenDoor();
	}

	public void failedToOpenDoor() {
		new AnimatedText("Failed to open Door!", 50, 55, 12, 5, 50);
	}

	/**
	 * Method called by the World Class once per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void tick() {
		if (autoClose || autoOpen) {
			List<Entity> aroundDoor = this.world.getNearByEntities(x, y, this.DoorRadius);
			if (aroundDoor.isEmpty() && autoClose) {
				closeDoor();
				OverrideStatus = false;
			} else {
				if (!this.locked && autoOpen && !OverrideStatus) {
					openDoor();
					OverrideStatus = false;
				}
			}
		}
	}

	/**
	 * Method to return the Door vertices.
	 * 
	 * @return Vector2f[] with the vertices.
	 * @author Joao Lourenco
	 */
	public Vector2f[][] getVertices() {
		if (this.alongXAxis) {
			Vector2f[] d1 = new Vector2f[] { //
			//
					new Vector2f(this.x - (this.DoorGap / 2), this.y), //
					new Vector2f(this.x - (this.DoorGap / 2), this.y + this.DoorSize), //
					new Vector2f(this.x - (this.DoorGap / 2) + this.DoorSize, this.y + this.DoorSize), //
					new Vector2f(this.x - (this.DoorGap / 2) + this.DoorSize, this.y) //
			};
			Vector2f[] d2 = new Vector2f[] { //
			//
					new Vector2f(this.x + (this.DoorGap / 2) + this.DoorSize, this.y), //
					new Vector2f(this.x + (this.DoorGap / 2) + this.DoorSize, this.y + this.DoorSize), //
					new Vector2f(this.x + (this.DoorGap / 2) + this.DoorSize + this.DoorSize, this.y + this.DoorSize), //
					new Vector2f(this.x + (this.DoorGap / 2) + this.DoorSize + this.DoorSize, this.y) //
			};
			return new Vector2f[][] { d1, d2 };
		} else {
			Vector2f[] d1 = new Vector2f[] { //
			//
					new Vector2f(this.x, this.y + (this.DoorGap / 2) + this.DoorSize), //
					new Vector2f(this.x + this.DoorSize, this.y + (this.DoorGap / 2) + this.DoorSize), //
					new Vector2f(this.x + this.DoorSize, this.y + (this.DoorGap / 2) + this.DoorSize + this.DoorSize), //
					new Vector2f(this.x, this.y + (this.DoorGap / 2) + this.DoorSize + this.DoorSize) //
			};
			Vector2f[] d2 = new Vector2f[] { //
			//
					new Vector2f(this.x, this.y - (this.DoorGap / 2)), //
					new Vector2f(this.x + this.DoorSize, this.y - (this.DoorGap / 2)), //
					new Vector2f(this.x + this.DoorSize, this.y - (this.DoorGap / 2) + this.DoorSize), //
					new Vector2f(this.x, this.y - (this.DoorGap / 2) + this.DoorSize) //
			};
			return new Vector2f[][] { d1, d2 };
		}
	}
}