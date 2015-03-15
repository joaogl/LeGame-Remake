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

package net.joaolourenco.legame.entity.mob;

import net.joaolourenco.legame.entity.actions.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.world.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Citizen extends Mob {

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Citizen(int x, int y, World w) {
		super(x, y, 64, 64);
		
		this.setTextureAtlas(Texture.Citizen, 3, 4, 1);
		this.updateTexture(0, 0);
		
		w.addEntity(this);
		moveActions.add(new RandomTargetedMovementAction(this));
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void update(double delta) {
		this.life -= 0.2f;

		// Setting up the variables.
		float xa = 0;
		float ya = 0;
		float speed = getSpeed(false);

		if (moveActions.get(0).finished()) moveActions.set(0, new RandomTargetedMovementAction(this));
		moveActions.get(0).update(speed);

		xa += moveActions.get(0).getXA();
		ya += moveActions.get(0).getYA();

		// Updating the player facing side.
		getSide(xa, ya);

		// Checking for Collision
		Vector2f a = move(xa, ya);
		xa = a.x;
		ya = a.y;

		// Moving the player to the final destination.
		this.x += xa * delta;
		this.y += ya * delta;

		if (xa != 0 || ya != 0) this.updateTexture((int) xa, (int) ya);
	}

	public void updateTexture(int xa, int ya) {
		if (this.side == 0) this.animation = textures[0];
		else if (this.side == 1) this.animation = textures[1];
		else if (this.side == 2) this.animation = textures[0];
		else if (this.side == 3) this.animation = textures[3];

		if (this.moving) this.animation.update();
		else this.animation.resetAnimation();
		this.texture = this.animation.getTexture();
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {
		if (this.life <= 0) this.died();
	}

}