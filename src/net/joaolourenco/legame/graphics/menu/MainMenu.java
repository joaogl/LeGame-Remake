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

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.menu.objects.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.world.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Joao Lourenco
 * 
 */
public class MainMenu extends Menu {

	/**
	 * Shader ID for the font.
	 */
	public Shader shader = new Shader(GeneralSettings.menuFragPath, GeneralSettings.defaultVertexPath);

	public int maxClouds = Registry.getScreenWidth() * 10 / 800;
	public MenuCloud[] clouds = new MenuCloud[maxClouds];

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public MainMenu() {
		super(Texture.Menus[0], 0, 0, Registry.getScreenWidth(), Registry.getScreenHeight());

		for (int i = 0; i < clouds.length; i++) {
			MenuCloud nc = new MenuCloud(shader);
			nc.setX((Float) nc.generateRandom(0, Registry.getScreenWidth(), 1));
			nc.setY((Float) nc.generateRandom(0, 100, 1));
			clouds[i] = nc;
		}

		int i = 0;
		int size = 30;
		int spacing = -5;
		int yPos = ((Registry.getScreenHeight() - 50 * 5) / 2) + 100;
		
		this.buttons.add(new MenuButton("New Game", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
			}
		});
		this.buttons.add(new MenuButton("Tutorial", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				Registry.getMainClass().setWorld(new Tutorial());
				m.close();
			}
		});
		this.buttons.add(new MenuButton("Load", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
			}
		});
		this.buttons.add(new MenuButton("Options", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
			}
		});
		this.buttons.add(new MenuButton("Exit", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				Registry.getMainClass().stop();
			}
		});
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
		render(this.x, this.y, Texture.Menus[0], this.shader, this.width, this.height);

		for (MenuCloud c : clouds)
			if (c != null) c.render();

		for (MenuButton b : this.buttons)
			if (b != null) b.render();

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

		for (MenuButton b : this.buttons)
			if (b != null) b.update();
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {
		for (MenuCloud c : clouds)
			if (c != null) c.tick();
	}

}
