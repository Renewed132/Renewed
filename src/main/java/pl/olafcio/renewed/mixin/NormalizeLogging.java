package pl.olafcio.renewed.mixin;

import net.minecraft.advancement.AchievementsAndCriterions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.ResourceDownloadThread;
import net.minecraft.entity.TrackedEntityInstance;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.level.storage.AnvilLevelStorage;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;
import paulscode.sound.SoundSystemLogger;

import java.io.PrintStream;
import java.util.logging.Logger;

@Mixin({ MinecraftApplet.class, Minecraft.class, AchievementsAndCriterions.class, RecipeDispatcher.class, SoundSystemLogger.class, ResourceDownloadThread.class, TrackedEntityInstance.class, Profiler.class, LevelStorage.class, BlockEntity.class, AnvilLevelStorage.class })
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
                    "message", "importantMessage",
                    // ResourceDownloadThread
                    "method_800",
                    // TrackedEntityInstance
                    "method_2182",
                    // Profiler
                    "pop",
                    // LevelStorage
                    "method_200",
                    // BlockEntity
                    "createFromNbt",
                    // AnvilLevelStorage
                    "convert", "makeMcrLevelDatBackup", "convertRegion"
            },
            require = 0
    )
    private static void println(PrintStream instance, String text) {
        Logger.getAnonymousLogger().info(text);
    }

    @SuppressWarnings("all")
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"
            ),
            target = {
                    // SoundSystemLogger
                    @Desc(owner = SoundSystemLogger.class, value = "errorMessage", args = { String.class, String.class, int.class })
            },
            require = 0
    )
    private static void printlnError(PrintStream instance, String text) {
        Logger.getAnonymousLogger().warning(text);
    }
}
