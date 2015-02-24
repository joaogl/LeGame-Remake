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

package net.joaolourenco.fallen.graphics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.joaolourenco.fallen.settings.GeneralSettings;

import org.lwjgl.opengl.ARBShaderObjects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Class that processes all the shaders stuff.
 * 
 * @author Joao Lourenco
 *
 */
public class Shader {

	/**
	 * The ID of the shader program.
	 */
	private int shader;
	/**
	 * The Code from the fragment file.
	 */
	private String fragmentPath;
	/**
	 * The Code from the vertex file.
	 */
	private String vertexPath;
	/**
	 * The ID for the fragment shader.
	 */
	private int fragmentID = 999999;
	/**
	 * The ID for the vertex shader.
	 */
	private int vertexID = 999999;

	/**
	 * Constructor for shaders with only fragments.
	 * 
	 * @param fragPath
	 *            : String with the path for the Fragment file.
	 */
	public Shader(String fragPath) {
		// Adding this shader to the public shaders list.
		GeneralSettings.shaders.add(this);
		// Load the code to a string
		String frag = loadAsString(fragPath);
		this.fragmentPath = fragPath;
		// Create the shader program
		create(frag);
	}

	/**
	 * Constructor for shaders with fragments and shaders.
	 * 
	 * @param fragPath
	 *            : String with the path for the Fragment file.
	 * @param vertPath
	 *            : String with the path for the Vertex file.
	 */
	public Shader(String fragPath, String vertPath) {
		// Adding this shader to the public shaders list.
		GeneralSettings.shaders.add(this);
		// Load the code to the strings.
		String frag = loadAsString(fragPath);
		String vert = loadAsString(vertPath);
		this.fragmentPath = fragPath;
		this.vertexPath = vertPath;
		// Create the shader program.
		create(frag, vert);
	}

	/**
	 * Method to load the code from files to strings.
	 * 
	 * @param path
	 *            : String with the path for the shader source to load
	 * @return String with the code.
	 */
	private String loadAsString(String path) {
		// Setting up the variables
		StringBuilder shaderSource = new StringBuilder();
		try {
			String line;
			// Reading from the file to the string.
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Returning the source has a String
		return shaderSource.toString();
	}

	/**
	 * Method to create a shader program with only fragment.
	 * 
	 * @param frag
	 *            : String with the Fragment code.
	 */
	private void create(String frag) {
		// Creating a program.
		this.shader = glCreateProgram();
		// Creating a fragment shader .
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		// Sending the source to the shader.
		glShaderSource(fragmentShader, frag);
		// Compiling the shader code.
		glCompileShader(fragmentShader);
		// Checking if it worked.
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) System.err.println("Fragment shader not compiled! " + getLog(fragmentShader));

		// Saving the fragment ID.
		this.fragmentID = fragmentShader;
		// Attaching the fragment to the shader program.
		glAttachShader(this.shader, fragmentShader);
		// Linking the program.
		glLinkProgram(this.shader);
		// Validating the shader program.
		glValidateProgram(this.shader);
	}

	public String getLog(int prog) {
		return ARBShaderObjects.glGetInfoLogARB(prog, ARBShaderObjects.glGetObjectParameteriARB(prog, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	/**
	 * Method to create a shader program with fragment and vertex.
	 * 
	 * @param frag
	 *            : String with the Fragment code.
	 * @param vert
	 *            : String with the Vertex code.
	 */
	private void create(String frag, String vert) {
		// Creating a program.
		this.shader = glCreateProgram();
		// Creating a fragment and vertex shader .
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		// Sending the source to the shader.
		glShaderSource(fragmentShader, frag);
		glShaderSource(vertexShader, vert);
		// Compiling the shader code.
		glCompileShader(fragmentShader);
		glCompileShader(vertexShader);
		// Checking if it worked.
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) System.err.println(fragmentPath + " : Fragment shader not compiled! " + getLog(fragmentShader));
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) System.err.println(vertexPath + " : Vertex shader not compiled! " + getLog(vertexShader));
		// Saving the fragment ID.
		this.fragmentID = fragmentShader;
		this.vertexID = vertexShader;
		// Attaching the fragment to the shader program.
		glAttachShader(this.shader, fragmentShader);
		glAttachShader(this.shader, vertexShader);
		// Linking the program.
		glLinkProgram(this.shader);
		// Validating the shader program.
		glValidateProgram(this.shader);
	}

	/**
	 * Method to recompile the shader program without having to restart the game.
	 */
	public void recompile() {
		// Removing the old shaders from memory
		cleanUp();
		// Loading the code
		String frag = loadAsString(this.fragmentPath);
		if (this.vertexPath != null) {
			String vert = loadAsString(this.vertexPath);
			// Creating the shaders again.
			create(frag, vert);
		} else create(frag);
	}

	/**
	 * Binding the shader program to render.
	 */
	public void bind() {
		glUseProgram(this.shader);
	}

	/**
	 * Realease the shader program from the rendering scope.
	 */
	public void release() {
		glUseProgram(0);
	}

	/**
	 * Method to get the Shader ID
	 * 
	 * @return int with the shader program ID.
	 */
	public int getShader() {
		return this.shader;
	}

	/**
	 * Method to cleanup the memory by removing all the shaders and programs.
	 */
	public void cleanUp() {
		glDeleteProgram(this.shader);
		if (this.fragmentID != 999999) glDeleteShader(this.fragmentID);
		if (this.vertexID != 999999) glDeleteShader(this.vertexID);
	}

	/**
	 * Method to get the Fragment file path.
	 * 
	 * @return String with the path for the fragment file.
	 */
	public String getFragPath() {
		return this.fragmentPath;
	}

	/**
	 * Method to get the Vertex file path.
	 * 
	 * @return String with the path for the vertex file.
	 */
	public String getVertPath() {
		return this.vertexPath;
	}

}