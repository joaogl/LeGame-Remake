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

import java.util.*;

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.entity.actions.*;
import net.joaolourenco.legame.entity.block.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.utils.Timer;
import net.joaolourenco.legame.world.tile.*;

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

	protected List<MovementAction> moveActions = new ArrayList<MovementAction>();

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
		this.renderHealthBar = true;
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
	}

	public Vector2f move(float xa, float ya) {
		if (isMobStuck()) return new Vector2f(xa, ya);
		return checkCollision(xa, ya);
	}

	public Vector2f checkCollision(float xa, float ya) {
		Vector2f none = new Vector2f(0, 0);
		// Check for mob states.
		if (this.frozen || this.inBed) return none;
		// Check for normal collision.
		if ((this.y + ya) < 0) ya = 0;
		if ((this.x + xa) < 0) xa = 0;
		if ((this.y + ya) > (this.world.getHeight() * 64) - this.height) ya = 0;
		if ((this.x + xa) > (this.world.getWidth() * 64) - this.width) xa = 0;

		// Check for Tile Collision.
		for (int c = 0; c < 4; c++) {
			int x1 = 23;
			int x2 = 20;

			int y1 = 32;
			int y2 = 30;

			int xt = (int) ((x + xa) + c % 2 * x1 + x2) / 64;
			int yt = (int) (y + c / 2 * y1 + y2) / 64;

			Tile t = this.world.getTile(xt, yt);
			if (t != null && t.isCollidable()) xa = 0;

			xt = (int) (x + c % 2 * x1 + x2) / 64;
			yt = (int) ((y + ya) + c / 2 * y1 + y2) / 64;

			t = this.world.getTile(xt, yt);
			if (t != null && t.isCollidable()) ya = 0;
		}

		// Check for Entity Collision
		List<Entity> ent = this.world.getNearByEntities(this.x, this.y, 160);

		for (Entity e : ent) {
			if (e.isCollidable() && e != this) {
				Vector2f[][] v = e.getVertices();
				if (e instanceof Door && this.world.getDistance(this, e) < 100) {
					if (((Door) e).getState() == Door.States.CLOSING) {
						if (((Door) e).alongXAxis) ya = 0;
						else xa = 0;
					}
				}
				for (int n = 0; n < v.length; n++) {
					Vector2f[] vertices = v[n];

					if ((this.x + this.width + xa - 14) > (vertices[0].x) && (this.x + xa) < (vertices[2].x - 15) && (this.y + this.height) > (vertices[0].y) && (this.y) < (vertices[2].y - 30)) xa = 0;

					if ((this.x + this.width - 14) > (vertices[0].x) && (this.x) < (vertices[2].x - 15) && (this.y + this.height + ya) > (vertices[0].y) && (this.y + ya) < (vertices[2].y - 30)) ya = 0;
				}
			}
		}

		return new Vector2f(xa, ya);
	}

	/**
	 * @return
	 * @author Joao Lourenco
	 */
	private boolean isMobStuck() {
		float speed = getSpeed(false);
		Vector2f movePos = checkCollision(speed, speed);
		Vector2f moveNeg = checkCollision(-speed, -speed);
		if (movePos.x == 0 && movePos.y == 0 && moveNeg.x == 0 && moveNeg.y == 0) return true;
		return false;
	}

	public void updateTexture(int xa, int ya) {
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
		if (!this.dying) {
			this.dying = true;
			this.frozen = true;

			new Timer("KilledAnimation", 1000, 1, new TimerResult(this) {
				public void timerCall(String caller) {
					((Entity) this.object).remove();
				}
			});
		}
	}

}