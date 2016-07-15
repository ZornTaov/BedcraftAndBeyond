package zornco.bedcraftbeyond.grill;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class BlockGrill extends Block {

	public static final PropertyBool TALL = PropertyBool.create("tall");
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool HAS_FOOD = PropertyBool.create("has_food");

    protected static AxisAlignedBB AABB = new AxisAlignedBB(2D/16D, 0.0D, 2D/16D, 14D/16D, 5D/16D, 14D/16D);
    
	public BlockGrill() {
		super(Material.IRON);
        setHardness(3f);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".grill");
        setRegistryName(BedCraftBeyond.MOD_ID, "grill");

        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(TALL, false)
                .withProperty(LIT, false)
                .withProperty(HAS_FOOD, false));

        this.setCreativeTab(BedCraftBeyond.MAIN_TAB);
        GameRegistry.register(this);

        GameRegistry.register(new ItemMultiTexture(this, this, new Function<ItemStack, String>()
        {
            @Nullable
            public String apply(@Nullable ItemStack stack)
            {
                return stack.getItemDamage() + "";
            }
        }).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {

        for (int i = 0; i < 2; ++i)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
	}
	
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, new IProperty[]{TALL, LIT, HAS_FOOD});
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TALL) ? 0 : 1;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        state = state.withProperty(TALL, (meta & 1) > 0);
        state = state.withProperty(LIT, (meta & 2) > 0);
        state = state.withProperty(HAS_FOOD, (meta & 4) > 0);
        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(TALL)) meta |= 1;
        if (state.getValue(LIT)) meta |= 2;
        if (state.getValue(HAS_FOOD)) meta |= 4;
        return meta;
    }
}
