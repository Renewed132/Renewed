package pl.olafcio.renewed.mixininterface;

import net.minecraft.client.option.GameOptions;

public interface IWindow {
    int unscaledWidth();
    int unscaledHeight();

    void update(GameOptions gameOptions, int width, int height);
}
