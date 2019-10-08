/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Deprecated
public interface Trailer {
	Player getPlayer ();
	ParticleMaker getParticles ();
	void start (Plugin plugin);
	void clear ();
}
