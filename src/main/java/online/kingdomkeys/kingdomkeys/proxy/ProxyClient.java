package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import online.kingdomkeys.kingdomkeys.client.gui.*;
import online.kingdomkeys.kingdomkeys.client.gui.hud.HUDElementHandler;
import online.kingdomkeys.kingdomkeys.corsair.KeyboardManager;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ProxyClient implements IProxy {

    public static KeyboardManager keyboardManager;

    @Override
    public void setup(FMLCommonSetupEvent event) {

       // ((KeybladeItem)ModItems.kingdomKey).getProperties().setTEISR(()->()->new KeybladeRenderer());

        /*if (ClientConfig.CORSAIR_KEYBOARD_LIGHTING) {
            keyboardManager = new KeyboardManager();
            MinecraftForge.EVENT_BUS.register(new CorsairTickHandler(keyboardManager));
            keyboardManager.showLogo();
        }*/

        //OBJLoader and B3DLoader currently aren't hooked up however, this is here for when they are
        //OBJLoader.INSTANCE.addDomain(KingdomKeys.MODID);
        //TODO convert B3D models to OBJ so we don't need this
        //B3DLoader.INSTANCE.addDomain(KingdomKeys.MODID);
       // new ScrollCallbackWrapper().setup(Minecraft.getInstance());
        MinecraftForge.EVENT_BUS.register(new HUDElementHandler());
        //MinecraftForge.EVENT_BUS.register(new CommandMenuGui());
        MinecraftForge.EVENT_BUS.register(new PlayerPortraitGui());
        MinecraftForge.EVENT_BUS.register(new HPGui());
        MinecraftForge.EVENT_BUS.register(new MPGui());
        MinecraftForge.EVENT_BUS.register(new DriveGui());
        MinecraftForge.EVENT_BUS.register(new InputHandler());

    }

    //Register the entity models
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModEntities.registerModels();
		//ModelLoader.setCustomModelResourceLocation(ModItems.kingdomKey, 0, new ModelResourceLocation("", "inventory"));
    }

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
            ClientRegistry.registerKeyBinding(key.getKeybind());
        
		MinecraftForge.EVENT_BUS.register(new GuiOverlay());

    }

    /*@SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        //TODO make this simpler for doing this for every model
        try {
            IUnbakedModel model = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(KingdomKeys.MODID + ":item/kingdom_key.obj"));

            if (model instanceof OBJModel) {
                IBakedModel bakedModel = model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(model.getDefaultState(), false), DefaultVertexFormats.ITEM);
                event.getModelRegistry().put(new ModelResourceLocation(KingdomKeys.MODID + ":kingdom_key", "inventory"), bakedModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}