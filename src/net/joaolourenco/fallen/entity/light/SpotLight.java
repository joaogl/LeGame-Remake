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

package net.joaolourenco.fallen.entity.light;

import net.joaolourenco.fallen.settings.GeneralSettings;
import net.joaolourenco.fallen.utils.Vector2f;

/**
 * Class for the Spot Light.
 * 
 * @author Joao Lourenco
 *
 */
public class SpotLight extends Light {

	/**
	 * Constructor to create the Light.
	 * 
	 * @param location
	 *            : Vector2f with the light location.
	 * @param red
	 *            : float with the red value for the light color.
	 * @param green
	 *            : float with the green value for the light color.
	 * @param blue
	 *            : float with the blue value for the light color.
	 */
	public SpotLight(Vector2f location, float red, float green, float blue) {
		super(location, red, green, blue);
		this.type = 2;
		this.size = GeneralSettings.defaultLightSize;
		this.facing = GeneralSettings.defaultLightFacing;
	}

	/**
	 * Constructor to create the Light.
	 * 
	 * @param location
	 *            : Vector2f with the light location.
	 * @param red
	 *            : float with the red value for the light color.
	 * @param green
	 *            : float with the green value for the light color.
	 * @param blue
	 *            : float with the blue value for the light color.
	 * @param inte
	 *            : float with the light intensity.
	 */
	public SpotLight(Vector2f location, float red, float green, float blue, float inte) {
		super(location, red, green, blue, inte);
		this.type = 2;
		this.size = GeneralSettings.defaultLightSize;
		this.facing = GeneralSettings.defaultLightFacing;
	}

	/**
	 * Constructor to create the Light.
	 * 
	 * @param location
	 *            : Vector2f with the light location.
	 * @param red
	 *            : float with the red value for the light color.
	 * @param green
	 *            : float with the green value for the light color.
	 * @param blue
	 *            : float with the blue value for the light color.
	 * @param inte
	 *            : float with the light intensity.
	 * @param size
	 *            : int with the light size.
	 * @param facing
	 *            : int with the light facing direction.
	 */
	public SpotLight(Vector2f location, float red, float green, float blue, float inte, int size, int facing) {
		super(location, red, green, blue, inte);
		this.type = 2;
		this.size = size;
		this.facing = facing;
	}

	/**
	 * Method called once per second by the World Class.
	 */
	public void tick() {
	}

}