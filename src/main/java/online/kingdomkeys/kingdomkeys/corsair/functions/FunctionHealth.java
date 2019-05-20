package online.kingdomkeys.kingdomkeys.corsair.functions;

import net.minecraft.client.entity.EntityPlayerSP;
import online.kingdomkeys.kingdomkeys.corsair.lib.CorsairUtils;

public class FunctionHealth extends BaseKeyboardFunctions {

	/**
	 * Health in certain key (used with various keys)
	 * 
	 * @param player
	 * @param key
	 * @return
	 */
	static int[] getRGBKeyColor(EntityPlayerSP player, int key) {
		int[] color = { 0, 0, 0 };

		if (player == null) {
			return CorsairUtils.defaultRGB;
		}

		float fill = getHealth(player) / 2;
		fill -= key;

		if (fill > 1.0F) {
			color[0] = 255;
			color[1] = 0;
			color[2] = 0;
		} else if (fill > 0.0F) {
			color[0] = (int) (255 * fill);
			color[1] = 0;
			color[2] = 0;
		}
		
		fill = getAbsorption(player)/2;
		fill -= key;
		if (fill > 1.0F) {
			color[0] = 255;
			color[1] = 255;
			color[2] = 0;
		} else if (fill > 0.0F) {
			color[0] = (int) (255 * fill);
			color[1] = (int) (255 * fill);
			color[2] = 0;
		}
		return color;
	}

	/**
	 * All health in 1 key
	 * 
	 * @param player
	 * @return
	 */
	static int[] getRGBKeyColor(EntityPlayerSP player) {
		if (player == null) {
			return CorsairUtils.defaultRGB;
		}

		// Fill = the amount of food /2 because of the half points
		float fill = getHealth(player) / 2;
		return decimalToRGB(getRGB(fill, getMaxHealth(player)));
	}
}
