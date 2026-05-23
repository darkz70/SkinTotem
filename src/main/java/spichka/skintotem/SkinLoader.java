package spichka.skintotem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SkinLoader {

    public static CompletableFuture<Void> loadSkin(String nickname) {
        Path skinTotemDir = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("skintotem");
        File skinFile = skinTotemDir.resolve(nickname + ".png").toFile();

        if (skinFile.exists()) {
            SkinTotem.LOGGER.info("Skin for {} already exists, loading from cache.", nickname);
            SimpleTextureLoader.loadDynamicTexture(nickname);
            return CompletableFuture.completedFuture(null);
        }

        return MojangApi.getTotemOrSkinFromAnySource(nickname).thenCompose(image -> {
            if (image == null) {
                SkinTotem.LOGGER.warn("Could not fetch skin for {} from any source.", nickname);
                return CompletableFuture.completedFuture(null);
            }

            return CompletableFuture.supplyAsync(() -> {
                try {
                    BufferedImage finalImage;
                    if (image.getWidth() == 64 && (image.getHeight() == 64 || image.getHeight() == 32)) {
                        SkinTotem.LOGGER.info("Generating totem from skin for {}.", nickname);
                        finalImage = TotemGenerator.generateTotemFromSkin(image);
                    } else {
                        SkinTotem.LOGGER.info("Using provided totem image for {}.", nickname);
                        finalImage = image;
                    }

                    if (!skinTotemDir.toFile().exists()) {
                        skinTotemDir.toFile().mkdirs();
                    }
                    ImageIO.write(finalImage, "PNG", skinFile);
                    SkinTotem.LOGGER.info("Saved totem for {} to {}.", nickname, skinFile.getAbsolutePath());

                    return nickname;
                } catch (Exception e) {
                    SkinTotem.LOGGER.error("Error loading or generating skin for {}: {}", nickname, e.getMessage());
                    return null;
                }
            });
        }).thenAccept(loadedNickname -> {
            if (loadedNickname != null) {
                SimpleTextureLoader.loadDynamicTexture(loadedNickname);
            }
        });
    }
}
