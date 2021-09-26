package chanceCubes.blocks;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFallingCustom extends FallingBlockEntity
{
	private int normY;
	private OffsetBlock osb;
	private BlockState fallTile;

	public BlockFallingCustom(Level level, double x, double y, double z, BlockState state, int normY, OffsetBlock osb)
	{
		super(level, x, y, z, state);
		fallTile = state;
		this.normY = normY;
		this.osb = osb;
	}

	@Override
	public void tick()
	{
		if(this.fallTile.getBlockState().isAir(level, this.getPosition()))
		{
			this.remove();
		}
		else
		{
			this.prevPosX = this.getX();
			this.prevPosY = this.getY();
			this.prevPosZ = this.getZ();
			Block block = this.fallTile.getBlock();
			if(this.fallTime++ == 0)
			{
				BlockPos blockpos = new BlockPos(this.getPosition());
				if(this.world.getBlockState(blockpos).getBlock() == block)
				{
					this.world.removeBlock(blockpos, false);
				}
				else if(this.world.isRemote)
				{
					this.remove();
					return;
				}
			}

			this.setMotion(0, this.getMotion().y - 0.04, 0);
			this.move(MoverType.SELF, this.getMotion());
			if(!this.world.isRemote)
			{
				BlockPos blockpos1 = new BlockPos(this.getPosition());
				if(this.onGround)
				{
					BlockState iblockstate = this.world.getBlockState(blockpos1);
					this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));

					if(iblockstate.getBlock() != Blocks.PISTON_HEAD)
					{
						this.remove();
						if(block instanceof FallingBlock)
							osb.placeInWorld(world, blockpos1, false, null);

						if(this.tileEntityData != null && block.hasTileEntity(fallTile))
						{
							TileEntity tileentity = this.world.getTileEntity(blockpos1);

							if(tileentity != null)
							{
								CompoundNBT nbttagcompound = new CompoundNBT();
								tileentity.write(nbttagcompound);

								for(String s : this.tileEntityData.keySet())
								{
									INBT nbtbase = this.tileEntityData.get(s);

									if(!s.equals("x") && !s.equals("y") && !s.equals("z"))
										nbttagcompound.put(s, nbtbase.copy());
								}

								tileentity.read(tileentity.getBlockState(), nbttagcompound);
								tileentity.markDirty();
							}
						}

					}
				}
				else if(this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
				{
					if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
						this.entityDropItem(new ItemStack(block, 1), 0.0F);

					this.remove();
				}
				else if((normY >= (getPosY() + this.getMotion().y) && this.getMotion().y <= 0) || this.getMotion().y == 0)
				{
					this.remove();
					osb.placeInWorld(world, blockpos1, false, null);
				}
			}
		}
	}
}