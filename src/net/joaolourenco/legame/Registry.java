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

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;
import net.joaolourenco.legame.graphics.menu.*;
import net.joaolourenco.legame.utils.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Registry {

	// This is the array list that will hold all the shaders for a clean up at the end of the running process.
	private static List<Shader> shaders = new ArrayList<Shader>();
	// This is the array list that will hold all the AnimatedText to keep them updated.
	private static List<AnimatedText> animatedText = new ArrayList<AnimatedText>();
	// This is the array list that will hold all the AnimatedText to keep them updated.
	private static List<Menu> menus = new ArrayList<Menu>();
	// Where the screen information is saved.
	private static Screen screen;
	// Font used to render fonts in the game.
	private static Font font;
	// Is the game focused.
	private static boolean focused = true;

	public static void registerShader(Shader s) {
		shaders.add(s);
	}

	public static void registerAnimatedText(AnimatedText t) {
		animatedText.add(t);
	}

	public static void registerScreen(int w, int h) {
		screen = new Screen(w, h);
	}

	public static void registerFont(Font f) {
		font = f;
	}

	public static void registerMenu(Menu m) {
		menus.add(m);
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

	public static List<Menu> getMenus() {
		return menus;
	}

	public static Shader getShader(int id) {
		return shaders.get(id);
	}

	public static AnimatedText getAnimatedText(int id) {
		return animatedText.get(id);
	}

	public static Screen getScreen() {
		return screen;
	}

	public static int getScreenWidth() {
		return screen.getWidth();
	}

	public static int getScreenHeight() {
		return screen.getHeight();
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

	public static void cleanRegistries() {
		// Cleaning all the Shaders.
		for (Shader shader : shaders)
			if (shader != null) shader.cleanUp();

		// Clear the arrays.
		shaders.clear();
		animatedText.clear();
	}

}