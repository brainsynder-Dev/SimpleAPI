/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.utils;

public enum AnvilSlot {
	INPUT_LEFT(0),
	INPUT_CENTER(1),
	OUTPUT(2);

	private int slot;

	AnvilSlot(int slot) {
		this.slot = slot;
	}

	public int getSlot() {
		return slot;
	}

	public static AnvilSlot bySlot(int slot) {
		for (AnvilSlot anvilSlot : values()) {
			if (anvilSlot.getSlot() == slot) {
				return anvilSlot;
			}
		}

		return null;
	}
}