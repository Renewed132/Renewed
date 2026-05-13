package pl.olafcio.renewed.mixinclass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.Chunk;
import pl.olafcio.renewed.mixin.accessors.ITextRenderer;
import pl.olafcio.renewed.mixininterface.IMinecraft;

@Environment(EnvType.CLIENT)
public final class DebugHud {
    public Minecraft mc;
    public DebugHud(Minecraft mc) {
        this.mc = mc;
    }

    public void render(float partialTicks, boolean inScreen, int mouseX, int mouseY) {
        int x = MathHelper.floor(this.mc.playerEntity.x);
        int y = MathHelper.floor(this.mc.playerEntity.y);
        int z = MathHelper.floor(this.mc.playerEntity.z);

        Chunk chunk = (
                this.mc.world != null &&
                        this.mc.world.isPosLoaded(x, y, z)
        )
                ? this.mc.world.getChunkFromPos(x, z)
                : null;

        String[] left = getLeft(chunk, x, y, z);

        int textY = 2;
        for (String line : left) {
            if (!line.isEmpty())
                draw(line, 2, textY);

            textY += 10;
        }

        long memMax = Runtime.getRuntime().maxMemory();
        long memTotal = Runtime.getRuntime().totalMemory();
        long memFree = Runtime.getRuntime().freeMemory();
        long memUsed = memTotal - memFree;

        String[] right = getRight(memUsed, memMax, memTotal);

        textY = 2;
        for (String line : right) {
            if (!line.isEmpty())
                draw(line, (int) (((IMinecraft) mc).window().getScaledWidth() - mc.textRenderer.getStringWidth(line) - 2), textY);

            textY += 10;
        }

//        this.drawWithShadow(textRenderer, "f: " + (MathHelper.floor((double)(this.mc.playerEntity.yaw * 4.0f / 360.0f) + 0.5) & 3), 2, 88, 0xE0E0E0);
//        n3 = MathHelper.floor(this.mc.playerEntity.x);
//        n2 = MathHelper.floor(this.mc.playerEntity.y);
//        n = MathHelper.floor(this.mc.playerEntity.z);
//        if (this.mc.world != null && this.mc.world.isPosLoaded(n3, n2, n)) {
//            Chunk chunk = this.mc.world.getChunkFromPos(n3, n);
//        }
    }

    private String[] getRight(long memUsed, long memMax, long memTotal) {
        return new String[]{
                "Java: " + System.getProperty("java.version") + " " + System.getProperty("sun.arch.data.model") + "bit",
                "Mem:  " + memUsed * 100L / memMax + "% " + memUsed / 1024L / 1024L + "/" + memMax / 1024L / 1024L + "MB",
                "Allocated:  " + memTotal * 100L / memMax + "% " + memTotal / 1024L / 1024L + "MB",
                "",
//                "CPU: ProcessorName",
//                "",
                "Display: " + mc.width + "x" + mc.height + " (Brand)"
//                "GraphicsCard",
//                "0.0.0 - Build 0.0.0.0"
        };
    }

    private String[] getLeft(Chunk chunk, int x, int y, int z) {
        return new String[]{
                "Minecraft {} ({}/vanilla)".replace("{}", FabricLoader.getInstance().getRawGameVersion()),
                this.mc.fpsDebugString,
                this.mc.getChunkDebugString(),
                this.mc.getEntitiesDebugString(),
                "T: " + this.mc.world.loadedEntities.size(),
//                "Chunks[C] W: 0, 0 E: 0,0,0",
//                "Chunks[S] W: 0, E: 0,0,0,0,0,0,0",
                this.mc.world.dimension.getName(),
                "",
                String.format("XYZ: %.3f / %.5f / %.3f", this.mc.cameraEntity.x, this.mc.cameraEntity.boundingBox.minY, this.mc.cameraEntity.z),
                String.format("Block: %.0f %.0f %.0f", this.mc.cameraEntity.x, this.mc.cameraEntity.y, this.mc.cameraEntity.z),
                String.format("Chunk: %d %d %d [%.0f %.0f in r.%d.%d.mca]", chunk.chunkX, (int) this.mc.cameraEntity.y >> 4, chunk.chunkZ, this.mc.cameraEntity.x - chunk.chunkX * 16, this.mc.cameraEntity.z - chunk.chunkZ * 16, chunk.chunkX, chunk.chunkZ),
//                "Facing: ",
                String.format("Client Light: %d (%d sky, %d block)", this.mc.world.getLightmapCoordinates(x, y, z, 0), this.mc.world.method_3642(LightType.SKY, x, y, z), this.mc.world.method_3642(LightType.BLOCK, x, y, z)),
//                "CH S: 0 M: 0",
//                "SH S: 0 O: 0 M: 0 ML: 0",
                "Biome: " + chunk.getBiome(x & 0xF, z & 0xF, this.mc.world.getBiomeSource()).name,
                String.format("Local Difficulty: %d", this.mc.world.difficulty),
//                "NoiseRouter ...",
//                "Biome builder PV: 0 C: Far inland E: 0 T: 0 H: 1",
//                "SC: 0, M: 0, C: 0, A: 0, A: 0, U: 0, W: 0, W: 0, M: 0",
//                String.format("Sounds: %d/247", this.mc.soundSystem.),
                "",
                "Debug: Pie [shift]: " + (this.mc.options.debugProfilerEnabled ? "visible" : "invisible"),
                "For help: press F3 + Q"
        };
    }

    private void draw(String text, int x, int y) {
        TextRenderer textRen = mc.textRenderer;
        ITextRenderer iTextRen = (ITextRenderer) textRen;

        int n = x + textRen.getStringWidth(text);

        DrawableHelper.fill(x-1, y-1, n+1, y+textRen.fontHeight+1, -1873784752);
        iTextRen.renderWithoutShadow(text, x, y, -2039584);
    }
}
