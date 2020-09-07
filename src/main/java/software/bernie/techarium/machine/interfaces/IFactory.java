package software.bernie.techariumbotanica.machine.interfaces;

import javax.annotation.Nonnull;

public interface IFactory<T> {

    @Nonnull
    T create();

}
