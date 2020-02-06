package com.software.ddk.coloredfire.common.block.torch;

import com.software.ddk.coloredfire.ModContent;
import com.software.ddk.coloredfire.common.item.torch.GenericTorchItem;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class GenericTorchBlock extends TorchBlock implements BlockEntityProvider {
    private int COLOR = 0xffffff;

    public GenericTorchBlock() {
        super(FabricBlockSettings.of(Material.PART)
                .noCollision()
                .breakInstantly()
                .lightLevel(14)
                .sounds(BlockSoundGroup.WOOD)
                .build(), ParticleTypes.EFFECT);

        //todo - check this.
    }

    public int getCOLOR(){
        return COLOR;
    }

    public void setCOLOR(int COLOR){
        this.COLOR = COLOR;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.isClient()){
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY() + 0.7D;
            double z = (double)pos.getZ() + 0.5D;

            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            world.addParticle(ModContent.GENERIC_FLAME_PARTICLE, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.abilities.creativeMode){
            this.dropItem(world, state, pos);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        //todo - fix piston broken torch not spawning
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    private void dropItem(World world, BlockState state, BlockPos pos){
        GenericTorchItem item = (GenericTorchItem) state.getBlock().asItem();
        ItemStack stack = new ItemStack(item);
        int color = (state.getBlock().hasBlockEntity()) ? ((GenericTorchBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).getCOLOR() : 0xffffff;
        item.setColor(stack, color);
        ItemEntity entity = new ItemEntity(world.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
        world.spawnEntity(entity);

    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        GenericTorchBlockEntity blockEntity = new GenericTorchBlockEntity();
        blockEntity.setCOLOR(COLOR);
        return blockEntity;
    }
}
