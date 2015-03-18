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

import java.util.List;

import net.joaolourenco.legame.Registry;
import net.joaolourenco.legame.graphics.Shader;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.font.Font;
import net.joaolourenco.legame.graphics.font.StaticText;
import net.joaolourenco.legame.graphics.menu.objects.ClickAction;
import net.joaolourenco.legame.graphics.menu.objects.MenuActionReader;
import net.joaolourenco.legame.graphics.menu.objects.MenuButton;
import net.joaolourenco.legame.graphics.menu.objects.MenuCheckBox;
import net.joaolourenco.legame.graphics.menu.objects.MenuCloud;
import net.joaolourenco.legame.graphics.menu.objects.MenuOptionSelect;
import net.joaolourenco.legame.graphics.menu.objects.MenuSlider;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.settings.Settings;
import net.joaolourenco.legame.utils.KeyboardFilter;
import net.joaolourenco.legame.world.RandomWorld;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Toolkit;

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

	public MenuCheckBox full, window, vsync;
	public MenuOptionSelect resolution;
	public MenuSlider FPS_Lock;

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
		// window = new MenuCheckBox("Fullscreen-Windowed", (this.xMax / 5) * 4, yPos + (50 * (i++ - 1)), size, spacing, this, shader);
		full.setSelected(Boolean.valueOf((String) Registry.getSetting("fullscreen")));
		// window.setSelected(Boolean.valueOf((String) Registry.getSetting("fullscreen_windowed")));
		// this.buttons.add(window);
		this.buttons.add(full);

		if (full.isSelected()) {
			window.setEnabled(false);
			window.setSelected(false);
		}
		this.full.addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				OptionsMenu menu = ((OptionsMenu) m);
				/*if (menu.full.isSelected()) {
					menu.window.setEnabled(false);
					menu.window.setSelected(false);
				} else menu.window.setEnabled(true);*/
			}
		});

		/*
		 * if (window.isSelected()) { full.setEnabled(false); full.setSelected(false); } this.window.addClickAction(new ClickAction() { public void onClick(Menu m) { OptionsMenu menu = ((OptionsMenu) m); if (menu.window.isSelected()) { menu.full.setEnabled(false); menu.full.setSelected(false); menu.resolution.setEnabled(false); } else { menu.full.setEnabled(true); menu.resolution.setEnabled(true); } } });
		 */

		/*
		 * resolution = new MenuOptionSelect("Resolution: ", (this.xMax / 5) * 3, yPos + (50 * i++), size + 5, spacing, this); if (window.isSelected()) resolution.setEnabled(false); List<DisplayMode> modes = Registry.getDisplayModes(); for (int ii = 0; ii < modes.size(); ii++) resolution.addOption(modes.get(ii).getWidth() + "x" + modes.get(ii).getHeight());
		 * 
		 * resolution.setActive(Registry.getScreenWidth() + "x" + Registry.getScreenHeight()); this.buttons.add(resolution);
		 */

		vsync = new MenuCheckBox("Vertical Sync", (this.xMax / 4) * 3, yPos + (50 * (i++ + 1)), size, spacing, this, shader);
		vsync.setSelected(Boolean.valueOf((String) Registry.getSetting("vsync")));
		this.buttons.add(vsync);

		FPS_Lock = new MenuSlider("FPS Lock: ", (this.xMax / 4), yPos + (50 * (i++)), 250, size, spacing, this, 30, 300, "FPS");
		FPS_Lock.setPosition(Integer.parseInt(Registry.getSetting("fps_lock")));
		this.buttons.add(FPS_Lock);

		this.buttons.add(new MenuButton("Apply", this.xMax / 4 - 40, this.yMax - 50, size, spacing, this));
		this.buttons.get(i++).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				((OptionsMenu) m).RecreateWindow();
				m.close();
			}
		});

		this.buttons.add(new MenuButton("Save", this.xMax / 4 + 40, this.yMax - 50, size, spacing, this));
		this.buttons.get(i++).addClickAction(new ClickAction() {
			public void onClick(Menu m) {
				((OptionsMenu) m).RecreateWindow();
				Settings.SettingsWritter();
				m.close();
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

	public void RecreateWindow() {
		// Display.destroy();
		// Registry.registerSetting("screen_width", resolution.getSelected().split("x")[0]);
		// Registry.registerSetting("screen_height", resolution.getSelected().split("x")[1]);
		Registry.registerSetting("fullscreen", String.valueOf(full.isSelected()));
		// Registry.registerSetting("fullscreen_windowed", String.valueOf(window.isSelected()));
		Registry.registerSetting("fps_lock", String.valueOf(FPS_Lock.getValue()));
		Registry.registerSetting("vsync", String.valueOf(vsync.isSelected()));

		Registry.clearDisplayModes();

		// Setting up the Display
		DisplayMode mode = null;
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (int i = 0; i < modes.length; i++)
				if (modes[i].getFrequency() == 60 && modes[i].getWidth() >= 800 && modes[i].getHeight() >= 600) Registry.registerDisplayMode(modes[i]);

			if (Boolean.valueOf((String) Registry.getSetting("fullscreen_windowed")) && !Boolean.valueOf((String) Registry.getSetting("fullscreen"))) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int) screenSize.getWidth();
				int height = (int) screenSize.getHeight();

				mode = Registry.getDisplayMode(width, height);
				Registry.registerSetting("screen_width", "" + width);
				Registry.registerSetting("screen_height", "" + height);

				Display.setLocation(-3, -20);
			}
			if (mode == null) {
				for (int i = 0; i < modes.length; i++) {
					if (modes[i].getWidth() == Registry.getScreenWidth() && modes[i].getHeight() == Registry.getScreenHeight() && modes[i].getBitsPerPixel() >= 32 && modes[i].getFrequency() == 60) {
						mode = modes[i];
						break;
					}
				}
			}

			Display.setDisplayMode(mode);
			Display.setFullscreen(Boolean.valueOf((String) Registry.getSetting("fullscreen")));
			Display.setVSyncEnabled(Boolean.valueOf((String) Registry.getSetting("vsync")));
			Display.setTitle(GeneralSettings.fullname);
			// Display.create(new PixelFormat(0, 32, 1));

			Registry.getMainClass().fps_lock = Integer.parseInt(Registry.getSetting("fps_lock"));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Registry.registerFont(new Font());

		// This is for debug purposes only.
		System.out.println("======== Changed Resolution ========");
		// System.out.println("Resolution " + mode.toString());
		System.out.println("OS name " + System.getProperty("os.name"));
		System.out.println("OS version " + System.getProperty("os.version"));
		System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
		System.out.println("OpenGL version " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("================");

		// Setting up all the Projections stuff for OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Registry.getScreenWidth(), Registry.getScreenHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);

		List<Shader> shaders = Registry.getShaders();
		for (int i = 0; i < shaders.size(); i++)
			shader.recompile();

		// Loading all the textures
		Texture.preload();
		Texture.load();

		if (Registry.getMainClass().getWorld() != null) {
			Registry.registerGameReload();
			Registry.getMainClass().setWorld(new RandomWorld(1));
		} else {
			Registry.registerMenu(new MainMenu());
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
