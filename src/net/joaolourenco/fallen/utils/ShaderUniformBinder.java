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

import java.util.ArrayList;

import net.joaolourenco.fallen.entity.Entity;
import net.joaolourenco.fallen.entity.light.Light;
import net.joaolourenco.fallen.graphics.Shader;
import net.joaolourenco.fallen.settings.GeneralSettings;
import net.joaolourenco.fallen.world.World;

import static org.lwjgl.opengl.GL20.*;

/**
 * Class to all the Uniforms Binder.
 * 
 * @author Joao Lourenco
 *
 */
public class ShaderUniformBinder {

	/**
	 * Method to bind the Texture uniforms, this is what make the shaders and stuff.
	 * 
	 * @param shade
	 *            : Shader of the element.
	 * @param w
	 *            : Current world that is being rendered.
	 * @param ent
	 *            : List of entities that emit light.
	 * @param lightAffected
	 *            : Is the tile affected by the light.
	 */
	public static void bindUniforms(Shader shade, World w, ArrayList<Entity> ent, boolean lightAffected) {
		// Is there 50 Lights or less?
		int howMany = GeneralSettings.howManyLightsToShader;
		if (howMany < ent.size()) howMany = ent.size();
		// Binding the shader program.
		shade.bind();
		// Setting up all the variables that will be passed to the shader
		float[] positions = new float[howMany * 2];
		float[] colors = new float[howMany * 3];
		float[] intensities = new float[howMany];
		float[] type = new float[howMany];
		float[] size = new float[howMany];
		float[] facing = new float[howMany];

		// Putting all the coordinates inside a float array.
		for (int i = 0; i < howMany * 2; i += 2) {
			if (i < ent.size() && ent.get(i >> 1) != null && ((Light) ent.get(i)).getLightState()) {
				float xx = ent.get(i >> 1).getX() - w.getXOffset();
				float yy = GeneralSettings.HEIGHT - (ent.get(i >> 1).getY() - w.getYOffset());

				positions[i] = xx;
				positions[i + 1] = yy;
			}
		}

		// Putting all the colors inside a float array.
		for (int i = 0; i < howMany * 3; i += 3) {
			if (i < ent.size() && ent.get(i / 3) != null && ((Light) ent.get(i)).getLightState()) {
				colors[i] = ((Light) ent.get(i / 3)).red;
				colors[i + 1] = ((Light) ent.get(i / 3)).green;
				colors[i + 2] = ((Light) ent.get(i / 3)).blue;
			}
		}

		// Putting the size, type, facing and intensities of the light inside a float array.
		for (int i = 0; i < howMany; i++) {
			if (i < ent.size() && ent.get(i) != null && ((Light) ent.get(i)).getLightState()) {
				type[i] = ((Light) ent.get(i)).getType();
				size[i] = ((Light) ent.get(i)).getSize();
				facing[i] = ((Light) ent.get(i)).getFacing();
				intensities[i] = ((Light) ent.get(i)).intensity;
			}
		}

		// Sending to the shader the current dayLight		
		float day_light = 1f;
		if (lightAffected) day_light = w.DAY_LIGHT;
		glUniform1f(glGetUniformLocation(shade.getShader(), "dayLight"), day_light * 2);

		// Sending all the previus information from the floats to the shader.
		glUniform1i(glGetUniformLocation(shade.getShader(), "lightAmount"), type.length);
		glUniform2(glGetUniformLocation(shade.getShader(), "lightPosition"), Buffer.createFloatBuffer(positions));
		glUniform3(glGetUniformLocation(shade.getShader(), "lightColor"), Buffer.createFloatBuffer(colors));
		glUniform1(glGetUniformLocation(shade.getShader(), "lightIntensity"), Buffer.createFloatBuffer(intensities));
		glUniform1(glGetUniformLocation(shade.getShader(), "lightType"), Buffer.createFloatBuffer(type));
		glUniform1(glGetUniformLocation(shade.getShader(), "lightSize"), Buffer.createFloatBuffer(size));
		glUniform1(glGetUniformLocation(shade.getShader(), "lightFacing"), Buffer.createFloatBuffer(facing));
	}

}