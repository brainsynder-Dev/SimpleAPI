/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.nms;

import org.bukkit.inventory.ItemStack;
import simple.brainsynder.utils.AnvilSlot;

public interface IAnvilGUI {
	void setSlot(AnvilSlot slot, ItemStack item);

	void open();
}
