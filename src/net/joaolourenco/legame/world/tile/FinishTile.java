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

package net.joaolourenco.legame.world.tile;

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.world.*;

/**
 * @author Joao Lourenco
 * 
 */
public class FinishTile extends SolidTile {

	boolean timer = false;
	World w;

	/**
	 * @param size
	 * @param tex
	 * @author Joao Lourenco
	 */
	public FinishTile(int size, int tex, World w) {
		super(size, tex);
		this.w = w;
	}

	/**
	 * @param size
	 * @param tex
	 * @param rotation
	 * @author Joao Lourenco
	 */
	public FinishTile(int size, int tex, int rotation, World w) {
		super(size, tex, rotation);
		this.w = w;
	}

	/**
	 * @param size
	 * @param tex
	 * @param light
	 * @author Joao Lourenco
	 */
	public FinishTile(int size, int tex, boolean light, World w) {
		super(size, tex, light);
		this.w = w;
	}

	public void entityOver(Entity e) {
		super.entityOver(e);
		System.out.println("Over");

		if (!timer) {
			new Timer("Finished-Level", 500, 1, new TimerResult(this.w) {
				public void timerCall(String caller) {
					World obj = (World) this.object;
					if (obj.levelEndable) obj.levelOver = true;
				}
			});
			timer = !timer;
		}
	}

}
