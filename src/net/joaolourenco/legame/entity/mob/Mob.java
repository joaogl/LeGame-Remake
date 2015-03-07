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

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.world.*;

/**
 * Abstract class for all the Mob
 * 
 * @author Joao Lourenco
 * 
 */
public abstract class Mob extends Entity {

	/**
	 * Is the player frozen
	 */
	protected boolean frozen = false;
	/**
	 * Is the player in Bed
	 */
	protected boolean inBed = false;
	/**
	 * Is the player moving
	 */
	protected boolean moving = false;
	/**
	 * Where is the player facing
	 */
	protected int side;
	/**
	 * Texture Animations for the Entity.
	 */
	protected AnimatedSprite[] textures;
	/**
	 * Texture Animations for the Entity.
	 */
	protected AnimatedSprite[] texturesDying;
	/**
	 * Texture Animations for the Entity.
	 */
	protected AnimatedSprite[] texturesAttacking;
	/**
	 * Current Animation playing.
	 */
	protected AnimatedSprite animation;

	protected float minX, minY, maxX, maxY;

	protected boolean attacking = false;

	/**
	 * Constructor for a normal Mob.
	 * 
	 * @param x
	 *            : mob x coordinates.
	 * @param y
	 *            : mob y coordinates.
	 * @param width
	 *            : mob width.
	 * @param height
	 *            : mob height.
	 * @author Joao Lourenco
	 */
	public Mob(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	/**
	 * Method to get if the Mob is frozen.
	 * 
	 * @return boolean, true if its frozen, false if its not.
	 * @author Joao Lourenco
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * Method to freeze the Mob.
	 * 
	 * @author Joao Lourenco
	 */
	public void freeze() {
		frozen = true;
	}

	/**
	 * Method to unfreeze the Mob.
	 * 
	 * @author Joao Lourenco
	 */
	public void unFreeze() {
		frozen = false;
	}

	/**
	 * Method to check if Mob is in Bed.
	 * 
	 * @return boolean, true if its in bed, false if its not.
	 * @author Joao Lourenco
	 */
	public boolean inBed() {
		return this.inBed;
	}

	/**
	 * Method to change in Bed setting.
	 * 
	 * @param a
	 *            : true if its in bed, false if its not.
	 * @author Joao Lourenco
	 */
	public void inBed(boolean a) {
		this.inBed = a;
	}

	/**
	 * Method to get the player facing side.
	 * 
	 * @param xa
	 *            : where is the player going.
	 * @param ya
	 *            : where is the player going.
	 * @author Joao Lourenco
	 */
	public void getSide(float xa, float ya) {
		if (xa > 0) this.side = 0;
		else if (xa < 0) this.side = 1;
		if (ya > 0) this.side = 2;
		else if (ya < 0) this.side = 3;

		if (xa != 0 || ya != 0) this.moving = true;
		else this.moving = false;
	}

	public float moveX(float a) {
		if ((this.x + a) < 0) return 0;
		if (this.frozen) return 0;
		if ((this.x + a) > (this.world.getWidth() * 64) - this.width) return 0;
		return a;
	}

	public float moveY(float a) {
		if ((this.y + a) < 0) return 0;
		if (this.frozen) return 0;
		if ((this.y + a) > (this.world.getHeight() * 64) - this.height) return 0;
		return a;
	}

	public void updateTexture() {
		if (this.side == 0) this.animation = textures[2];
		else if (this.side == 1) this.animation = textures[1];
		else if (this.side == 2) this.animation = textures[0];
		else if (this.side == 3) this.animation = textures[3];

		if (this.moving) this.animation.update();
		else this.animation.resetAnimation();
		this.texture = this.animation.getTexture();
	}

	public void setTextureAtlas(int[] textures, int animation_Size, int total_animations, int holding) {
		this.textures = new AnimatedSprite[total_animations];
		for (int i = 0; i < this.textures.length; i++) {
			int[] toSend = new int[animation_Size];
			for (int n = 0; n < toSend.length; n++)
				toSend[n] = textures[n + animation_Size * i];
			this.textures[i] = new AnimatedSprite(toSend, animation_Size, holding);
		}
	}

	public void setDyingTextureAtlas(int[] textures, int animation_Size, int total_animations, int holding) {
		this.texturesDying = new AnimatedSprite[total_animations];
		for (int i = 0; i < this.texturesDying.length; i++) {
			int[] toSend = new int[animation_Size];
			for (int n = 0; n < toSend.length; n++)
				toSend[n] = textures[n + animation_Size * i];
			this.texturesDying[i] = new AnimatedSprite(toSend, animation_Size, holding);
		}
	}

	public void setAttackingTextureAtlas(int[] textures, int animation_Size, int total_animations, int holding) {
		this.texturesAttacking = new AnimatedSprite[total_animations];
		for (int i = 0; i < this.texturesAttacking.length; i++) {
			int[] toSend = new int[animation_Size];
			for (int n = 0; n < toSend.length; n++)
				toSend[n] = textures[n + animation_Size * i];
			this.texturesAttacking[i] = new AnimatedSprite(toSend, animation_Size, holding);
		}
	}

	public void died() {
		this.dying = true;
		this.frozen = true;

		new Timer("KilledAnimation", 1000, 1, new TimerResult(this) {
			public void timerCall(String caller) {
				((Entity) this.object).renderable = false;
			}
		});
	}

}