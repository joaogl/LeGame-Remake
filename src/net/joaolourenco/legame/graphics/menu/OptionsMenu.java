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
public class OptionsMenu extends Menu {

	/**
	 * Shader ID for the font.
	 */
	public Shader shader = new Shader(GeneralSettings.menuFragPath, GeneralSettings.defaultVertexPath);
	public Shader shaderBack = new Shader(GeneralSettings.menuBackFragPath, GeneralSettings.defaultVertexPath);

	public int maxClouds = Registry.getScreenWidth() * 10 / 800;
	public MenuCloud[] clouds = new MenuCloud[maxClouds];
	public boolean Color = false;

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public OptionsMenu() {
		super(Texture.Menus[0], 0, 0, Registry.getScreenWidth(), Registry.getScreenHeight());

		if (Registry.getMainClass().getWorld() != null) Color = true;

		for (int i = 0; i < clouds.length; i++) {
			MenuCloud nc = new MenuCloud(shader);
			nc.setX((Float) nc.generateRandom(0, Registry.getScreenWidth(), 1));
			nc.setY((Float) nc.generateRandom(0, 100, 1));
			clouds[i] = nc;
		}

		int i = 0;
		int size = 20;
		int spacing = -5;
		int yPos = ((Registry.getScreenHeight() - 50 * 5) / 2) + 100;

		MenuCheckBox full = new MenuCheckBox("Fullscreen", this.xMax / 2, yPos + (50 * i++), size, spacing, this, shader);
		full.setSelected(Boolean.valueOf((String) Registry.getSetting("fullscreen")));
		this.buttons.add(full);

		MenuCheckBox vsync = new MenuCheckBox("Vertical Sync", this.xMax / 2, yPos + (50 * i++), size, spacing, this, shader);
		vsync.setSelected(Boolean.valueOf((String) Registry.getSetting("vsync")));
		this.buttons.add(vsync);

		MenuCheckBox window = new MenuCheckBox("Fullscreen-Windowed", this.xMax / 2, yPos + (50 * i++), size, spacing, this, shader);
		window.setSelected(Boolean.valueOf((String) Registry.getSetting("fullscreen_windowed")));
		this.buttons.add(window);

		//MenuDropDown d1 = new MenuDropDown(this.xMax / 2, yPos + (50 * i++), size, spacing, this, shader);
		//d1.addOption("16");
		//d1.addOption("32");
		//d1.addOption("64");
		// this.buttons.add(d1);

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

		Shader sha = this.shader;
		if (Color) sha = this.shaderBack;

		// Binding the shader
		sha.bind();
		// Render it.
		render(this.x, this.y, this.texture, sha, this.width, this.height);
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
