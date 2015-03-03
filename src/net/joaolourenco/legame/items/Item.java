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

package net.joaolourenco.legame.items;

import java.util.*;

import net.joaolourenco.legame.entity.*;

/**
 * @author Joao Lourenco
 * 
 */
public abstract class Item {

	public String name;
	public int current_life, inicial_life;
	public boolean destructable;

	public Item() {
	}

	public Item(String name, int life) {
		this.name = name;
		this.inicial_life = life;
		this.current_life = life;
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

	public void use(Entity activator) {
		useItem(activator);
		if (this.destructable) {
			this.current_life--;
			if (this.current_life == 0) {
				if (itemBreak()) {
					activator.removeItem(this);
					itemBroken(activator);
				}
			}
		}
	}

	public abstract void useItem(Entity activator);

	public abstract void itemBroken(Entity activator);

	public boolean itemBreak() {
		return true;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The current_life
	 */
	public int getCurrent_life() {
		return current_life;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param current_life
	 *            The current_life to set
	 */
	public void setCurrent_life(int current_life) {
		this.current_life = current_life;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The inicial_life
	 */
	public int getInicial_life() {
		return inicial_life;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param inicial_life
	 *            The inicial_life to set
	 */
	public void setInicial_life(int inicial_life) {
		this.inicial_life = inicial_life;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @return The destructable
	 */
	public boolean isDestructable() {
		return destructable;
	}

	/**
	 * 
	 * @author Joao Lourenco
	 * 
	 * @param destructable
	 *            The destructable to set
	 */
	public void setDestructable(boolean destructable) {
		this.destructable = destructable;
	}

}