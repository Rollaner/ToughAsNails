/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

import toughasnails.api.season.Season.SubSeason;

public interface ISeasonData 
{
    /**
     * The elapsed time in ticks for the current season cycle.
     * Cycles are split into seasons rather than sub seasons
     * i.e. Summer, Autumn, Winter and Spring.
     * 
     * @return The time in ticks
     */
    int getSeasonCycleTicks();
    
    
    SubSeason getSubSeason();
}
