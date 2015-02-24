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

package net.joaolourenco.fallen.entity.mob;

import net.joaolourenco.fallen.graphics.Texture;
import net.joaolourenco.fallen.settings.GeneralSettings;

import org.lwjgl.input.Keyboard;

/**
 * Player Class.
 * 
 * @author Joao Lourenco
 *
 */
public class Player extends Mob {

	/**
	 * Constructor for a new Player.
	 */
	public Player(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.isLightCollidable(true);
		this.texture = Texture.Player;
	}

	/**
	 * Update Method called by the World Class 60 times per second.
	 */
	@Override
	public void update() {
		// Setting up the variables.
		float xa = 0;
		float ya = 0;
		float speed = 0;

		// Getting the moving speed for the player.
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) speed = getSpeed(true);
		else speed = getSpeed(false);

		// Where is the player going to move.
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) ya -= speed;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) ya += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xa -= speed;
		else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xa += speed;

		// Updating the player facing side.
		getSide(xa, ya);

		// Moving the player to the final destination.
		this.x += xa;
		this.y += ya;

		// Update the Offset of the world.
		this.world.setOffset((int) ((this.x + (this.width / 2)) - GeneralSettings.WIDTH / 2), (int) (this.y + (this.height / 2) - GeneralSettings.HEIGHT / 2));
	}

	/**
	 * Tick Method called by the World Class once per second.
	 */
	public void tick() {
	}

}