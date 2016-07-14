package zornco.bedcraftbeyond.grill;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class BlockGrill extends Block {

	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool HAS_FOOD = PropertyBool.create("has_food");
    
	public BlockGrill() {
		super(Material.IRON);
        setHardness(3f);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".grill");
        setRegistryName(BedCraftBeyond.MOD_ID, "grill");

        this.setDefaultState(this.blockState.getBaseState()
            .withProperty(LIT, false)
            .withProperty(HAS_FOOD, false));

        this.setCreativeTab(BedCraftBeyond.MAIN_TAB);
        GameRegistry.register(this);

        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
    	return new BlockStateContainer(this, new IProperty[]{LIT, HAS_FOOD});
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        state = state.withProperty(LIT, (meta & 1) > 0);
        state = state.withProperty(HAS_FOOD, (meta & 2) > 0);
        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(LIT)) meta |= 1;
        if (state.getValue(HAS_FOOD)) meta |= 2;
        return meta;
    }
}
