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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * Class Buffer handles all the OpenGL Buffers.
 * 
 * @author Joao Lourenco
 *
 */
public class Buffer {

	/**
	 * Method to move a normal float array to a FloatBuffer.
	 * 
	 * @param array
	 *            : float[] with data.
	 * @return FloatBuffer
	 */
	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer result = BufferUtils.createFloatBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	/**
	 * Method to move a normal byte array to a ByteBuffer.
	 * 
	 * @param array
	 *            : byte[] with data.
	 * @return ByteBuffer
	 */
	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer result = BufferUtils.createByteBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	/**
	 * Method to move a normal int array to a result.
	 * 
	 * @param array
	 *            : int[] with data.
	 * @return result
	 */
	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer result = BufferUtils.createIntBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}
}
