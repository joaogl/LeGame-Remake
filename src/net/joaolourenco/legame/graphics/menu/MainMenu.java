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

import org.lwjgl.input.Keyboard;

import net.joaolourenco.legame.Registry;
import net.joaolourenco.legame.graphics.Shader;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.menu.objects.*;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.utils.KeyboardFilter;
import net.joaolourenco.legame.world.RandomWorld;
import net.joaolourenco.legame.world.Tutorial;

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
public class MainMenu extends Menu {

	/**
	 * Shader ID for the font.
	 */
	public Shader shader = new Shader(GeneralSettings.menuFragPath, GeneralSettings.defaultVertexPath);
	public Shader shaderBack = new Shader(GeneralSettings.menuBackFragPath, GeneralSettings.defaultVertexPath);

	public int maxClouds = Registry.getScreenWidth() * 10 / 800;
	public MenuCloud[] clouds = new MenuCloud[maxClouds];
	public boolean Color = false;

	private int maxSelect;
	private int selected = 0;

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

		if (Registry.getMainClass().getWorld() != null) Color = true;

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

		if (Color) {
			this.buttons.add(new MenuButton("Resume Game", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
			this.buttons.get(i - 1).addClickAction(new ClickAction() {
				public void onClick(Menu m) {
					m.close();
				}
			});
			this.buttons.add(new MenuButton("Main Menu", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
			this.buttons.get(i - 1).addClickAction(new ClickAction() {
				public void onClick(Menu m) {
					Registry.getMainClass().setWorld(null);
					m.close();
					Registry.registerMenu(new MainMenu());
				}
			});
		}
		this.buttons.add(new MenuButton("New Game", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				Registry.getMainClass().setWorld(new RandomWorld(1));
				m.close();
			}
		});
		if (Color) {
			this.buttons.add(new MenuButton("Save", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
			this.buttons.get(i - 1).addClickAction(new ClickAction() {
				public void onClick(Menu m) {

					m.close();
				}
			});
		} else {
			this.buttons.add(new MenuButton("Tutorial", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
			this.buttons.get(i - 1).addClickAction(new ClickAction() {
				public void onClick(Menu m) {
					Registry.getMainClass().setWorld(new Tutorial());
					m.close();
				}
			});
		}
		this.buttons.add(new MenuButton("Load", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
			}
		});
		this.buttons.add(new MenuButton("Options", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				m.close();
				Registry.registerMenu(new OptionsMenu());
			}
		});
		this.buttons.add(new MenuButton("Exit", this.xMax / 2, yPos + (50 * i++), size, spacing, this));
		this.buttons.get(i - 1).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				Registry.getMainClass().stop();
			}
		});

		this.maxSelect = i;
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

		Shader sha = this.shader;
		if (Color) sha = this.shaderBack;

		// Binding the shader
		sha.bind();
		// Render it.
		render(this.x, this.y, this.texture, sha, this.width, this.height, false);
		sha.release();

		// Binding the shader
		this.shader.bind();

		for (MenuCloud c : clouds)
			if (c != null) c.render();

		for (MenuActionReader b : this.buttons)
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

		for (MenuActionReader b : this.buttons)
			if (b != null) b.update();

		if (KeyboardFilter.isKeyDown(Keyboard.KEY_ESCAPE) && Registry.getMainClass().getWorld() != null) this.close();
		if (KeyboardFilter.isKeyDown(Keyboard.KEY_UP)) {
			if (this.selected <= 0) this.selected = this.maxSelect;
			else this.selected--;
		} else if (KeyboardFilter.isKeyDown(Keyboard.KEY_DOWN)) {
			if (this.selected >= this.maxSelect) this.selected = 0;
			else this.selected++;
		}
		if (KeyboardFilter.isKeyDown(Keyboard.KEY_RETURN)) {
			this.buttons.get(this.selected);
		}
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
