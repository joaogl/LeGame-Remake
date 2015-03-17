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

import net.joaolourenco.legame.entity.Entity;
import net.joaolourenco.legame.entity.actions.PersistentTargetedMovementAction;
import net.joaolourenco.legame.entity.actions.RandomTargetedMovementAction;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.Vector2f;
import net.joaolourenco.legame.world.World;

/**
 * @author Joao Lourenco
 * 
 */
public class Spider extends Mob {

	private Entity target;
	private int searchArea = 400;
	private int attackRange = 50;

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Spider(int x, int y, World w, Entity target) {
		super(x, y, 64, 64);

		this.target = target;

		this.setTextureAtlas(Texture.SpiderWalking, 4, 4, 1);
		this.setAttackingTextureAtlas(Texture.SpiderAttacking, 2, 4, 1);
		this.setDyingTextureAtlas(Texture.SpiderDying, 2, 2, 1);

		w.addEntity(this);

		moveActions.add(new RandomTargetedMovementAction(this));
		this.updateTexture(0, 0);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void update(double delta) {
		// Setting up the variables.
		float xa = 0;
		float ya = 0;
		float speed = this.getSpeed(false);

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

		if (this.world.getDistance(this, target) <= this.attackRange) target.attacked(this);
	}

	public void updateTexture(int xa, int ya) {
		if (xa != 0 || ya != 0) this.moving = true;
		else this.moving = false;

		if (this.side == 0) this.animation = textures[3];
		else if (this.side == 1) this.animation = textures[1];
		else if (this.side == 2) this.animation = textures[0];
		else if (this.side == 3) this.animation = textures[2];

		if (this.moving) this.animation.update();
		else this.animation.resetAnimation();
		this.texture = this.animation.getTexture();
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {
		if (this.world.getDistance(this, this.target) <= this.searchArea) {
			if (this.moveActions.get(0) instanceof RandomTargetedMovementAction) moveActions.set(0, new PersistentTargetedMovementAction(this, this.target));
		} else {
			if (this.moveActions.get(0) instanceof PersistentTargetedMovementAction) moveActions.set(0, new RandomTargetedMovementAction(this));
		}

		if (this.life <= 0) this.died();
	}

	/**
	 * Method to get the moving speed of an Entity.
	 * 
	 * @param running
	 *            : Boolean true if they are running, false if they are not.
	 * @author Joao Lourenco
	 */
	public float getSpeed(boolean running) {
		float speed = 0;
		if (running) speed = GeneralSettings.defaultEntityRunning;
		else speed = GeneralSettings.defaultEntityWalking;
		return speed;
	}

}
