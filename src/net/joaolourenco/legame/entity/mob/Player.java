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
import net.joaolourenco.legame.graphics.menu.*;
import net.joaolourenco.legame.utils.*;
import net.joaolourenco.legame.utils.Vector2f;
import net.joaolourenco.legame.world.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Player Class.
 * 
 * @author Joao Lourenco
 * 
 */
public class Player extends Mob {

	public float stamina = 100;
	protected VertexHandlers VertexID;

	/**
	 * Constructor for a new Player.
	 * 
	 * @author Joao Lourenco
	 */
	public Player(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.isLightCollidable(true);

		setTextureAtlas(Texture.PlayerWalking, 3, 4, 1);
		setDyingTextureAtlas(Texture.PlayerDying, 3, 3, 1);

		this.VertexID = Registry.registerVertexHandler(w, h);
	}

	/**
	 * Method called by the World Class to render the Entity.
	 * 
	 * @author Joao Lourenco
	 */
	public void render() {
		if (this.renderable) {
			if (this.dying) {
				time++;
				if (time % rate != 0) { return; }
			}

			if (!this.dying) {
				// Health bar
				render(x + (this.width / 4), y - 15, 0, shade, (this.width / 2), 5, new Vector4f(0.0f, 0.0f, 0.0f, 1f));

				float w = this.life * (this.width / 2) / 100;
				if (this.life <= 0) w = 0 * (this.width / 2) / 100;
				render(x + (this.width / 4), y - 15, 0, shade, w, 5, new Vector4f(0.0f, 0.6f, 0.0f, 0.5f));

				// Stamina bar
				render(x + (this.width / 4), y - 8, 0, shade, (this.width / 2), 5, new Vector4f(0.0f, 0.0f, 0.0f, 1f));

				w = this.stamina * (this.width / 2) / 100;
				if (this.stamina <= 0) w = 0 * (this.width / 2) / 100;
				render(x + (this.width / 4), y - 8, 0, shade, w, 5, new Vector4f(0.0f, 0.0f, 1f, 0.8f));
			}

			// Setting up OpenGL for render
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ZERO);

			// Binding the shader
			this.shade.bind();

			// Calculating the required light.
			float day_light = 1f;
			if (lightAffected) day_light = world.DAY_LIGHT;
			// Sending the required light to the shader.
			glUniform1f(glGetUniformLocation(shade.getShader(), "dayLight"), day_light * 2);

			// Releasing the shader
			shade.release();
			if (renderHealthBar && !this.dying) {
				render(x + (this.width / 4), y - 15, 0, shade, (this.width / 2), 5, new Vector4f(0.0f, 0.0f, 0.0f, 1f));

				float w = this.life * (this.width / 2) / 100;
				if (this.life <= 0) w = 0 * (this.width / 2) / 100;
				render(x + (this.width / 4), y - 15, 0, shade, w, 5, new Vector4f(0.0f, 0.6f, 0.0f, 0.5f));
			}
			// Binding the shader
			this.shade.bind();

			// Rendering the Quad.
			render(x, y, texture, shade, this.VertexID);

			// Disabling BLEND and releasing shader for next render.
			glDisable(GL_BLEND);
			shade.release();
			glClear(GL_STENCIL_BUFFER_BIT);
		}
	}

	/**
	 * Update Method called by the World Class 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	@Override
	public void update(double delta) {
		// Setting up the variables.
		float xa = 0;
		float ya = 0;
		float speed = 0;

		// Getting the moving speed for the player.
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && this.stamina >= 1) {
			speed = getSpeed(true);
			for (AnimatedSprite s : this.textures)
				s.setFrameRate(5);
		} else {
			if (this.stamina < 100) this.stamina += 0.5f;
			speed = getSpeed(false);
			for (AnimatedSprite s : this.textures)
				s.setFrameRate(6);
		}

		// System.out.println("Speed: " + speed + " Delta: " + Registry.getMainClass().getDelta());

		// Where is the player going to move.
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) ya -= speed;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) ya += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xa -= speed;
		else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xa += speed;

		// Updating the player facing side.
		getSide(xa, ya);

		// Checking for Collision
		Vector2f a = move(xa, ya);
		xa = a.x;
		ya = a.y;

		// Moving the player to the final destination.
		if (!this.frozen) {
			if (Math.abs(xa) > this.getSpeed(false) || Math.abs(ya) > this.getSpeed(false)) this.stamina--;
			this.x += xa * delta; // (xa * Registry.getMainClass().getDelta());
			this.y += ya * delta; // (ya * Registry.getMainClass().getDelta());
		}

		// Update the Offset of the world.
		int xOff = (int) ((this.x + (this.width / 2)) - Registry.getScreenWidth() / 2);
		int yOff = (int) (this.y + (this.height / 2) - Registry.getScreenHeight() / 2);

		// if (xOff < 0) xOff = 1;
		// if (yOff < 0) yOff = 1;

		this.world.setOffset(xOff, yOff);

		this.updateTexture((int) xa, (int) ya);

		if (KeyboardFilter.isKeyDown(Keyboard.KEY_F)) this.died();
		if (KeyboardFilter.isKeyDown(Keyboard.KEY_G)) this.attacking = !this.attacking;

		if (KeyboardFilter.isKeyDown(Keyboard.KEY_ESCAPE) && !(this.world instanceof Tutorial)) Registry.registerMenu(new MainMenu());
	}

	public void updateTexture(int xa, int ya) {
		if (xa != 0 || ya != 0) this.moving = true;
		else this.moving = false;

		if (this.dying) this.animation = texturesDying[1];
		else {
			if (this.side == 0) this.animation = textures[2];
			else if (this.side == 1) this.animation = textures[1];
			else if (this.side == 2) this.animation = textures[0];
			else if (this.side == 3) this.animation = textures[3];
		}

		if (this.moving || this.dying || this.attacking) {
			if (this.dying) {
				if (this.animation.getFrame() < (this.animation.getLength() - 1)) this.animation.update();
			} else this.animation.update();
		} else this.animation.resetAnimation();
		this.texture = this.animation.getTexture();
	}

	/**
	 * Tick Method called by the World Class once per second.
	 * 
	 * @author Joao Lourenco
	 */
	public void tick() {
		if (life <= 0) this.died();

		if (KeyboardFilter.isKeyDown(Keyboard.KEY_RETURN)) {
			Door door = this.world.getNearByDoor(this.x, this.y, 300);
			if (door != null) door.ativateDoor(this);
		}
	}

	/**
	 * Method to return the Enity vertices.
	 * 
	 * @return Vector2f[] with the vertices.
	 * @author Joao Lourenco
	 */
	public Vector2f[][] getVertices() {
		int xSize = 25;
		int yOffset = 2;
		Vector2f[] d1 = new Vector2f[] { //
		//
				new Vector2f(this.x + xSize, this.y + yOffset + 5), //
				new Vector2f(this.x + xSize, this.y + this.height), //
				new Vector2f(this.x - xSize + this.width, this.y + this.height), //
				new Vector2f(this.x - xSize + this.width, this.y + 5) //
		};
		xSize = 20;
		Vector2f[] d2 = new Vector2f[] { //
		//
				new Vector2f(this.x + xSize, this.y + yOffset + 5), //
				new Vector2f(this.x + xSize, this.y + this.height - 5), //
				new Vector2f(this.x - xSize + this.width, this.y + this.height - 5), //
				new Vector2f(this.x - xSize + this.width, this.y + 5) //
		};
		return new Vector2f[][] { d1, d2 };
	}

}