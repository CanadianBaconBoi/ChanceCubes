package chanceCubes.rewards.biodomeGen;

import java.util.List;
import java.util.Random;

import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class OceanBiome implements IBioDomeBiome
{
	private Random rand = new Random();

	@Override
	public void spawnEntities(BlockPos pos, World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			SquidEntity squid = EntityType.SQUID.create(world);
			squid.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
			squid.setCustomName(new StringTextComponent("Mango"));
			world.addEntity(squid);
		}
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.CLAY;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y == 0 || dist >= 0)
			return;
		blocks.add(new OffsetBlock(x, y, z, Blocks.WATER, false, delay / BioDomeReward.delayShorten));
	}
}