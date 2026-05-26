package com.darkz.skintotem.client;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TotemTextureGenerator {

    public static BufferedImage generate(BufferedImage skin) {
        if (skin == null) return null;
        boolean newFmt = skin.getHeight() >= 64;

        BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        blit(g, skin, 8, 8, 8, 8, 6, 0, 4, 4);           // head base
        blitAlpha(skin, out, 40, 8, 8, 8, 6, 0, 4, 4);   // hat overlay
        blit(g, skin, 20, 20, 8, 12, 6, 4, 4, 6);         // body
        blit(g, skin, 44, 20, 4, 12, 4, 4, 2, 6);         // right arm
        if (newFmt) blit(g, skin, 36, 52, 4, 12, 10, 4, 2, 6);
        else        blitMirror(g, skin, 44, 20, 4, 12, 10, 4, 2, 6); // left arm
        blit(g, skin, 4, 20, 4, 12, 6, 10, 2, 6);         // right leg
        if (newFmt) blit(g, skin, 20, 52, 4, 12, 8, 10, 2, 6);
        else        blitMirror(g, skin, 4, 20, 4, 12, 8, 10, 2, 6);  // left leg

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
        float rx = (float) sw / dw, ry = (float) sh / dh;
        for (int py = 0; py < dh; py++) {
            for (int px = 0; px < dw; px++) {
                int qx = sx + (int)(px * rx), qy = sy + (int)(py * ry);
                if (qx < src.getWidth() && qy < src.getHeight()) {
                    int fg = src.getRGB(qx, qy);
                    if (((fg >> 24) & 0xFF) > 0)
                        dst.setRGB(dx+px, dy+py, alphaBlend(dst.getRGB(dx+px, dy+py), fg));
                }
            }
        }
    }

    private static int alphaBlend(int bg, int fg) {
        int fa = (fg >> 24) & 0xFF;
        if (fa == 255) return fg;
        float a = fa / 255f;
        int r = (int)((fg>>16&0xFF)*a + (bg>>16&0xFF)*(1-a));
        int g = (int)((fg>> 8&0xFF)*a + (bg>> 8&0xFF)*(1-a));
        int b = (int)((fg    &0xFF)*a + (bg    &0xFF)*(1-a));
        return (Math.max(fa, (bg>>24)&0xFF) << 24) | (r<<16) | (g<<8) | b;
    }
}
