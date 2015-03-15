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

package net.joaolourenco.legame.entity.actions;

import java.util.*;

import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.world.*;

/**
 * @author Joao Lourenco
 * 
 */
public class RandomTargetedMovementAction extends MovementAction {

	private final Mob entity;
	private final Vector2f target;
	private List<Node> path;
	private int time = 0;

	public RandomTargetedMovementAction(Mob entity) {
		this.entity = entity;
		int xTarget = (Integer) this.generateRandom(0, this.entity.getWorld().getWidth(), 0);
		int yTarget = (Integer) this.generateRandom(0, this.entity.getWorld().getHeight(), 0);

		this.target = new Vector2f(xTarget, yTarget);
	}

	public void update(float speed) {
		time++;
		this.xa = 0;
		this.ya = 0;
		Vector2f start = new Vector2f(this.entity.getTX(), this.entity.getTY());
		if (time % 60 == 0) path = this.entity.getWorld().findPath(start, this.target);
		if (path != null) {
			if (path.size() > 0) {
				Vector2f vec = path.get(path.size() - 1).tile;
				if ((int) this.entity.getX() < (int) vec.getX() << GeneralSettings.TILE_SIZE_MASK) this.xa = (int) speed;
				if ((int) this.entity.getX() > (int) vec.getX() << GeneralSettings.TILE_SIZE_MASK) this.xa = (int) -speed;
				if ((int) this.entity.getY() < (int) vec.getY() << GeneralSettings.TILE_SIZE_MASK) this.ya = (int) speed;
				if ((int) this.entity.getY() > (int) vec.getY() << GeneralSettings.TILE_SIZE_MASK) this.ya = (int) -speed;
			} else this.finished = true;
		}
	}

}