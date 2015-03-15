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

import java.util.*;

import net.joaolourenco.legame.entity.mob.*;
import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.*;
import net.joaolourenco.legame.settings.*;

import org.lwjgl.opengl.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Registry {

	/**
	 * This is the array list that will hold all the shaders for a clean up at the end of the running process.
	 */
	private static List<Shader> shaders = new ArrayList<Shader>();
	/**
	 * This is the array list that will hold all the AnimatedText to keep them updated.
	 */
	private static List<AnimatedText> animatedText = new ArrayList<AnimatedText>();
	/**
	 * 
	 */
	private static List<StaticText> staticText = new ArrayList<StaticText>();
	/**
	 * This is the array list that will hold all the DisplayModes available.
	 */
	private static List<DisplayMode> displaymodes = new ArrayList<DisplayMode>();
	/**
	 * This is the array list that will hold all the settings available and their value.
	 */
	private static List<Settings_Key> settings = new ArrayList<Settings_Key>();
	/**
	 * This is the array list that will hold all the AnimatedText to keep them updated.
	 */
	private static List<Menu> menus = new ArrayList<Menu>();
	/**
	 * Font used to render fonts in the game.
	 */
	private static Font font;
	/**
	 * Is the game focused.
	 */
	private static boolean focused = true;
	/**
	 * Main class of the game.
	 */
	private static Main main;
	/**
	 * Player Instance.
	 */
	private static Player player;

	public static void registerShader(Shader s) {
		shaders.add(s);
	}

	public static void registerAnimatedText(AnimatedText t) {
		animatedText.add(t);
	}

	public static void registerStaticText(StaticText t) {
		staticText.add(t);
	}

	public static void registerFont(Font f) {
		font = f;
	}

	public static void registerMenu(Menu m) {
		focused = false;
		menus.add(m);
	}

	public static void registerMainClass(Main m) {
		main = m;
	}

	public static void registerPlayer(Player p) {
		player = p;
	}

	public static void registerDisplayMode(DisplayMode dm) {
		displaymodes.add(dm);
	}

	public static void registerSetting(String key, String value) {
		for (Settings_Key k : settings)
			if (k.getKey().equalsIgnoreCase(key)) {
				k.setValue(value);
				return;
			}
		settings.add(new Settings_Key(key, value));
	}

	public static void focusGame() {
		focused = true;
	}

	public static void unFocusGame() {
		focused = false;
	}

	public static List<Shader> getShaders() {
		return shaders;
	}

	public static List<AnimatedText> getAnimatedTexts() {
		return animatedText;
	}

	public static List<StaticText> getStaticTexts() {
		return staticText;
	}

	public static List<Menu> getMenus() {
		return menus;
	}

	public static Shader getShader(int id) {
		return shaders.get(id);
	}

	public static AnimatedText getAnimatedText(int id) {
		return animatedText.get(id);
	}

	public static StaticText getStaticText(int id) {
		return staticText.get(id);
	}

	public static int getScreenWidth() {
		return Integer.parseInt(getSetting("screen_width"));
	}

	public static int getScreenHeight() {
		return Integer.parseInt(getSetting("screen_height"));
	}

	public static Font getFont() {
		return font;
	}

	public static Menu getMenu(int i) {
		return menus.get(i);
	}

	public static boolean isGameFocused() {
		return focused;
	}

	public static Main getMainClass() {
		return main;
	}

	public static Player getPlayer() {
		return player;
	}

	public static DisplayMode getDisplayMode(int w, int h) {
		for (DisplayMode dm : displaymodes)
			if (dm.getWidth() == w && dm.getHeight() == h) return dm;
		return null;
	}

	public static DisplayMode getCurrentDisplayMode() {
		int w = getScreenWidth();
		int h = getScreenHeight();
		for (DisplayMode dm : displaymodes)
			if (dm.getWidth() == w && dm.getHeight() == h) return dm;
		return null;
	}

	public static String getSetting(String key) {
		for (Settings_Key k : settings)
			if (k.getKey().equalsIgnoreCase(key)) return k.getValue().toString();
		return null;
	}

	public static List<Settings_Key> getSettings() {
		return settings;
	}

	public static List<DisplayMode> getDisplayModes() {
		return displaymodes;
	}

	public static void removeMenu(Menu m) {
		// focused = true;
		menus.remove(m);
	}

	public static void removeDisplayMode(DisplayMode dm) {
		displaymodes.remove(dm);
	}

	public static void removeSetting(String key) {
		for (int i = 0; i < settings.size(); i++)
			if (settings.get(i).getKey() == key) settings.remove(i);
	}

	public static void clearAnimatedTexts() {
		// Clear the array.
		animatedText.clear();
	}

	public static void clearStaticTexts() {
		// Clear the array.
		staticText.clear();
	}

	public static void clearDisplayModes() {
		// Clear the array.
		displaymodes.clear();
	}

	public static void cleanRegistries() {
		// Cleaning all the Shaders.
		for (Shader shader : shaders)
			if (shader != null) shader.cleanUp();

		// Clear the arrays.
		shaders.clear();
		animatedText.clear();
		displaymodes.clear();
		settings.clear();
	}

}