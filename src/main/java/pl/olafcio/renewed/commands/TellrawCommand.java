package pl.olafcio.renewed.commands;

import com.google.gson.*;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.command.SyntaxException;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;

public class TellrawCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "tellraw";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return source.translate("commands.tellraw.usage");
    }

    private final HashMap<String, String> COLOR_MAP = new HashMap<String, String>() {{
        put("white", "f");
        put("red", "c");
        put("gold", "6");
        put("yellow", "e");
        put("green", "a");
        put("blue", "3");
        put("aqua", "b");
        put("pink", "d");
        put("gray", "7");
        put("black", "0");
        put("dark_red", "4");
        put("dark_green", "2");
        put("dark_blue", "1");
        put("dark_pink", "5");
        put("dark_gray", "8");
    }};

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args.length > 0) {
            char[] raw = method_2892(args, 0).toCharArray();

            try {
                JsonElement element = new Gson().fromJson(new CharArrayReader(raw), JsonElement.class);
                String output = handleObject(source, element);

                MinecraftServer.getServer().getPlayerManager().sendToAll(new ChatMessageS2CPacket(output));
            } catch (JsonSyntaxException e) {
                throw new SyntaxException("commands.tellraw.invalid_json");
            }
        } else {
            throw new IncorrectUsageException("commands.tellraw.usage");
        }
    }

    private String handleObject(CommandSource source, JsonElement element) {
        StringBuilder output = new StringBuilder();

        if (element instanceof JsonObject) {
            JsonObject compound = (JsonObject) element;

            if (!(compound.get("text") instanceof JsonPrimitive) ||
                !((JsonPrimitive) compound.get("text")).isString())
                throw new SyntaxException("commands.tellraw.expected_text");

            String text = compound.get("text").getAsString();
            String color;

            if (compound.get("color") != null) {
                color = compound.get("color").getAsString();

                if (COLOR_MAP.containsKey(color)) {
                    output.append("§").append(COLOR_MAP.get(color));
                } else if (color.startsWith("#")) {
                    output.append("§x").append(color.substring(1).replaceAll("([a-zA-Z0-9])", "§$1"));
                } else {
                    throw new SyntaxException("commands.tellraw.expected_a_color");
                }
            }

            boolean bold = getBoolean(compound, "bold");
            boolean italic = getBoolean(compound, "italic");
            boolean underline = getBoolean(compound, "underlined");
            boolean strikethrough = getBoolean(compound, "strikethrough");
            boolean obfuscated = getBoolean(compound, "obfuscated");

            if (bold) output.append("§l");
            if (italic) output.append("§o");
            if (underline) output.append("§n");
            if (strikethrough) output.append("§m");
            if (obfuscated) output.append("§k");

            if (compound.has("extra")) {
                JsonElement extra = compound.get("extra");
                if (extra.isJsonArray())
                    output.append(handleObject(source, extra));
                else throw new SyntaxException("commands.tellraw.expected_an_array.extra");
            }

            output.append(text);
        } else if (element instanceof JsonArray) {
            JsonArray array = (JsonArray) element;
            for (JsonElement el : array)
                output.append(handleObject(source, el));
        } else if (element instanceof JsonPrimitive) {
            JsonPrimitive primitive = (JsonPrimitive) element;
            if (primitive.isString())
                output.append(primitive.getAsString());
            else throw new SyntaxException("commands.tellraw.expected_a_message");
        } else {
            throw new SyntaxException("commands.tellraw.expected_a_message");
        }

        return output.toString();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this == o ? 0 : -1;
    }

    protected boolean getBoolean(JsonObject object, String member) {
        if (!object.has(member))
            return false;

        return object.get(member).getAsBoolean();
    }
}
