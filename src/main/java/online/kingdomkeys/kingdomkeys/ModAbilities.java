package online.kingdomkeys.kingdomkeys;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModAbilities {
	public static IForgeRegistry<Ability> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerAbilitiesRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Ability>().setName(new ResourceLocation(KingdomKeys.MODID, "abilities")).setType(Ability.class).create();
		}

		public static Ability createAbility(String name, int apCost, AbilityType type, int order) {
			return new Ability(KingdomKeys.MODID + ":" + Strings.AB_Prefix + name, apCost, type, order);
		}

		@SubscribeEvent
		public static void registerAbilities(RegistryEvent.Register<Ability> event) {
			int order = 0;
			event.getRegistry().registerAll(
				// Growth
				createAbility("high_jump", 2, AbilityType.GROWTH, order++), 
				createAbility("quick_run", 2, AbilityType.GROWTH, order++), 
				createAbility("dodge_roll", 3, AbilityType.GROWTH, order++),
				createAbility("aerial_dodge", 3, AbilityType.GROWTH, order++),
				createAbility("glide", 3, AbilityType.GROWTH, order++),
				
				// Support
				createAbility("scan", 1, AbilityType.SUPPORT, order++)
			);
		}
	}
}