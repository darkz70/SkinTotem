package com.darkz.skintotem.skin.provider.extended;

import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.api.*;
import com.darkz.skintotem.doll.data.SkinTotemData;
import com.darkz.skintotem.skin.data.ParsedSkinData;
import com.darkz.skintotem.skin.provider.StandardSkinProvider;
import net.minecraft.resources.Identifier;

/**
 * Провайдер прямой ссылки на скин (Url|https://...).
 * В отличие от остальных провайдеров, не ходит ни в какой API —
 * ссылка, переданная пользователем, УЖЕ является итоговым URL скина.
 */
public class UrlSkinProvider extends StandardSkinProvider {
    private static final UrlSkinProvider INSTANCE = new UrlSkinProvider();
    private UrlSkinProvider() { super(false); }
    public static UrlSkinProvider getInstance() { return INSTANCE; }

    @Override
    protected Response<ParsedSkinData> loadDollFromAPI(String value) {
        // ВНИМАНИЕ: сигнатура ParsedSkinData(skinUrl, capeUrl, elytraUrl, slim) взята по аналогии
        // с остальными провайдерами (не подтверждена вашим реальным ParsedSkinData.java) —
        // если конструктор другой, пришлите файл, поправлю.
        return Response.of(200, new ParsedSkinData(value, null, null, false));
    }

    @Override
    public SkinTotemData createNewDoll(String value) {
        return SkinTotemData.create(value);
    }

    @Override
    public Set<String> getLoadedKeys() {
        return this.getCache().values().stream().map(SkinTotemData::getNickname).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    protected Identifier getId(String value, String type) {
        String hash = Integer.toHexString(value.toLowerCase().hashCode());
        return SkinTotem.getDollTextureId("url/%s/%s".formatted(type, hash));
    }

    @Override
    public boolean canProcess(String value) {
        return value != null && (value.startsWith("http://") || value.startsWith("https://"));
    }
}
