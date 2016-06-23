package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.beds.base.BlockBedBase;
import zornco.bedcraftbeyond.beds.parts.linens.LinenColorer;
import zornco.bedcraftbeyond.beds.wooden.BlockWoodenBed;
import zornco.bedcraftbeyond.beds.wooden.WoodenBedColorer;
import zornco.bedcraftbeyond.core.BcbBlocks;
import zornco.bedcraftbeyond.core.BcbItems;
import zornco.bedcraftbeyond.dyes.DyeColorer;
import zornco.bedcraftbeyond.rugs.RugColorer;
import zornco.bedcraftbeyond.core.util.RenderingHelper;
import zornco.bedcraftbeyond.core.util.TextureHelper;

import java.awt.*;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() {

        ModelLoader.setCustomStateMapper(BcbBlocks.woodenBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBedBase.OCCUPIED, BlockBedBase.HEAD, BlockWoodenBed.SHEETS, BlockWoodenBed.BLANKETS}).build());
        ModelLoader.setCustomStateMapper(BcbBlocks.stoneBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBedBase.OCCUPIED}).build());

        RenderingHelper.registerItemModel(BcbItems.scissors);
        for (int i = 0; i < 16; ++i)
            RenderingHelper.registerItemModel(BcbItems.rug, "inventory", i);

        RenderingHelper.registerItemModel(BcbItems.blanket);
        RenderingHelper.registerItemModel(BcbItems.sheets);

        RenderingHelper.registerItemModel(BcbItems.drawerKey);
        RenderingHelper.registerItemModel(BcbItems.stoneBed);
        RenderingHelper.registerItemModel(BcbItems.woodenBed, "storage=false,head=true,status=head");

        RenderingHelper.registerItemModel(BcbItems.dyeBottle);
        RenderingHelper.registerItemModel(BcbItems.eyedropper);

        B3DLoader.INSTANCE.addDomain(BedCraftBeyond.MOD_ID);
    }

    public void init() {
        super.init();

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        // Wooden bed
        WoodenBedColorer woodenColorer = new WoodenBedColorer();
        itemColors.registerItemColorHandler(woodenColorer, BcbItems.woodenBed);
        blockColors.registerBlockColorHandler(woodenColorer, BcbBlocks.woodenBed);

        // Rugs
        RugColorer rugs = new RugColorer();
        itemColors.registerItemColorHandler(rugs, BcbItems.rug);

        // Dyes - Bottles and eyedropper
        DyeColorer dyes = new DyeColorer();
        itemColors.registerItemColorHandler(dyes, BcbItems.dyeBottle, BcbItems.eyedropper);

        LinenColorer linens = new LinenColorer();
        itemColors.registerItemColorHandler(linens, BcbItems.blanket, BcbItems.sheets);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
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
}
