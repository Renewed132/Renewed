package pl.olafcio.renewed.mixininterface;

import org.jetbrains.annotations.ApiStatus;

public interface IParticle {
    @ApiStatus.Internal
    void setLoadedFromTerrain(boolean value);
}
