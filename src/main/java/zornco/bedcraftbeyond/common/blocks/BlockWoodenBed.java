package zornco.bedcraftbeyond.common.blocks;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedPartStatus;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.item.linens.ItemBlanket;
import zornco.bedcraftbeyond.common.item.linens.ItemSheets;
import zornco.bedcraftbeyond.frames.FrameException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * A colored bed has sheets and blankets that are separately dyeable.
 * It works the same as a regular bed otherwise.
 */
public class BlockWoodenBed extends BlockBedBase {

    public static PropertyBool HAS_STORAGE = PropertyBool.create("storage");
    public static PropertyEnum<EnumBedPartStatus> STATUS = PropertyEnum.create("status", EnumBedPartStatus.class);

    public static PropertyEnum<EnumBedFabricType> BLANKETS = PropertyEnum.create("color_blankets", EnumBedFabricType.class);
    public static PropertyEnum<EnumBedFabricType> SHEETS = PropertyEnum.create("color_sheets", EnumBedFabricType.class);



    public enum EnumColoredPart {BLANKETS, SHEETS, PLANKS}

    public BlockWoodenBed() {
        setRegistryName(BedCraftBeyond.MOD_ID, "wooden_bed");
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".beds.wooden");
        setDefaultState(getDefaultState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(OCCUPIED, false)
            .withProperty(HEAD, false)
            .withProperty(HAS_STORAGE, false)
            .withProperty(STATUS, EnumBedPartStatus.FOOT)
            .withProperty(BLANKETS, EnumBedFabricType.NONE)
            .withProperty(SHEETS, EnumBedFabricType.NONE));

        GameRegistry.register(this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HEAD, OCCUPIED, FACING, HAS_STORAGE, STATUS, BLANKETS, SHEETS);
    }

    private boolean hasBlanketsAndSheets(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileWoodenBed tile = (TileWoodenBed) getTileForBed(world, state, pos);
        return tile.getLinenPart(EnumColoredPart.SHEETS, false) != null &&
            tile.getLinenPart(EnumColoredPart.BLANKETS, false) != null;
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
        return hasBlanketsAndSheets(state, world, pos);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(HEAD) ? new TileWoodenBed(world) : null;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileWoodenBed tile = (TileWoodenBed) getTileForBed(world, state, pos);

        if (tile == null) return true;
        if (world.isRemote) return true;

        state = getActualState(state, world, pos);
        // Add/remove blankets and sheets


        PlayerInvWrapper wrapper = new PlayerInvWrapper(player.inventory);
        if (heldItem != null) {
            if (heldItem.getItem() instanceof ItemBlanket) {
                boolean set = tile.setLinenPart(EnumColoredPart.BLANKETS, heldItem);
                if (set && !player.isCreative())
                    wrapper.extractItem(player.inventory.currentItem, 1, false);
            }

            if (heldItem.getItem() instanceof ItemSheets) {
                boolean set = tile.setLinenPart(EnumColoredPart.SHEETS, heldItem);
                if (set && !player.isCreative())
                    wrapper.extractItem(player.inventory.currentItem, 1, false);
            }
        }

        if (heldItem == null) {
            if (player.isSneaking()) {
                // TODO: Open bed gui here

            } else
                if(isBed(state, world, pos, player))
                    onBedActivated(world, pos, state, player);
        }

        return true;
    }


    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (getTileForBed(worldIn, state, pos) == null) return state;

        TileWoodenBed bed = (TileWoodenBed) getTileForBed(worldIn, state, pos);
        boolean hl = hasBlanketsAndSheets(state, worldIn, pos);
        state = state.withProperty(BLANKETS, bed.getPartType(EnumColoredPart.BLANKETS));
        state = state.withProperty(SHEETS, bed.getPartType(EnumColoredPart.SHEETS));
        state = state.withProperty(STATUS,
            state.getValue(HEAD) ?
                (hl ? EnumBedPartStatus.HEAD_LINENS : EnumBedPartStatus.HEAD) :
                (hl ? EnumBedPartStatus.FOOT_LINENS : EnumBedPartStatus.FOOT));

        // TODO: Storage
        return state;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        // TODO: Check accuracy
        ItemStack stack = new ItemStack(BcbBlocks.woodenBed, 1, 0);

        TileWoodenBed tile = (TileWoodenBed) getTileForBed(world, state, pos);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("frame", tile.getPlankData());

        return stack;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (!state.getValue(HEAD)) return Collections.emptyList();

        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        ItemStack bedItem = new ItemStack(BcbItems.woodenBed);
        NBTTagCompound tags = new NBTTagCompound();
        state = getActualState(state, world, pos);
        TileWoodenBed twb = (TileWoodenBed) getTileForBed(world, state, pos);

        NBTTagCompound frameData = twb.getPlankData();
        tags.setTag("frame", frameData);
        bedItem.setTagCompound(tags);
        drops.add(bedItem);

        if(twb.getLinenPart(EnumColoredPart.BLANKETS, false) != null)
            drops.add(twb.getLinenPart(EnumColoredPart.BLANKETS, true));
        if(twb.getLinenPart(EnumColoredPart.SHEETS, false) != null)
            drops.add(twb.getLinenPart(EnumColoredPart.SHEETS, true));
        return drops;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileWoodenBed twb = (TileWoodenBed) getTileForBed(worldIn, state, pos);
        if(twb == null) return;

        try {
            NBTTagCompound frame = stack.getTagCompound().getCompoundTag("frame");
            twb.setPlankType(frame, true);
        } catch (FrameException e) {
            BedCraftBeyond.LOGGER.error("Could not set frame type from item. Invalid whitelist entry.");
            // TODO: FrameWhitelist.getFirstValidType();
            worldIn.destroyBlock(pos, true);
        }

        if(worldIn.isRemote) twb.recachePlankColor();
    }
}
