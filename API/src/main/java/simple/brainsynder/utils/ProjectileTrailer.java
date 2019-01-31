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

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.api.Trailer;

public class ProjectileTrailer implements Trailer {
    private Projectile projectile1;
    private ParticleMaker particle;
    private Player player;
    private boolean onlyPlayer = false;
    
    public ProjectileTrailer(Projectile projectile1, Player player, ParticleMaker particle) {
        this (false, projectile1, player, particle);
    }
    
    public ProjectileTrailer(boolean onlyPlayer, Projectile projectile1, Player player, ParticleMaker particle) {
        this.projectile1 = projectile1;
        this.particle = particle;
        this.player = player;
        this.onlyPlayer = onlyPlayer;
    }

    @Override
    public void clear() {
        if (this.projectile1.isValid()) {
            this.projectile1.remove();
        }
    }

    public Projectile getProjectile() {
        return this.projectile1;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ParticleMaker getParticles() {
        return this.particle;
    }

    @Override
    public void start(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!getProjectile().isValid()) {
                    cancel();
                    return;
                }

                if (getProjectile().isOnGround()) {
                    cancel();
                    return;
                }

                if (!getPlayer().isOnline()) {
                    cancel();
                    return;
                }
                
                if (onlyPlayer) {
                    getParticles().sendToPlayer(getPlayer(), getProjectile().getLocation());
                }else{
                    getParticles().sendToLocation(getProjectile().getLocation());
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
