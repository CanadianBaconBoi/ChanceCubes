package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.MazeGenerator;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class MazeReward extends BaseCustomReward
{
	public MazeReward()
	{
		super(CCubesCore.MODID + ":maze", -25);
	}

	@Override
	public void trigger(final ServerWorld world, final BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		RewardsUtil.sendMessageToPlayer(player, "Generating maze..... May be some lag...");
		final MazeGenerator gen = new MazeGenerator(world, pos, player.getPosition());
		gen.generate(world, 20, 20);
		BlockPos initialPos = new BlockPos(pos.getX() - 8, pos.getY(), pos.getZ() - 8);
		player.setPositionAndUpdate(initialPos.getX() - 0.5, initialPos.getY(), initialPos.getZ() - 0.5);

		int duration = super.getSettingAsInt(settings, "time", 900, 600, 4800);

		Scheduler.scheduleTask(new Task("Maze_Reward_Update", duration, 20)
		{
			@Override
			public void callback()
			{
				gen.endMaze(player);
				if(RewardsUtil.isPlayerOnline(player))
					player.attackEntityFrom(CCubesDamageSource.MAZE_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(initialPos.distanceSq(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), false) < 4)
				{
					this.delayLeft++;
					return;
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);

				if(!world.getBlockState(new BlockPos(gen.endBlockWorldCords.getX(), gen.endBlockWorldCords.getY(), gen.endBlockWorldCords.getZ())).getBlock().equals(Blocks.OAK_SIGN))
				{
					gen.endMaze(player);
					RewardsUtil.sendMessageToPlayer(player, "Hey! You won!");
					RewardsUtil.sendMessageToPlayer(player, "Here, have a item!");
					player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
			}
		});

		RewardsUtil.sendMessageToPlayer(player, "Beat the maze and find the sign!");
		RewardsUtil.sendMessageToPlayer(player, "You have 45 seconds!");
	}
}