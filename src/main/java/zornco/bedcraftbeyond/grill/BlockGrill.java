package zornco.bedcraftbeyond.grill;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockStone;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;

public class BlockGrill extends Block implements ITileEntityProvider {

	//-------------------------------------------------------------------------------
    // Setup
	public static final PropertyBool TALL = PropertyBool.create("tall");
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool HAS_FOOD = PropertyBool.create("has_food");

    protected static AxisAlignedBB AABBShort = BlockGrill.makeAxisAlignedBB(2, 0, 2, 14, 11, 14);
    protected static AxisAlignedBB AABBTall = BlockGrill.makeAxisAlignedBB(2, 0, 2, 14, 15, 14);
    
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
	
	//-------------------------------------------------------------------------------
    // World Interact
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(heldItem != null && heldItem.getItem() == Items.FLINT_AND_STEEL)
		{
			if (hitY < AABBShort.maxY) {
	            worldIn.setBlockState(pos, state.withProperty(LIT, true), 3);

	            if (heldItem.getItem() == Items.FLINT_AND_STEEL)
	            {
	                heldItem.damageItem(1, playerIn);
	            }
	            if (!worldIn.isRemote)
	            	worldIn.playSound((EntityPlayer)null, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return true;
		}
		return false;
	}

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModContent.Blocks.grill, 1, state.getValue(TALL)?0:1);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 60;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	if (state.getValue(LIT)) {
    		if(!state.getValue(HAS_FOOD))
    		{
    			if(worldIn.rand.nextInt(10) > 7)
    				worldIn.setBlockState(pos, state.withProperty(LIT, false), 3);
    		}
    		else
    			;//cook food
		}
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
    }
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
    }
    
    //-------------------------------------------------------------------------------
    // Tile Entity
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileGrill();
	}

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileGrill)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileGrill)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    //-------------------------------------------------------------------------------
    // Rendering
	
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	if (state.getValue(TALL)) 
    		return AABBTall;
    	else
    		return AABBShort;
    }
    public static AxisAlignedBB makeAxisAlignedBB(int x1, int y1, int z1, int x2, int y2, int z2)
    {
    	return new AxisAlignedBB(x1/16D, y1/16D, z1/16D, x2/16D, y2/16D, z2/16D);
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

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    
    //-------------------------------------------------------------------------------
    // State
    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, new IProperty[]{TALL, LIT, HAS_FOOD});
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
	public static void setState(boolean burning, World worldObj, BlockPos pos) {

        IBlockState iblockstate = worldObj.getBlockState(pos);
        TileEntity tileentity = worldObj.getTileEntity(pos);

        worldObj.setBlockState(pos, ModContent.Blocks.grill.getDefaultState().withProperty(LIT, burning), 3);
        

        if (tileentity != null)
        {
            tileentity.validate();
            worldObj.setTileEntity(pos, tileentity);
        }
	}
}
