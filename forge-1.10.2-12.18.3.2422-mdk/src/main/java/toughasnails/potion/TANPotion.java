/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TANPotion extends Potion {
	private static final ResourceLocation POTIONS_LOCATION = new ResourceLocation(
			"toughasnails:textures/potions/tanpotionfx.png");

	protected TANPotion(boolean isBadEffectIn, int liquidColorIn, int x, int y) {
		super(isBadEffectIn, liquidColorIn);
		this.setIconIndex(x, y);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(POTIONS_LOCATION);
		return super.hasStatusIcon();
	}
}
