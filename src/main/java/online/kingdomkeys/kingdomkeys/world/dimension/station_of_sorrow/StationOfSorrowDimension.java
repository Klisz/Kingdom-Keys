package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

@Mod.EventBusSubscriber
public class StationOfSorrowDimension{
    //Event Listeners//

    //Set the fog density to fade out the bottom of the platform
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            if (world.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                RenderSystem.setShaderFogStart(0.0F);
                RenderSystem.setShaderFogEnd(30);
            }
        }
    }

    //Prevent player from falling off the platform
    @SubscribeEvent
    public static void entityTick(LivingUpdateEvent event) {
        if (event.getEntityLiving().level.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
        	if(event.getEntityLiving() instanceof Player player) {
    			if (!player.isCreative()) {
	                if (player.getY() < 10) {
	                    player.teleportTo(0, 25, 0);
	                }
	            }
	        }
        	
        	if(event.getEntityLiving() instanceof MarluxiaEntity marluxia) {
	            if (marluxia.getY() < 10) {
	            	marluxia.teleportTo(0, 25, 0);
	            }
	        }
    	}
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getPlayer().level.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                if (event.getWorld().getBlockEntity(event.getPos()) != null) { //If is a TE
                    if (event.getPlayer().isShiftKeyDown()) { //If the player is shifting cancel it (places blocks)
                        event.setCanceled(true);
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }
}
