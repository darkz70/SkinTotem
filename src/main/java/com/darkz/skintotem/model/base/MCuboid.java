package com.darkz.skintotem.model.base;

import lombok.Getter;
import net.minecraft.client.model.*;
import net.minecraft.client.model.ModelPart.*;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

import java.util.Set;

@Getter
public class MCuboid extends ModelPart.Cuboid {

	private static final Set<Direction> EMPTY_SET = Set.of();
	private final Dilation dilation;

	public MCuboid(Vector3f pos, Vector3f size, Quad[] quads, Dilation dilation) {
		super(0, 0, pos.x(), pos.y(), pos.z(), size.x(), size.y(), size.z(), 0, 0, 0, false, 0, 0, EMPTY_SET);
		this.sides    = quads;
		this.dilation = dilation;
	}

	public Cuboid asCuboid() {
		return this;
	}
}
