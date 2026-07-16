package com.darkz.skintotem.skin.provider.extended;

import net.minecraft.util.Identifier;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.api.Response;
import com.darkz.skintotem.doll.data.SkinTotemData;
import com.darkz.skintotem.skin.data.ParsedSkinData;
import com.darkz.skintotem.skin.provider.StandardSkinProvider;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Провайдер скинов по прямой ссылке на PNG.
 *
 * Используется когда ник содержит префикс "url:":
 *   "url:https://example.com/skin.png" — загружает скин напрямую по ссылке
 *
 * Автор: Darkz | K-TEAM | KlashRaick
 */
public class UrlSkinProvider extends StandardSkinProvider {

    private static final UrlSkinProvider INSTANCE = new UrlSkinProvider();

    /** Префикс для обозначения прямой ссылки на скин в тэге тотема */
    public static final String PREFIX = "url:";

    private UrlSkinProvider() {
        super(false);
    }

    public static UrlSkinProvider getInstance() {
        return INSTANCE;
    }

    @Override
    protected Response<ParsedSkinData> loadDollFromAPI(String value) {
        String url = stripPrefix(value);
        return Response.of(200, new ParsedSkinData(url, null, null, false));
    }

    @Override
    public SkinTotemData createNewDoll(String value) {
        return SkinTotemData.create(stripPrefix(value));
    }

    @Override
    protected SkinTotemData getFromCache(String value) {
        return super.getFromCache(normalise(value));
    }

    @Override
    protected void putToCache(String value, SkinTotemData data) {
        super.putToCache(normalise(value), data);
    }

    @Override
    public Set<String> getLoadedKeys() {
        return this.getCache().values().stream()
                .map(SkinTotemData::getNickname)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    protected Identifier getId(String value, String type) {
        return SkinTotemMod.getDollTextureId("url/%s/%s".formatted(type, hash(value)));
    }

    /**
     * Принимает только значения с явным префиксом "url:", за которым следует
     * похожая на ссылку строка (http:// или https://).
     */
    @Override
    public boolean canProcess(String value) {
        if (value == null) return false;
        if (!value.startsWith(PREFIX)) return false;
        String url = stripPrefix(value);
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private static String stripPrefix(String value) {
        return value.startsWith(PREFIX) ? value.substring(PREFIX.length()) : value;
    }

    private static String normalise(String value) {
        return stripPrefix(value).toLowerCase();
    }

    /** Ссылки могут содержать символы, недопустимые в Identifier — сводим к безопасному хэшу. */
    private static String hash(String value) {
        return UUID.nameUUIDFromBytes(normalise(value).getBytes(StandardCharsets.UTF_8)).toString();
    }
}
