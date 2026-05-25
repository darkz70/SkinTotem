package com.darkz.skintotem.client;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Builds a 16x16 flat totem figure from a 64x64 Minecraft skin.
 *
 * Output layout (matches skinmc.net/totem):
 *   Head  4x4 @ (6,0)
 *   Body  4x6 @ (6,4)
 *   R.Arm 2x6 @ (4,4)
 *   L.Arm 2x6 @ (10,4)
 *   R.Leg 2x6 @ (6,10)
 *   L.Leg 2x6 @ (8,10)
 */
public class TotemTextureGenerator {

    public static BufferedImage generate(BufferedImage skin) {
        if (skin == null) return null;

        boolean newFmt = skin.getHeight() >= 64 && skin.getWidth() >= 64;
        BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Head base + overlay
        blit(g, skin, 8, 8, 8, 8, 6, 0, 4, 4);
        blitAlpha(skin, out, 40, 8, 8, 8, 6, 0, 4, 4);

        // Body
        blit(g, skin, 20, 20, 8, 12, 6, 4, 4, 6);

        // Right arm
        blit(g, skin, 44, 20, 4, 12, 4, 4, 2, 6);

        // Left arm
        if (newFmt) blit(g, skin, 36, 52, 4, 12, 10, 4, 2, 6);
        else        blitMirror(g, skin, 44, 20, 4, 12, 10, 4, 2, 6);

        // Right leg
        blit(g, skin, 4, 20, 4, 12, 6, 10, 2, 6);

        // Left leg
        if (newFmt) blit(g, skin, 20, 52, 4, 12, 8, 10, 2, 6);
        else        blitMirror(g, skin, 4, 20, 4, 12, 8, 10, 2, 6);

        g.dispose();
        return out;
    }

    private static void blit(Graphics2D g, BufferedImage src,
                              int sx, int sy, int sw, int sh,
                              int dx, int dy, int dw, int dh) {
        g.drawImage(src, dx, dy, dx+dw, dy+dh, sx, sy, sx+sw, sy+sh, null);
    }

    private static void blitMirror(Graphics2D g, BufferedImage src,
                                   int sx, int sy, int sw, int sh,
                                   int dx, int dy, int dw, int dh) {
        g.drawImage(src, dx+dw, dy, dx, dy+dh, sx, sy, sx+sw, sy+sh, null);
    }

    private static void blitAlpha(BufferedImage src, BufferedImage dst,
                                  int sx, int sy, int sw, int sh,
                                  int dx, int dy, int dw, int dh) {
        float sx2 = (float) sw / dw, sy2 = (float) sh / dh;
        for (int py = 0; py < dh; py++) {
            for (int px = 0; px < dw; px++) {
                int qx = sx + (int)(px * sx2), qy = sy + (int)(py * sy2);
                if (qx < src.getWidth() && qy < src.getHeight()) {
                    int argb = src.getRGB(qx, qy);
                    if (((argb >> 24) & 0xFF) > 0)
                        dst.setRGB(dx+px, dy+py, blend(dst.getRGB(dx+px, dy+py), argb));
                }
            }
        }
    }

    private static int blend(int bg, int fg) {
        int fa = (fg >> 24) & 0xFF;
        if (fa == 255) return fg;
        float a = fa / 255f;
        int r = (int)((fg >> 16 & 0xFF) * a + (bg >> 16 & 0xFF) * (1-a));
        int g = (int)((fg >> 8  & 0xFF) * a + (bg >> 8  & 0xFF) * (1-a));
        int b = (int)((fg       & 0xFF) * a + (bg       & 0xFF) * (1-a));
        return (Math.max(fa, (bg >> 24) & 0xFF) << 24) | (r << 16) | (g << 8) | b;
    }
}
