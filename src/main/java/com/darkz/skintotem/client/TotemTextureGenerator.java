package com.darkz.skintotem.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Generates a 16x16 flat totem texture from a 64x64 Minecraft skin.
 * Layout is identical to skinmc.net/totem (flat 2D figure).
 *
 * Skin regions used (Steve/Alex 64x64):
 *   Head face   : (8,8)  8x8
 *   Head overlay: (40,8) 8x8
 *   Body front  : (20,20) 8x12
 *   Right arm   : (44,20) 4x12
 *   Left arm    : (36,52) 4x12  [new format]
 *   Right leg   : (4,20)  4x12
 *   Left leg    : (20,52) 4x12  [new format]
 *
 * Output 16x16 layout:
 *   Head  4x4 at (6,0)
 *   Body  4x6 at (6,4)
 *   R.Arm 2x6 at (4,4)
 *   L.Arm 2x6 at (10,4)
 *   R.Leg 2x6 at (6,10)
 *   L.Leg 2x6 at (8,10)
 */
@Environment(EnvType.CLIENT)
public class TotemTextureGenerator {

    public static BufferedImage generate(BufferedImage skin) {
        if (skin == null) return null;

        boolean newFormat = skin.getHeight() >= 64 && skin.getWidth() >= 64;

        BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Head base
        blit(g, skin, 8, 8, 8, 8, 6, 0, 4, 4);
        // Head overlay (hat) - with alpha
        blitAlpha(skin, out, 40, 8, 8, 8, 6, 0, 4, 4);

        // Body
        blit(g, skin, 20, 20, 8, 12, 6, 4, 4, 6);

        // Right arm
        blit(g, skin, 44, 20, 4, 12, 4, 4, 2, 6);

        // Left arm
        if (newFormat) {
            blit(g, skin, 36, 52, 4, 12, 10, 4, 2, 6);
        } else {
            blitMirror(g, skin, 44, 20, 4, 12, 10, 4, 2, 6);
        }

        // Right leg
        blit(g, skin, 4, 20, 4, 12, 6, 10, 2, 6);

        // Left leg
        if (newFormat) {
            blit(g, skin, 20, 52, 4, 12, 8, 10, 2, 6);
        } else {
            blitMirror(g, skin, 4, 20, 4, 12, 8, 10, 2, 6);
        }

        g.dispose();
        return out;
    }

    private static void blit(Graphics2D g, BufferedImage src,
                              int sx, int sy, int sw, int sh,
                              int dx, int dy, int dw, int dh) {
        g.drawImage(src,
                dx, dy, dx + dw, dy + dh,
                sx, sy, sx + sw, sy + sh,
                null);
    }

    private static void blitMirror(Graphics2D g, BufferedImage src,
                                   int sx, int sy, int sw, int sh,
                                   int dx, int dy, int dw, int dh) {
        // Flip horizontally
        g.drawImage(src,
                dx + dw, dy, dx, dy + dh,
                sx, sy, sx + sw, sy + sh,
                null);
    }

    /** Manual per-pixel alpha blend for overlay layers */
    private static void blitAlpha(BufferedImage src, BufferedImage dst,
                                  int sx, int sy, int sw, int sh,
                                  int dx, int dy, int dw, int dh) {
        float scaleX = (float) sw / dw;
        float scaleY = (float) sh / dh;
        for (int py = 0; py < dh; py++) {
            for (int px = 0; px < dw; px++) {
                int srcX = sx + (int)(px * scaleX);
                int srcY = sy + (int)(py * scaleY);
                if (srcX < src.getWidth() && srcY < src.getHeight()) {
                    int argb = src.getRGB(srcX, srcY);
                    if (((argb >> 24) & 0xFF) > 0) {
                        dst.setRGB(dx + px, dy + py, alphaBlend(dst.getRGB(dx + px, dy + py), argb));
                    }
                }
            }
        }
    }

    private static int alphaBlend(int bg, int fg) {
        int fa = (fg >> 24) & 0xFF;
        if (fa == 255) return fg;
        int ba = (bg >> 24) & 0xFF;
        float alpha = fa / 255f;
        int r = blend((bg >> 16) & 0xFF, (fg >> 16) & 0xFF, alpha);
        int g = blend((bg >> 8) & 0xFF, (fg >> 8) & 0xFF, alpha);
        int b = blend(bg & 0xFF, fg & 0xFF, alpha);
        int a = Math.max(fa, ba);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int blend(int bg, int fg, float alpha) {
        return Math.min(255, (int)(fg * alpha + bg * (1 - alpha)));
    }
}
