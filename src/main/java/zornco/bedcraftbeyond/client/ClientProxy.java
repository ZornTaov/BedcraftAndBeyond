package zornco.bedcraftbeyond.client;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.util.RenderingHelper;
import zornco.bedcraftbeyond.client.util.TextureHelper;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.BlockBedBase;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.CommonProxy;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.util.ColorHelper;

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

        ItemColors c = Minecraft.getMinecraft().getItemColors();
        IItemColor dyeBottleColorer = new Colors.DyeItemColorer();
        IItemColor linenColorer = new Colors.LinenColorer();
        IItemColor woolDamageColorer = new Colors.WoolDamageColorer();
        IItemColor frameColorer = new Colors.FrameItemColorer();

        c.registerItemColorHandler(frameColorer, BcbItems.woodenBed);
        c.registerItemColorHandler(dyeBottleColorer, BcbItems.dyeBottle, BcbItems.eyedropper);
        c.registerItemColorHandler(woolDamageColorer, BcbItems.rug);
        c.registerItemColorHandler(linenColorer, BcbItems.blanket, BcbItems.sheets);

        IBlockColor coloredBed = new Colors.BedColorer();
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(coloredBed, BcbBlocks.woodenBed);
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
