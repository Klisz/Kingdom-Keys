package online.kingdomkeys.kingdomkeys.commands;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class KKExpCommand extends BaseCommand{ //kkexp <give/take/set> <amount> [player]
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kkexp").requires(source -> source.hasPermissionLevel(3));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("exp", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKExpCommand::setValue)
				)
				.executes(KKExpCommand::setValue)
			)
		);
		
		builder.then(Commands.literal("give")
			.then(Commands.argument("exp", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKExpCommand::addValue)
				)
				.executes(KKExpCommand::addValue)
			)
		);
		
		/*builder.then(Commands.literal("take")
			.then(Commands.argument("exp", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKExpCommand::removeValue)
				)
				.executes(KKExpCommand::removeValue)
			)
		);*/
		
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
	}

	private static int setValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int exp = IntegerArgumentType.getInteger(context, "exp");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
			playerData.setLevel(1);
			playerData.setExperience(0);
			playerData.setMaxHP(20);
            player.setHealth(20);
    		player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
            playerData.setMaxMP(0);            
            playerData.setMaxAP(10);
            
            //TODO Change this with the SOA choice
            playerData.setStrength(1);
            playerData.setMagic(1);
            playerData.setDefense(1);
            
            playerData.clearAbilities();
		
            playerData.addExperience(player, exp);
			
			LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
			Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, int[]> entry = it.next();
				int dfLevel = entry.getValue()[0];
				DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(entry.getKey()));
				for(int i=1;i<dfLevel;i++) {
					String baseAbility = form.getBaseAbilityForLevel(i);
			     	if(!baseAbility.equals("")) {
			     		playerData.addAbility(baseAbility, false);
			     	}
				}

			}
			
			player.heal(playerData.getMaxHP());
			playerData.setMP(playerData.getMaxMP());
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Set "+player.getDisplayName().getFormattedText()+" experience to "+exp), true);
			}
			player.sendMessage(new TranslationTextComponent("Your experience is now "+exp));
		}
		return 1;
	}
	

	private static int addValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "exp");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addExperience(player, value);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);

			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Added "+value+" experience to "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your experience has been increased by "+value));		
		}
		return 1;
	}
	
	
	/*private static int removeValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int exp = IntegerArgumentType.getInteger(context, "exp");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
			playerData.setLevel(1);
			playerData.setExperience(0);
			playerData.setMaxHP(20);
            player.setHealth(playerData.getMaxHP());
            playerData.setMaxMP(0);
            playerData.setMP(playerData.getMaxMP());
            
            playerData.setMaxAP(10);
            playerData.setConsumedAP(0);
            
            //TODO Change this with the SOA choice
            playerData.setStrength(1);
            playerData.setMagic(1);
            playerData.setDefense(1);
            
            playerData.clearAbilities();
            
            playerData.addExperience(player, exp);
			
			LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
			Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, int[]> entry = it.next();
				int dfLevel = entry.getValue()[0];
				DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(entry.getKey()));
				for(int i=1;i<dfLevel;i++) {
					String baseAbility = form.getBaseAbilityForLevel(i);
			     	if(!baseAbility.equals("")) {
			     		playerData.addAbility(baseAbility, false);
			     	}
				}

			}
			
			player.heal(playerData.getMaxHP());
			playerData.setMP(playerData.getMaxMP());

			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Set "+player.getDisplayName().getFormattedText()+" experience to "+exp), true);
			}
			player.sendMessage(new TranslationTextComponent("Your experience is now "+exp));
		}
		return 1;
	}*/
	
}