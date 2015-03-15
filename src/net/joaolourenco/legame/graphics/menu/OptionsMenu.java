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

import java.util.*;

import net.joaolourenco.legame.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.objects.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.utils.*;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

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

	public MenuCheckBox full, window;
	public MenuOptionSelect resolution;

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
			nc.setY((Float) nc.generateRandom(0, 50, 1));
			clouds[i] = nc;
		}

		int i = 0;
		int size = 15;
		if (Registry.getScreenWidth() > 1000) size = 20;
		int spacing = -5;
		int yPos = ((Registry.getScreenHeight() - 50 * 5) / 2);

		Registry.registerStaticText(new StaticText("Display Mode: ", this.xMax / 6, yPos + (50 * i), size + 2));
		Registry.registerStaticText(new StaticText("Leave deselected to use Window Mode.", this.xMax / 5, yPos + (50 * i) + 20, size - 3 + 2));

		full = new MenuCheckBox("Fullscreen", this.xMax / 2, yPos + (50 * i++), size, spacing, this, shader);
		window = new MenuCheckBox("Fullscreen-Windowed", (this.xMax / 5) * 4, yPos + (50 * (i++ - 1)), size, spacing, this, shader);
		full.setSelected(Boolean.valueOf((String) Registry.getSetting("fullscreen")));
		window.setSelected(Boolean.valueOf((String) Registry.getSetting("fullscreen_windowed")));
		this.buttons.add(window);
		this.buttons.add(full);

		if (full.isSelected()) {
			window.setEnabled(false);
			window.setSelected(false);
		}
		this.full.addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				OptionsMenu menu = ((OptionsMenu) m);
				if (menu.full.isSelected()) {
					menu.window.setEnabled(false);
					menu.window.setSelected(false);
				} else menu.window.setEnabled(true);
			}
		});

		if (window.isSelected()) {
			full.setEnabled(false);
			full.setSelected(false);
		}
		this.window.addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				OptionsMenu menu = ((OptionsMenu) m);
				if (menu.window.isSelected()) {
					menu.full.setEnabled(false);
					menu.full.setSelected(false);
				} else menu.full.setEnabled(true);
			}
		});

		resolution = new MenuOptionSelect("Resolution: ", (this.xMax / 5) * 3, yPos + (50 * i++), size + 5, spacing, this);
		List<DisplayMode> modes = Registry.getDisplayModes();
		for (int ii = 0; ii < modes.size(); ii++)
			resolution.addOption(modes.get(ii).getWidth() + "x" + modes.get(ii).getHeight());
		this.buttons.add(resolution);

		MenuCheckBox vsync = new MenuCheckBox("Vertical Sync", (this.xMax / 4) * 3, yPos + (50 * (i++ + 1)), size, spacing, this, shader);
		vsync.setSelected(Boolean.valueOf((String) Registry.getSetting("vsync")));
		this.buttons.add(vsync);

		MenuSlider FPS_Lock = new MenuSlider("FPS Lock: ", (this.xMax / 4), yPos + (50 * (i++)), 250, size, spacing, this, 30, 300, "FPS");
		this.buttons.add(FPS_Lock);

		this.buttons.add(new MenuButton("Apply", this.xMax / 4, this.yMax - 50, size, spacing, this));
		this.buttons.get(i++).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				m.close();
				Registry.registerMenu(new MainMenu());
			}
		});

		this.buttons.add(new MenuButton("Back", (this.xMax / 4) * 3, this.yMax - 50, size, spacing, this));
		this.buttons.get(i++).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				m.close();
				Registry.registerMenu(new MainMenu());
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
		if (KeyboardFilter.isKeyDown(Keyboard.KEY_ESCAPE) && Registry.getMainClass().getWorld() != null) {
			this.close();
			Registry.registerMenu(new MainMenu());
		}

		for (int i = 0; i < maxClouds; i++)
			if (clouds[i] == null) clouds[i] = new MenuCloud(shader, 50);
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
