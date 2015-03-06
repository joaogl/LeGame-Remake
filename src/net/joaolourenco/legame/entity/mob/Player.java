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

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.entity.block.*;
import net.joaolourenco.legame.graphics.*;

import org.lwjgl.input.*;

/**
 * Player Class.
 * 
 * @author Joao Lourenco
 * 
 */
public class Player extends Mob {

	/**
	 * Constructor for a new Player.
	 * 
	 * @author Joao Lourenco
	 */
	public Player(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.isLightCollidable(true);
		// this.texture = Texture.Player[0];
	}

	/**
	 * Update Method called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	@Override
	public void update() {
		// Setting up the variables.
		float xa = 0;
		float ya = 0;
		float speed = 0;

		// Getting the moving speed for the player.
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			speed = getSpeed(true);
			for (AnimatedSprite s : this.textures )
				s.setFrameRate(5);
		} else {
			speed = getSpeed(false);
			for (AnimatedSprite s : this.textures )
				s.setFrameRate(6);
		}

		// Where is the player going to move.
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) ya -= speed;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) ya += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xa -= speed;
		else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xa += speed;

		xa = moveX(xa);
		ya = moveY(ya);

		// Updating the player facing side.
		getSide(xa, ya);

		// Moving the player to the final destination.
		this.x += xa;
		this.y += ya;

		// Update the Offset of the world.
		this.world.setOffset((int) ((this.x + (this.width / 2)) - Registry.getScreenWidth() / 2), (int) (this.y + (this.height / 2) - Registry.getScreenHeight() / 2));
		this.updateTexture();
	}

	/**
	 * Tick Method called by the World Class once per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void tick() {
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			Door door = this.world.getNearByDoor(this.x, this.y, 300);
			if (door != null) door.ativateDoor(this);
		}
	}

}