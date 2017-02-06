package squeek.hungerpersistence;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ModConfig
{
	public static final ModConfig instance = new ModConfig();

	private ModConfig()
	{
	}

	public static Configuration config;

	/*
	 * MAIN
	 */
	public static final String CATEGORY_MAIN = "main";
	private static final String CATEGORY_MAIN_COMMENT = "";

	public static int MINIMUM_HUNGER_ON_RESPAWN = ModConfig.MINIMUM_HUNGER_ON_RESPAWN_DEFAULT;
	private static final String MINIMUM_HUNGER_ON_RESPAWN_NAME = "Minimum hunger on respawn";
	private static final int MINIMUM_HUNGER_ON_RESPAWN_DEFAULT = 4;
	private static final String MINIMUM_HUNGER_ON_RESPAWN_COMMENT = "If a player's hunger on death is lower than this value, then it will be set to this value on respawn.\n"
		+ "Default Minecraft sets your hunger to 20 on respawn (full hunger).\n"
		+ "Note: 1 hunger unit = 1/2 hunger bar, so, for example, a value of 4 here means respawning with 2 hunger bars.";

	public static double MINIMUM_SATURATION_ON_RESPAWN = ModConfig.MINIMUM_SATURATION_ON_RESPAWN_DEFAULT;
	private static final String MINIMUM_SATURATION_ON_RESPAWN_NAME = "Minimum saturation on respawn";
	private static final double MINIMUM_SATURATION_ON_RESPAWN_DEFAULT = 0.0D;
	private static final String MINIMUM_SATURATION_ON_RESPAWN_COMMENT = "If a player's saturation on death is lower than this value, then it will be set to this value on respawn.\n"
		+ "Default Minecraft sets your saturation to 5.0 on respawn.\n"
		+ "Note: 20 saturation is 'full', and saturation can never be higher than the player's current hunger level";

	public static double MAXIMUM_SATURATION_ON_RESPAWN = ModConfig.MAXIMUM_SATURATION_ON_RESPAWN_DEFAULT;
	private static final String MAXIMUM_SATURATION_ON_RESPAWN_NAME = "Maximum saturation on respawn";
	private static final double MAXIMUM_SATURATION_ON_RESPAWN_DEFAULT = 20.0D;
	private static final String MAXIMUM_SATURATION_ON_RESPAWN_COMMENT = "If a player's saturation on death is higher than this value, then it will be set to this value on respawn.\n"
		+ "Set this to 0 to always have the player respawn with no saturation.\n"
		+ "Note: 20 saturation is 'full', and saturation can never be higher than the player's current hunger level.\n";

	public static void init(File file)
	{
		config = new Configuration(file);

		load();
		sync();

		MinecraftForge.EVENT_BUS.register(new ModConfig());
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ModInfo.MODID))
			ModConfig.sync();
	}

	public static void sync()
	{
		config.getCategory(CATEGORY_MAIN).setComment(CATEGORY_MAIN_COMMENT);

		MINIMUM_HUNGER_ON_RESPAWN = config.get(CATEGORY_MAIN, MINIMUM_HUNGER_ON_RESPAWN_NAME, MINIMUM_HUNGER_ON_RESPAWN_DEFAULT, MINIMUM_HUNGER_ON_RESPAWN_COMMENT).getInt(MINIMUM_HUNGER_ON_RESPAWN_DEFAULT);
		MINIMUM_SATURATION_ON_RESPAWN = config.get(CATEGORY_MAIN, MINIMUM_SATURATION_ON_RESPAWN_NAME, MINIMUM_SATURATION_ON_RESPAWN_DEFAULT, MINIMUM_SATURATION_ON_RESPAWN_COMMENT).getDouble(MINIMUM_SATURATION_ON_RESPAWN_DEFAULT);
		MAXIMUM_SATURATION_ON_RESPAWN = config.get(CATEGORY_MAIN, MAXIMUM_SATURATION_ON_RESPAWN_NAME, MAXIMUM_SATURATION_ON_RESPAWN_DEFAULT, MAXIMUM_SATURATION_ON_RESPAWN_COMMENT).getDouble(MAXIMUM_SATURATION_ON_RESPAWN_DEFAULT);

		if (config.hasChanged())
			save();
	}

	public static void save()
	{
		config.save();
	}

	public static void load()
	{
		config.load();
	}
}
