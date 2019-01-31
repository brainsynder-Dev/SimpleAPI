package simple.brainsynder.trailer;

import org.bukkit.entity.Player;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;

public interface IAsyncTrailer<T> extends ITrailer<T> {

    T getTarget ();

    void setTarget (T target);

    void setPlayer (Player player);

    IStorage<ParticleMaker> getParticles ();

    void setParticles (IStorage<ParticleMaker> particles);
}
