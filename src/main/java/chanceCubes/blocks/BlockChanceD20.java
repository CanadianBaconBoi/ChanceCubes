package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketTriggerD20;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.PacketDistributor;

public class BlockChanceD20 extends BaseChanceBlock
{

	public BlockChanceD20()
	{
		super(getBuilder().hardnessAndResistance(-1f, Integer.MAX_VALUE).lightValue(7), "chance_icosahedron");
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileChanceD20();
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
	{
		this.startd20(world, pos, player);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		return this.startd20(world, pos, player);
	}

	public boolean startd20(World world, BlockPos pos, PlayerEntity player)
	{
		if(world.isRemote || player == null || player instanceof FakePlayer)
			return true;

		TileChanceD20 te = (TileChanceD20) world.getTileEntity(pos);
		if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(CCubesItems.CHANCE_ICOSAHEDRON, 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.isScanned() ? te.getChance() : -101);
			spawnAsEntity(world, pos, stack);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			world.removeTileEntity(pos);
			return false;
		}

		RewardsUtil.executeCommand(world, player, player.getPositionVec(), "/advancement grant @p only chancecubes:chance_icosahedron");
		te.startBreaking(player);
		CCubesPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 50, world.getDimension().getType())), new PacketTriggerD20(pos));
		return false;
	}
}