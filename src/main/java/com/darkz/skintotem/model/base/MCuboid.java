package com.darkz.skintotem.model.base;

import lombok.Getter;
import net.minecraft.client.model.*;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.client.model.CubeDeformation;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

import java.util.Set;

@Getter
public class MCuboid extends ModelPart.Cuboid {

	private static final Set<Direction> EMPTY_SET = Set.of();
	private final CubeDeformation dilation;

	public MCuboid(Vector3f pos, Vector3f size, Quad[] quads, CubeDeformation dilation) {
		super(0, 0, pos.x(), pos.y(), pos.z(), size.x(), size.y(), size.z(), 0, 0, 0, false, 0, 0, EMPTY_SET);
		this.sides    = quads;
		this.dilation = dilation;
	}

	public Cuboid asCuboid() {
		return this;
	}
}
