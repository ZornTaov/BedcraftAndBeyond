package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.tiles.TileBedcraftBed;
import zornco.bedcraftbeyond.util.BedUtils;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.Random;

public class BlockBedBase extends BlockBed {

  protected Random random;

  public BlockBedBase() {
    random = new Random();
    setHardness(1.0f);
    setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
  }

  @Override
  public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
    return state.getBlock() instanceof BlockColoredBed;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, IBlockState meta, EntityPlayer player) {
    if (!player.capabilities.isCreativeMode) {
      TileBedcraftBed tile = (TileBedcraftBed) world.getTileEntity(pos);
      ItemStack itemstack = new ItemStack(BedCraftBeyond.bedItem, 1);
      ItemStack plank = tile.getPlankType();
      NBTTagCompound nbt = new NBTTagCompound();
      //PlankHelper.validatePlank(nbt, tile.getPlankType());
      itemstack.setTagCompound(nbt);
      NBTTagCompound tags = itemstack.getTagCompound();
      tags.setInteger("blankets", tile.getBlanketsColor().getMetadata());
      tags.setInteger("sheets", tile.getSheetsColor().getMetadata());
      tags.setString("plankNameSpace", PlankHelper.plankStringfromItemStack(plank));

      itemstack.setTagCompound(tags);
      if (itemstack != null) {
        float f = random.nextFloat() * 0.8F + 0.1F;
        float f1 = random.nextFloat() * 0.8F + 0.1F;
        float f2 = random.nextFloat() * 0.8F + 0.1F;
        while (itemstack.stackSize > 0) {
          int i1 = random.nextInt(21) + 10;
          if (i1 > itemstack.stackSize) {
            i1 = itemstack.stackSize;
          }
          itemstack.stackSize -= i1;
          EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2,
                  new ItemStack(itemstack.getItem(), i1));
          float f3 = 0.05F;
          entityitem.motionX = (float) random.nextGaussian() * f3;
          entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
          entityitem.motionZ = (float) random.nextGaussian() * f3;
          if (itemstack.hasTagCompound()) {
            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
          }
          world.spawnEntityInWorld(entityitem);
        }
      }
    }
    super.onBlockHarvested(world, pos, meta, player);

  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    this.onBedActivated(world, pos, state, player);
    return true;
  }

  private boolean onBedActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
    if (worldIn.isRemote) {
      return true;
    } else {
      if (state.getValue(PART) != BlockBed.EnumPartType.HEAD) {
        pos = pos.offset(state.getValue(FACING));
        state = worldIn.getBlockState(pos);

        if (state.getBlock() != this) {
          return true;
        }
      }
      NBTTagCompound compound = new NBTTagCompound();
      TileBedcraftBed te = (TileBedcraftBed) worldIn.getTileEntity(pos);
      te.writeToNBT(compound);
      if (worldIn.provider.canRespawnHere() && false /* IS HELL */) {
        if (state.getValue(OCCUPIED).booleanValue()) {
          EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

          if (entityplayer != null) {
            playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.occupied"));
            return true;
          }

          state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
          worldIn.setBlockState(pos, state, 2);
          ((TileBedcraftBed) worldIn.getTileEntity(pos)).readFromNBT(compound);
        }

        EntityPlayer.EnumStatus entityplayer$enumstatus = playerIn.trySleep(pos);

        if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
          state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
          worldIn.setBlockState(pos, state, 2);
          worldIn.getTileEntity(pos).readFromNBT(compound);
          return true;
        } else {
          if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
            playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep"));
          } else if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
            playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.notSafe"));
          }

          return true;
        }
      } else {
        worldIn.setBlockToAir(pos);
        BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());

        if (worldIn.getBlockState(blockpos).getBlock() == this) {
          worldIn.setBlockToAir(blockpos);
        }

        worldIn.newExplosion(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, true);
        return true;
      }
    }
  }

  private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
    for (EntityPlayer entityplayer : worldIn.playerEntities) {
      if (entityplayer.isPlayerSleeping() && entityplayer.playerLocation.equals(pos)) {
        return entityplayer;
      }
    }

    return null;
  }

  @Override
  public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis) {
    return false;
  }

  /**
   * ejects contained items into the world, and notifies neighbours of an update, as appropriate
   */
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState meta) {
    super.breakBlock(world, pos, meta);
    world.removeTileEntity(pos);
  }

  /**
   * Returns the ID of the items to drop on destruction.
   */
  @Override
  public Item getItemDropped(IBlockState par1, Random par2Random, int par3) {
    return par1.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : BedCraftBeyond.bedItem;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileBedcraftBed();
  }

}
