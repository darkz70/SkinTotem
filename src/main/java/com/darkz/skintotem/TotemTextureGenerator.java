package com.darkz.skintotem;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Generates a 16x16 totem-style texture from a Minecraft skin.
 * Layout mirrors skinmc.net/totem — flat 2D, face on body, arms, legs.
 *
 * Skin layout (Steve/Alex 64x64):
 *  Head face:   x=8,  y=8,  w=8, h=8
 *  Head overlay: x=40, y=8, w=8, h=8
 *  Body:        x=20, y=20, w=8, h=12
 *  Right arm:   x=44, y=20, w=4, h=12
 *  Left arm:    x=36, y=52, w=4, h=12  (new skin format)
 *  Right leg:   x=4,  y=20, w=4, h=12
 *  Left leg:    x=20, y=52, w=4, h=12  (new skin format)
 */
public class TotemTextureGenerator {

    // Output 16x16 totem texture layout:
    //  Head:  4x4 centered at top (x=6, y=0)
    //  Body:  4x6 below head (x=6, y=4)
    //  Arms:  2x6 on sides of body (x=4,y=4) and (x=10,y=4)
    //  Legs:  2x6 below body (x=6,y=10) and (x=8,y=10)

    public static BufferedImage generate(BufferedImage skin) {
        if (skin == null) return null;

        boolean isNewFormat = skin.getHeight() == 64;

        BufferedImage totem = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = totem.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // --- Head (4x4) at (6, 0) ---
        // Draw base face layer (8x8 from skin -> 4x4)
        drawRegion(g, skin, 8, 8, 8, 8, 6, 0, 4, 4);
        // Draw head overlay (hat layer, 8x8 -> 4x4) on top
        drawRegionBlend(g, skin, 40, 8, 8, 8, 6, 0, 4, 4);

        // --- Body (4x6) at (6, 4) ---
        // Body front: x=20, y=20, w=8, h=12 -> 4x6
        drawRegion(g, skin, 20, 20, 8, 12, 6, 4, 4, 6);

        // --- Right Arm (2x6) at (4, 4) ---
        // Right arm front: x=44, y=20, w=4, h=12 -> 2x6
        drawRegion(g, skin, 44, 20, 4, 12, 4, 4, 2, 6);

        // --- Left Arm (2x6) at (10, 4) ---
        if (isNewFormat) {
            // New skin: left arm at x=36, y=52
            drawRegion(g, skin, 36, 52, 4, 12, 10, 4, 2, 6);
        } else {
            // Old skin: mirror right arm
            drawRegionMirrorX(g, skin, 44, 20, 4, 12, 10, 4, 2, 6);
        }

        // --- Right Leg (2x6) at (6, 10) ---
        // Right leg front: x=4, y=20, w=4, h=12 -> 2x6
        drawRegion(g, skin, 4, 20, 4, 12, 6, 10, 2, 6);

        // --- Left Leg (2x6) at (8, 10) ---
        if (isNewFormat) {
            // New skin: left leg at x=20, y=52
            drawRegion(g, skin, 20, 52, 4, 12, 8, 10, 2, 6);
        } else {
            // Old skin: mirror right leg
            drawRegionMirrorX(g, skin, 4, 20, 4, 12, 8, 10, 2, 6);
        }

        g.dispose();
        return totem;
    }

    /** Copy a region of src into dst at (dstX, dstY) scaled to (dstW x dstH), pixel-perfect. */
    private static void drawRegion(Graphics2D g, BufferedImage src,
                                   int srcX, int srcY, int srcW, int srcH,
                                   int dstX, int dstY, int dstW, int dstH) {
        g.drawImage(src,
            dstX, dstY, dstX + dstW, dstY + dstH,
            srcX, srcY, srcX + srcW, srcY + srcH,
            null);
    }

    /** Same but flipped horizontally (mirror on X axis) for old-format skin arm/leg mirroring. */
    private static void drawRegionMirrorX(Graphics2D g, BufferedImage src,
                                          int srcX, int srcY, int srcW, int srcH,
                                          int dstX, int dstY, int dstW, int dstH) {
        g.drawImage(src,
            dstX + dstW, dstY, dstX, dstY + dstH,
            srcX, srcY, srcX + srcW, srcY + srcH,
            null);
    }

    /**
     * Draw with alpha blending (for overlay layers like hat).
     * Skips fully-transparent pixels from the overlay.
     */
    private static void drawRegionBlend(Graphics2D g, BufferedImage src,
                                        int srcX, int srcY, int srcW, int srcH,
                                        int dstX, int dstY, int dstW, int dstH) {
        // Scale manually pixel by pixel to respect transparency
        float scaleX = (float) srcW / dstW;
        float scaleY = (float) srcH / dstH;
        for (int dy = 0; dy < dstH; dy++) {
            for (int dx = 0; dx < dstW; dx++) {
                int sx = srcX + (int) (dx * scaleX);
                int sy = srcY + (int) (dy * scaleY);
                if (sx < src.getWidth() && sy < src.getHeight()) {
                    int argb = src.getRGB(sx, sy);
                    int alpha = (argb >> 24) & 0xFF;
                    if (alpha > 0) {
                        // Blend on top of existing pixel
                        g.setColor(new Color(argb, true));
                        g.fillRect(dstX + dx, dstY + dy, 1, 1);
                    }
                }
            }
        }
    }
}
