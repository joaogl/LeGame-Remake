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

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;

import net.joaolourenco.legame.entity.mob.Player;
import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.graphics.font.AnimatedText;
import net.joaolourenco.legame.graphics.font.Font;
import net.joaolourenco.legame.graphics.menu.MainMenu;
import net.joaolourenco.legame.graphics.menu.Menu;
import net.joaolourenco.legame.settings.GeneralSettings;
import net.joaolourenco.legame.world.World;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

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
		// Setting up the Display
		try {
			// Registry.registerScreen(1024, 768);
			Registry.registerScreen(800, 600);

			DisplayMode mode = null;
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++)
				System.out.println("W: " + modes[i].getWidth() + " H: " + modes[i].getHeight() + " Bits: " + modes[i].getBitsPerPixel() + " Freq: " + modes[i].getFrequency());

			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == Registry.getScreenWidth() && modes[i].getHeight() == Registry.getScreenHeight() && modes[i].getBitsPerPixel() >= 32 && modes[i].getFrequency() == 60) {
					mode = modes[i];
					break;
				}
			}

			Display.setDisplayMode(mode);
			Display.setTitle(GeneralSettings.fullname);
			Display.create(new PixelFormat(0, 16, 1));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Registry.registerFont(new Font());

		// This is for debug purposes only.
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
		double delta = 0;
		long lastTimer = System.currentTimeMillis();
		long tickTimer = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		int checking = 0;
		int sum = 0;
		int avg = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				// 60 times per second this is reached
				update();
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
				String title = GeneralSettings.fullname + " FPS: " + frames + " UPS: " + updates;
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
			Display.sync(120);
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
		// Render the Menus
		for (Menu m : Registry.getMenus())
			m.render();

		// Render the AnimatedText
		try {
			for (AnimatedText at : Registry.getAnimatedTexts())
				at.render();
		} catch (ConcurrentModificationException e) {
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This is the update Method and it is updated 60 times per second.
	 * 
	 * @author Joao Lourenco
	 */
	private void update() {
		if (world != null) world.update();
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
		if (world != null) world.tick();
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
}