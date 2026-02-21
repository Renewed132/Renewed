package pl.olafcio.renewed.commands;

import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.LevelProperties;
import org.jetbrains.annotations.NotNull;

public class WeatherCommand
       extends AbstractCommand
{
    @Override
    public String getCommandName() {
        return "weather";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.weather.usage");
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length != 1)
            throw new IncorrectUsageException("commands.weather.usage");

        if (!(source instanceof ServerPlayerEntity))
            throw new CommandException("commands.weather.player_only");

        ServerWorld world = ((ServerPlayerEntity) source).getServerWorld();
        LevelProperties properties = world.getLevelProperties();

        switch (args[0]) {
            case "clear":
                properties.setThundering(false);
                properties.setRaining(false);
                break;

            case "rain":
                properties.setThundering(false);
                properties.setRaining(true);
                break;

            case "thunder":
                properties.setThundering(true);
                properties.setRaining(true);
                break;

            default:
                throw new SyntaxException("commands.weather.expected_weather", args[0]);
        }

        source.method_3331(source.translate("commands.weather.success." + args[0])); // sendMessage
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }
}
