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

package net.joaolourenco.legame.entity.mob;

import net.joaolourenco.legame.graphics.Texture;
import net.joaolourenco.legame.world.World;

/**
 * @author Joao Lourenco
 * 
 */
public class Skeleton extends Mob {

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @author Joao Lourenco
	 */
	public Skeleton(int x, int y, World w) {
		super(x, y, 128, 128);

		this.setTextureAtlas(Texture.SkeletonWalking, 3, 4, 1);
		this.setAttackingTextureAtlas(Texture.SkeletonAttacking, 3, 3, 1);
		this.setDyingTextureAtlas(Texture.SkeletonDying, 3, 3, 1);

		w.addEntity(this);
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void update() {
		this.updateTexture(0, 0);
	}

	public void updateTexture(int xa, int ya) {
		if (this.side == 0) this.animation = textures[0];
		else if (this.side == 1) this.animation = textures[1];
		else if (this.side == 2) this.animation = textures[0];
		else if (this.side == 3) this.animation = textures[3];

		if (this.moving) this.animation.update();
		else this.animation.resetAnimation();
		this.texture = this.animation.getTexture();
	}

	/**
	 * @author Joao Lourenco
	 */
	@Override
	public void tick() {
	}

}
