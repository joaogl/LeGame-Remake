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

package net.joaolourenco.legame.items;

import java.util.*;

import net.joaolourenco.legame.entity.*;
import net.joaolourenco.legame.entity.block.*;
import net.joaolourenco.legame.graphics.font.*;

/**
 * Class to manage door keys.
 * 
 * @author Joao Lourenco
 * 
 */
public class DoorKey extends Item {

	public List<String> doors = new ArrayList<String>();

	/**
	 * @author Joao Lourenco
	 */
	public DoorKey(String doorkey) {
		this.name = generateRandom(0, 200, 0) + "-door-" + generateRandom(0, 200, 0) + "-key-" + generateRandom(0, 200, 0);
		this.destructable = false;
		doors.add(doorkey);
	}

	public DoorKey(int uses, String doorkey) {
		this.name = generateRandom(0, 200, 0) + "-door-" + generateRandom(0, 200, 0) + "-key-" + generateRandom(0, 200, 0);
		this.current_life = uses;
		this.inicial_life = uses;
		this.destructable = true;
		doors.add(doorkey);
	}

	public boolean canOpenDoor(Door door) {
		if (door.isJammed()) return false;
		if (doors.contains(door.key)) return true;
		return false;
	}

	public void addDoor(String door) {
		doors.add(door);
	}

	public void useItem(Entity activator) {
	}

	public void itemBroken(Entity activator) {
		new AnimatedText("Ho snap! Key broke.", 50, 55, 12, 5, 50);
	}

}