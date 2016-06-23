package zornco.bedcraftbeyond.beds.parts.drawer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.config.ConfigSettings;

public class ItemDrawer extends Item implements ICapabilityProvider {

    private ItemStackHandler items = new ItemStackHandler(ConfigSettings.DRAWER_ITEM_LIMIT);

    public ItemDrawer() {
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setMaxStackSize(4);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".beds.drawer");
        setRegistryName(BedCraftBeyond.MOD_ID, "drawer");

        GameRegistry.register(this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return null;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos).getActualState(worldIn, pos);
        if(!(state.getBlock() instanceof IDrawerHolder)) return EnumActionResult.FAIL;

        ItemStack accepted = ((IDrawerHolder) state.getBlock()).acceptDrawer(stack, playerIn, hand, worldIn, pos, true);
        if(accepted == null){
            ((IDrawerHolder) state.getBlock()).acceptDrawer(stack, playerIn, hand, worldIn, pos, false);
            playerIn.setHeldItem(hand, null);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
