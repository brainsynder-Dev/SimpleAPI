/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.trailer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;

public interface ITrailer<T> {

	T getTarget ();

	void setTarget (T target);

	void setPlayer (Player player);

	IStorage<ParticleMaker> getParticles ();

	void setParticles (IStorage<ParticleMaker> particles);

	Player getPlayer();

	void start(JavaPlugin plugin);

	void clear();
}
