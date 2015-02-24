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

package net.joaolourenco.fallen.utils;

/**
 * Class to simulate LWJGL Vector2f Class.
 * 
 * @author Joao Lourenco
 *
 */
public class Vector2f {

	/**
	 * X and Y values for the vector.
	 */
	public float x, y;

	/**
	 * Constructor for the Vector.
	 * 
	 * @param x
	 *            : x value for the vector.
	 * @param y
	 *            : y value for the vector.
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Method to set x and y values.
	 * 
	 * @param x
	 *            : x value for the vector.
	 * @param y
	 *            : y value for the vector.
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Method to add vectors.
	 * 
	 * @param left
	 *            : Vector one.
	 * @param right
	 *            : Vector two.
	 * @param dest
	 *            : Final Vector.
	 * @return The Final Vector.
	 */
	public static Vector2f add(Vector2f left, Vector2f right, Vector2f dest) {
		if (dest == null) return new Vector2f(left.x + right.x, left.y + right.y);
		else {
			dest.set(left.x + right.x, left.y + right.y);
			return dest;
		}
	}

	/**
	 * Method to sub vectors.
	 * 
	 * @param left
	 *            : Vector one.
	 * @param right
	 *            : Vector two.
	 * @param dest
	 *            : Final Vector.
	 * @return The Final Vector.
	 */
	public static Vector2f sub(Vector2f left, Vector2f right, Vector2f dest) {
		if (dest == null) return new Vector2f(left.x - right.x, left.y - right.y);
		else {
			dest.set(left.x - right.x, left.y - right.y);
			return dest;
		}
	}

	/**
	 * Method to apply the dot cal.
	 * 
	 * @param left
	 *            : Vector one.
	 * @param right
	 *            : Vector two.
	 * @return The dot product has a float.
	 */
	public static float dot(Vector2f left, Vector2f right) {
		return left.x * right.x + left.y * right.y;
	}

	/**
	 * Method that scales the Vector.
	 * 
	 * @param scale
	 *            : The scale value.
	 * @return The vector scaled.
	 */
	public Vector2f scale(float scale) {
		x *= scale;
		y *= scale;

		return this;
	}

	/**
	 * Method to get the x value from the Vector.
	 * 
	 * @return x value from the Vector.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Method to get the y value from the Vector.
	 * 
	 * @return y value from the Vector.
	 */
	public float getY() {
		return this.y;
	}

}