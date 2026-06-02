package ru.kteam.skintotem.render;

public enum ModelVariant {
    DOLL_3D("3d_doll", "3D Doll"),
    TOTEM_2D("2d_totem", "2D Totem");

    public final String id;
    public final String displayName;

    ModelVariant(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static ModelVariant fromId(String id) {
        for (ModelVariant v : values()) {
            if (v.id.equalsIgnoreCase(id)) return v;
        }
        return DOLL_3D;
    }
}
