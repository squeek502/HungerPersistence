package squeek.hungerpersistence;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, name = "Hunger Persistence", acceptableRemoteVersions = "*", guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class HungerPersistence
{
	public static final Logger Log = LogManager.getLogger(ModInfo.MODID);

	@Instance(ModInfo.MODID)
	public static HungerPersistence instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
		ModConfig.init(event.getSuggestedConfigurationFile());
	}

	private static final Field foodStatsField = ReflectionHelper.findField(EntityPlayer.class, "foodStats", "field_71100_bB", "bw");
	private static final Field saturationLevelField = ReflectionHelper.findField(FoodStats.class, "foodSaturationLevel", "field_75125_b", "b");

	private void serverSetFoodSaturationLevel(FoodStats foodStats, float saturationLevel)
	{
		try
		{
			saturationLevelField.set(foodStats, saturationLevel);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
		if (!event.isWasDeath())
			return;

		try
		{
			foodStatsField.set(event.getEntityPlayer(), event.getOriginal().getFoodStats());
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		FoodStats foodStats = event.getEntityPlayer().getFoodStats();
		if (foodStats.getFoodLevel() < ModConfig.MINIMUM_HUNGER_ON_RESPAWN)
			foodStats.setFoodLevel(ModConfig.MINIMUM_HUNGER_ON_RESPAWN);
		if (foodStats.getFoodLevel() > ModConfig.MAXIMUM_HUNGER_ON_RESPAWN)
			foodStats.setFoodLevel(ModConfig.MAXIMUM_HUNGER_ON_RESPAWN);
		if (foodStats.getSaturationLevel() < ModConfig.MINIMUM_SATURATION_ON_RESPAWN)
			serverSetFoodSaturationLevel(foodStats, (float) ModConfig.MINIMUM_SATURATION_ON_RESPAWN);
		if (foodStats.getSaturationLevel() > ModConfig.MAXIMUM_SATURATION_ON_RESPAWN)
			serverSetFoodSaturationLevel(foodStats, (float) ModConfig.MAXIMUM_SATURATION_ON_RESPAWN);
	}
}
