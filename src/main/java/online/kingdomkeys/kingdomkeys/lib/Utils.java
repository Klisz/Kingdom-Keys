package online.kingdomkeys.kingdomkeys.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.Ability;
import online.kingdomkeys.kingdomkeys.ModAbilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.magic.ModMagics;

/**
 * Created by Toby on 19/07/2016.
 */
public class Utils {
	
	public static int getInt(String num) {
		int number;
		try {
			number = Integer.parseInt(num);
			return number;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Method for generating random integer between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min
	 *            minimum value that the random integer can be
	 * @param max
	 *            maximum value that the random integer can be
	 * @return a random integer
	 */
	public static int randomWithRange(int min, int max) {
		int range = Math.abs(max - min) + 1;
		return (int) (Math.random() * range) + (min <= max ? min : max);
	}

	/**
	 * Method for generating random doubles between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min
	 *            minimum value that the random double can be
	 * @param max
	 *            maximum value that the random double can be
	 * @return a random double
	 */
	public static double randomWithRange(double min, double max) {
		double range = Math.abs(max - min);
		return (Math.random() * range) + (min <= max ? min : max);
	}

	/**
	 * Method for generating random floats between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min
	 *            minimum value that the random float can be
	 * @param max
	 *            maximum value that the random float can be
	 * @return a random float
	 */
	public static float randomWithRange(float min, float max) {
		float range = Math.abs(max - min) + 1;
		return (float) (Math.random() * range) + (min <= max ? min : max);
	}
	
	/**
	 * Replacement for
	 * {@link net.minecraft.util.text.translation.I18n#translateToLocalFormatted(String, Object...)}
	 * 
	 * @param name
	 *            the unlocalized string to translate
	 * @param format
	 *            the format of the string
	 * @return the translated string
	 */
	public static String translateToLocalFormatted(String name, Object... format) {
		TranslationTextComponent translation = new TranslationTextComponent(name, format);
		return translation.getFormattedText();
	}

	/**
	 * Replacement for
	 * {@link net.minecraft.util.text.translation.I18n#translateToLocal(String)}
	 * 
	 * @param name
	 *            the unlocalized string to translate
	 * @return the translated string
	 */
	public static String translateToLocal(String name) {
		TranslationTextComponent translation = new TranslationTextComponent(name);
		return translation.getFormattedText();
	}

	public static enum OrgMember {
		XEMNAS, XIGBAR, XALDIN, VEXEN, LEXAEUS, ZEXION, SAIX, AXEL, DEMYX, LUXORD, MARLUXIA, LARXENE, ROXAS, NONE
	}
	
	
	public static int getDriveFormLevel(Map<String,int[]> map, String driveForm) {
		if(driveForm.equals(Strings.Form_Anti))
			return 7;
		return map.get(driveForm)[0];
	}
	
	public static LinkedHashMap<String, int[]> getSortedAbilities(LinkedHashMap<String, int[]> abilities) {
		List<Ability> list = new ArrayList<Ability>();		
		
		Iterator<String> it = abilities.keySet().iterator();
		while(it.hasNext()) {
			String entry = it.next();
			list.add(ModAbilities.registry.getValue(new ResourceLocation(entry)));
		}
		
		Collections.sort(list, Comparator.comparingInt(Ability::getOrder));
		
		LinkedHashMap<String, int[]> map = new LinkedHashMap<String, int[]>();
		for(int i=0;i<list.size();i++) {
			map.put(list.get(i).getRegistryName().toString(), abilities.get(list.get(i).getRegistryName().toString()));
		}
		return map;
	}
	
	public static LinkedHashMap<String, int[]> getSortedDriveForms(LinkedHashMap<String, int[]> driveFormsMap) {
		List<DriveForm> list = new ArrayList<DriveForm>();		
		
		Iterator<String> it = driveFormsMap.keySet().iterator();
		while(it.hasNext()) {
			String entry = it.next();
			list.add(ModDriveForms.registry.getValue(new ResourceLocation(entry)));
		}
		
		Collections.sort(list, Comparator.comparingInt(DriveForm::getOrder));
		
		LinkedHashMap<String, int[]> map = new LinkedHashMap<String, int[]>();
		for(int i=0;i<list.size();i++) {
			map.put(list.get(i).getName(), driveFormsMap.get(list.get(i).getName()));
		}
		
		//Old way
		/*String[] keys = new String[driveFormsMap.size()];
		int[][] values = new int[driveFormsMap.size()][2];
		Iterator<Entry<String, int[]>> it = driveFormsMap.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, int[]> entry = it.next();
			int order = ModDriveForms.registry.getValue(new ResourceLocation(entry.getKey())).getOrder();
			keys[order] = entry.getKey();
			values[order] = entry.getValue();
		}
		
		LinkedHashMap<String, int[]> map = new LinkedHashMap<String, int[]>();
		for(int i=0;i<keys.length;i++) {
			map.put(keys[i], values[i]);
		}*/

		return map; 
	}

	public static List<String> getSortedMagics(List<String> list) {
		Collections.sort(list, ((a,b)-> Integer.compare(ModMagics.registry.getValue(new ResourceLocation(a)).getOrder(), ModMagics.registry.getValue(new ResourceLocation(b)).getOrder())));
		return list;
		
		/*String[] keys = new String[list.size()];
		int[][] values = new int[list.size()][2];
		Iterator<String> it = list.iterator();
		while(it.hasNext()) {
			String entry = it.next();
			int order = ModMagics.registry.getValue(new ResourceLocation(entry)).getOrder();
			keys[order] = entry.getKey();
			values[order] = entry.getValue();
		}
		
		LinkedHashMap<String, int[]> map = new LinkedHashMap<String, int[]>();
		for(int i=0;i<keys.length;i++) {
			map.put(keys[i], values[i]);
		}

		return map; */
	}

	/**
	 *
	 * MOST OF THIS WON'T BE HERE ANYMORE
	 *
	 */
	/*public static boolean summonWeapon(EntityPlayer player, EnumHand hand, int keychainSlot) {
		SummonKeybladeCapability.ISummonKeyblade summonCap = player.getCapability(ModCapabilities.SUMMON_KEYBLADE, null);
		OrganizationXIIICapability.IOrganizationXIII organizationXIIICap = player.getCapability(ModCapabilities.ORGANIZATION_XIII, null);

		if (organizationXIIICap.getMember() == Utils.OrgMember.NONE) {
			if (ItemStack.areItemStacksEqual(summonCap.getInventoryKeychain().getStackInSlot(keychainSlot), ItemStack.EMPTY)) {
				player.sendMessage(new TextComponentTranslation(TextFormatting.RED + "Missing keychain to summon"));
				return false;
			}
			if (!summonCap.getIsKeybladeSummoned(hand) && ItemStack.areItemStacksEqual(player.getHeldItem(hand), ItemStack.EMPTY) && summonCap.getInventoryKeychain().getStackInSlot(0).getItem() instanceof KeychainItem) {
				summonCap.setActiveSlot(player.inventory.currentItem);

				ItemStack keychain = summonCap.getInventoryKeychain().getStackInSlot(keychainSlot);
				ItemStack keyblade = new ItemStack(((KeychainItem) (keychain.getItem())).getKeyblade());

				if (hand == EnumHand.MAIN_HAND) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
				} else {
					player.inventory.offHandInventory.set(0, keyblade);
				}

				if (player.world.isRemote)
					PacketDispatcher.sendToServer(new SummonKeyblade(hand, keychainSlot));

				return true;
			} else if (!ItemStack.areItemStacksEqual(player.getHeldItem(hand), ItemStack.EMPTY) && player.getHeldItem(hand).getItem() instanceof ItemRealKeyblade && summonCap.getIsKeybladeSummoned(hand)) {
				if (player.world.isRemote)
					PacketDispatcher.sendToServer(new DeSummonKeyblade(hand));
				player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
				player.inventory.offHandInventory.set(0, ItemStack.EMPTY);
				return true;
			} else {
				return false;
			}
		} else {
			if (!organizationXIIICap.summonedWeapon(hand) && ItemStack.areItemStacksEqual(player.getHeldItem(hand), ItemStack.EMPTY)) {
				if (player.world.isRemote)
					PacketDispatcher.sendToServer(new SummonOrgWeapon(hand, organizationXIIICap.currentWeapon()));
				if (hand == EnumHand.MAIN_HAND)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(organizationXIIICap.currentWeapon()));
				else
					player.inventory.offHandInventory.set(0, new ItemStack(organizationXIIICap.currentWeapon()));
				organizationXIIICap.setWeaponSummoned(hand, true);
				return true;
			} else if (!ItemStack.areItemStacksEqual(player.getHeldItem(hand), ItemStack.EMPTY) && player.getHeldItem(hand).getItem() instanceof IOrgWeapon || (organizationXIIICap.getMember() == Utils.OrgMember.ROXAS && !ItemStack.areItemStacksEqual(player.getHeldItem(hand), ItemStack.EMPTY) && player.getHeldItem(hand).getItem() instanceof KeybladeItem)) {
				if (player.world.isRemote) {
					PacketDispatcher.sendToServer(new DeSummonOrgWeapon(hand));
				}
				organizationXIIICap.setWeaponSummoned(hand, false);
				if (hand == EnumHand.MAIN_HAND)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
				else
					player.inventory.offHandInventory.set(0, ItemStack.EMPTY);
				return true;
			} else {
				return false;
			}
		}
	}*/



	/*public static IDAndBlockPos getDimensionIDAndBlockPos(String dimension) {
		IDAndBlockPos idAndBlockPos = new IDAndBlockPos();

		switch (dimension) {
		case Strings.SoA:
			idAndBlockPos.id = ModDimensions.diveToTheHeartID;
			idAndBlockPos.pos = new BlockPos(-1, 80, 7);
			idAndBlockPos.offset = new BlockPos(0, 0, 0);
			idAndBlockPos.name = dimension;
			break;

		case Strings.TraverseTown:
			idAndBlockPos.id = ModDimensions.traverseTownID;
			idAndBlockPos.pos = new BlockPos(192, 5, 161);
			idAndBlockPos.offset = new BlockPos(0, 0, 0);
			idAndBlockPos.name = dimension;
			break;

		case Strings.DestinyIslands:
			idAndBlockPos.id = ModDimensions.destinyIslandsID;
			idAndBlockPos.pos = new BlockPos(145, 27 + 60, 200);
			idAndBlockPos.offset = new BlockPos(0, 60, 0);
			idAndBlockPos.name = dimension;
			break;
		
		case "overworld":
			idAndBlockPos.id = 0;
			idAndBlockPos.name = dimension;
			break;
		default:
			idAndBlockPos.id = -500;
			idAndBlockPos.pos = new BlockPos(0, 0, 0);
			System.out.println("ID not found for world " + dimension);
		}
		return idAndBlockPos;

	}

	public static IDAndBlockPos getDimensionIDAndBlockPos(int dimension) {
		IDAndBlockPos idAndBlockPos = new IDAndBlockPos();

		if (dimension == ModDimensions.diveToTheHeartID) {
			idAndBlockPos.id = dimension;
			idAndBlockPos.name = Strings.SoA;
			idAndBlockPos.pos = new BlockPos(-1, 80, 7);
			idAndBlockPos.offset = new BlockPos(0, 0, 0);
		
		} else if (dimension == ModDimensions.traverseTownID) {
			idAndBlockPos.id = dimension;
			idAndBlockPos.name = Strings.TraverseTown;
			idAndBlockPos.pos = new BlockPos(192, 5, 161);
			idAndBlockPos.offset = new BlockPos(0, 0, 0);

		} else if (dimension == ModDimensions.destinyIslandsID) {
			idAndBlockPos.id = dimension;
			idAndBlockPos.name = Strings.DestinyIslands;
			idAndBlockPos.pos = new BlockPos(145, 27 + 60, 200);
			idAndBlockPos.offset = new BlockPos(0, 60, 0);
		}
		return idAndBlockPos;

	}*/

  /*  @SideOnly(Side.CLIENT)
    public static void drawScaledModalRect(Gui gui, float x, float y, int u, int v, int width, int height, float scaleX, float scaleY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scaleX, scaleY, 1);
        gui.drawTexturedModalRect(0, 0, u, v, width, height);
        GlStateManager.popMatrix();
    }
    
    public static Ability getAbilityFromName(String name) {
		return GameRegistry.findRegistry(Ability.class).getValue(new ResourceLocation(Reference.MODID+":"+name));
    }*/
}
