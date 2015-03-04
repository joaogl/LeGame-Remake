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

package net.joaolourenco.legame.utils;

import net.joaolourenco.legame.*;

/**
 * @author Joao Lourenco
 * 
 */
public class Timer implements Runnable {

	/**
	 * Thread where the timer will run.
	 */
	private Thread thread;
	/**
	 * Variable that keeps track if the timer is running or not.
	 */
	private boolean running = false;

	private String name;
	private boolean finit = false;
	private long lastTimer;
	private int spacing, left;
	private TimerResult result;

	/**
	 * 
	 * @author Joao Lourenco
	 */
	public Timer(String name, int spacing, int amout, TimerResult res) {
		this.name = name;
		this.spacing = spacing;
		this.left = amout;
		this.finit = true;
		this.result = res;
		start();
	}

	public Timer(String name, int spacing, TimerResult res) {
		this.name = name;
		this.spacing = spacing;
		this.finit = false;
		this.result = res;
		start();
	}

	/**
	 * Method that starts the timer.
	 * 
	 * @author Joao Lourenco
	 */
	public synchronized void start() {
		this.thread = new Thread(this, "Timer-" + this.name);
		this.running = true;
		this.thread.start();
	}

	public void run() {
		this.lastTimer = System.currentTimeMillis();
		Main main = Registry.getMainClass();
		while (this.running) {
			if (System.currentTimeMillis() - this.lastTimer > this.spacing) {
				this.lastTimer += this.spacing;
				call();
			}
			if (!main.isRunning()) this.running = false;
		}
	}

	public void call() {
		if (this.finit) {
			this.left--;
			this.result.timerCall(this.name);
			if (this.left <= 0) this.running = false;
		}
	}

}