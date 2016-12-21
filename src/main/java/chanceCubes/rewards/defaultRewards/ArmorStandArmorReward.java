package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArmorStandArmorReward implements IChanceCubeReward
{
	private Random random = new Random();
	// @formatter:off
	private ItemStack[] headItems = {new ItemStack(Items.CHAINMAIL_HELMET), new ItemStack(Items.DIAMOND_HELMET),
			new ItemStack(Items.GOLDEN_HELMET), new ItemStack(Items.IRON_HELMET), new ItemStack(Items.LEATHER_HELMET),
			new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.SKULL, 1, 2),
			new ItemStack(Items.SKULL, 1, 3), new ItemStack(Items.SKULL, 1, 4), new ItemStack(Items.SKULL, 1, 5),
			new ItemStack(Blocks.CHEST)};
	
	private ItemStack[] chestItems = {new ItemStack(Items.CHAINMAIL_CHESTPLATE), new ItemStack(Items.DIAMOND_CHESTPLATE),
			new ItemStack(Items.GOLDEN_CHESTPLATE), new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.LEATHER_CHESTPLATE),
			new ItemStack(Items.ELYTRA), new ItemStack(Items.BANNER)};
	
	private ItemStack[] legsItems = {new ItemStack(Items.CHAINMAIL_LEGGINGS), new ItemStack(Items.DIAMOND_LEGGINGS),
			new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.LEATHER_LEGGINGS)};
	
	private ItemStack[] bootsItems = {new ItemStack(Items.CHAINMAIL_BOOTS), new ItemStack(Items.DIAMOND_BOOTS),
			new ItemStack(Items.GOLDEN_BOOTS), new ItemStack(Items.IRON_BOOTS), new ItemStack(Items.LEATHER_BOOTS)};
	
	private ItemStack[] handItems = {new ItemStack(Items.CAKE), new ItemStack(Blocks.TORCH),
			new ItemStack(Items.SHIELD), new ItemStack(Items.IRON_SWORD), new ItemStack(Items.DIAMOND_HOE),
			new ItemStack(Items.BANNER), new ItemStack(Items.COOKIE), new ItemStack(Items.STICK),
			new ItemStack(Items.GOLDEN_CARROT)};
	// @formatter:on

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		EntityArmorStand armorStand = new EntityArmorStand(world);
		armorStand.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
		armorStand.setItemStackToSlot(EntityEquipmentSlot.HEAD, headItems[random.nextInt(headItems.length)].copy());
		armorStand.setItemStackToSlot(EntityEquipmentSlot.CHEST, chestItems[random.nextInt(chestItems.length)].copy());
		armorStand.setItemStackToSlot(EntityEquipmentSlot.LEGS, legsItems[random.nextInt(legsItems.length)].copy());
		armorStand.setItemStackToSlot(EntityEquipmentSlot.FEET, bootsItems[random.nextInt(bootsItems.length)].copy());
		armorStand.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, handItems[random.nextInt(handItems.length)].copy());
		armorStand.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, handItems[random.nextInt(handItems.length)].copy());
		world.spawnEntityInWorld(armorStand);
	}

	@Override
	public int getChanceValue()
	{
		return 40;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Armor_Stand_Armor";
	}

}