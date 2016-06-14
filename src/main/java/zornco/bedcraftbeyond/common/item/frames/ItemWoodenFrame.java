package zornco.bedcraftbeyond.common.item.frames;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.BlockBedBase;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.common.frames.FrameException;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.frames.FrameWhitelist;

import java.util.List;

public class ItemWoodenFrame extends ItemFramePlacer {

    public ItemWoodenFrame(Block b) {
        super(b);
        setUnlocalizedName("frames.wooden");
        this.setHasSubtypes(true);

        GameRegistry.register(this);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubItems(Item item, CreativeTabs tab, List subItems) {
        // TODO: Fix list of wooden beds.
        if(!(tab instanceof TabBeds)) return;
        FrameWhitelist wood =  FrameRegistry.getFrameWhitelist(FrameRegistry.EnumFrameType.WOOD);
        for(ResourceLocation rl : wood.getValidRegistryEntries()){
            ItemStack bed = new ItemStack(item, 1);
            NBTTagCompound tags = new NBTTagCompound();
            tags.setString("frameType", rl.toString());
            tags.setInteger("frameMeta", 0);
            bed.setTagCompound(tags);
            subItems.add(bed);
        }
    }

    @SuppressWarnings({"deprecation"})
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tags, boolean advanced) {
        if (!stack.hasTagCompound()) return;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("frameType")) return;
        String frameType = nbt.getString("frameType");
        Block b = Block.getBlockFromName(frameType);
        if(b == null) return;

        // TODO: Stop depending on meta here in next version (because no meta?)
        IBlockState state = b.getStateFromMeta(nbt.hasKey("frameMeta") ? nbt.getInteger("frameMeta") : 0);
        ItemStack frameStack = b.getPickBlock(state, null, player.getEntityWorld(), null, player);
        if(frameStack != null) tags.add(TextFormatting.GREEN + "Frame: " + TextFormatting.RESET + frameStack.getDisplayName());
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != EnumFacing.UP) return EnumActionResult.FAIL;

        boolean canPlaceBedHere = testSimpleBedPlacement(world, player, pos, stack);
        if (!canPlaceBedHere) return EnumActionResult.FAIL;

        pos = pos.up();
        try {
            BlockPos btmHalf = pos;
            BlockPos topHalf = btmHalf.offset(player.getHorizontalFacing());

            if(!world.isRemote) {
                IBlockState foot = BcbBlocks.woodenBed.getDefaultState().withProperty(BlockBedBase.FACING, player.getHorizontalFacing())
                    .withProperty(BlockBedBase.HEAD, false);
                if (!world.setBlockState(btmHalf, foot, 3))
                    throw new Exception();

                IBlockState head = BcbBlocks.woodenBed.getDefaultState()
                    .withProperty(BlockBedBase.HEAD, true)
                    .withProperty(BlockBedBase.FACING, player.getHorizontalFacing().getOpposite());
                if (!world.setBlockState(topHalf, head, 2))
                    throw new Exception("Failed to set blockstate.");

                TileWoodenBed tile = BlockWoodenBed.getTileEntity(world, world.getBlockState(pos), pos);
                if (tile != null) {
                    try {
                        tile.setPlankType(new ResourceLocation(stack.getTagCompound().getString("frameType")), stack.getTagCompound().getInteger("frameMeta"), false);
                    } catch (FrameException e) {
                        BedCraftBeyond.LOGGER.error("Could not set frame type from item. Invalid whitelist entry.");
                        // TODO: FrameWhitelist.getFirstValidType();
                        return EnumActionResult.FAIL;
                    }
                }
            }
        } catch (Exception e) {
            return EnumActionResult.FAIL;
        }

        // If not creative mode, remove placer item
        if (!player.capabilities.isCreativeMode) new PlayerInvWrapper(player.inventory).extractItem(player.inventory.currentItem, 1, false);

        return EnumActionResult.SUCCESS;
    }
}
