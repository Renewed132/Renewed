package pl.olafcio.renewed.mixin;

import net.minecraft.advancement.Achievement;
import net.minecraft.advancement.AchievementsAndCriterions;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.AchievementsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.CommonI18n;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static pl.olafcio.renewed.Terrain.TERRAIN_SPRITESHEET_HEIGHT;
import static pl.olafcio.renewed.Terrain.TERRAIN_SPRITESHEET_ROWS;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin extends Screen {
    @Shadow protected double attemptedCenterX;
    @Shadow protected double attemptedCenterY;

    @Shadow protected double targetCenterX;
    @Shadow protected double targetCenterY;

    @Shadow @Final private static int MIN_PAN_X;
    @Shadow @Final private static int MIN_PAN_Y;

    @Shadow @Final private static int MAX_PAN_X;
    @Shadow @Final private static int MAX_PAN_Y;

    @Shadow protected int originX;
    @Shadow protected int originY;

    @Shadow
    private StatHandler handler;

    @Inject(at = @At("HEAD"), method = "method_1095", cancellable = true)
    private void method_1095(int i, int j, float f, CallbackInfo ci) {
        ci.cancel();

        int x = MathHelper.floor(this.attemptedCenterX + (this.targetCenterX - this.attemptedCenterX) * (double)f);
        int y = MathHelper.floor(this.attemptedCenterY + (this.targetCenterY - this.attemptedCenterY) * (double)f);
        if (x < MIN_PAN_X) {
            x = MIN_PAN_X;
        }

        if (y < MIN_PAN_Y) {
            y = MIN_PAN_Y;
        }

        if (x >= MAX_PAN_X) {
            x = MAX_PAN_X - 1;
        }

        if (y >= MAX_PAN_Y) {
            y = MAX_PAN_Y - 1;
        }

        int terrain = this.mc.textureManager.getTextureFromPath("/terrain.png");
        int background = this.mc.textureManager.getTextureFromPath("/achievement/bg.png");

        int var8 = (this.width - this.originX) / 2;
        int var9 = (this.height - this.originY) / 2;

        int var10 = var8 + 16;
        int var11 = var9 + 17;

        this.zOffset = 0.0F;

        GL11.glDepthFunc(518);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);

        this.mc.textureManager.bindTexture(terrain);

        int add = 288 - 256;
        int height = (int)TERRAIN_SPRITESHEET_HEIGHT + add;

        int var12 = x + 288 >> 4;
        int var13 = (y + height) / (int)TERRAIN_SPRITESHEET_ROWS;
        int var14 = (x + 288) % 16;
        int var15 = (y + height) % (int)TERRAIN_SPRITESHEET_ROWS;

        Random random = new Random();

        for(int var22 = 0; var22 * 16 - var15 < 155; ++var22) {
            float var23 = 0.6F - (float)(var13 + var22) / 25.0F * 0.3F;
            GL11.glColor4f(var23, var23, var23, 1.0F);

            for(int var24 = 0; var24 * 16 - var14 < 224; ++var24) {
                random.setSeed((long)(1234 + var12 + var24));
                random.nextInt();
                int var25 = random.nextInt(1 + var13 + var22) + (var13 + var22) / 2;
                int id = Block.SAND_BLOCK.field_439;
                if (var25 <= 37 && var13 + var22 != 35) {
                    if (var25 == 22) {
                        if (random.nextInt(2) == 0) {
                            id = Block.DIAMOND_ORE.field_439;
                        } else {
                            id = Block.REDSTONE_ORE.field_439;
                        }
                    } else if (var25 == 10) {
                        id = Block.IRON_ORE.field_439;
                    } else if (var25 == 8) {
                        id = Block.COAL_ORE.field_439;
                    } else if (var25 > 4) {
                        id = Block.STONE_BLOCK.field_439;
                    } else if (var25 > 0) {
                        id = Block.DIRT.field_439;
                    }
                } else {
                    id = Block.BEDROCK.field_439;
                }

                this.drawTextureBlock(var10 + var24 * 16 - var14, var11 + var22 * 16 - var15, id % 16 << 4, id >> 4 << 4, 16, 16);
            }
        }

        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glDisable(3553);

        for(int var32 = 0; var32 < AchievementsAndCriterions.ACHIEVEMENTS.size(); ++var32) {
            Achievement var34 = (Achievement)AchievementsAndCriterions.ACHIEVEMENTS.get(var32);
            if (var34.parent != null) {
                int var36 = var34.column * 24 - x + 11 + var10;
                int var39 = var34.row * 24 - y + 11 + var11;
                int var42 = var34.parent.column * 24 - x + 11 + var10;
                int var27 = var34.parent.row * 24 - y + 11 + var11;
                boolean var28 = this.handler.method_1728(var34);
                boolean var29 = this.handler.hasParentAchievement(var34);
                int var30 = Math.sin((double)(Minecraft.getTime() % 600L) / (double)600.0F * Math.PI * (double)2.0F) > 0.6 ? 255 : 130;
                int var31 = -16777216;
                if (var28) {
                    var31 = -9408400;
                } else if (var29) {
                    var31 = '\uff00' + (var30 << 24);
                }

                this.drawHorizontalLine(var36, var42, var39, var31);
                this.drawVerticalLine(var42, var39, var27, var31);
            }
        }

        Achievement var33 = null;
        ItemRenderer var35 = new ItemRenderer();
        DiffuseLighting.enable();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);

        for(int var37 = 0; var37 < AchievementsAndCriterions.ACHIEVEMENTS.size(); ++var37) {
            Achievement var40 = (Achievement)AchievementsAndCriterions.ACHIEVEMENTS.get(var37);
            int var43 = var40.column * 24 - x;
            int var45 = var40.row * 24 - y;
            if (var43 >= -24 && var45 >= -24 && var43 <= 224 && var45 <= 155) {
                if (this.handler.method_1728(var40)) {
                    float var47 = 1.0F;
                    GL11.glColor4f(var47, var47, var47, 1.0F);
                } else if (this.handler.hasParentAchievement(var40)) {
                    float var48 = Math.sin((double)(Minecraft.getTime() % 600L) / (double)600.0F * Math.PI * (double)2.0F) < 0.6 ? 0.6F : 0.8F;
                    GL11.glColor4f(var48, var48, var48, 1.0F);
                } else {
                    float var49 = 0.3F;
                    GL11.glColor4f(var49, var49, var49, 1.0F);
                }

                this.mc.textureManager.bindTexture(background);
                int var50 = var10 + var43;
                int var53 = var11 + var45;
                if (var40.isChallenge()) {
                    this.drawTexture(var50 - 2, var53 - 2, 26, 202, 26, 26);
                } else {
                    this.drawTexture(var50 - 2, var53 - 2, 0, 202, 26, 26);
                }

                if (!this.handler.hasParentAchievement(var40)) {
                    float var56 = 0.1F;
                    GL11.glColor4f(var56, var56, var56, 1.0F);
                    var35.field_2123 = false;
                }

                GL11.glEnable(2896);
                GL11.glEnable(2884);
                var35.renderGlint(this.mc.textRenderer, this.mc.textureManager, var40.logo, var50 + 3, var53 + 3);
                GL11.glDisable(2896);
                if (!this.handler.hasParentAchievement(var40)) {
                    var35.field_2123 = true;
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (i >= var10 && j >= var11 && i < var10 + 224 && j < var11 + 155 && i >= var50 && i <= var50 + 22 && j >= var53 && j <= var53 + 22) {
                    var33 = var40;
                }
            }
        }

        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(background);
        this.drawTexture(var8, var9, 0, 0, this.originX, this.originY);
        GL11.glPopMatrix();
        this.zOffset = 0.0F;
        GL11.glDepthFunc(515);
        GL11.glDisable(2929);
        GL11.glEnable(3553);
        super.render(i, j, f);
        if (var33 != null) {
            String var38 = CommonI18n.translate(var33.getStringId());
            String var41 = var33.getDescription();
            int var44 = i + 12;
            int var46 = j - 4;
            if (this.handler.hasParentAchievement(var33)) {
                int var51 = Math.max(this.textRenderer.getStringWidth(var38), 120);
                int var54 = this.textRenderer.getHeightSplit(var41, var51);
                if (this.handler.method_1728(var33)) {
                    var54 += 12;
                }

                this.fillGradient(var44 - 3, var46 - 3, var44 + var51 + 3, var46 + var54 + 3 + 12, -1073741824, -1073741824);
                this.textRenderer.drawTrimmed(var41, var44, var46 + 12, var51, -6250336);
                if (this.handler.method_1728(var33)) {
                    this.textRenderer.method_956(CommonI18n.translate("achievement.taken"), var44, var46 + var54 + 4, -7302913);
                }
            } else {
                int var52 = Math.max(this.textRenderer.getStringWidth(var38), 120);
                String var55 = CommonI18n.translate("achievement.requires", CommonI18n.translate(var33.parent.getStringId()));
                int var57 = this.textRenderer.getHeightSplit(var55, var52);
                this.fillGradient(var44 - 3, var46 - 3, var44 + var52 + 3, var46 + var57 + 12 + 3, -1073741824, -1073741824);
                this.textRenderer.drawTrimmed(var55, var44, var46 + 12, var52, -9416624);
            }

            this.textRenderer.method_956(var38, var44, var46, this.handler.hasParentAchievement(var33) ? (var33.isChallenge() ? -128 : -1) : (var33.isChallenge() ? -8355776 : -8355712));
        }

        GL11.glEnable(2929);
        GL11.glEnable(2896);
        DiffuseLighting.disable();
    }

    @Unique
    private void drawTextureBlock(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625f;
        float f2 = 1F / (float) TERRAIN_SPRITESHEET_HEIGHT;

        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.begin();
        tessellator.vertex(x + 0, y + height, this.zOffset, (float)(u + 0) * f, (float)(v + height) * f2);
        tessellator.vertex(x + width, y + height, this.zOffset, (float)(u + width) * f, (float)(v + height) * f2);
        tessellator.vertex(x + width, y + 0, this.zOffset, (float)(u + width) * f, (float)(v + 0) * f2);
        tessellator.vertex(x + 0, y + 0, this.zOffset, (float)(u + 0) * f, (float)(v + 0) * f2);
        tessellator.end();
    }
}
