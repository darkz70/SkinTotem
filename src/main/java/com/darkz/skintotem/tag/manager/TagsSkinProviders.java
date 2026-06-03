package com.darkz.skintotem.tag.manager;

import com.darkz.skintotem.doll.data.TotemDollData;
import com.darkz.skintotem.doll.manager.StandardTotemDollManager;
import com.darkz.skintotem.skin.provider.SkinProvider;
import com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider;
import com.darkz.skintotem.skin.provider.extended.NameMCSkinProvider;
import com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider;

import java.util.*;
import org.jetbrains.annotations.Nullable;

/**
 * Реестр провайдеров скинов.
 *
 * Поддерживаемые провайдеры:
 *   MojangAPI  — обычный ник (без префикса)  → Mojang API
 *   NameMC     — "NameMC|nickname"            → NameMC API
 *   ElyBy      — "@nickname"                  → Ely.by skinsystem
 *   TLauncher  — "#nickname"                  → TLauncher skin server
 *
 * Использование в тотеме:
 *   Положить тотем в наковальню и написать:
 *     Notch          → Mojang скин
 *     @Notch         → Ely.by скин
 *     #Notch         → TLauncher скин
 *     NameMC|Notch   → NameMC скин
 */
public class TagsSkinProviders {

    private static final Map<String, SkinProvider> SKIN_PROVIDERS_IDS = new HashMap<>();

    public static Map<String, SkinProvider> getSkinProvidersIds() {
        return SKIN_PROVIDERS_IDS;
    }

    public static void register() {
        registerProvider("NameMC",    NameMCSkinProvider.getInstance());
        registerProvider("ElyBy",     ElyBySkinProvider.getInstance());
        registerProvider("TLauncher", TLauncherSkinProvider.getInstance());
    }

    public static void registerProvider(String id, SkinProvider provider) {
        SKIN_PROVIDERS_IDS.put(id, provider);
    }

    /**
     * Проверяет, относится ли значение к провайдеру через префикс или формат "Id|nick".
     */
    public static boolean isProvider(String o) {
        if (o == null) return false;

        // Префиксные провайдеры: @nick (ElyBy), #nick (TLauncher)
        if (o.startsWith(ElyBySkinProvider.PREFIX)     && ElyBySkinProvider.getInstance().canProcess(o))     return true;
        if (o.startsWith(TLauncherSkinProvider.PREFIX) && TLauncherSkinProvider.getInstance().canProcess(o)) return true;

        // Формат "ProviderName|nickname"
        int b = o.lastIndexOf("|");
        if (b == -1) return SKIN_PROVIDERS_IDS.containsKey(o);
        String id = o.substring(0, b).split("\\|")[0].trim();
        return SKIN_PROVIDERS_IDS.containsKey(id);
    }

    @Nullable
    public static SkinProvider getProviderFor(String o) {
        if (o == null) return null;

        // Префиксные провайдеры
        if (o.startsWith(ElyBySkinProvider.PREFIX)     && ElyBySkinProvider.getInstance().canProcess(o))
            return ElyBySkinProvider.getInstance();
        if (o.startsWith(TLauncherSkinProvider.PREFIX) && TLauncherSkinProvider.getInstance().canProcess(o))
            return TLauncherSkinProvider.getInstance();

        // Формат "ProviderName|nickname"
        if (o.contains("|")) {
            String id = o.split("\\|")[0].trim();
            return SKIN_PROVIDERS_IDS.get(id);
        }
        return SKIN_PROVIDERS_IDS.get(o);
    }

    public static TotemDollData loadDollFromProvider(String o) {
        // Префиксные провайдеры
        if (o.startsWith(ElyBySkinProvider.PREFIX) && ElyBySkinProvider.getInstance().canProcess(o))
            return ElyBySkinProvider.getInstance().getOrLoadDoll(o);
        if (o.startsWith(TLauncherSkinProvider.PREFIX) && TLauncherSkinProvider.getInstance().canProcess(o))
            return TLauncherSkinProvider.getInstance().getOrLoadDoll(o);

        // Формат "ProviderName|nickname"
        if (!o.contains("|")) return StandardTotemDollManager.getStandardDoll();
        String[] split = o.split("\\|");
        String id = split[0].trim();
        SkinProvider skinProvider = SKIN_PROVIDERS_IDS.get(id);
        if (skinProvider == null || split.length < 2) return StandardTotemDollManager.getStandardDoll();
        return skinProvider.getOrLoadDoll(split[1].trim());
    }
}
