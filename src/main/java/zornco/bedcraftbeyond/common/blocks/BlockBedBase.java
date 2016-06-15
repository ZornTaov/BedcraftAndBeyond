package zornco.bedcraftbeyond.common.blocks;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeHell;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.lwjgl.util.vector.Vector2f;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.tiles.TileGenericBed;

import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import java.util.List;
import java.util.Random;

public abstract class BlockBedBase extends Block {

    protected Random random;

    public static PropertyBool HEAD = PropertyBool.create("head");
    public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static PropertyBool OCCUPIED = PropertyBool.create("occupied");

    protected static AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);

    public BlockBedBase() {
        super(Material.CLOTH);
        random = new Random();
        setHardness(1.0f);
        setCreativeTab(BedCraftBeyond.MAIN_TAB);
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
        return true;
    }

    @Override
    public boolean isBedFoot(IBlockAccess world, BlockPos pos) {
        return !world.getBlockState(pos).getValue(HEAD);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(HEAD);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(HEAD) ? new TileGenericBed(world) : null;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        BedCraftBeyond.LOGGER.debug("break block");
        super.breakBlock(worldIn, pos, state);
    }

    protected void onBlockDestroyed(World world, BlockPos pos, IBlockState state){
        BlockPos other = pos.offset(state.getValue(FACING));
        IBlockState stateOther = world.getBlockState(other);
        if(stateOther.getBlock() instanceof BlockBedBase)
            world.destroyBlock(other, false);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        IBlockState state = worldIn.getBlockState(pos);
        state = state.getActualState(worldIn, pos);
        onBlockDestroyed(worldIn, pos, state);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        BedCraftBeyond.LOGGER.debug("player destroyed");
        state = state.getActualState(worldIn, pos);
        onBlockDestroyed(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return this.onBedActivated(world, pos, state, player);
    }

    public TileEntity getTileForBed(World world, IBlockState state, BlockPos pos){
        if(state.getValue(HEAD))
            return world.getTileEntity(pos);

        if (!(state.getBlock() instanceof BlockBedBase)) return null;
        BlockPos actualTileHolder = pos.offset(state.getValue(FACING));

        TileEntity realHolder = world.getTileEntity(actualTileHolder);
        if (realHolder == null || !(realHolder instanceof TileGenericBed)) return null;
        return realHolder;
    }

    protected boolean onBedActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (worldIn.isRemote) return true;

        if (!state.getValue(HEAD)) {
            pos = pos.offset(state.getValue(FACING));
            state = worldIn.getBlockState(pos);

            if (state.getBlock() != this) return true;
        }

        // If we can sleep
        if (worldIn.provider.canRespawnHere() && !(worldIn.getBiomeGenForCoords(pos) instanceof BiomeHell)) {

            NBTTagCompound tileBackup = new NBTTagCompound();
            TileGenericBed bedTile = (TileGenericBed) ((BlockBedBase) state.getBlock()).getTileForBed(worldIn, state, pos);;
            bedTile.writeToNBT(tileBackup);

            if(state.getValue(OCCUPIED)){
                if(getPlayerInBed(worldIn, pos) != null) {
                    playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.occupied"));
                    return true;
                }

                worldIn.setBlockState(pos, state.withProperty(OCCUPIED, false), 4);
                bedTile.readFromNBT(tileBackup);
            }

            // Actually run sleep code
            EntityPlayer.SleepResult status = playerIn.trySleep(pos);
            switch (status) {
                case OK:
                    worldIn.setBlockState(pos, state.withProperty(OCCUPIED, true), 4);
                    bedTile.readFromNBT(tileBackup);
                    return true;

                case NOT_POSSIBLE_NOW:
                    playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep"));
                    break;

                case NOT_SAFE:
                    playerIn.addChatComponentMessage(new TextComponentTranslation("tile.bed.notSafe"));
                    break;
            }

            return true;
        } else {
            // Sleeping not allowed (Hell or End) - explode!
            doBedExplosion(worldIn, state, pos);
            return true;
        }
    }

    public EntityPlayer getPlayerInBed(World w, BlockPos pos){
        Vec3i range = new Vec3i(1.0, 0.8, 1.0);
        List<EntityPlayer> playersInRange = w.getPlayers(EntityPlayer.class, player -> player.isPlayerSleeping());

        if(playersInRange.size() == 0) return null;
        return playersInRange.get(0);
    }

    /**
     * Boom, baby!
     *
     * @param world
     * @param state
     * @param pos
     */
    protected void doBedExplosion(World world, IBlockState state, BlockPos pos){
        world.setBlockToAir(pos);
        if(state.getBlock() instanceof IBedTileHolder) world.removeTileEntity(pos);
        BlockPos otherHalf = pos.offset(state.getValue(FACING).getOpposite());

        if (world.getBlockState(otherHalf).getBlock() == this) {
            world.setBlockToAir(otherHalf);
        }

        world.newExplosion(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, true);
    }

    @Override
    public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis) {
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos head = pos.offset(state.getValue(FACING));
        if (worldIn.getBlockState(head).getBlock() == this)
            worldIn.destroyBlock(head, !player.capabilities.isCreativeMode);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int par3) {
        return state.getValue(HEAD) ? Item.getItemFromBlock(this) : null;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{HEAD, FACING, OCCUPIED}, new IUnlistedProperty[0]);
    }

    // States - rightmost is occupied, next is part (true = head)
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        state = state.withProperty(FACING, EnumFacing.getHorizontal(meta));
        state = state.withProperty(BlockBed.OCCUPIED, (meta & 4) > 0);
        state = state.withProperty(HEAD, (meta & 8) > 0);
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(OCCUPIED)) meta |= 4;
        if (state.getValue(HEAD)) meta |= 8;
        return meta;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }
}
