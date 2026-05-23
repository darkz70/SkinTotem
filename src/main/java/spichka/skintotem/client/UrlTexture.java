package spichka.skintotem.client;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

import java.io.InputStream;
import java.net.URL;

public class UrlTexture extends NativeImageBackedTexture {

    public UrlTexture(String url) throws Exception {
        super(load(url));
    }

    private static NativeImage load(String url) throws Exception {
        try (InputStream stream = new URL(url).openStream()) {
            return NativeImage.read(stream);
        }
    }
}
