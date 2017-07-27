package toughasnails.api;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public interface ITANBlock {

	public Class<? extends ItemBlock> getItemClass();

	@SuppressWarnings("rawtypes")
	public IProperty[] getPresetProperties();

	@SuppressWarnings("rawtypes")
	public IProperty[] getNonRenderingProperties();

	public String getStateName(IBlockState state);

}