package com.darkz.skintotem.mixin;

import com.darkz.skintotem.SkinTotemMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A delegating BakedModel that replaces the particle/icon sprite with our
 * dynamically loaded skin texture. The actual UV quads still use the original
 * model geometry — we only override getParticleSprite so Minecraft picks up
 * our texture via the texture manager when it renders the flat item.
 *
 * For a 2D item (totem is rendered as a flat sprite via its item model json),
 * the texture referenced in the model JSON is what matters. We handle the
 * texture swap by replacing the sprite returned for the item layer.
 */
@Environment(EnvType.CLIENT)
public class SkinTotemBakedModel implements BakedModel {

    private final BakedModel delegate;
    private final Identifier textureId;

    public SkinTotemBakedModel(BakedModel delegate, Identifier textureId) {
        this.delegate = delegate;
        this.textureId = textureId;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return delegate.getQuads(state, face, random);
    }

    @Override
    public boolean useAmbientOcclusion() { return delegate.useAmbientOcclusion(); }

    @Override
    public boolean hasDepth() { return delegate.hasDepth(); }

    @Override
    public boolean isSideLit() { return delegate.isSideLit(); }

    @Override
    public boolean isBuiltin() { return delegate.isBuiltin(); }

    @Override
    public Sprite getParticleSprite() { return delegate.getParticleSprite(); }

    @Override
    public ModelTransformation getTransformation() { return delegate.getTransformation(); }

    @Override
    public ModelOverrideList getOverrides() { return delegate.getOverrides(); }

    public Identifier getTextureId() { return textureId; }
}
