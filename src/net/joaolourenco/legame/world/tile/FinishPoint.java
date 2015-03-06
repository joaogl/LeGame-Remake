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

package net.joaolourenco.legame.world.tile;

import net.joaolourenco.legame.graphics.*;
import net.joaolourenco.legame.settings.*;
import net.joaolourenco.legame.world.*;

/**
 * @author Joao Lourenco
 * 
 */
public class FinishPoint {

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public FinishPoint(World w, int x, int y, int texture) {
		FinishTile t = new FinishTile(GeneralSettings.TILE_SIZE, Texture.FinishPod[0], true, w);
		t.setSecondTexture(texture);
		w.setTile(x, y, t);

		t = new FinishTile(GeneralSettings.TILE_SIZE, Texture.FinishPod[1], true, w);
		t.setSecondTexture(texture);
		w.setTile(x + 1, y, t);

		t = new FinishTile(GeneralSettings.TILE_SIZE, Texture.FinishPod[2], true, w);
		t.setSecondTexture(texture);
		w.setTile(x, y + 1, t);

		t = new FinishTile(GeneralSettings.TILE_SIZE, Texture.FinishPod[3], true, w);
		t.setSecondTexture(texture);
		w.setTile(x + 1, y + 1, t);
	}

}