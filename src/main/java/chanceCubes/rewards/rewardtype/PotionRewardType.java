package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class PotionRewardType extends BaseRewardType<PotionPart>
{
	public PotionRewardType(PotionPart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(final PotionPart part, final ServerWorld world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Potion Reward Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				ItemStack potion = new ItemStack(Items.SPLASH_POTION);

				List<EffectInstance> effects = new ArrayList<>();
				effects.add(part.getEffect());
				PotionUtils.appendEffects(potion, effects);

				PotionEntity entity = new PotionEntity(world, player);
				entity.setItem(PotionUtils.appendEffects(new ItemStack(Items.SPLASH_POTION), effects));
				entity.setPosition(player.getPosX(), player.getPosY() + 2, player.getPosZ());
				entity.setMotion(0, 0.1, 0);

				world.addEntity(entity);
			}
		});
	}
}