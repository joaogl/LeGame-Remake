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

package net.joaolourenco.legame.graphics.menu;

import net.joaolourenco.legame.Registry;
import net.joaolourenco.legame.graphics.Shader;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.menu.objects.MenuActionReader;
import net.joaolourenco.legame.graphics.menu.objects.MenuCloud;
import net.joaolourenco.legame.settings.GeneralSettings;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * @author Joao Lourenco
 * 
 */
public class Loading extends Menu {

	/**
	 * Shader ID for the font.
	 */
	public Shader shader = new Shader(GeneralSettings.menuFragPath, GeneralSettings.defaultVertexPath);

	public int maxClouds = Registry.getScreenWidth() * 10 / 800;
	public MenuCloud[] clouds = new MenuCloud[maxClouds];
	public float rot = 0, rotOffset = 0;

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Loading() {
		super(Texture.Menus[0], 0, 0, Registry.getScreenWidth(), Registry.getScreenHeight());

		for (int i = 0; i < clouds.length; i++) {
			MenuCloud nc = new MenuCloud(this.shader);
			nc.setX((Float) nc.generateRandom(0, Registry.getScreenWidth(), 1));
			nc.setY((Float) nc.generateRandom(0, 100, 1));
			clouds[i] = nc;
		}
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void render() {
		// Setting up OpenGL for render
		glEnable(GL_BLEND);
		// Enabling Alpha chanel
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// Binding the shader
		this.shader.bind();

		// Render it.
		render(this.x, this.y, Texture.Menus[0], this.shader, this.width, this.height, false);
		render((xMax) / 2, (yMax / 2) + 150, Texture.loading, this.shader, 90, 90, rot);

		for (MenuCloud c : clouds)
			if (c != null) c.render();

		for (MenuActionReader b : this.buttons)
			if (b != null) b.render();

		this.font.drawString("Your PC ain't good enough", (xMax - this.font.getStringSize("Your Pc ain't good enough", 18, -5)) / 2, yMax / 2, 18, -5);
		this.font.drawString("Just wait for the load to finish", (xMax - this.font.getStringSize("Just wait for the load to finish", 18, -5)) / 2, (yMax / 2) + 50, 18, -5);

		// Releasing the shader
		this.shader.release();
		// Disabling the Blend
		glDisable(GL_BLEND);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void update() {
		for (int i = 0; i < maxClouds; i++)
			if (clouds[i] == null) clouds[i] = new MenuCloud(shader);
			else if (clouds[i].toRemove()) clouds[i] = null;

		for (MenuCloud c : clouds)
			if (c != null) c.update();

		for (MenuActionReader b : this.buttons)
			if (b != null) b.update();

		rot += rotOffset;
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {
		for (MenuCloud c : clouds)
			if (c != null) c.tick();
		rotOffset = (Float) generateRandom(0.5f, 1, 1);
	}

}