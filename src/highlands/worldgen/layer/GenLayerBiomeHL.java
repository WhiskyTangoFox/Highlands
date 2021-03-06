package highlands.worldgen.layer;

import org.apache.logging.log4j.Level;

import fabricator77.multiworld.api.biomeregistry.AdvancedBiomeRegistry;
import highlands.Highlands;
import highlands.Logs;
import highlands.api.HighlandsBiomes;
import highlands.integration.BoPBiomeLayerHelper;

import com.google.common.collect.ObjectArrays;

import net.minecraft.util.WeightedRandom;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class GenLayerBiomeHL extends GenLayer
{
    private BiomeEntry[] hotBiomes; // field_151623_c
    private BiomeEntry[] warmBiomes; // field_151621_d
    private BiomeEntry[] coolBiomes; // field_151622_e
    private BiomeEntry[] iceBiomes;  // field_151620_f

    public GenLayerBiomeHL(long par1, GenLayer par3GenLayer, WorldType par4WorldType)
    {
        super(par1);
        // Heat biomes
        this.hotBiomes = AdvancedBiomeRegistry.getBiomesOfType("hot");
        // Warm biomes
        this.warmBiomes = AdvancedBiomeRegistry.getBiomesOfType("warm");
        // Cool biomes
        this.coolBiomes = AdvancedBiomeRegistry.getBiomesOfType("cool");
        // Ice Biomes
        this.iceBiomes = AdvancedBiomeRegistry.getBiomesOfType("ice");
        this.parent = par3GenLayer;
        
        // mod
        if (HighlandsBiomes.biomesForHighlands.size() == 0) {
        	Logs.log(Level.FATAL, "[Highlands] no biomes loaded");
        	return;
        }
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    @Override
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] aint = this.parent.getInts(par1, par2, par3, par4);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i1 = 0; i1 < par4; ++i1)
        {
            for (int j1 = 0; j1 < par3; ++j1)
            {
                this.initChunkSeed((long)(j1 + par1), (long)(i1 + par2));
                int k1 = aint[j1 + i1 * par3];
                int l1 = (k1 & 3840) >> 8;
                k1 &= -3841;
                
                // if (k1 < 0) k1 = 0; // vanilla bug ??

                if (isBiomeOceanic(k1))
                {
                	if (k1 == 0 && Highlands.improvedOceans && HighlandsBiomes.ocean2 != null) {
                		k1 = HighlandsBiomes.ocean2.biomeID;
                	}
                    aint1[j1 + i1 * par3] = k1;
                }
                else if (k1 == BiomeGenBase.mushroomIsland.biomeID)
                {
                    aint1[j1 + i1 * par3] = k1;
                }
                // if plains
                else if (k1 == 1)
                {
                	aint1[j1 + i1 * par3] = getWeightedBiomeIDFromType(this.hotBiomes);
                }
                // if desert
                else if (k1 == 2)
                {
                	aint1[j1 + i1 * par3] = getWeightedBiomeIDFromType(this.warmBiomes);
                }
                // if extreme hills
                else if (k1 == 3)
                {
                    aint1[j1 + i1 * par3] = getWeightedBiomeIDFromType(this.coolBiomes);
                }
                // forest
                else if (k1 == 4)
                {
                	aint1[j1 + i1 * par3] = getWeightedBiomeIDFromType(this.iceBiomes);
                }
                else
                {
                    aint1[j1 + i1 * par3] = BiomeGenBase.mushroomIsland.biomeID;
                }
            }
        }

        return aint1;
    }
    
    private int getWeightedBiomeIDFromType(BiomeEntry[] biomeType)
    {
    	if (biomeType.length == 0) return 1;
    	int total = (int)this.nextLong(WeightedRandom.getTotalWeight(biomeType));
    	BiomeEntry biomeEntry = (BiomeEntry)WeightedRandom.getItem(biomeType, total);
    	return biomeEntry.biome.biomeID;
    }
    
    /**
     * returns true if the biomeId is one of the various ocean biomes.
     */
    protected static boolean isBiomeOceanic(int biomeID)
    {
    	if (HighlandsBiomes.ocean2 != null && biomeID == HighlandsBiomes.ocean2.biomeID) {
    		return true;
    	}
        return biomeID == BiomeGenBase.ocean.biomeID || biomeID == BiomeGenBase.deepOcean.biomeID || biomeID == BiomeGenBase.frozenOcean.biomeID;
    }
}
