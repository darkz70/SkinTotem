package spichka.skintotem;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TotemGenerator {

    public static BufferedImage generateTotemFromSkin(BufferedImage skin) {
        BufferedImage totem = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = totem.createGraphics();

        int skinWidth = skin.getWidth();
        int skinHeight = skin.getHeight();
        boolean isOldSkin = skinHeight == 32;

        // Helper method to draw a skin region to totem, with optional flip
        // sx, sy, sw, sh: source x, y, width, height on skin
        // dx, dy, dw, dh: destination x, y, width, height on totem
        // flipX: true to flip horizontally
        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 8, 8, 8, 8, 4, 0, 8, 8, false);
        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 40, 8, 8, 8, 4, 0, 8, 8, false);

        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 20, 20, 8, 12, 4, 8, 8, 5, false);
        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 20, 36, 8, 12, 4, 8, 8, 5, false);

        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 44, 20, 4, 12, 0, 8, 4, 5, false);
        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 44, 36, 4, 12, 0, 8, 4, 5, false);

        if (isOldSkin) {
            drawSkinRegion(g2d, skin, skinWidth, skinHeight, 44, 20, 4, 12, 12, 8, 4, 5, true);
        } else {
            drawSkinRegion(g2d, skin, skinWidth, skinHeight, 36, 52, 4, 12, 12, 8, 4, 5, false);
        }
        if (!isOldSkin) {
            drawSkinRegion(g2d, skin, skinWidth, skinHeight, 52, 52, 4, 12, 12, 8, 4, 5, false);
        }

        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 4, 20, 4, 12, 4, 13, 4, 3, false);
        drawSkinRegion(g2d, skin, skinWidth, skinHeight, 4, 36, 4, 12, 4, 13, 4, 3, false);

        if (isOldSkin) {
            drawSkinRegion(g2d, skin, skinWidth, skinHeight, 4, 20, 4, 12, 8, 13, 4, 3, true);
        } else {
            drawSkinRegion(g2d, skin, skinWidth, skinHeight, 19, 52, 4, 12, 8, 13, 4, 3, false);
        }
        if (!isOldSkin) {
            drawSkinRegion(g2d, skin, skinWidth, skinHeight, 4, 52, 4, 12, 8, 13, 4, 3, false);
        }

        g2d.dispose();
        return totem;
    }

    private static void drawSkinRegion(Graphics2D g2d, BufferedImage skin, int skinWidth, int skinHeight,
                                       int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, boolean flipX) {
        // Bounds check
        if (sx < 0 || sy < 0 || sx + sw > skinWidth || sy + sh > skinHeight) {
            SkinTotem.LOGGER.warn("Skipping out-of-bounds skin region: sx={}, sy={}, sw={}, sh={}, skinWidth={}, skinHeight={}", sx, sy, sw, sh, skinWidth, skinHeight);
            return;
        }

        if (flipX) {
            g2d.drawImage(skin, dx + dw, dy, dx, dy + dh, sx, sy, sx + sw, sy + sh, null);
        } else {
            g2d.drawImage(skin, dx, dy, dx + dw, dy + dh, sx, sy, sx + sw, sy + sh, null);
        }
    }
}
