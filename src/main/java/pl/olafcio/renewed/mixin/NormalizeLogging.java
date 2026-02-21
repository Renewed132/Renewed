package pl.olafcio.renewed.mixin;

import net.minecraft.advancement.AchievementsAndCriterions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.ResourceDownloadThread;
import net.minecraft.recipe.RecipeDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import paulscode.sound.SoundSystemLogger;

import java.io.PrintStream;
import java.util.logging.Logger;

@Mixin({ MinecraftApplet.class, Minecraft.class, AchievementsAndCriterions.class, RecipeDispatcher.class, SoundSystemLogger.class, ResourceDownloadThread.class })
public class NormalizeLogging {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"
            ),
            method = {
                    // MinecraftApplet
                    "init",
                    // Minecraft
                    "initializeGame", "forceReload", "stop",
                    // RecipeDispatcher
                    "<init>",
                    // AchievementsAndCriterions
                    "<clinit>",
                    // SoundSystemLogger
                    "message", "importantMessage", "errorMessage",
                    // ResourceDownloadThread
                    "method_800"
            },
            require = 0
    )
    private static void println(PrintStream instance, String text) {
        Logger.getAnonymousLogger().info(text);
    }
}
