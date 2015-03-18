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

import java.util.List;

import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.entity.mob.Mob;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.Node;
import net.joaolourenco.legame.utils.Vector2f;

/**
 * @author Joao Lourenco
 * 
 */
public class PersistentTargetedMovementAction extends MovementAction {

	private final Mob entity;
	private final Entity target_ent;
	private List<Node> path;

	public PersistentTargetedMovementAction(Mob entity, Entity target) {
		this.entity = entity;
		this.target_ent = target;
	}

	public void update(float speed) {
		this.xa = 0;
		this.ya = 0;

		Vector2f start = new Vector2f(this.entity.getTX(true), this.entity.getTY(true));
		Vector2f target = new Vector2f(this.target_ent.getTX(true), this.target_ent.getTY(true));
		path = this.entity.getWorld().findPath(start, target);

		if (path != null) {
			if (path.size() > 0) {
				Vector2f vec = path.get(path.size() - 1).tile;
				if (this.entity.getX(true) < (int) vec.getX() << GeneralSettings.TILE_SIZE_MASK) this.xa = (int) speed;
				if (this.entity.getX(true) > (int) vec.getX() << GeneralSettings.TILE_SIZE_MASK) this.xa = (int) -speed;
				if (this.entity.getY(true) < (int) vec.getY() << GeneralSettings.TILE_SIZE_MASK) this.ya = (int) speed;
				if (this.entity.getY(true) > (int) vec.getY() << GeneralSettings.TILE_SIZE_MASK) this.ya = (int) -speed;
			}
		}
	}

}