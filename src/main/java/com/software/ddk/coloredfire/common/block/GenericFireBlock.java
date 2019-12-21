package com.software.ddk.coloredfire.common.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GenericFireBlock extends FireBlock {
    private int fireTime;
    private StatusEffect statusEffect;
    private int effectTime;
    private int effectLevel;
    private int colorInt;
    private boolean effect = true;
    private VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);

    public GenericFireBlock(StatusEffect statusEffect, int colorInt, int fireTime, int effectTime, int effectLevel) {
        super(FabricBlockSettings.of(Material.FIRE)
                .noCollision()
                .ticksRandomly()
                .breakInstantly()
                .lightLevel(15)
                .sounds(BlockSoundGroup.WOOL)
                .dropsNothing()
                .build());

        this.fireTime = fireTime;
        this.statusEffect = statusEffect;
        this.effectTime = effectTime;
        this.effectLevel = effectLevel;
        this.colorInt = colorInt;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        //disable particles
    }

    public void setEffect(boolean bool){
        this.effect = bool;
    }

    public boolean isEffect(){
        return this.effect;
    }

    public int getColorInt() {
        //never used but, possibly in the future.
        return colorInt;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity_1) {
        entity_1.setOnFireFor(fireTime);
        if (isEffect() && entity_1.isAlive()){
            ((LivingEntity) entity_1).addStatusEffect(new StatusEffectInstance(statusEffect, effectTime, effectLevel));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos) {
        return SHAPE;
    }
}
