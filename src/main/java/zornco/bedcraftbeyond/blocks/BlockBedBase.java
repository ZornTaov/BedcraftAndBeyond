package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import scala.util.parsing.combinator.PackratParsers;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.Random;

public abstract class BlockBedBase extends Block {

  protected Random random;

  public static PropertyBool HEAD = PropertyBool.create("head");
  public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static PropertyBool OCCUPIED = PropertyBool.create("occupied");

  public BlockBedBase() {
    super(Material.cloth);
    random = new Random();
    setHardness(1.0f);
    setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
  }

  @Override
  public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
    return true;
  }

  @Override
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    EnumFacing facing = state.getValue(FACING);
    if (state.getValue(HEAD) && worldIn.getBlockState(pos.offset(facing)).getBlock() != this) {
      worldIn.setBlockToAir(pos);
      if (!worldIn.isRemote) this.dropBlockAsItem(worldIn, pos, state, 0);
    }
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    return this.onBedActivated(world, pos, state, player);
  }

  protected boolean onBedActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
    if (worldIn.isRemote) return true;

    if (!state.getValue(HEAD)) {
      pos = pos.offset(state.getValue(FACING));
      state = worldIn.getBlockState(pos);

      if (state.getBlock() != this) return true;
    }

    if (worldIn.provider.canRespawnHere() && !(worldIn.getBiomeGenForCoords(pos) instanceof BiomeGenHell)) {
      if (state.getValue(OCCUPIED)) {
        EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

        if (entityplayer != null) {
          playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.occupied"));
          return true;
        }

        state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
        worldIn.setBlockState(pos, state, 2);
      }

      EntityPlayer.EnumStatus status = playerIn.trySleep(pos);
      switch(status){
        case OK:
          state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
          worldIn.setBlockState(pos, state, 2);
          break;

        case NOT_POSSIBLE_NOW:
          playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep"));
          break;

        case NOT_SAFE:
          playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.notSafe"));
          break;
      }

      return true;
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

  @Override
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    BlockPos head = pos.offset(state.getValue(FACING));
    if (worldIn.getBlockState(head).getBlock() == this) worldIn.destroyBlock(head, !player.capabilities.isCreativeMode);
  }

  /**
   * Returns the ID of the items to drop on destruction.
   */
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int par3) {
    return state.getValue(HEAD) ? Item.getItemFromBlock(this) : null;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new ExtendedBlockState(this, new IProperty[]{ HEAD, FACING, OCCUPIED }, new IUnlistedProperty[0] );
  }

  // States - rightmost is occupied, next is part (true = head)
  @Override
  public IBlockState getStateFromMeta(int meta) {
    IBlockState state = getDefaultState();
    state = state.withProperty(FACING, EnumFacing.getHorizontal(meta));
    state = state.withProperty(BlockBed.OCCUPIED, (meta & 4) > 0);
    state = state.withProperty(HEAD, (meta & 8) > 0);
    return state;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int meta = state.getValue(FACING).getHorizontalIndex();
    if(state.getValue(OCCUPIED)) meta |= 4;
    if(state.getValue(HEAD)) meta |= 8;
    return meta;
  }
}
