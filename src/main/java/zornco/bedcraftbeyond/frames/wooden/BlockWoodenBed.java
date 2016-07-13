package zornco.bedcraftbeyond.frames.wooden;

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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.frames.base.BlockBedBase;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.linens.LinenHandler;
import zornco.bedcraftbeyond.linens.PropertyFabricType;
import zornco.bedcraftbeyond.parts.IPart;
import zornco.bedcraftbeyond.parts.IPartAcceptor;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.storage.handling.CapabilityStorageHandler;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.handling.MessageStorageUpdate;
import zornco.bedcraftbeyond.storage.handling.StoragePacketHandler;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * A colored bed has sheets and blankets that are separately dyeable.
 * It works the same as a regular bed otherwise.
 */
public class BlockWoodenBed extends BlockBedBase implements IPartAcceptor {

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
        return state.getValue(HEAD) ? new zornco.bedcraftbeyond.frames.wooden.TileWoodenBed(world) : null;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        zornco.bedcraftbeyond.frames.wooden.TileWoodenBed tile = (zornco.bedcraftbeyond.frames.wooden.TileWoodenBed) getTileForBed(world, state, pos);

        if (tile == null) return true;
        state = getActualState(state, world, pos);

        if (heldItem == null) {
            if (hitY < .5) {
                // TODO: Drawer locking?
                String storageID = state.getValue(HEAD) ? "head" : "foot";
                IStorageHandler handler = (IStorageHandler) tile.getCapability((Capability) CapabilityStorageHandler.INSTANCE, side);
                if(!handler.isSlotFilled(storageID)) return true;

                StoragePacketHandler.openStorage(world, player, tile.getPos(), side, handler, storageID);
                return true;
            }

            if (hitY >= .5 && hitY < .7) {
                if (!player.isSneaking() && hasBlanketsAndSheets(state, world, pos)) {
                    onBedActivated(world, pos, state, player);
                    return true;
                }
            }

            return false;
        }

        // Manipulation after this point- if we're on the client, exit early.
        if(world.isRemote) return true;

        Part.Type partType = Part.getPartType(heldItem);
        if (partType == Part.Type.INVALID || partType == Part.Type.UNKNOWN) return false;
        if(!canAcceptPart(world, state, pos, side, new Vector3f(hitX,hitY,hitZ),heldItem)) return false;

        if(partType == Part.Type.STORAGE && hitY < 0.5f){
            ItemStack acceptedPart = addPart(world, state, pos, side, new Vector3f(hitX, hitY, hitZ), heldItem);
            if(!player.isCreative()) player.setHeldItem(hand, acceptedPart);
            return true;
        }

        if(partType.isLinenPart() && hitY > 0.5f){
            ItemStack acceptedPart = addPart(world, state, pos, side, new Vector3f(hitX, hitY, hitZ), heldItem);
            if(!player.isCreative()) player.setHeldItem(hand, acceptedPart);
            return true;
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (getTileForBed(worldIn, state, pos) == null) return state;

        zornco.bedcraftbeyond.frames.wooden.TileWoodenBed bed = (zornco.bedcraftbeyond.frames.wooden.TileWoodenBed) getTileForBed(worldIn, state, pos);
        boolean hl = hasBlanketsAndSheets(state, worldIn, pos);
        state = state.withProperty(BLANKETS, bed.getLinenHandler().getLinenType(Part.Type.BLANKETS));
        state = state.withProperty(SHEETS, bed.getLinenHandler().getLinenType(Part.Type.SHEETS));
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

        zornco.bedcraftbeyond.frames.wooden.TileWoodenBed tile = (zornco.bedcraftbeyond.frames.wooden.TileWoodenBed) getTileForBed(world, state, pos);
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
        zornco.bedcraftbeyond.frames.wooden.TileWoodenBed twb = (zornco.bedcraftbeyond.frames.wooden.TileWoodenBed) getTileForBed(world, state, pos);

        NBTTagCompound frameData = twb.getPlankData();
        tags.setTag("frame", frameData);
        bedItem.setTagCompound(tags);
        drops.add(bedItem);

        LinenHandler linens = twb.getLinenHandler();
        if (linens.getLinenPart(Part.Type.BLANKETS, false) != null)
            drops.add(linens.getLinenPart(Part.Type.BLANKETS, true));
        if (linens.getLinenPart(Part.Type.SHEETS, false) != null)
            drops.add(linens.getLinenPart(Part.Type.SHEETS, true));
        return drops;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        zornco.bedcraftbeyond.frames.wooden.TileWoodenBed twb = (zornco.bedcraftbeyond.frames.wooden.TileWoodenBed) getTileForBed(worldIn, state, pos);
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


    @Override
    public boolean canAcceptPart(World world, IBlockState state, BlockPos pos, EnumFacing side, Vector3f hit, ItemStack stack) {
        Part.Type type = Part.getPartType(stack);
        if (type.isUnknownOrInvalid()) return false;

        TileWoodenBed twb = (TileWoodenBed) getTileForBed(world, state, pos);
        switch (type) {
            case STORAGE:
                if(!twb.hasCapability(CapabilityStorageHandler.INSTANCE, side)) return false;
                IStorageHandler handler = (IStorageHandler) twb.getCapability((Capability) CapabilityStorageHandler.INSTANCE, side);

                return !handler.isSlotFilled(state.getValue(HEAD) ? "head" : "foot");

            case BLANKETS:
            case SHEETS:
                return !twb.getLinenHandler().hasLinenPart(type);
            default:
                return false;
        }
    }

    @Override
    public ItemStack addPart(World world, IBlockState state, BlockPos pos, EnumFacing side, Vector3f hit, ItemStack stack) {
        if (!canAcceptPart(world, state, pos, side, hit, stack)) return stack.copy();

        Part part = ((IPart) stack.getItem()).getPartReference();
        Part.Type type = part.getPartType();

        TileWoodenBed twb = (TileWoodenBed) getTileForBed(world, state, pos);

        // Drawers and storage
        if (type == Part.Type.STORAGE) {
            IStorageHandler handler = (IStorageHandler) twb.getCapability((Capability) CapabilityStorageHandler.INSTANCE, side);
            String area = state.getValue(HEAD) ? "head" : "foot";

            if(handler.isSlotFilled(area)) return stack.copy();
            ItemStack pushed = handler.fillSlot(area, stack);

            if (pushed == null || pushed.stackSize < stack.stackSize){
                MessageStorageUpdate update = new MessageStorageUpdate(twb.getPos(), area, side, ItemHandlerHelper.copyStackWithSize(stack, 1));
                BedCraftBeyond.NETWORK.sendToAllAround(update,
                    new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                        pos.getX(), pos.getY(), pos.getZ(), 20));
            }

            return pushed;
        }

        // Blankets and sheets
        if (type.isLinenPart()) {
            boolean hasPart = twb.getLinenHandler().hasLinenPart(type);
            if (hasPart) return stack.copy();
            twb.getLinenHandler().setLinenPart(type, stack);
            twb.updateClients(type);

            if (stack.stackSize > 1)
                return ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - 1);

            if (stack.stackSize == 1) return null;
        }

        return stack.copy();
    }
}
