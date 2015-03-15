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

package net.joaolourenco.legame;

import java.util.List;

import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.font.Font;
import net.joaolourenco.legame.graphics.menu.*;
import net.joaolourenco.legame.graphics.menu.Menu;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.world.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

/**
 * Main Class for the game.
 * 
 * @author Joao Lourenco
 * 
 */
public class Main implements Runnable {

	/**
	 * Thread where the game will run.
	 */
	private Thread thread;
	/**
	 * Variable that keeps track if the game is running or not.
	 */
	private boolean running = false;

	/**
	 * This is the instance for the world.
	 */
	public World world;

	public int fps_lock;

	private double public_delta = 0;
	private int public_fps;

	/**
	 * Main method that runs the game.
	 * 
	 * @param args
	 *            : String[] with anykind of arguments.
	 * @author Joao Lourenco
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}

	/**
	 * Method that starts the game.
	 * 
	 * @author Joao Lourenco
	 */
	public synchronized void start() {
		thread = new Thread(this, GeneralSettings.fullname);
		running = true;
		thread.start();
	}

	/**
	 * Method that stops the game.
	 * 
	 * @author Joao Lourenco
	 */
	public synchronized void stop() {
		running = false;
	}

	/**
	 * Method that initializes the game.
	 * 
	 * @author Joao Lourenco
	 */
	private void init() {
		Settings.SettingsLoader();
		// Setting up the Display
		DisplayMode mode = null;
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (int i = 0; i < modes.length; i++)
				if (modes[i].getBitsPerPixel() == 16 && modes[i].getFrequency() == 60 && modes[i].getWidth() >= 800 && modes[i].getHeight() >= 600) Registry.registerDisplayMode(modes[i]);

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
			Display.create(new PixelFormat(0, 16, 1));

			fps_lock = Integer.parseInt((String) Registry.getSetting("fps_lock"));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Registry.registerFont(new Font());

		// This is for debug purposes only.
		System.out.println("Resolution " + mode.toString());
		System.out.println("OS name " + System.getProperty("os.name"));
		System.out.println("OS version " + System.getProperty("os.version"));
		System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
		System.out.println("OpenGL version " + GL11.glGetString(GL11.GL_VERSION));

		// Loading all the textures
		Texture.preload();

		Registry.registerMainClass(this);
		Registry.registerMenu(new MainMenu());
		Registry.getMenu(0).open();

		Registry.registerPlayer(new Player(32, 32, 64, 64));

		// Setting up all the Projections stuff for OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Registry.getScreenWidth(), Registry.getScreenHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);
	}

	/**
	 * This Method is called on the end of the game to clean the memory.
	 * 
	 * @author Joao Lourenco
	 */
	private void cleanup() {
		// Cleaning data in the Registry class.
		Registry.cleanRegistries();
		// Destroying the Display.
		Display.destroy();
	}

	/**
	 * This Method holds the game loop.
	 * 
	 * @author Joao Lourenco
	 */
	public void run() {
		// Calling the initialize Method to setup our environment
		init();
		// Variables for the FPS counter
		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		long lastTimer = System.currentTimeMillis();
		long tickTimer = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		int checking = 0;
		int sum = 0;
		int avg = 0;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				// 60 times per second this is reached
				update(this.public_delta);
				updates++;
				delta--;
			}
			// Render as many times as u can
			render();
			frames++;
			Display.update();
			if (System.currentTimeMillis() - tickTimer > 100) {
				tickTimer += 100;
				tick();
			}
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				// Once per second this is reached
				this.public_delta = delta;
				this.public_fps = frames;
				String title = GeneralSettings.fullname + " FPS: " + frames + " UPS: " + updates + " Delta: " + this.getDelta();
				if (GeneralSettings.useAverageFPS) title += " Average: " + avg;
				if (GeneralSettings.showLightFloat) {
					if (world != null) title += " Light: " + world.DAY_LIGHT;
				}
				Display.setTitle(title);
				if (GeneralSettings.useAverageFPS) {
					sum += frames;
					checking++;
					if (checking == GeneralSettings.ticksPerAverage) {
						avg = (sum / checking);
						checking = 0;
						sum = 0;
					}
				}
				updates = 0;
				frames = 0;
			}
			if (Display.isCloseRequested()) running = false;
			Display.sync(fps_lock);
		}
		// If the game is closed, cleanup!
		cleanup();
	}

	/**
	 * This is the render Method.
	 * 
	 * @author Joao Lourenco
	 */
	private void render() {
		// Clear the screen from the last render.
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Render the new stuff.
		if (world != null) world.render();

		List<Menu> menus = Registry.getMenus();
		// Render the Menus
		for (int i = 0; i < menus.size(); i++)
			menus.get(i).render();

		// Render the AnimatedText
		List<AnimatedText> animatedTexts = Registry.getAnimatedTexts();
		for (int i = 0; i < animatedTexts.size(); i++)
			animatedTexts.get(i).render();

		// Render the StaticText
		List<StaticText> staticTexts = Registry.getStaticTexts();
		for (int i = 0; i < staticTexts.size(); i++)
			staticTexts.get(i).render();
	}

	/**
	 * This is the update Method and it is updated 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	private void update(double delta) {
		if (world != null && Registry.isGameFocused()) world.update(1);
		// Update the Menus
		List<Menu> menus = Registry.getMenus();
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i) != null) {
				if (menus.get(i).toRemove()) Registry.removeMenu(menus.get(i));
				else menus.get(i).update();
			}
		}
		// Update the AnimatedText
		List<AnimatedText> a = Registry.getAnimatedTexts();
		for (int i = 0; i < a.size(); i++) {
			AnimatedText at = a.get(i);
			if (at != null && !at.isRemoved()) at.update();
			else if (at != null && at.isRemoved()) a.remove(at);
		}
	}

	/**
	 * This is the tick Method and it is updated 10 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	private void tick() {
		GeneralSettings.defaultEntityWalking = this.public_fps * 2.0f / 120.0f;
		GeneralSettings.defaultEntityRunning = this.public_fps * 4.0f / 120.0f;

		if (world != null && Registry.isGameFocused()) world.tick();
		// Tick the Menus
		for (Menu m : Registry.getMenus())
			m.tick();
	}

	public void setWorld(World w) {
		this.world = w;
	}

	public boolean isRunning() {
		return this.running;
	}

	public World getWorld() {
		return this.world;
	}

	public double getDelta() {
		return public_delta;
	}

	public double getFPS() {
		return public_fps;
	}

}