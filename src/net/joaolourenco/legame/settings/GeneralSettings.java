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

package net.joaolourenco.legame.settings;

import java.util.*;

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.graphics.font.*;

/**
 * Class to handle General Settings.
 * 
 * @author Joao Lourenco
 *
 */
public class GeneralSettings {

	/**
	 * ---------------------------------- // Game Variables // ----------------------------------
	 **/
		// This is the array list that will hold all the shaders for a clean up at the end of the running process.
		public static List<Shader> shaders = new ArrayList<Shader>();	
		// This is the array list that will hold all the Font's for a clean up at the end of the running process.
		public static List<Font> fonts = new ArrayList<Font>();	
		// This is the array list that will hold all the AnimatedText to keep them updated.
		public static List<AnimatedText> animatedText = new ArrayList<AnimatedText>();

	/**
	 * ---------------------------------- // General Game Settings // ----------------------------------
	 **/
		// Game version
		public final static String version = "BETA V0.1";
	
		// Game Name
		public final static String name = "LeGame - Remake";
		public final static String fullname = name + " " + version;
	
		// Window Size		
		public final static int WIDTH = 800;
		public final static int HEIGHT = 600;
		public final static int TILE_SIZE = 64;
		public final static int TILE_SIZE_MASK = 6;

	/**
	 * ---------------------------------- // Debugging Settings // ----------------------------------
	 **/
		// Game debugging
		public final static boolean useAverageFPS = true;
		public final static int ticksPerAverage = 10;
		public final static boolean showLightFloat = false;

	/**
	 * ---------------------------------- // Entity Settings // ----------------------------------
	 **/
		// Entity Speed Settings
		public final static float defaultEntityWalking = 2.5f;
		public final static float defaultEntityRunning = 4f;
		
		// Light Settings
		public final static int defaultLightPointSize = 5;
		public final static int defaultLightSize = 10;
		public final static int defaultLightFacing = 10;

	/**
	 * ---------------------------------- // Shaders Settings // ----------------------------------
	 **/
		// Shaders Settings
		public final static String defaultVertexPath = "/shaders/default.vert";
		public final static String blockFragPath = "/shaders/block.frag";
		public final static String lightFragPath = "/shaders/light.frag";
		public final static String fontFragPath = "/shaders/font.frag";
		public final static int howManyLightsToShader = 10; // How many lights will be passed to the shaders.

		public final static Font font = new Font();

}