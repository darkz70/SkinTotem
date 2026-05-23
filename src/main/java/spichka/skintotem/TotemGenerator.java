package spichka.skintotem;

import net.minecraft.client.texture.NativeImage;

public class TotemGenerator {
    public static NativeImage generateTotemFromSkin(NativeImage skin) {
        NativeImage totem = new NativeImage(16, 16, true);
        boolean old = skin.getHeight() == 32;

        // Head
        copy(skin, 8, 8, 4, 0, 8, 8, totem, false);
        copy(skin, 40, 8, 4, 0, 8, 8, totem, false);
        // Body
        copy(skin, 20, 20, 4, 8, 8, 5, totem, false);
        copy(skin, 20, 36, 4, 8, 8, 5, totem, false);
        // L Arm
        copy(skin, 44, 20, 0, 8, 4, 5, totem, false);
        copy(skin, 44, 36, 0, 8, 4, 5, totem, false);
        // R Arm
        if (old) copy(skin, 44, 20, 12, 8, 4, 5, totem, true);
        else {
            copy(skin, 36, 52, 12, 8, 4, 5, totem, false);
            copy(skin, 52, 52, 12, 8, 4, 5, totem, false);
        }
        // L Leg
        copy(skin, 4, 20, 4, 13, 4, 3, totem, false);
        copy(skin, 4, 36, 4, 13, 4, 3, totem, false);
        // R Leg
        if (old) copy(skin, 4, 20, 8, 13, 4, 3, totem, true);
        else {
            copy(skin, 19, 52, 8, 13, 4, 3, totem, false);
            copy(skin, 4, 52, 8, 13, 4, 3, totem, false);
        }
        return totem;
    }

    private static void copy(NativeImage s, int sx, int sy, int dx, int dy, int w, int h, NativeImage t, boolean flip) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int color = s.getColor(sx + x, sy + y);
                if ((color >> 24 & 255) > 0) { // Alpha check
                    t.setColor(flip ? dx + w - 1 - x : dx + x, dy + y, color);
                }
            }
        }
    }
}
