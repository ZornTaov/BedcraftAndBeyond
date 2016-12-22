package zornco.bedcraftbeyond.frames.wooden;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.util.ColorHelper;
import zornco.bedcraftbeyond.dyes.IColoredItem;
import zornco.bedcraftbeyond.dyes.eyedropper.ItemEyedropper;
import zornco.bedcraftbeyond.dyes.eyedropper.MessageEyedropperUpdate;
import zornco.bedcraftbeyond.frames.base.BlockBedBase;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.linens.ILinenItem;
import zornco.bedcraftbeyond.linens.LinenFabricTypes;
import zornco.bedcraftbeyond.linens.LinenType;
import zornco.bedcraftbeyond.linens.LinenUpdate;
import zornco.bedcraftbeyond.linens.cap.CapabilityLinenHandler;
import zornco.bedcraftbeyond.linens.cap.ILinenHandler;
import zornco.bedcraftbeyond.linens.cap.LinenHandler;
import zornco.bedcraftbeyond.linens.impl.ItemColoredBlanket;
import zornco.bedcraftbeyond.storage.IStorageItem;
import zornco.bedcraftbeyond.storage.handling.CapabilityStorageHandler;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.handling.StoragePacketHandler;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/***
 * A colored bed has sheets and blankets that are separately dyeable.
 * It works the same as a regular bed otherwise.
 */
public class BlockWoodenBed extends BlockBedBase {

    public static PropertyBool HAS_STORAGE = PropertyBool.create("storage");
    public static PropertyEnum<EnumWoodenFabricStatus> STATUS = PropertyEnum.create("status", EnumWoodenFabricStatus.class);

    public static PropertyEnum<LinenFabricTypes.BlanketTypes> BLANKETS = PropertyEnum.create("blankets", LinenFabricTypes.BlanketTypes.class);
    public static PropertyEnum<LinenFabricTypes.SheetTypes> SHEETS = PropertyEnum.create("sheets", LinenFabricTypes.SheetTypes.class);

    public BlockWoodenBed() {
        setRegistryName(BedCraftBeyond.MOD_ID, "wooden_bed");
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".beds.wooden");
        setDefaultState(getDefaultState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(OCCUPIED, false)
            .withProperty(HEAD, false)
            .withProperty(HAS_STORAGE, false)
            .withProperty(STATUS, EnumWoodenFabricStatus.FOOT)
            .withProperty(BLANKETS, LinenFabricTypes.BlanketTypes.NONE)
            .withProperty(SHEETS, LinenFabricTypes.SheetTypes.NONE));

        GameRegistry.register(this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HEAD, OCCUPIED, FACING, HAS_STORAGE, STATUS, BLANKETS, SHEETS);
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
        return true;
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

                try {
                    StoragePacketHandler.openStorage(world, player, tile.getPos(), side, handler, storageID);
                } catch (Exception e) {
                    player.addChatMessage(new TextComponentString("Error opening storage: " + e.getMessage()));
                }
                return true;
            }

            if (hitY >= .5 && hitY < .7) {
                if (!player.isSneaking()) {
                    onBedActivated(world, pos, state, player);
                    return true;
                }
            }

            return false;
        }

        // Manipulation after this point- if we're on the client, exit early.
        if(world.isRemote) return true;

        if(heldItem.getItem() instanceof ILinenItem) {
            ILinenItem linen = (ILinenItem) heldItem.getItem();
            LinenType linenType = linen.getLinenType();

            ILinenHandler linenHandler = tile.getCapability(CapabilityLinenHandler.INSTANCE, EnumFacing.UP);
            if(linenHandler.isSlotFilled(linenType)) {
                player.addChatMessage(new TextComponentTranslation("bedcraftbeyond.errors.slot_taken"));
                return false;
            }

            linenHandler.setSlotItem(linenType, heldItem.copy());
            if(!player.isCreative()) player.setHeldItem(hand, ItemHandlerHelper.copyStackWithSize(heldItem, heldItem.stackSize - 1));
            tile.markDirty();

            tile.sendLinenUpdate(linenType);
            return true;
        }

        if(heldItem.getItem() instanceof IStorageItem) {
            String storageArea = state.getValue(HEAD) ? "head" : "foot";
            IStorageHandler handler = tile.getCapability(CapabilityStorageHandler.INSTANCE, side);
            if(handler.isSlotFilled(storageArea)) return false;
            handler.setSlotItem(storageArea, ItemHandlerHelper.copyStackWithSize(heldItem, 1));
            tile.sendStorageUpdate(storageArea);
            if(!player.isCreative()) player.setHeldItem(hand, ItemHandlerHelper.copyStackWithSize(heldItem, heldItem.stackSize - 1));
            return true;
        }

        if(heldItem.getItem() instanceof ItemEyedropper) {
            LinenType storageArea = state.getValue(HEAD) ? LinenType.SHEET : LinenType.BLANKET;
            ILinenHandler handler = tile.getCapability(CapabilityLinenHandler.INSTANCE, EnumFacing.UP);

            if(handler.isSlotFilled(storageArea)) {
                ItemStack linen = handler.getSlotItem(storageArea, false);
                Color currColor = ((IColoredItem) linen.getItem()).getColorFromStack(linen);
                player.addChatMessage(new TextComponentString(TextFormatting.GREEN + storageArea.name() + ": " + TextFormatting.RESET + ColorHelper.getFormattedColorValues(currColor)));
                linen.getTagCompound().setTag("color", ColorHelper.getTagForColor(currColor));

                MessageEyedropperUpdate meu = new MessageEyedropperUpdate(player, hand, currColor.getRGB());
                BedCraftBeyond.NETWORK.sendTo(meu, (EntityPlayerMP) player);
                return true;
            }
        }

