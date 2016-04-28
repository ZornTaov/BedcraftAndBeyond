package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.tiles.TileBedcraftBed;
import zornco.bedcraftbeyond.util.BedUtils;
import zornco.bedcraftbeyond.util.PlankHelper;

/***
 * A colored bed has sheets and blankets that are separately dyable.
 * It works the same as a regular bed otherwise.
 */
public class BlockColoredBed extends BlockBedBase {

  public static PropertyBool HasStorage = PropertyBool.create("storage");
  public static PropertyEnum<EnumDyeColor> BlanketColor = PropertyEnum.create("blankets", EnumDyeColor.class);
  public static PropertyEnum<EnumDyeColor> SheetColor = PropertyEnum.create("sheets", EnumDyeColor.class);

  public BlockColoredBed() {
    setRegistryName(BedCraftBeyond.MOD_ID, "colored_bed");
    setUnlocalizedName("beds.colored");
    setDefaultState(getDefaultState()
            .withProperty(HasStorage, false)
            .withProperty(BlanketColor, EnumDyeColor.WHITE)
            .withProperty(SheetColor, EnumDyeColor.WHITE));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new ExtendedBlockState(this, new IProperty[]{ PART, OCCUPIED, FACING, HasStorage, BlanketColor, SheetColor }, new IUnlistedProperty[0] );
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if(!player.isSneaking() && (heldItem != null && heldItem.getItem() instanceof ItemDye)) {
      super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
      return true;
    }

    TileBedcraftBed tile = (TileBedcraftBed) world.getTileEntity(pos);

    if (tile == null) return true;
    if (world.isRemote) return true;

    boolean foot = this.isBedFoot(world, pos);

    // heldItem is a dye
    EnumDyeColor color = EnumDyeColor.byDyeDamage(heldItem.getItemDamage());

    if(foot)
      BedUtils.setColorOfBed(BedUtils.EnumColoredBedPiece.BLANKETS, pos, world, color);
    else
      BedUtils.setColorOfBed(BedUtils.EnumColoredBedPiece.SHEETS, pos, world, color);

    return true;
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    if(worldIn.getTileEntity(pos) == null) return state;

    TileBedcraftBed bed = (TileBedcraftBed) worldIn.getTileEntity(pos);
    try {
      state = state.withProperty(BlanketColor, bed.getBlanketsColor());
      state = state.withProperty(SheetColor, bed.getSheetsColor());
    }

    catch (Exception e){
      state = state.withProperty(BlanketColor, EnumDyeColor.CYAN).withProperty(SheetColor, EnumDyeColor.WHITE);
    }

    return state;
    // TODO: Add inventory and plank types
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState();
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    if(world.setBlockToAir(pos)) {
      world.removeTileEntity(pos);
      return true;
    }

    return false;
  }

  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    // TODO: Check accuracy
    ItemStack stack = new ItemStack(BedCraftBeyond.coloredBedBlock, 1, state.getBlock().getMetaFromState(state));
    TileBedcraftBed tile = (TileBedcraftBed) world.getTileEntity(pos);
    //PlankHelper.validatePlank(nbt, tile.getPlankType());
    stack.setTagCompound(new NBTTagCompound());

    NBTTagCompound stackTags = stack.getTagCompound();
    stackTags.setString("plankNameSpace", PlankHelper.plankStringfromItemStack(tile.getPlankType()));

    return stack;
  }




}
