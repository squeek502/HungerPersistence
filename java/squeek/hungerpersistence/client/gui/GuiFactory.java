package squeek.hungerpersistence.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import squeek.hungerpersistence.ModConfig;
import squeek.hungerpersistence.ModInfo;

public class GuiFactory extends DefaultGuiFactory
{
	public GuiFactory()
	{
		super(ModInfo.MODID, GuiConfig.getAbridgedConfigPath(ModConfig.config.toString()));
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new GuiConfig(parentScreen, new ConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_MAIN)).getChildElements(), modid, false, false, title);
	}
}