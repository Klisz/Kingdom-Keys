package online.kingdomkeys.kingdomkeys.client.gui.menu;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyCreate;

public class GuiMenu_Party_Create extends GuiMenu_Background {

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	TextFieldWidget tfName;
	Button togglePriv, accept, size;
	GuiMenuButton back;
		
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	ExtendedWorldData worldData;
	
	Party party;
		
	public GuiMenu_Party_Create(String name) {
		super(name);
		drawPlayerInfo = true;
		worldData = ExtendedWorldData.get(minecraft.world);
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_None("No Party"));
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "accept":
			if(!tfName.getText().equals("")) { //Accept Party creation
				Party localParty = new Party(tfName.getText(), minecraft.player.getUniqueID(), minecraft.player.getName().getFormattedText(), priv, Byte.parseByte(size.getMessage()));
				PacketHandler.sendToServer(new CSPartyCreate(localParty));
				
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				minecraft.displayGuiScreen(new GuiMenu_Party_Leader("Party Leader"));
			}
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 1;
			} else {
				pSize++;
			}
			size.setMessage(pSize+"");
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		togglePriv.setMessage(priv ? "Private" : "Public");

		
		//TBName
		togglePriv.visible = true;
		accept.visible = true;
		tfName.visible = true;
		size.visible = true;
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;


		addButton(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20, Utils.translateToLocal("Private/public"), (e) -> { action("togglePriv"); }));
		addButton(accept = new Button((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 100, 20, Utils.translateToLocal("Accept"), (e) -> { action("accept"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addButton(size = new Button((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (3 * 18), (int) 20, 20, Party.PARTY_LIMIT+"", (e) -> { action("size"); }));
		
		addButton(tfName = new TextFieldWidget(minecraft.fontRenderer, (int)(width*0.25), (int)(height*0.25), 100, 15, ""));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		worldData = ExtendedWorldData.get(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());

		
		int buttonX = (int)(width*0.25);
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "CREATE", 2, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
		
		drawString(minecraft.fontRenderer, "Party Name", buttonX, (int)(height * 0.2), 0xFFFFFF);
		drawString(minecraft.fontRenderer, "Accessibility and limit", buttonX, (int)(height * 0.35), 0xFFFFFF);
	}
	
}