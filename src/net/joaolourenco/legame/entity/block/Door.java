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

import java.util.List;

import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.entity.mob.Mob;
import net.joaolourenco.legame.graphics.font.AnimatedText;
import net.joaolourenco.legame.items.DoorKey;
import net.joaolourenco.legame.items.Item;
import net.joaolourenco.legame.utils.Vector2f;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;

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
	public int texture2;

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
		super(x + (width / 4), y + (height / 2), width, height);
		this.key = "DOOR(" + x + "," + y + ")";
		this.state = States.CLOSED;
		usesKey = false;
		this.collidable = true;
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
	 * Method to get the current Entity x coordinates.
	 * 
	 * @return int with the position.
	 * @author Joao Lourenco
	 */
	public int getX() {
		return (int) (this.x - (this.width / 4));
	}

	/**
	 * Method to get the current Entity y coordinates.
	 * 
	 * @return int with the position.
	 * @author Joao Lourenco
	 */
	public int getY() {
		return (int) (this.y - (this.height / 2));
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param texture
	 *            The texture to set
	 */
	public void setTexture(int[] texture) {
		this.texture = texture[0];
		this.texture2 = texture[1];
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
			render(x - (DoorGap / 2), getY(), texture, shade, DoorSize);
			render(x + (DoorGap / 2) + DoorSize, getY(), texture2, shade, DoorSize);
		} else {
			render(x, y + (DoorGap / 2) + DoorSize, texture2, shade, DoorSize, DoorSize, 90);
			render(x, y - (DoorGap / 2), texture, shade, DoorSize, DoorSize, 90);
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
		float pX0, pY0, pX1, pY3;

		Vector2f[][] doorV = this.getVertices();

		if (this.alongXAxis) {
			pX1 = doorV[0][3].x;
			pX0 = doorV[1][3].x;

			pY0 = doorV[1][0].y;
			pY3 = doorV[1][2].y;
		} else {
			pX1 = doorV[0][0].x;
			pX0 = doorV[1][2].x;

			pY0 = doorV[1][3].y;
			pY3 = doorV[0][3].y;
		}

		List<Entity> ent = this.world.getNearByEntities(this.getX(), this.getY(), 160);
		boolean clear = true;

		for (Entity e : ent) {
			if (e.isCollidable() && e instanceof Mob) {
				Vector2f[][] v = e.getVertices();
				for (int n = 0; n < v.length; n++) {
					Vector2f[] vertices = v[n];

					if ((vertices[0].x > pX1 && vertices[0].x < pX0 || vertices[3].x > pX1 && vertices[3].x < pX0) && ((vertices[3].y + 25) > pY0 && (vertices[3].y + 25) < pY3 || vertices[2].y > pY0 && vertices[2].y < pY3)) clear = false;
				}
			}
		}

		if (clear) {
			if (this.state == States.OPEN && (this.state != States.CLOSED && this.state != States.CLOSING) || this.state == States.OPENING && (this.state != States.CLOSED && this.state != States.CLOSING)) this.state = States.CLOSING;
			OverrideStatus = true;
		}
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
					new Vector2f(this.getX() + (this.DoorSize / 2) - (this.DoorGap / 2), this.getY()), //
					new Vector2f(this.getX() + (this.DoorSize / 2) - (this.DoorGap / 2), this.getY() + this.DoorSize), //
					new Vector2f(this.getX() + (this.DoorSize / 2) - (this.DoorGap / 2) + this.DoorSize, this.getY() + this.DoorSize), //
					new Vector2f(this.getX() + (this.DoorSize / 2) - (this.DoorGap / 2) + this.DoorSize, this.getY()) //
			};
			Vector2f[] d2 = new Vector2f[] { //
			//
					new Vector2f(this.getX() + (this.DoorSize / 2) + (this.DoorGap / 2) + this.DoorSize, this.getY()), //
					new Vector2f(this.getX() + (this.DoorSize / 2) + (this.DoorGap / 2) + this.DoorSize, this.getY() + this.DoorSize), //
					new Vector2f(this.getX() + (this.DoorSize / 2) + (this.DoorGap / 2) + this.DoorSize + this.DoorSize, this.getY() + this.DoorSize), //
					new Vector2f(this.getX() + (this.DoorSize / 2) + (this.DoorGap / 2) + this.DoorSize + this.DoorSize, this.getY()) //
			};
			return new Vector2f[][] { d1, d2 };
		} else {
			Vector2f[] d1 = new Vector2f[] { //
			//
					new Vector2f(this.getX(), this.getY() + (this.DoorGap / 2) + this.DoorSize), //
					new Vector2f(this.getX() + this.DoorSize, this.getY() + (this.DoorGap / 2) + this.DoorSize), //
					new Vector2f(this.getX() + this.DoorSize, this.getY() + (this.DoorGap / 2) + this.DoorSize + this.DoorSize), //
					new Vector2f(this.getX(), this.getY() + (this.DoorGap / 2) + this.DoorSize + this.DoorSize) //
			};
			Vector2f[] d2 = new Vector2f[] { //
			//
					new Vector2f(this.getX(), this.getY() - (this.DoorGap / 2)), //
					new Vector2f(this.getX() + this.DoorSize, this.getY() - (this.DoorGap / 2)), //
					new Vector2f(this.getX() + this.DoorSize, this.getY() - (this.DoorGap / 2) + this.DoorSize), //
					new Vector2f(this.getX(), this.getY() - (this.DoorGap / 2) + this.DoorSize) //
			};
			return new Vector2f[][] { d1, d2 };
		}
	}
}