package pl.olafcio.renewed.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.SharedConstants;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {
    @Shadow private boolean obfuscated;
    @Shadow private boolean bold;
    @Shadow private boolean underline;
    @Shadow private boolean strikethrough;
    @Shadow private boolean italic;

    @Shadow private int[] colorCodes;
    @Shadow private int color;

    @Shadow private float red;
    @Shadow private float green;
    @Shadow private float blue;
    @Shadow private float alpha;

    @Shadow public Random random;
    @Shadow private int[] characterWidths;

    @Shadow protected abstract float method_952(int i, char c, boolean bl);

    @Shadow private float x;
    @Shadow private float y;

    @Shadow public int fontHeight;

    @Shadow public abstract int getCharWidth(char character);

    @Inject(at = @At("HEAD"), method = "draw", cancellable = true)
    public void draw(String text, boolean shadow, CallbackInfo ci) {
        ci.cancel();

        for (int index = 0; index < text.length(); ++index) {
            char ch = text.charAt(index);
            if (ch == 167 && index + 1 < text.length()) {
                int code = "0123456789abcdefklmnorx".indexOf(text.toLowerCase().charAt(index + 1));
                if (code == 22) {
                    this.obfuscated = false;
                    this.bold = false;
                    this.strikethrough = false;
                    this.underline = false;
                    this.italic = false;

                    index++;
                    if (index + 12 < text.length()) {
                        hex:
                        {
                            StringBuilder hex = new StringBuilder();
                            for (int i = 0; i < 6; i++) {
                                if (text.charAt(++index) != '§')
                                    break hex;

                                char cc = text.charAt(++index);
                                if ("0123456789abcdef".indexOf(cc) == -1)
                                    break hex;

                                hex.append(cc);
                            }

                            this.color = Integer.parseInt(hex.toString(), 16);
                            applyColor();
                        }
                    }

                    continue;
                } else if (code < 16) {
                    this.obfuscated = false;
                    this.bold = false;
                    this.strikethrough = false;
                    this.underline = false;
                    this.italic = false;

                    if (code < 0 || code > 15)
                        code = 15;

                    if (shadow)
                        code += 16;

                    int color = this.colorCodes[code];
                    setColor(color);
                } else if (code == 16) {
                    this.obfuscated = true;
                } else if (code == 17) {
                    this.bold = true;
                } else if (code == 18) {
                    this.strikethrough = true;
                } else if (code == 19) {
                    this.underline = true;
                } else if (code == 20) {
                    this.italic = true;
                } else if (code == 21) {
                    this.obfuscated = false;
                    this.bold = false;
                    this.strikethrough = false;
                    this.underline = false;
                    this.italic = false;
                    resetColor();
                }

                ++index;
            } else {
                int var5 = SharedConstants.FONT_CHARS.indexOf(ch);
                if (this.obfuscated && var5 > 0) {
                    int var6;
                    do {
                        var6 = this.random.nextInt(SharedConstants.FONT_CHARS.length());
                    } while (
                            this.characterWidths[var5 + 32] !=
                            this.characterWidths[var6 + 32]
                    );

                    var5 = var6;
                }

                float width = this.method_952(var5, ch, this.italic);
                if (this.bold) {
                    ++this.x;
                    this.method_952(var5, ch, this.italic);
                    --this.x;
                    ++width;
                }

                if (this.strikethrough)
                    strikethrough(width);

                if (this.underline)
                    underline(width);

                this.x += (float)((int)width);
            }
        }
    }

    @Unique
    private void underline(float width) {
        Tessellator var12 = Tessellator.INSTANCE;
        GL11.glDisable(3553);
        var12.begin();
        int var8 = this.underline ? -1 : 0;
        var12.vertex((double)(this.x + (float)var8), (double)(this.y + (float)this.fontHeight), (double)0.0F);
        var12.vertex((double)(this.x + width), (double)(this.y + (float)this.fontHeight), (double)0.0F);
        var12.vertex((double)(this.x + width), (double)(this.y + (float)this.fontHeight - 1.0F), (double)0.0F);
        var12.vertex((double)(this.x + (float)var8), (double)(this.y + (float)this.fontHeight - 1.0F), (double)0.0F);
        var12.end();
        GL11.glEnable(3553);
    }

    @Unique
    private void strikethrough(float var10) {
        Tessellator var7 = Tessellator.INSTANCE;
        GL11.glDisable(3553);
        var7.begin();
        var7.vertex((double)this.x, (double)(this.y + (float)(this.fontHeight / 2)), (double)0.0F);
        var7.vertex((double)(this.x + var10), (double)(this.y + (float)(this.fontHeight / 2)), (double)0.0F);
        var7.vertex((double)(this.x + var10), (double)(this.y + (float)(this.fontHeight / 2) - 1.0F), (double)0.0F);
        var7.vertex((double)this.x, (double)(this.y + (float)(this.fontHeight / 2) - 1.0F), (double)0.0F);
        var7.end();
        GL11.glEnable(3553);
    }

    @Unique
    private void resetColor() {
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
    }

    @Unique
    private void setColor(int color) {
        this.color = color;
        applyColor();
    }

    @Unique
    private void applyColor() {
        GL11.glColor4f((float)(color >> 16) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, this.alpha);
    }

    @Inject(at = @At("HEAD"), method = "getStringWidth", cancellable = true)
    public void getStringWidget(String text, CallbackInfoReturnable<Integer> cir) {
        if (text == null) {
            cir.setReturnValue(0);
        } else {
            int var2 = 0;
            boolean var3 = false;

            for (int var4 = 0; var4 < text.length(); var4++) {
                char var5 = text.charAt(var4);
                if (var5 == '§') {
                    var4++;
                    continue;
                }

                int var6 = this.getCharWidth(var5);
                if (var6 < 0 && var4 < text.length() - 1) {
                    var5 = text.charAt(++var4);
                    if (var5 == 'l' || var5 == 'L') {
                        var3 = true;
                    } else if (var5 == 'r' || var5 == 'R') {
                        var3 = false;
                    }

                    var6 = this.getCharWidth(var5);
                }

                var2 += var6;
                if (var3)
                    var2++;
            }

            cir.setReturnValue(var2);
        }
    }
}
