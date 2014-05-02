package highlands.worldgen.layer;

import highlands.api.HighlandsBiomes;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddIslandHL extends GenLayer
{

    public GenLayerAddIslandHL(long par1, GenLayer par3GenLayer)
    {
        super(par1);
        this.parent = par3GenLayer;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);
        // int oceanBiomeID = BiomeGenBase.ocean.biomeID;
        int oceanBiomeID = HighlandsBiomes.ocean2.biomeID;

        for (int i2 = 0; i2 < par4; ++i2)
        {
            for (int j2 = 0; j2 < par3; ++j2)
            {
                int k2 = aint[j2 + 0 + (i2 + 0) * k1];
                int l2 = aint[j2 + 2 + (i2 + 0) * k1];
                int i3 = aint[j2 + 0 + (i2 + 2) * k1];
                int j3 = aint[j2 + 2 + (i2 + 2) * k1];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];
                this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));

                if (isOceanBiome(k3) && (!isOceanBiome(k2) || !isOceanBiome(l2) || !isOceanBiome(i3) || !isOceanBiome(j3)))
                {
                	// make island plains, but randomly make some forest
                    int l3 = 1;// counter
                    int i4 = 1;// plains

                    if (k2 != oceanBiomeID && this.nextInt(l3++) == 0)
                    {
                        i4 = k2;
                    }

                    if (l2 != oceanBiomeID && this.nextInt(l3++) == 0)
                    {
                        i4 = l2;
                    }

                    if (i3 != oceanBiomeID && this.nextInt(l3++) == 0)
                    {
                        i4 = i3;
                    }

                    if (j3 != oceanBiomeID && this.nextInt(l3++) == 0)
                    {
                        i4 = j3;
                    }

                    if (this.nextInt(3) == 0)
                    {
                        aint1[j2 + i2 * par3] = i4;
                    }
                    else if (isIslandBiome(i4)) // forest island
                    {
                        aint1[j2 + i2 * par3] = i4; // forest
                    }
                    else
                    {
                        aint1[j2 + i2 * par3] = oceanBiomeID;
                    }
                }
                else if (k3 > 0 && (isOceanBiome(k2) || isOceanBiome(l2) || isOceanBiome(i3) || isOceanBiome(j3)))
                {
                    if (this.nextInt(5) == 0)
                    {
                        if (isIslandBiome(k3)) // island
                        {
                            aint1[j2 + i2 * par3] = k3; // island
                        }
                        else
                        {
                            aint1[j2 + i2 * par3] = oceanBiomeID;
                        }
                    }
                    else
                    {
                        aint1[j2 + i2 * par3] = k3;
                    }
                }
                else
                {
                    aint1[j2 + i2 * par3] = k3;
                }
            }
        }

        return aint1;
    }
    
    protected boolean isIslandBiome (int id) {
    	if (id == HighlandsBiomes.jungleIsland.biomeID ||
		id == HighlandsBiomes.forestIsland.biomeID ||
		id == HighlandsBiomes.desertIsland.biomeID ||
		id ==  HighlandsBiomes.snowIsland.biomeID ||
		id ==  HighlandsBiomes.volcanoIsland.biomeID ||
		id ==  HighlandsBiomes.rockIsland.biomeID ||
		id ==  HighlandsBiomes.windyIsland.biomeID ||
		id == BiomeGenBase.forest.biomeID)
    	{
    		return true;
    	}
    	return false;
    }
    
    protected boolean isOceanBiome (int id) {
    	if (id == 0 || id == BiomeGenBase.deepOcean.biomeID || id == HighlandsBiomes.ocean2.biomeID) {
    		return true;
    	}
    	return false;
    }
}