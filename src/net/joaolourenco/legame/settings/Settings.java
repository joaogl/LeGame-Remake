/* Copyright 2014 Joao Lourenco
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

import java.io.*;
import java.util.*;

import net.joaolourenco.legame.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Settings {

	static String classPath = Settings.class.getProtectionDomain().getCodeSource().getLocation().toString();

	public static void SettingsDefault() {
		Registry.registerSetting("screen_width", "1024");
		Registry.registerSetting("screen_height", "768");
		Registry.registerSetting("fullscreen", "false");
		Registry.registerSetting("fullscreen_windowed", "false");
		Registry.registerSetting("fps_lock", "120");
		Registry.registerSetting("vsync", "true");
	}

	public static void SettingsLoader() {
		System.out.println("Loading settings from: " + classPath);
		try {
			Scanner sc = new Scanner(new BufferedReader(new FileReader("data/settings.conf")));
			while (sc.hasNextLine()) {
				String[] data = sc.nextLine().split("=");
				Registry.registerSetting(data[0].toString(), data[1]);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Settings not found. Using default settings.");
			SettingsDefault();
		}
	}

	public static void SettingsWritter() {
		File f = new File("data/");
		try {
			if (!f.exists()) f.mkdir();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/settings.conf", true)));
			for (Settings_Key s : Registry.getSettings())
				out.println(s.getKey() + "=" + s.getValue());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}