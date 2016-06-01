package zornco.bedcraftbeyond.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.gui.GuiCarpenter;
import zornco.bedcraftbeyond.client.gui.IClientGui;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.gui.ContainerCarpenter;
import zornco.bedcraftbeyond.server.gui.IServerGui;

import javax.annotation.Nullable;

public class BlockCarpenter extends Block implements IClientGui, IServerGui {

    public BlockCarpenter() {
        super(Material.WOOD);
        setRegistryName(BedCraftBeyond.MOD_ID, "carpenter");
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".carpenter");
        setDefaultState(getDefaultState());

        GameRegistry.register(this);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCarpenter();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        playerIn.openGui(BedCraftBeyond.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public Object getClientGui(World w, BlockPos pos, EntityPlayer player) {
        return new GuiCarpenter(player, (TileCarpenter) w.getTileEntity(pos));
    }

    @Override
    public Object getServerGui(World w, BlockPos pos, EntityPlayer player) {
        return new ContainerCarpenter(player, (TileCarpenter) w.getTileEntity(pos));
    }
}
