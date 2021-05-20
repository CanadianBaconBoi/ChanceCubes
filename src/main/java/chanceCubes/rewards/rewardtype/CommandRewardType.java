package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class CommandRewardType extends BaseRewardType<CommandPart>
{

	public CommandRewardType(CommandPart... commands)
	{
		super(commands);
	}

	public CommandRewardType(String... commands)
	{
		super(convertToCommandParts(commands));
	}

	private static CommandPart[] convertToCommandParts(String... commands)
	{
		CommandPart[] toReturn = new CommandPart[commands.length];
		for(int i = 0; i < commands.length; i++)
			toReturn[i] = new CommandPart(commands[i]);
		return toReturn;
	}

	@Override
	public void trigger(ServerWorld world, int x, int y, int z, PlayerEntity player)
	{
		CommandPart.randUUIDs.clear();
		super.trigger(world, x, y, z, player);
	}

	@Override
	public void trigger(final CommandPart command, final ServerWorld world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Command Reward Delay", command.getDelay())
		{
			@Override
			public void callback()
			{
				int copies = command.getCopies().getIntValue() + 1;
				String commandStr = command.getParsedCommand(world, x, y, z, player);
				for(int i = 0; i < copies; i++)
				{
					if(command.areCopiesSoft().getBoolValue())
						commandStr = command.getParsedCommand(world, x, y, z, player);
					RewardsUtil.executeCommand(world, player, new BlockPos(x, y, z), commandStr);
				}
			}
		});
	}
}
