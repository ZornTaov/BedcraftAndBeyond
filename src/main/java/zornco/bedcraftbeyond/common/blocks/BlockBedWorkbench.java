package zornco.bedcraftbeyond.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.tiles.TileBedWorkbench;

public class BlockBedWorkbench extends Block {

   public BlockBedWorkbench(){
      super(Material.wood);
      setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
      setUnlocalizedName("bed_workbench");
      setRegistryName(BedCraftBeyond.MOD_ID, "workbench");
      setHardness(1.0f);
      setHarvestLevel("axe", 1);
   }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      // open gui
      return true;
   }

   @Override
   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   @Override
   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileBedWorkbench();
   }
}