//        Part.Type partType = Part.getPartType(heldItem);
//        if (partType == Part.Type.INVALID || partType == Part.Type.UNKNOWN) return false;
//        if(!canAcceptPart(world, state, pos, side, new Vector3f(hitX,hitY,hitZ),heldItem)) return false;
//
//        if(partType == Part.Type.STORAGE && hitY < 0.5f){
//            ItemStack acceptedPart = addPart(world, state, pos, side, new Vector3f(hitX, hitY, hitZ), heldItem);
//            if(!player.isCreative()) player.setHeldItem(hand, acceptedPart);
//            return true;
//        }
//
//        if(partType.isLinenPart() && hitY > 0.5f){
//            ItemStack acceptedPart = addPart(world, state, pos, side, new Vector3f(hitX, hitY, hitZ), heldItem);
//            if(!player.isCreative()) player.setHeldItem(hand, acceptedPart);
//            return true;
//        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (getTileForBed(worldIn, state, pos) == null) return state;

        zornco.bedcraftbeyond.frames.wooden.TileWoodenBed bed = (zornco.bedcraftbeyond.frames.wooden.TileWoodenBed) getTileForBed(worldIn, state, pos);

        ILinenHandler linens = bed.getCapability(CapabilityLinenHandler.INSTANCE, EnumFacing.UP);

        ItemStack blanket = linens.getSlotItem(LinenType.BLANKET, false);
        if(blanket != null) state = state.withProperty(BLANKETS, LinenFabricTypes.BlanketTypes.valueOf(blanket.getTagCompound().getString("type")));

        ItemStack sheet = linens.getSlotItem(LinenType.SHEET, false);
        if(sheet != null) state = state.withProperty(SHEETS, LinenFabricTypes.SheetTypes.valueOf(sheet.getTagCompound().getString("type")));

        boolean hasLinens = linens.isSlotFilled(LinenType.SHEET) && linens.isSlotFilled(LinenType.BLANKET);

        state = state.withProperty(STATUS,
            state.getValue(HEAD) ?
                (hasLinens ? EnumWoodenFabricStatus.HEAD_LINENS : EnumWoodenFabricStatus.HEAD) :
                (hasLinens ? EnumWoodenFabricStatus.FOOT_LINENS : EnumWoodenFabricStatus.FOOT));

        IStorageHandler storage = bed.getCapability(CapabilityStorageHandler.INSTANCE, null);
        boolean filled = storage.isSlotFilled(state.getValue(HEAD) ? "head" : "foot");
        state = state.withProperty(HAS_STORAGE, filled);

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
        if(twb == null) return drops;

        NBTTagCompound frameData = twb.getPlankData();
        tags.setTag("frame", frameData);
        bedItem.setTagCompound(tags);
        drops.add(bedItem);

        // TODO: Reimplement drops
        ILinenHandler linens = twb.getCapability(CapabilityLinenHandler.INSTANCE, EnumFacing.UP);
        Iterator i = linens.getAvailableSlots().iterator();
        while(i.hasNext()) {
            LinenType type = (LinenType) i.next();
            if(linens.isSlotFilled(type)) {
                ItemStack drop = linens.getSlotItem(type, true);
                drops.add(drop);
            }
        }

        drops.addAll(twb.storage.getItems());
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

}
