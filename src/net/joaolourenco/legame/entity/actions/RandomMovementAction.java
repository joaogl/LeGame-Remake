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

import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.utils.*;

/**
 * @author Joao Lourenco
 * 
 */
public class RandomMovementAction extends MovementAction {

	private final Mob entity;
	private final Vector2f target;

	public RandomMovementAction(Mob entity) {
		this.entity = entity;
		this.target = new Vector2f((Float) this.generateRandom(0, this.entity.getWorld().getWidth(), 1), (Float) this.generateRandom(0, this.entity.getWorld().getHeight(), 1));
	}

	public void update(float speed) {
		this.xa = 1;
	}

}