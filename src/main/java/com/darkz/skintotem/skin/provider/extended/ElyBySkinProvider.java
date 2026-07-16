package com.darkz.skintotem.skin.provider.extended;

import net.minecraft.util.Identifier;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.api.ElyByAPI;
import com.darkz.skintotem.api.Response;
import com.darkz.skintotem.doll.data.SkinTotemData;
import com.darkz.skintotem.skin.data.ParsedSkinData;
import com.darkz.skintotem.skin.provider.StandardSkinProvider;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Провайдер скинов Ely.by
 *
 * Используется когда ник содержит символ "@" перед ником:
 *   "@nickname" — загружает скин с skinsystem.ely.by
 *
 * Автор: Darkz | K-TEAM | KlashRaick 
 */
public class ElyBySkinProvider extends StandardSkinProvider {

    private static final ElyBySkinProvider INSTANCE = new ElyBySkinProvider();

    /** Префикс для обозначения Ely.by ника в тэге тотема */
    public static final String PREFIX = "@";

    private ElyBySkinProvider() {
        super(true);
    }

    public static ElyBySkinProvider getInstance() {
        return INSTANCE;
    }

    @Override
    protected Response<ParsedSkinData> loadDollFromAPI(String value) {
        String nick = stripPrefix(value);
        return ElyByAPI.getSkinData(nick);
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
        return SkinTotemMod.getDollTextureId("elyby/%s/%s".formatted(type, normalise(value)));
    }

    /**
     * Принимает только ники с явным префиксом "@".
     * Пример: "@Notch" — работает, "Notch" — не принимается.
     * Ely.by допускает ники от 2 до 25 символов: буквы, цифры, '_', '-', '.'.
     */
    @Override
    public boolean canProcess(String value) {
        if (value == null) return false;
        if (!value.startsWith(PREFIX)) return false;
        String nick = stripPrefix(value);
        int len = nick.length();
        if (len < 2 || len > 25) return false;
        for (int i = 0; i < len; i++) {
            char c = nick.charAt(i);
            if (c == '_' || c == '-' || c == '.'
                    || (c >= '0' && c <= '9')
                    || (c >= 'A' && c <= 'Z')
                    || (c >= 'a' && c <= 'z')) continue;
            return false;
        }
        return true;
    }

    private static String stripPrefix(String value) {
        return value.startsWith(PREFIX) ? value.substring(PREFIX.length()) : value;
    }

    private static String normalise(String value) {
        return stripPrefix(value).toLowerCase();
    }
}
