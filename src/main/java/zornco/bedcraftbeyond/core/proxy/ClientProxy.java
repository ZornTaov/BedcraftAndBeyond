package zornco.bedcraftbeyond.core.proxy;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import zornco.bedcraftbeyond.beds.renderer.RainbowBedTER;
import zornco.bedcraftbeyond.beds.tileentity.RainbowBedTileEntity;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.util.TextureHelper;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() {

        B3DLoader.INSTANCE.addDomain(BedCraftBeyond.MOD_ID);
    }

    public void init() {
        super.init();
        BedCraftBeyond.LOGGER.info("registered " + RainbowBedTileEntity.class.toString());
        ClientRegistry.bindTileEntitySpecialRenderer(RainbowBedTileEntity.class, new RainbowBedTER());
        // ItemColors itemColors = Minecraft.getInstance().getItemColors();
        // BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        BedCraftBeyond.LOGGER.info("printing stacks {}", ItemTags.PLANKS.getAllElements().size());

        for (Block iterable_element : BlockTags.PLANKS.getAllElements()) {
            Item item = iterable_element.asItem();
            ItemStack stack = new ItemStack(item);
            BedCraftBeyond.LOGGER.info("stack {} color {}", stack.getDisplayName(), getColorFromTexture(stack));
        }
        // Wooden bed
        // WoodenBedColorer woodenColorer = new WoodenBedColorer();
        // itemColors.registerItemColorHandler(woodenColorer,
        // ModContent.Items.woodenBed);
        // blockColors.registerBlockColorHandler(woodenColorer,
        // ModContent.Blocks.woodenBed);
        //
        //// Rugs
        // RugColorer rugs = new RugColorer();
        // itemColors.registerItemColorHandler(rugs, ModContent.Items.rug);
        //
        //// Dyes - Bottles and eyedropper
        // DyeColorer dyes = new DyeColorer();
        // itemColors.registerItemColorHandler(dyes, ModContent.Items.dyeBottle,
        // ModContent.Items.eyedropper);
        //
        // LinenColorer linens = new LinenColorer();
        // itemColors.registerItemColorHandler(linens,
        // ModContent.BedParts.blanket.getPartItem(),
        // ModContent.BedParts.sheet.getPartItem());

    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public Color getColorFromTexture(ItemStack stack) {
        try {
            return TextureHelper.getItemTextureColor(stack);
        } catch (Exception e) {
            BedCraftBeyond.LOGGER.error("There was an error getting a color from a texture:");
            BedCraftBeyond.LOGGER.error(e);
            return Color.WHITE;
        }
    }

    @Override
    public PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    // @Override
    // public void openStorage(IStorageHandler handler, BlockPos tilePos, String id)
    // {
    // Minecraft mc = Minecraft.getInstance();
    // TileEntity tile = mc.theWorld.getTileEntity(tilePos);
    // mc.displayGuiScreen((GuiContainer)
    // handler.getSlotPart(id).createGUI(mc.thePlayer, tile, handler, id));
    // }
}
