package com.software.ddk.coloredfire.common.item;

import com.software.ddk.coloredfire.ModContent;
import com.software.ddk.coloredfire.common.block.GreenFireBlock;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.PortalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

import java.util.Iterator;

public class FlintAndSteelGreenItem extends Item {
    public FlintAndSteelGreenItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
        PlayerEntity playerEntity_1 = itemUsageContext_1.getPlayer();
        IWorld iWorld_1 = itemUsageContext_1.getWorld();
        BlockPos blockPos_1 = itemUsageContext_1.getBlockPos();
        BlockPos blockPos_2 = blockPos_1.offset(itemUsageContext_1.getSide());
        BlockState blockState_2;
        if (canIgnite(iWorld_1.getBlockState(blockPos_2), iWorld_1, blockPos_2)) {
            iWorld_1.playSound(playerEntity_1, blockPos_2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
            blockState_2 = ((GreenFireBlock) ModContent.GREEN_FIRE_BLOCK).getStateForPosition(iWorld_1, blockPos_2);
            iWorld_1.setBlockState(blockPos_2, blockState_2, 11);
            ItemStack itemStack_1 = itemUsageContext_1.getStack();
            if (playerEntity_1 instanceof ServerPlayerEntity) {
                Criterions.PLACED_BLOCK.handle((ServerPlayerEntity)playerEntity_1, blockPos_2, itemStack_1);
                itemStack_1.damage(1, (LivingEntity)playerEntity_1, ((playerEntity_1x) -> {
                    playerEntity_1x.sendToolBreakStatus(itemUsageContext_1.getHand());
                }));
            }

            return ActionResult.SUCCESS;
        } else {
            blockState_2 = iWorld_1.getBlockState(blockPos_1);
            if (isIgnitable(blockState_2)) {
                iWorld_1.playSound(playerEntity_1, blockPos_1, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
                iWorld_1.setBlockState(blockPos_1, (BlockState)blockState_2.with(Properties.LIT, true), 11);
                if (playerEntity_1 != null) {
                    itemUsageContext_1.getStack().damage(1, (LivingEntity)playerEntity_1, ((playerEntity_1x) -> {
                        playerEntity_1x.sendToolBreakStatus(itemUsageContext_1.getHand());
                    }));
                }

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        }
    }

    public static boolean isIgnitable(BlockState blockState_1) {
        return blockState_1.getBlock() == Blocks.CAMPFIRE && !(Boolean)blockState_1.get(Properties.WATERLOGGED) && !(Boolean)blockState_1.get(Properties.LIT);
    }

    public static boolean canIgnite(BlockState blockState_1, IWorld iWorld_1, BlockPos blockPos_1) {
        BlockState blockState_2 = ((FireBlock)Blocks.FIRE).getStateForPosition(iWorld_1, blockPos_1);
        boolean boolean_1 = false;
        Iterator var5 = Direction.Type.HORIZONTAL.iterator();

        while(var5.hasNext()) {
            Direction direction_1 = (Direction)var5.next();
            if (iWorld_1.getBlockState(blockPos_1.offset(direction_1)).getBlock() == Blocks.OBSIDIAN && ((PortalBlock)Blocks.NETHER_PORTAL).createAreaHelper(iWorld_1, blockPos_1) != null) {
                boolean_1 = true;
            }
        }

        return blockState_1.isAir() && (blockState_2.canPlaceAt(iWorld_1, blockPos_1) || boolean_1);
    }

}