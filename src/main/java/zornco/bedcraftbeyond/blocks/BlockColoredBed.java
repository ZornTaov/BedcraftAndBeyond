package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.item.BcbItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * A colored bed has sheets and blankets that are separately dyeable.
 * It works the same as a regular bed otherwise.
 */
public class BlockColoredBed extends BlockBedBase {

  public static PropertyBool HAS_STORAGE = PropertyBool.create("storage");
  public static PropertyBool HAS_BLANKETS = PropertyBool.create("has_blankets");
  public static PropertyBool HAS_SHEETS = PropertyBool.create("has_sheets");

  public static PropertyEnum<EnumDyeColor> BLANKETS = PropertyEnum.create("color_blankets", EnumDyeColor.class);
  public static PropertyEnum<EnumDyeColor> SHEETS = PropertyEnum.create("color_sheets", EnumDyeColor.class);

  public enum EnumColoredPart { BLANKETS, SHEETS, PLANKS }

  public BlockColoredBed() {
    setRegistryName(BedCraftBeyond.MOD_ID, "colored_bed");
    setUnlocalizedName("beds.colored");
    setDefaultState(getDefaultState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(OCCUPIED, false)
            .withProperty(HEAD, false)
            .withProperty(HAS_STORAGE, false)
            .withProperty(HAS_BLANKETS, false)
            .withProperty(HAS_SHEETS, false)
            .withProperty(BLANKETS, EnumDyeColor.WHITE)
            .withProperty(SHEETS, EnumDyeColor.WHITE));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new ExtendedBlockState(this, new IProperty[]{ HEAD, OCCUPIED, FACING, HAS_STORAGE, HAS_BLANKETS, HAS_SHEETS, BLANKETS, SHEETS}, new IUnlistedProperty[0] );
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return state.getValue(HEAD) ? new TileColoredBed() : null;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return state.getValue(HEAD);
  }

  public static TileColoredBed getTileEntity(IBlockAccess world, BlockPos bedPos){
    TileEntity te = world.getTileEntity(bedPos);
    if(te instanceof TileColoredBed) return (TileColoredBed) te;

    IBlockState state = world.getBlockState(bedPos);
    if(!(state.getBlock() instanceof BlockColoredBed)) return null;

    state = state.getActualState(world, bedPos);
    BlockPos actualTileHolder = bedPos.offset(state.getValue(BlockHorizontal.FACING));

    TileEntity realHolder = world.getTileEntity(actualTileHolder);
    if(realHolder == null || !(realHolder instanceof TileColoredBed)) return null;
    return (TileColoredBed) realHolder;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileColoredBed tile = getTileEntity(world, pos);

    if (tile == null) return true;
    if (world.isRemote) return true;

    // heldItem is a dye
    EnumDyeColor color = EnumDyeColor.byDyeDamage(heldItem.getItemDamage());

    state = getActualState(state, world, pos);
    if(state.getValue(HEAD))
      setPartColor(EnumColoredPart.SHEETS, pos, world, color);
    else
      setPartColor(EnumColoredPart.BLANKETS, pos, world, color);

    return true;
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    if(worldIn.getTileEntity(pos) == null) return state;

    TileColoredBed bed = getTileEntity(worldIn, pos);
    state = state.withProperty(BLANKETS, bed.getBlanketsColor());
    state = state.withProperty(SHEETS, bed.getSheetsColor());

    return state;
    // TODO: Add inventory and plank types
  }

  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    // TODO: Check accuracy
    ItemStack stack = new ItemStack(BcbBlocks.coloredBed, 1, state.getBlock().getMetaFromState(state));

    TileColoredBed tile = getTileEntity(world, pos);
    stack.setTagCompound(new NBTTagCompound());
    NBTTagCompound stackTags = stack.getTagCompound();


    return stack;
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    // TODO: Implement blankets, sheets, and frame separate here
    if(!state.getValue(HEAD)) return Collections.emptyList();

    ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
    ItemStack bedItem = new ItemStack(BcbItems.coloredBed);
    NBTTagCompound tags = new NBTTagCompound();
    state = getActualState(state, world, pos);
    tags.setInteger("blankets", state.getValue(BLANKETS).getMetadata());
    tags.setInteger("sheets", state.getValue(SHEETS).getMetadata());
    tags.setString("frame", "minecraft:planks@0");
    bedItem.setTagCompound(tags);
    drops.add(bedItem);
    return drops;
  }

  public static EnumDyeColor getPartColorFromItem(ItemStack stack, EnumColoredPart type){
    if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
    NBTTagCompound stackNBT = stack.getTagCompound();
    String key = type.toString().toLowerCase();

    if(!stackNBT.hasKey(key)){
      stackNBT.setInteger(key, EnumDyeColor.WHITE.getMetadata());
      stack.setTagCompound(stackNBT);
      return EnumDyeColor.WHITE;
    }

    return EnumDyeColor.byMetadata(stackNBT.getInteger(key));
  }

  public static void setPartColor(EnumColoredPart piece, BlockPos pos, World w, EnumDyeColor color){
    IBlockState stateHead = w.getBlockState(pos);
    IProperty toChange = BlockColoredBed.BLANKETS;
    TileColoredBed tile = getTileEntity(w, pos);

    switch (piece){
      case BLANKETS:
        toChange = BlockColoredBed.BLANKETS;
        tile.setBlanketsColor(color);
        break;

      case SHEETS:
        toChange = BlockColoredBed.SHEETS;
        tile.setSheetsColor(color);
        break;

      case PLANKS:
        BedCraftBeyond.logger.debug("Plank dynamic changing not yet implemented. Stahp please.");
        break;
    }

    stateHead = stateHead.withProperty(toChange, color);
    w.setBlockState(pos, stateHead, 3);
  }
}
