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
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.dyes.DyeColorer;
import zornco.bedcraftbeyond.rugs.RugColorer;
import zornco.bedcraftbeyond.core.util.RenderingHelper;
import zornco.bedcraftbeyond.core.util.TextureHelper;

import java.awt.*;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() {

        ModelLoader.setCustomStateMapper(ModContent.Blocks.woodenBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBedBase.OCCUPIED, BlockBedBase.HEAD, BlockWoodenBed.SHEETS, BlockWoodenBed.BLANKETS}).build());
        ModelLoader.setCustomStateMapper(ModContent.Blocks.stoneBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBedBase.OCCUPIED}).build());

        RenderingHelper.registerItemModel(ModContent.Items.scissors);
        for (int i = 0; i < 16; ++i)
            RenderingHelper.registerItemModel(ModContent.Items.rug, "inventory", i);

        RenderingHelper.registerItemModel(ModContent.BedParts.blanket.getPartItem());
        RenderingHelper.registerItemModel(ModContent.BedParts.sheet.getPartItem());

        RenderingHelper.registerItemModel(ModContent.Items.drawerKey);
        RenderingHelper.registerItemModel(ModContent.Items.stoneBed);
        RenderingHelper.registerItemModel(ModContent.Items.suitcase);
        RenderingHelper.registerItemModel(ModContent.Items.woodenBed, "storage=false,head=true,status=head");

        RenderingHelper.registerItemModel(ModContent.Items.dyeBottle);
        RenderingHelper.registerItemModel(ModContent.Items.eyedropper);

        B3DLoader.INSTANCE.addDomain(BedCraftBeyond.MOD_ID);
    }

    public void init() {
        super.init();

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        // Wooden bed
        WoodenBedColorer woodenColorer = new WoodenBedColorer();
        itemColors.registerItemColorHandler(woodenColorer, ModContent.Items.woodenBed);
        blockColors.registerBlockColorHandler(woodenColorer, ModContent.Blocks.woodenBed);

        // Rugs
        RugColorer rugs = new RugColorer();
        itemColors.registerItemColorHandler(rugs, ModContent.Items.rug);

        // Dyes - Bottles and eyedropper
        DyeColorer dyes = new DyeColorer();
        itemColors.registerItemColorHandler(dyes, ModContent.Items.dyeBottle, ModContent.Items.eyedropper);

        LinenColorer linens = new LinenColorer();
        itemColors.registerItemColorHandler(linens, ModContent.BedParts.blanket.getPartItem(), ModContent.BedParts.sheet.getPartItem());
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
