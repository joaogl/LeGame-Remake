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

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.items.*;

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
	public boolean usesKey, locked, jammed;

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
		state = States.CLOSED;
		usesKey = false;
	}

	public Door(int x, int y, int width, int height, String _key) {
		super(x, y, width, height);
		state = States.CLOSED;
		key = _key;
		usesKey = true;
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
		
	}

	/**
	 * @see net.joaolourenco.legame.entity.Entity#update()
	 * @author Joao Lourenco
	 */
	@Override
	public void update() {

	}

	/**
	 * @see net.joaolourenco.legame.entity.Entity#tick()
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {

	}

}