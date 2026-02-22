package pl.olafcio.renewed.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.BlockRenderer;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {
    @Unique
    private static final double TERRAIN_SPRITESHEET_HEIGHT = 288.0;

    @Shadow private int field_2049;
    @Shadow private boolean field_2050;
    @Shadow private int field_2052;
    @Shadow private boolean field_2058;
    @Shadow private float field_2038;
    @Shadow private int field_2034;
    @Shadow private float field_2042;
    @Shadow private float field_2046;
    @Shadow private float field_2005;
    @Shadow private float field_2006;
    @Shadow private int field_2037;
    @Shadow private float field_2041;
    @Shadow private float field_2040;
    @Shadow private float field_2039;
    @Shadow private int field_2035;
    @Shadow private int field_2036;
    @Shadow private float field_2044;
    @Shadow private float field_2043;
    @Shadow private float field_2045;
    @Shadow private float field_2007;
    @Shadow private int field_2053;
    @Shadow private int field_2054;
    @Shadow private int field_2055;
    @Shadow private int field_2056;
    @Shadow private int field_2057;

    @Inject(at = @At("HEAD"), method = "method_1461", cancellable = true)
    public void render_1461(Block block, double d, double e, double f, int i, CallbackInfo ci) {
        ci.cancel();

        Tessellator tessellator = Tessellator.INSTANCE;
        if (this.field_2049 >= 0)
            i = this.field_2049;

        int var10 = (i & 15) << 4;
        int var11 = i & 496;

        double u2 = (var10 + block.boundingBoxMinX * 16.0) / 256.0;
        double u4 = (var10 + block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
        double v2 = (var11 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
        double v4 = (var11 + 16 - block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;

        if (this.field_2050) {
            double var20 = u2;
            u2 = u4;
            u4 = var20;
        }

        if (block.boundingBoxMinX < 0.0 || block.boundingBoxMaxX > 1.0) {
            u2 = (var10 + 0.0F) / 256.0F;
            u4 = (var10 + 15.99F) / 256.0F;
        }

        if (block.boundingBoxMinY < 0.0 || block.boundingBoxMaxY > 1.0) {
            v2 = (var11 + 0.0F) / TERRAIN_SPRITESHEET_HEIGHT;
            v4 = (var11 + 15.99F) / TERRAIN_SPRITESHEET_HEIGHT;
        }

        double u = u4;
        double u3 = u2;
        double v = v2;
        double v3 = v4;
        if (this.field_2052 == 2) {
            u2 = (var10 + block.boundingBoxMinY * 16.0) / 256.0;
            v2 = (var11 + 16 - block.boundingBoxMinX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            u4 = (var10 + block.boundingBoxMaxY * 16.0) / 256.0;
            v4 = (var11 + 16 - block.boundingBoxMaxX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            v = v2;
            v3 = v4;
            u = u2;
            u3 = u4;
            v2 = v4;
            v4 = v2;
        } else if (this.field_2052 == 1) {
            u2 = (var10 + 16 - block.boundingBoxMaxY * 16.0) / 256.0;
            v2 = (var11 + block.boundingBoxMaxX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            u4 = (var10 + 16 - block.boundingBoxMinY * 16.0) / 256.0;
            v4 = (var11 + block.boundingBoxMinX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            u = u4;
            u3 = u2;
            u2 = u4;
            u4 = u2;
            v = v4;
            v3 = v2;
        } else if (this.field_2052 == 3) {
            u2 = (var10 + 16 - block.boundingBoxMinX * 16.0) / 256.0;
            u4 = (var10 + 16 - block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
            v2 = (var11 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            v4 = (var11 + block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
            u = u4;
            u3 = u2;
            v = v2;
            v3 = v4;
        }

        double x1 = d + block.boundingBoxMinX;
        double x2 = d + block.boundingBoxMaxX;
        double y2 = e + block.boundingBoxMinY;
        double y1 = e + block.boundingBoxMaxY;
        double z = f + block.boundingBoxMinZ;
        if (this.field_2058) {
            vertex1(tessellator, x1, y1, z, u, v);
            vertex2(tessellator, x2, y1, z, u2, v2);
            vertex3(tessellator, x2, y2, z, u3, v3);
            vertex4(tessellator, x1, y2, z, u4, v4);
        } else {
            tessellator.vertex(x1, y1, z, u, v);
            tessellator.vertex(x2, y1, z, u2, v2);
            tessellator.vertex(x2, y2, z, u3, v3);
            tessellator.vertex(x1, y2, z, u4, v4);
        }
    }

    @Unique
    private void vertex1(Tessellator tessellator, double x, double y, double z, double u, double v) {
        tessellator.color(this.field_2038, this.field_2042, this.field_2046);
        tessellator.setLight(this.field_2034);
        tessellator.vertex(x, y, z, u, v);
    }

    @Unique
    private void vertex2(Tessellator tessellator, double x2, double y1, double z, double u2, double v2) {
        tessellator.color(this.field_2039, this.field_2043, this.field_2005);
        tessellator.setLight(this.field_2035);
        tessellator.vertex(x2, y1, z, u2, v2);
    }

    @Unique
    private void vertex3(Tessellator tessellator, double x2, double y2, double z, double u3, double v3) {
        tessellator.color(this.field_2040, this.field_2044, this.field_2006);
        tessellator.setLight(this.field_2036);
        tessellator.vertex(x2, y2, z, u3, v3);
    }

    @Unique
    private void vertex4(Tessellator tessellator, double x1, double y2, double z, double u4, double v4) {
        tessellator.color(this.field_2041, this.field_2045, this.field_2007);
        tessellator.setLight(this.field_2037);
        tessellator.vertex(x1, y2, z, u4, v4);
    }

    @Inject(at = @At("HEAD"), method = "method_1465", cancellable = true)
    public void render_1465(Block block, double d, double e, double f, int i, CallbackInfo ci) {
        ci.cancel();

        Tessellator var9 = Tessellator.INSTANCE;
        if (this.field_2049 >= 0) {
            i = this.field_2049;
        }

        int var10 = (i & 15) << 4;
        int var11 = i & 496;
        double var12 = (var10 + block.boundingBoxMinX * 16.0) / 256.0;
        double var14 = (var10 + block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
        double var16 = (var11 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
        double var18 = (var11 + 16 - block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
        if (this.field_2050) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.boundingBoxMinX < 0.0 || block.boundingBoxMaxX > 1.0) {
            var12 = (var10 + 0.0F) / 256.0F;
            var14 = (var10 + 15.99F) / 256.0F;
        }

        if (block.boundingBoxMinY < 0.0 || block.boundingBoxMaxY > 1.0) {
            var16 = (var11 + 0.0F) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + 15.99F) / TERRAIN_SPRITESHEET_HEIGHT;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_2053 == 1) {
            var12 = (var10 + block.boundingBoxMinY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + 16 - block.boundingBoxMinX * 16.0) / 256.0;
            var14 = (var10 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var16 = (var11 + 16 - block.boundingBoxMaxX * 16.0) / 256.0;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var16;
        } else if (this.field_2053 == 2) {
            var12 = (var10 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var16 = (var11 + block.boundingBoxMinX * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMinY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + block.boundingBoxMaxX * 16.0) / 256.0;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var12;
            var24 = var18;
            var26 = var16;
        } else if (this.field_2053 == 3) {
            var12 = (var10 + 16 - block.boundingBoxMinX * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
            var16 = (var11 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = d + block.boundingBoxMinX;
        double var30 = d + block.boundingBoxMaxX;
        double var32 = e + block.boundingBoxMinY;
        double var34 = e + block.boundingBoxMaxY;
        double var36 = f + block.boundingBoxMaxZ;
        if (this.field_2058) {
            vertex1(var9, var28, var34, var36, var12, var16);
            vertex2(var9, var28, var32, var36, var22, var26);
            vertex3(var9, var30, var32, var36, var14, var18);
            vertex4(var9, var30, var34, var36, var42, var24);
        } else {
            var9.vertex(var28, var34, var36, var12, var16);
            var9.vertex(var28, var32, var36, var22, var26);
            var9.vertex(var30, var32, var36, var14, var18);
            var9.vertex(var30, var34, var36, var42, var24);
        }
    }

    @Inject(at = @At("HEAD"), method = "method_1468", cancellable = true)
    public void render_1468(Block block, double d, double e, double f, int i, CallbackInfo ci) {
        ci.cancel();

        Tessellator var9 = Tessellator.INSTANCE;
        if (this.field_2049 >= 0) {
            i = this.field_2049;
        }

        int var10 = (i & 15) << 4;
        int var11 = i & 496;
        double var12 = (var10 + block.boundingBoxMinZ * 16.0) / 256.0;
        double var14 = (var10 + block.boundingBoxMaxZ * 16.0 - 0.01) / 256.0;
        double var16 = (var11 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
        double var18 = (var11 + 16 - block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
        if (this.field_2050) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.boundingBoxMinZ < 0.0 || block.boundingBoxMaxZ > 1.0) {
            var12 = (var10 + 0.0F) / 256.0F;
            var14 = (var10 + 15.99F) / 256.0F;
        }

        if (block.boundingBoxMinY < 0.0 || block.boundingBoxMaxY > 1.0) {
            var16 = (var11 + 0.0F) / 256.0F;
            var18 = (var11 + 15.99F) / 256.0F;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_2055 == 1) {
            var12 = (var10 + block.boundingBoxMinY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var16 = (var11 + 16 - block.boundingBoxMaxZ * 16.0) / 256.0;
            var14 = (var10 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + 16 - block.boundingBoxMinZ * 16.0) / 256.0;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var16;
        } else if (this.field_2055 == 2) {
            var12 = (var10 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var16 = (var11 + block.boundingBoxMinZ * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMinY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + block.boundingBoxMaxZ * 16.0) / 256.0;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var12;
            var24 = var18;
            var26 = var16;
        } else if (this.field_2055 == 3) {
            var12 = (var10 + 16 - block.boundingBoxMinZ * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMaxZ * 16.0 - 0.01) / 256.0;
            var16 = (var11 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = d + block.boundingBoxMinX;
        double var30 = e + block.boundingBoxMinY;
        double var32 = e + block.boundingBoxMaxY;
        double var34 = f + block.boundingBoxMinZ;
        double var36 = f + block.boundingBoxMaxZ;
        if (this.field_2058) {
            vertex1(var9, var28, var32, var36, var42, var24);
            vertex2(var9, var28, var32, var34, var12, var16);
            vertex3(var9, var28, var30, var34, var22, var26);
            vertex4(var9, var28, var30, var36, var14, var18);
        } else {
            var9.vertex(var28, var32, var36, var42, var24);
            var9.vertex(var28, var32, var34, var12, var16);
            var9.vertex(var28, var30, var34, var22, var26);
            var9.vertex(var28, var30, var36, var14, var18);
        }
    }

    @Inject(at = @At("HEAD"), method = "method_1470", cancellable = true)
    public void render_1470(Block block, double d, double e, double f, int i, CallbackInfo ci) {
        ci.cancel();

        Tessellator var9 = Tessellator.INSTANCE;
        if (this.field_2049 >= 0) {
            i = this.field_2049;
        }

        int var10 = (i & 15) << 4;
        int var11 = i & 496;
        double var12 = (var10 + block.boundingBoxMinZ * 16.0) / 256.0;
        double var14 = (var10 + block.boundingBoxMaxZ * 16.0 - 0.01) / 256.0;
        double var16 = (var11 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
        double var18 = (var11 + 16 - block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
        if (this.field_2050) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.boundingBoxMinZ < 0.0 || block.boundingBoxMaxZ > 1.0) {
            var12 = (var10 + 0.0F) / 256.0F;
            var14 = (var10 + 15.99F) / 256.0F;
        }

        if (block.boundingBoxMinY < 0.0 || block.boundingBoxMaxY > 1.0) {
            var16 = (var11 + 0.0F) / 256.0F;
            var18 = (var11 + 15.99F) / 256.0F;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_2054 == 2) {
            var12 = (var10 + block.boundingBoxMinY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var16 = (var11 + 16 - block.boundingBoxMinZ * 16.0) / 256.0;
            var14 = (var10 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + 16 - block.boundingBoxMaxZ * 16.0) / 256.0;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var16;
        } else if (this.field_2054 == 1) {
            var12 = (var10 + 16 - block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var16 = (var11 + block.boundingBoxMaxZ * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMinY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + block.boundingBoxMinZ * 16.0) / 256.0;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var12;
            var24 = var18;
            var26 = var16;
        } else if (this.field_2054 == 3) {
            var12 = (var10 + 16 - block.boundingBoxMinZ * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMaxZ * 16.0 - 0.01) / 256.0;
            var16 = (var11 + block.boundingBoxMaxY * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + block.boundingBoxMinY * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = d + block.boundingBoxMaxX;
        double var30 = e + block.boundingBoxMinY;
        double var32 = e + block.boundingBoxMaxY;
        double var34 = f + block.boundingBoxMinZ;
        double var36 = f + block.boundingBoxMaxZ;
        if (this.field_2058) {
            vertex1(var9, var28, var30, var36, var22, var26);
            vertex2(var9, var28, var30, var34, var14, var18);
            vertex3(var9, var28, var32, var34, var42, var24);
            vertex4(var9, var28, var32, var36, var12, var16);
        } else {
            var9.vertex(var28, var30, var36, var22, var26);
            var9.vertex(var28, var30, var34, var14, var18);
            var9.vertex(var28, var32, var34, var42, var24);
            var9.vertex(var28, var32, var36, var12, var16);
        }
    }

    @Inject(at = @At("HEAD"), method = "method_1456", cancellable = true)
    public void render_topFace(Block block, double d, double e, double f, int i, CallbackInfo ci) {
        ci.cancel();

        Tessellator var9 = Tessellator.INSTANCE;
        if (this.field_2049 >= 0) {
            i = this.field_2049;
        }

        int var10 = (i & 15) << 4;
        int var11 = (i / 16) * 16;

        double var12 = (var10 + block.boundingBoxMinX * 16.0) / 256.0;
        double var14 = (var10 + block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
        double var16 = (var11 + block.boundingBoxMinZ * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
        double var18 = (var11 + block.boundingBoxMaxZ * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
        if (block.boundingBoxMinX < 0.0 || block.boundingBoxMaxX > 1.0) {
            var12 = (var10 + 0.0F) / 256.0F;
            var14 = (var10 + 15.99F) / 256.0F;
        }

        if (block.boundingBoxMinZ < 0.0 || block.boundingBoxMaxZ > 1.0) {
            var16 = (var11 + 0.0F) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + 15.99F) / TERRAIN_SPRITESHEET_HEIGHT;
        }

        double var20 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_2056 == 1) {
            var12 = (var10 + block.boundingBoxMinZ * 16.0) / 256.0;
            var16 = (var11 + 16 - block.boundingBoxMaxX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var14 = (var10 + block.boundingBoxMaxZ * 16.0) / 256.0;
            var18 = (var11 + 16 - block.boundingBoxMinX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var24 = var16;
            var26 = var18;
            var20 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var16;
        } else if (this.field_2056 == 2) {
            var12 = (var10 + 16 - block.boundingBoxMaxZ * 16.0) / 256.0;
            var16 = (var11 + block.boundingBoxMinX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var14 = (var10 + 16 - block.boundingBoxMinZ * 16.0) / 256.0;
            var18 = (var11 + block.boundingBoxMaxX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var20 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var12;
            var24 = var18;
            var26 = var16;
        } else if (this.field_2056 == 3) {
            var12 = (var10 + 16 - block.boundingBoxMinX * 16.0) / 256.0;
            var14 = (var10 + 16 - block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
            var16 = (var11 + 16 - block.boundingBoxMinZ * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            var18 = (var11 + 16 - block.boundingBoxMaxZ * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;
            var20 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = d + block.boundingBoxMinX;
        double var30 = d + block.boundingBoxMaxX;
        double var32 = e + block.boundingBoxMaxY;
        double var34 = f + block.boundingBoxMinZ;
        double var36 = f + block.boundingBoxMaxZ;
        if (this.field_2058) {
            vertex1(var9, var30, var32, var36, var14, var18);
            vertex2(var9, var30, var32, var34, var20, var24);
            vertex3(var9, var28, var32, var34, var12, var16);
            vertex4(var9, var28, var32, var36, var22, var26);
        } else {
            var9.vertex(var30, var32, var36, var14, var18);
            var9.vertex(var30, var32, var34, var20, var24);
            var9.vertex(var28, var32, var34, var12, var16);
            var9.vertex(var28, var32, var36, var22, var26);
        }
    }

    @Inject(at = @At("HEAD"), method = "method_1444", cancellable = true)
    public void render_bottomFace(Block block, double d, double e, double f, int i, CallbackInfo ci) {
        ci.cancel();

        Tessellator var9 = Tessellator.INSTANCE;
        if (this.field_2049 >= 0) {
            i = this.field_2049;
        }

        int var10 = (i & 15) << 4;
        int var11 = (i / 16) * 16;

        double u = (var10 + block.boundingBoxMinX * 16.0) / 256.0;
        double u_nw = (var10 + block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
        double v = (var11 + block.boundingBoxMinZ * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
        double v_nw = (var11 + block.boundingBoxMaxZ * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;

        if (block.boundingBoxMinX < 0.0 || block.boundingBoxMaxX > 1.0) {
            u = (var10 + 0.0F) / 256.0F;
            u_nw = (var10 + 15.99F) / 256.0F;
        }

        if (block.boundingBoxMinZ < 0.0 || block.boundingBoxMaxZ > 1.0) {
            v = (var11 + 0.0F) / TERRAIN_SPRITESHEET_HEIGHT;
            v_nw = (var11 + 15.99F) / TERRAIN_SPRITESHEET_HEIGHT;
        }

        double var20 = u_nw;
        double var22 = u;
        double var24 = v;
        double var26 = v_nw;

        if (this.field_2057 == 2) {
            u    = (var10 + block.boundingBoxMinZ * 16.0) / 256.0;
            v    = (var11 + 16 - block.boundingBoxMaxX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            u_nw = (var10 + block.boundingBoxMaxZ * 16.0) / 256.0;
            v_nw = (var11 + 16 - block.boundingBoxMinX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;

            var24 = v;
            var26 = v_nw;
            var20 = u;
            var22 = u_nw;

            v = v_nw;
            v_nw = v;
        } else if (this.field_2057 == 1) {
            u = (var10 + 16 - block.boundingBoxMaxZ * 16.0) / 256.0;
            v = (var11 + block.boundingBoxMinX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            u_nw = (var10 + 16 - block.boundingBoxMinZ * 16.0) / 256.0;
            v_nw = (var11 + block.boundingBoxMaxX * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;

            var20 = u_nw;
            var22 = u;

            u = u_nw;
            u_nw = u;

            var24 = v_nw;
            var26 = v;
        } else if (this.field_2057 == 3) {
            u = (var10 + 16 - block.boundingBoxMinX * 16.0) / 256.0;
            u_nw = (var10 + 16 - block.boundingBoxMaxX * 16.0 - 0.01) / 256.0;
            v = (var11 + 16 - block.boundingBoxMinZ * 16.0) / TERRAIN_SPRITESHEET_HEIGHT;
            v_nw = (var11 + 16 - block.boundingBoxMaxZ * 16.0 - 0.01) / TERRAIN_SPRITESHEET_HEIGHT;

            var20 = u_nw;
            var22 = u;
            var24 = v;
            var26 = v_nw;
        }

        double var28 = d + block.boundingBoxMinX;
        double var30 = d + block.boundingBoxMaxX;
        double var32 = e + block.boundingBoxMinY;
        double var34 = f + block.boundingBoxMinZ;
        double var36 = f + block.boundingBoxMaxZ;
        if (this.field_2058) {
            vertex1(var9, var28, var32, var36, var22, var26);
            vertex2(var9, var28, var32, var34, u, v);
            vertex3(var9, var30, var32, var34, var20, var24);
            vertex4(var9, var30, var32, var36, u_nw, v_nw);
        } else {
            var9.vertex(var28, var32, var36, var22, var26);
            var9.vertex(var28, var32, var34, u, v);
            var9.vertex(var30, var32, var34, var20, var24);
            var9.vertex(var30, var32, var36, u_nw, v_nw);
        }
    }

    @ModifyConstant(
            constant = {
                    @Constant(intValue = 240)
            },
            method = {
                    "renderBed", "renderBrewingStand", "renderRepeater", "method_1432", "method_1454", "method_1460",
                    "renderLever", "renderTripwireHook", "renderTripwire", "renderFire", "renderRedstone", "renderRail",
                    "renderLadder", "renderVines", "renderPane", "method_1443", "method_1445", "method_1446",
                    "renderLilyPad", "method_1448", "method_1457", "renderFluid", "renderCocoa", "method_1444",
                    "method_1456", "method_1461", "method_1465", "method_1468", "method_1470"
            }
    )
    public int xand(int constant) {
        return 496;
    }

    @ModifyConstant(
            constant = {
                    // Redstone: 1650
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3)
            },
            method = {
                    "renderRedstone", "renderLadder", "renderRail", "renderVines", "method_1443", "method_1445",
                    "method_1446", "renderLilyPad", "method_1448", "method_1457"
            },
            require = 2
    )
    public float divideHeight(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // Method_1446: 2567
                    @Constant(doubleValue = 256, ordinal = 0)
            },
            method = {
                    "method_1446", "method_1448"
            },
            require = 1
    )
    public double divideHeight(double constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 1650
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3),
                    // 1694
                    @Constant(floatValue = 256, ordinal = 6),
                    @Constant(floatValue = 256, ordinal = 7),
                    // 1765
                    @Constant(floatValue = 256, ordinal = 10),
                    @Constant(floatValue = 256, ordinal = 11),
            },
            method = "renderRedstone",
            require = 4
    )
    public float redstoneSzfajse(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 2053
                    @Constant(floatValue = 256, ordinal = 3),
                    @Constant(floatValue = 256, ordinal = 4),
                    // 2059
                    @Constant(floatValue = 256, ordinal = 7),
                    @Constant(floatValue = 256, ordinal = 8),
                    @Constant(floatValue = 256, ordinal = 9),
            },
            method = "renderPane",
            require = 5
    )
    public float pane__pain(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 2777
                    @Constant(floatValue = 256, ordinal = 1)
            },
            method = {
                    "renderFluid", "method_1432", "method_1454", "method_1460"
            },
            require = 1
    )
    public float fluid__floatPain(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 2772
                    @Constant(doubleValue = 256, ordinal = 1),
                    @Constant(doubleValue = 256, ordinal = 3),
                    // 2861
                    @Constant(doubleValue = 256, ordinal = 5),
                    @Constant(doubleValue = 256, ordinal = 6),
                    @Constant(doubleValue = 256, ordinal = 7),
            },
            method = "renderFluid",
            require = 5
    )
    public double fluid__doublePain(double constant) {
        return TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 3812
                    @Constant(doubleValue = 256, ordinal = 2),
                    @Constant(doubleValue = 256, ordinal = 3),
                    // 3864
                    @Constant(doubleValue = 256, ordinal = 5),
                    // 3876
                    @Constant(doubleValue = 256, ordinal = 7),
            },
            method = "renderCocoa",
            require = 4
    )
    public double cocoa__kys__iHateThis(double constant) {
        return TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 3863
                    @Constant(floatValue = 256, ordinal = 1),
                    // 3875
                    @Constant(floatValue = 256, ordinal = 3),
            },
            method = {
                    "renderCocoa", "renderBed"
            },
            require = 2
    )
    public float cocoa__etc__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 3863
                    @Constant(doubleValue = 256, ordinal = 1),
                    // 3875
                    @Constant(doubleValue = 256, ordinal = 3),
            },
            method = "renderBed",
            require = 2
    )
    public double bed__doubles(double constant) {
        return TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 409
                    @Constant(floatValue = 256, ordinal = 0),
                    @Constant(floatValue = 256, ordinal = 1),
            },
            method = "renderBrewingStand",
            require = 2
    )
    public float brewingStand__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 409
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3),
            },
            method = "renderRepeater",
            require = 2
    )
    public float repeater__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 409
                    @Constant(doubleValue = 256, ordinal = 1)
            },
            method = {
                    "method_1432", "method_1454", "method_1460"
            },
            require = 1
    )
    public double m_1432__double(double constant) {
        return TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 1451
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3),
                    // 1460
                    @Constant(floatValue = 256, ordinal = 6),
                    @Constant(floatValue = 256, ordinal = 7),
                    // 1525
                    @Constant(floatValue = 256, ordinal = 10),
                    @Constant(floatValue = 256, ordinal = 11),
                    // 1536
                    @Constant(floatValue = 256, ordinal = 14),
                    @Constant(floatValue = 256, ordinal = 15),
                    // 1549
                    @Constant(floatValue = 256, ordinal = 18),
                    @Constant(floatValue = 256, ordinal = 19),
                    // 1576
                    @Constant(floatValue = 256, ordinal = 22),
                    @Constant(floatValue = 256, ordinal = 23),
                    // 1604
                    @Constant(floatValue = 256, ordinal = 26),
                    @Constant(floatValue = 256, ordinal = 27),
            },
            method = "renderFire",
            require = 14
    )
    public float fire__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 882
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3),
                    // 956
                    @Constant(floatValue = 256, ordinal = 6),
                    @Constant(floatValue = 256, ordinal = 7),
                    // 961
                    @Constant(floatValue = 256, ordinal = 10),
                    @Constant(floatValue = 256, ordinal = 11),
            },
            method = "renderLever",
            require = 6
    )
    public float lever__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 1054
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3),
                    // 1121
                    @Constant(floatValue = 256, ordinal = 6),
                    @Constant(floatValue = 256, ordinal = 7),
                    // 1135
                    @Constant(floatValue = 256, ordinal = 10),
                    @Constant(floatValue = 256, ordinal = 11),
                    // 1220
                    @Constant(floatValue = 256, ordinal = 14),
                    @Constant(floatValue = 256, ordinal = 15),
                    // 1234
                    @Constant(floatValue = 256, ordinal = 18),
                    @Constant(floatValue = 256, ordinal = 19),
                    // 1268
                    @Constant(floatValue = 256, ordinal = 22),
                    @Constant(floatValue = 256, ordinal = 23),
            },
            method = "renderTripwireHook",
            require = 12
    )
    public float tripwireHook__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }

    @ModifyConstant(
            constant = {
                    // 1332
                    @Constant(floatValue = 256, ordinal = 2),
                    @Constant(floatValue = 256, ordinal = 3)
            },
            method = "renderTripwire",
            require = 2
    )
    public float tripwire__floats(float constant) {
        return (float) TERRAIN_SPRITESHEET_HEIGHT;
    }
}
