package simple.brainsynder.trailer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;

import java.util.List;

public class Trailer<T> implements ITrailer<T> {
    private Player player;
    private IStorage<ParticleMaker> particles;
    private T target;

    @Override public void start(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (target == null) {
                    cancel();
                    return;
                }
    
                if (target instanceof Entity) {
                    if (!((Entity) target).isValid()) {
                        cancel();
                        return;
                    }
                }
    
                if (target instanceof Projectile) {
                    if (((Projectile) target).isOnGround()) {
                        cancel();
                        return;
                    }
                }

                if (getPlayer() == null) {
                    cancel();
                    return;
                }

                if (!getPlayer().isOnline()) {
                    cancel();
                    return;
                }

                IStorage<ParticleMaker> storage = getParticles().copy();
                if (!storage.isEmpty()) {
                    while (storage.hasNext()) {
                        ParticleMaker particle = storage.next();
                        if (target instanceof Entity) {
                            particle.sendToLocation(((Entity) target).getLocation());
                        } else if (target instanceof Location) {
                            particle.sendToLocation(((Location) target));
                        } else if (target instanceof List) {
                            List<T> lists = ((List<T>) target);
                            if (!lists.isEmpty())
                                for (T v : lists) {
                                    if (v instanceof Entity) {
                                        particle.sendToLocation(((Entity) v).getLocation());
                                    } else if (v instanceof Location) {
                                        particle.sendToLocation(((Location) v));
                                    }
                                }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);

    }

    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public void setTarget(T target) {
        this.target = target;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public IStorage<ParticleMaker> getParticles() {
        return particles;
    }

    @Override
    public void setParticles(IStorage<ParticleMaker> particles) {
        this.particles = particles;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override public void clear() {
        target = null;
    }
}
