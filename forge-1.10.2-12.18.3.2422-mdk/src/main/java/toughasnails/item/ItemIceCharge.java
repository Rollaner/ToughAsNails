package toughasnails.item;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemIceCharge extends Item
{

    public ItemIceCharge()
    {
        this.setHasSubtypes(true);
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        RayTraceResult movingobjectposition = this.rayTrace(worldIn, playerIn, true);
        
        if (movingobjectposition == null)
        {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
        }
        else
        {
            if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = movingobjectposition.getBlockPos();

                if (!worldIn.isBlockModifiable(playerIn, blockpos))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
                }

                if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
                }

                BlockPos blockpos1 = blockpos.up();
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (iblockstate.getMaterial() == Material.WATER && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0 && worldIn.isAirBlock(blockpos1))
                {
                    net.minecraftforge.common.util.BlockSnapshot blocksnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(worldIn, blockpos1);
                                        
                    worldIn.setBlockState(blockpos, Blocks.ICE.getDefaultState());
                    if (net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace(playerIn, blocksnapshot, net.minecraft.util.EnumFacing.UP).isCanceled())
                    {
                        blocksnapshot.restore(true, false);
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
                    }

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        --itemStackIn.stackSize;
                    }

                    playerIn.addStat(StatList.getObjectUseStats(this));
                }
            }

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
        }
    }
}
