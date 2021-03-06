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

package net.joaolourenco.legame.entity.light.specific;

import net.joaolourenco.legame.entity.light.PointLight;
import net.joaolourenco.legame.utils.Vector2f;

/**
 * Class for the Fire Light.
 * 
 * @author Joao Lourenco
 * 
 */
public class FireLight extends PointLight {

	/**
	 * Animation time.
	 */
	int time = 0;

	/**
	 * Constructor for the Fire Light.
	 * 
	 * @param location
	 *            : where is the fire light.
	 * @param red
	 *            : red color value for the light.
	 * @param green
	 *            : green color value for the light.
	 * @param blue
	 *            : blue color value for the light.
	 * @author Joao Lourenco
	 */
	public FireLight(Vector2f location, float red, float green, float blue) {
		super(location, red, green, blue);
		this.hasLightSpot = 0;
	}

	/**
	 * Method for update called 60 times per second by the World Class.
	 * 
	 * @author Joao Lourenco
	 */
	public void update(double delta) {
		// Calling the super method.
		super.update(delta);
		// Adding time.
		time++;
		// Generating random value to animate the fire.
		if (time % (Integer) generateRandom(10, 50, 0) == 0) this.intensity = (Float) generateRandom(2f, 2.5f, 1);
		this.green = (Float) generateRandom(0f, 0.4f, 1);
	}

	/**
	 * Method for tick called once per second by the World Class.
	 * 
	 * @author Joao Lourenco
	 */
	public void tick() {
	}
}