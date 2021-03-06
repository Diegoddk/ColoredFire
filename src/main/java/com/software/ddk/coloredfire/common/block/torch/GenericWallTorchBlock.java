package com.software.ddk.coloredfire.common.block.torch;

import com.software.ddk.coloredfire.ModContent;
import com.software.ddk.coloredfire.common.item.torch.GenericTorchItem;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class GenericWallTorchBlock extends WallTorchBlock implements BlockEntityProvider {
    private int COLOR = 0xffffff;

    public GenericWallTorchBlock() {
        super(FabricBlockSettings.of(Material.PART)
                .noCollision()
                .breakInstantly()
                .lightLevel(14)
                .sounds(BlockSoundGroup.WOOD)
                .dropsLike(Blocks.GLASS).build(), ModContent.GENERIC_FLAME_PARTICLE);
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
            Direction direction = (Direction)state.get(FACING);
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY() + 0.7D;
            double z = (double)pos.getZ() + 0.5D;
            double g = 0.22D;
            double h = 0.27D;
            Direction direction2 = direction.getOpposite();
            world.addParticle(ParticleTypes.SMOKE, x + 0.27D * (double)direction2.getOffsetX(), y + 0.22D, z + 0.27D * (double)direction2.getOffsetZ(), 0.0D, 0.0D, 0.0D);
            world.addParticle(ModContent.GENERIC_FLAME_PARTICLE, x + 0.27D * (double)direction2.getOffsetX(), y + 0.22D, z + 0.27D * (double)direction2.getOffsetZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && (!player.isCreative() && !isPistonAdyacent(world, pos))){
            this.dropItem(world, state, pos);
        }
        super.onBreak(world, pos, state, player);
    }

    private boolean isPistonAdyacent(World world, BlockPos pos){
        BlockPos[] nearby = new BlockPos[]{
                pos.east(), pos.west(), pos.north(), pos.south(), pos.up(), pos.down()
        };

        for (BlockPos blockPos : nearby) {
            if (world.getBlockState(blockPos).getBlock() instanceof PistonBlock) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (isPistonAdyacent(world, pos)){
            this.dropItem(world, state, pos);
        }
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
