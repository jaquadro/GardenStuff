package com.jaquadro.minecraft.gardenstuff.world.gen;

import com.jaquadro.minecraft.gardenstuff.block.BlockCandelilla;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenCandelilla extends WorldGenerator implements IWorldGenerator
{
    public WorldGenCandelilla () {
    }

    @Override
    public void generate (Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;

        Biome biome = world.getBiome(new BlockPos(x, 0, z));
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD ))
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)
            || BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)
            || BiomeDictionary.hasType(biome, BiomeDictionary.Type.WET)
            || BiomeDictionary.hasType(biome, BiomeDictionary.Type.WASTELAND)
            || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY))
            return;

        if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY))
            return;

        if (random.nextInt(15) > 0)
            return;

        generate(world, random, new BlockPos(x, world.getSeaLevel(), z));
    }

    @Override
    public boolean generate (World world, Random rand, BlockPos pos) {
        do {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (!(block.isLeaves(state, world, pos) || block.isAir(state, world, pos)))
                break;
            pos = pos.down();
        } while (pos.getY() > 0);

        do {
            if (world.isAirBlock(pos))
                break;
            pos = pos.up();
        } while (pos.getY() < world.getActualHeight() - 1);

        int range = 5;
        for (int l = 0; l < 32; ++l) {
            BlockPos pos2 = pos.add(
                rand.nextInt(range) - rand.nextInt(range),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(range) - rand.nextInt(range)
            );

            int level = 1 + rand.nextInt(7);
            IBlockState state = ModBlocks.candelilla.getDefaultState().withProperty(BlockCandelilla.AGE, level);

            if (world.isAirBlock(pos2) && (!world.provider.isNether() || pos2.getY() < 255) && ModBlocks.candelilla.canBlockStay(world, pos2, state))
                world.setBlockState(pos2, state, 2);
        }

        return true;
    }
}
