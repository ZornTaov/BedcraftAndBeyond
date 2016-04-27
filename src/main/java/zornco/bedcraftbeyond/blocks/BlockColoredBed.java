package zornco.bedcraftbeyond.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.PlankHelper;

public class BlockColoredBed extends BlockBed {

  /** Maps the head-of-bed block to the foot-of-bed block. */
  /**
   * Maps the foot-of-bed block to the head-of-bed block.
   */
  public static final int[][] footBlockToHeadBlockMap = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

  private int colorCombo = 241;
  protected Random random;

  public BlockColoredBed() {
    // was 134
    super();
    random = new Random();
  }

  @Override
  public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
    return state.getBlock() instanceof BlockColoredBed;
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    TileColoredBed tile = (TileColoredBed) world.getTileEntity(pos);
    if (tile != null) {
      colorCombo = tile.getColorCombo();
    }
    return world.setBlockToAir(pos);
  }

  @Override
  /**
   * This returns a complete list of items dropped from this block.
   *
   * @param world The current world
   * @param x X Position
   * @param y Y Position
   * @param z Z Position
   * @param metadata Current metadata
   * @param fortune Breakers fortune level
   * @return A ArrayList containing all items this block drops
   */
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState meta, int fortune) {
    List<ItemStack> ret = new ArrayList<ItemStack>();

		/*int count = quantityDropped(metadata, fortune, world.rand);
    //int combo = 241; //red blnket white sheets
		for(int i = 0; i < count; i++)
		{
			Item id = getItemDropped(metadata, world.rand, fortune);
			if (id != null)
			{
				Block block = id instanceof ItemBlock && !isFlowerPot() ? Block.getBlockFromItem(id) : this;
				ItemStack stack = new ItemStack(id, 1, block.getDamageValue(world, x, y, z));
				NBTTagCompound nbt = new NBTTagCompound();        

				TileColoredBed tile = this.gett(TileColoredBed)world.getTileEntity(x, y, z);
				PlankHelper.validatePlank(nbt, tile.getPlankType());
				stack.setTagCompound(nbt);
				ret.add(stack);
			}
		}*/
    return ret;
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, IBlockState meta, EntityPlayer player) {
    if (!player.capabilities.isCreativeMode) {
      TileColoredBed tile = (TileColoredBed) world.getTileEntity(pos);
      ItemStack itemstack = new ItemStack(this.getBedItem(), 1, tile.getColorCombo());
      ItemStack plank = tile.getPlankType();
      NBTTagCompound nbt = new NBTTagCompound();
      //PlankHelper.validatePlank(nbt, tile.getPlankType());
      itemstack.setTagCompound(nbt);
      itemstack.getTagCompound().setString("plankNameSpace", PlankHelper.plankStringfromItemStack(plank));


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
                  new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
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

  public Item getBedItem() {
    return BedCraftBeyond.bedItem;
  }

  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    // TODO: Check accuracy
    ItemStack stack = new ItemStack(BedCraftBeyond.bedBlock, 1, state.getBlock().getMetaFromState(state));
    NBTTagCompound nbt = new NBTTagCompound();

    TileColoredBed tile = (TileColoredBed) world.getTileEntity(pos);
    //PlankHelper.validatePlank(nbt, tile.getPlankType());
    stack.setTagCompound(nbt);
    stack.getTagCompound().setString("plankNameSpace", PlankHelper.plankStringfromItemStack(tile.getPlankType()));

    return stack;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (heldItem == null || (heldItem != null && heldItem.getItem() != null && !(heldItem.getItem() instanceof ItemDye))) {
      this.onBedActivated(world, pos, state, player);
      // TODO: world.markBlockForUpdate(pos);
      return true;
    }
    TileColoredBed tile = (TileColoredBed) world.getTileEntity(pos);

    if (tile == null) {
      return true;
    }

    if (world.isRemote) {
      return true;
    }
    boolean foot = !this.isBedFoot(world, pos);

    // heldItem is a dye
    int color = heldItem.getItemDamage();
    int combo = tile.getColorCombo();

    tile.setColorCombo(BlockColoredBed.setColorToInt(combo, color, foot ? 1 : 2));
    // world.markBlockForUpdate(pos);
    EnumFacing i1 = getBedDirection(state, world, pos);
    if (!isBedFoot(world, pos)) {
      pos = pos.offset(i1.getOpposite());
    } else {
      pos = pos.offset(i1);
    }
    tile = (TileColoredBed) world.getTileEntity(pos);
    tile.setColorCombo(BlockColoredBed.setColorToInt(combo, color, foot ? 1 : 2));


    // world.markBlockForUpdate(pos);
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
      TileColoredBed te = (TileColoredBed) worldIn.getTileEntity(pos);
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
          ((TileColoredBed) worldIn.getTileEntity(pos)).readFromNBT(compound);
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

  public static int getColorFromInt(int meta, int color) {
    switch (color) {
      case 0:
        return meta >> 8;
      case 1:
        return meta >> 4 & 0xF;
      case 2:
        return meta & 0xF;
    }
    return 0;
  }

  public static int setColorToInt(int combo, int newColor, int offset) {
    switch (offset) {
      case 0:
        return (combo & (0xFF)) | (newColor << 8); // will lose top 8 bits
      case 1:
        return (combo & ~(0xF << 4)) | (newColor << 4);
      case 2:
        return (combo & ~0xF) | newColor;
    }
    return combo;
  }

  public static int getColorFromTile(IBlockAccess par1World, BlockPos pos) {

    TileColoredBed tile = (TileColoredBed) par1World.getTileEntity(pos);
    if (tile != null) {
      return tile.getColorCombo();
    }
    return 241;
  }

  public static int getPlankColorFromTile(IBlockAccess par1World, BlockPos pos) {

    TileColoredBed tile = (TileColoredBed) par1World.getTileEntity(pos);
    if (tile != null && tile.getPlankTypeNS() != null) {
      return PlankHelper.getPlankColor(tile.getPlankTypeNS());
    }
    return PlankHelper.oakColor;
  }

  public static int getColorFromTilePerPass(IBlockAccess par1World, BlockPos pos, int pass) {
    int combo = getColorFromTile(par1World, pos);
    switch (pass) {
      case 0:
        return ItemDye.dyeColors[getColorFromInt(combo, 2)];
      case 1:
        return ItemDye.dyeColors[getColorFromInt(combo, 1)];
      case 2:
        return getPlankColorFromTile(par1World, pos);
    }
    return 0;
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
    return new TileColoredBed();
  }

  /**
   * ejects contained items into the world, and notifies neighbours of an update, as appropriate
   */
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState meta) {
    super.breakBlock(world, pos, meta);

    world.removeTileEntity(pos);
  }

  public String getName() {
    // TODO: Forge registry changes
    return "CbedBlock";
  }
}
