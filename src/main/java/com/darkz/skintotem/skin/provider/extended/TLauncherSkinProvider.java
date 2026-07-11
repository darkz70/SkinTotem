package com.darkz.skintotem.skin.provider.extended;

import net.minecraft.resources.ResourceLocation;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.api.Response;
import com.darkz.skintotem.api.TLauncherAPI;
import com.darkz.skintotem.doll.data.TotemDollData;
import com.darkz.skintotem.skin.data.ParsedSkinData;
import com.darkz.skintotem.skin.provider.StandardSkinProvider;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Провайдер скинов TLauncher
 *
 * Используется когда ник содержит префикс "#":
 *   "#nickname" — загружает скин с auth.tlauncher.org
 *
 * Автор: Darkz | K-TEAM | KlashRaick 
 */
public class TLauncherSkinProvider extends StandardSkinProvider {

    private static final TLauncherSkinProvider INSTANCE = new TLauncherSkinProvider();

    /** Префикс для обозначения TLauncher ника в тэге тотема */
    public static final String PREFIX = "#";

    private TLauncherSkinProvider() {
        super(true);
    }

    public static TLauncherSkinProvider getInstance() {
        return INSTANCE;
    }

    @Override
    protected Response<ParsedSkinData> loadDollFromAPI(String value) {
        String nick = stripPrefix(value);
        return TLauncherAPI.getSkinData(nick);
    }

    @Override
    public TotemDollData createNewDoll(String value) {
        return TotemDollData.create(stripPrefix(value));
    }

    @Override
    protected TotemDollData getFromCache(String value) {
        return super.getFromCache(normalise(value));
    }

    @Override
    protected void putToCache(String value, TotemDollData data) {
        super.putToCache(normalise(value), data);
    }

    @Override
    public Set<String> getLoadedKeys() {
        return this.getCache().values().stream()
                .map(TotemDollData::getNickname)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    protected ResourceLocation getId(String value, String type) {
        return SkinTotem.getDollTextureId("tlauncher/%s/%s".formatted(type, normalise(value)));
    }

    @Override
    public boolean canProcess(String value) {
        if (value == null) return false;
        if (!value.startsWith(PREFIX)) return false;
        String nick = stripPrefix(value);
        int len = nick.length();
        if (len < 3 || len > 16) return false;
        for (int i = 0; i < len; i++) {
            char c = nick.charAt(i);
            if (c == '_' || (c >= '0' && c <= '9')
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
