package zornco.bedcraftbeyond.beds.wooden;

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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import zornco.bedcraftbeyond.beds.base.BlockBedBase;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.parts.BedPart;
import zornco.bedcraftbeyond.beds.parts.IBedPart;
import zornco.bedcraftbeyond.beds.parts.drawer.DrawerHandler;
import zornco.bedcraftbeyond.beds.parts.linens.LinenHandler;
import zornco.bedcraftbeyond.beds.parts.linens.PropertyFabricType;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * A colored bed has sheets and blankets that are separately dyeable.
 * It works the same as a regular bed otherwise.
 */
public class BlockWoodenBed extends BlockBedBase {

    public static PropertyBool HAS_STORAGE = PropertyBool.create("storage");
    public static PropertyEnum<EnumWoodenFabricStatus> STATUS = PropertyEnum.create("status", EnumWoodenFabricStatus.class);

    public static PropertyEnum<PropertyFabricType> BLANKETS = PropertyEnum.create("color_blankets", PropertyFabricType.class);
    public static PropertyEnum<PropertyFabricType> SHEETS = PropertyEnum.create("color_sheets", PropertyFabricType.class);

    public BlockWoodenBed() {
        setRegistryName(BedCraftBeyond.MOD_ID, "wooden_bed");
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".beds.wooden");
        setDefaultState(getDefaultState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(OCCUPIED, false)
            .withProperty(HEAD, false)
            .withProperty(HAS_STORAGE, false)
            .withProperty(STATUS, EnumWoodenFabricStatus.FOOT)
            .withProperty(BLANKETS, PropertyFabricType.NONE)
            .withProperty(SHEETS, PropertyFabricType.NONE));

        GameRegistry.register(this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HEAD, OCCUPIED, FACING, HAS_STORAGE, STATUS, BLANKETS, SHEETS);
    }

    @Override
    public ItemStack addPart(IBlockAccess world, IBlockState state, BlockPos pos, ItemStack stack, boolean simulate) {
        if(!(stack.getItem() instanceof IBedPart)) return stack.copy();
        BedPart part = ((IBedPart) stack.getItem()).getPartReference();
        BedPart.Type type = part.getPartType();

        TileWoodenBed twb = (TileWoodenBed) getTileForBed(world, state, pos);

        // Drawers and storage
        if(type == BedPart.Type.STORAGE) {
            ItemStack accepted = twb.getDrawerHandler().addDrawer(stack, simulate);
            if((accepted == null || accepted.stackSize < stack.stackSize) && !simulate)
                twb.updateClients(part.getPartType());

            return accepted;
        }

        // Blankets and sheets
        if(type.isLinenPart()) {
            boolean hasPart = twb.getLinenHandler().hasLinenPart(type);
            if(hasPart) return stack.copy();
            if(!simulate){
                twb.getLinenHandler().setLinenPart(part.getPartType(), stack);
                twb.updateClients(part.getPartType());
            }

            if(stack.stackSize > 1) {
                ItemStack copy = stack.copy();
                copy.stackSize--;
                return copy;
            }

            if(stack.stackSize == 1) return null;
        }

        return stack.copy();
    }

    private boolean hasBlanketsAndSheets(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileWoodenBed tile = (TileWoodenBed) getTileForBed(world, state, pos);
        return tile.getLinenHandler().hasBothParts();
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
        return hasBlanketsAndSheets(state, world, pos);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(HEAD) ? new zornco.bedcraftbeyond.beds.wooden.TileWoodenBed(world) : null;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        zornco.bedcraftbeyond.beds.wooden.TileWoodenBed tile = (zornco.bedcraftbeyond.beds.wooden.TileWoodenBed) getTileForBed(world, state, pos);

        if (tile == null) return true;
        if (world.isRemote) return true;

        state = getActualState(state, world, pos);
        // Add/remove blankets and sheets


        PlayerInvWrapper wrapper = new PlayerInvWrapper(player.inventory);
        if (heldItem != null) {
            if (heldItem.getItem() instanceof IBedPart) {
                ItemStack acceptedPart = addPart(world, state, pos, heldItem, true);
                if(acceptedPart == null || acceptedPart.stackSize < heldItem.stackSize) {
                    addPart(world, state, pos, heldItem, false);
                    player.setHeldItem(hand, acceptedPart);
                    return true;
                }
            }
        }

        if (heldItem == null) {
            if (player.isSneaking()) {
                // TODO: Open bed gui here
            } else if (isBed(state, world, pos, player))
                onBedActivated(world, pos, state, player);
        }

        return true;
    }


    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (getTileForBed(worldIn, state, pos) == null) return state;

        zornco.bedcraftbeyond.beds.wooden.TileWoodenBed bed = (zornco.bedcraftbeyond.beds.wooden.TileWoodenBed) getTileForBed(worldIn, state, pos);
        boolean hl = hasBlanketsAndSheets(state, worldIn, pos);
        state = state.withProperty(BLANKETS, bed.getLinenHandler().getLinenType(BedPart.Type.BLANKETS));
        state = state.withProperty(SHEETS, bed.getLinenHandler().getLinenType(BedPart.Type.SHEETS));
        state = state.withProperty(STATUS,
            state.getValue(HEAD) ?
                (hl ? EnumWoodenFabricStatus.HEAD_LINENS : EnumWoodenFabricStatus.HEAD) :
                (hl ? EnumWoodenFabricStatus.FOOT_LINENS : EnumWoodenFabricStatus.FOOT));

        // TODO: Storage
        return state;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        // TODO: Check accuracy
        ItemStack stack = new ItemStack(ModContent.Blocks.woodenBed, 1, 0);

        zornco.bedcraftbeyond.beds.wooden.TileWoodenBed tile = (zornco.bedcraftbeyond.beds.wooden.TileWoodenBed) getTileForBed(world, state, pos);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("frame", tile.getPlankData());

        return stack;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (!state.getValue(HEAD)) return Collections.emptyList();

        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        ItemStack bedItem = new ItemStack(ModContent.Items.woodenBed);
        NBTTagCompound tags = new NBTTagCompound();
        state = getActualState(state, world, pos);
        zornco.bedcraftbeyond.beds.wooden.TileWoodenBed twb = (zornco.bedcraftbeyond.beds.wooden.TileWoodenBed) getTileForBed(world, state, pos);

        NBTTagCompound frameData = twb.getPlankData();
        tags.setTag("frame", frameData);
        bedItem.setTagCompound(tags);
        drops.add(bedItem);

        LinenHandler linens = twb.getLinenHandler();
        if (linens.getLinenPart(BedPart.Type.BLANKETS, false) != null)
            drops.add(linens.getLinenPart(BedPart.Type.BLANKETS, true));
        if (linens.getLinenPart(BedPart.Type.SHEETS, false) != null)
            drops.add(linens.getLinenPart(BedPart.Type.SHEETS, true));
        return drops;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        zornco.bedcraftbeyond.beds.wooden.TileWoodenBed twb = (zornco.bedcraftbeyond.beds.wooden.TileWoodenBed) getTileForBed(worldIn, state, pos);
        if (twb == null) return;

        try {
            NBTTagCompound frame = stack.getTagCompound().getCompoundTag("frame");
            twb.setPlankType(frame, true);
        } catch (FrameException e) {
            BedCraftBeyond.LOGGER.error("Could not set frame type from item. Invalid whitelist entry.");
            // TODO: FrameWhitelist.getFirstValidType();
            worldIn.destroyBlock(pos, true);
        }

        if (worldIn.isRemote) twb.recachePlankColor();
    }


}
