package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSetEquippedAbility {

	String ability;
	int level;

	public PacketSetEquippedAbility() {
	}

	public PacketSetEquippedAbility(String ability, int level) {
		this.ability = ability;
		this.level = level;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.ability.length());
		buffer.writeString(this.ability);
		buffer.writeInt(this.level);
	}

	public static PacketSetEquippedAbility decode(PacketBuffer buffer) {
		PacketSetEquippedAbility msg = new PacketSetEquippedAbility();
		int length = buffer.readInt();
		msg.ability = buffer.readString(length);
		msg.level = buffer.readInt();
		return msg;
	}

	public static void handle(PacketSetEquippedAbility message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			int apCost = ModAbilities.registry.getValue(new ResourceLocation(message.ability)).getAPCost();
			int newConsumedAP = message.level > -1 ? apCost : -apCost;
			props.setConsumedAP(props.getConsumedAP() + newConsumedAP);
			//System.out.println("adding/sub " + message.ability + " by " + message.level + " adding: " + newConsumedAP);
			props.addEquippedAbilityLevel(message.ability, message.level);
		});
		ctx.get().setPacketHandled(true);
	}

}
