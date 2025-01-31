package steve_gall.minecolonies_solcarrot.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

public class LanguageGenerator extends LanguageProvider
{
	public LanguageGenerator(DataGenerator gen, String locale)
	{
		super(gen, MineColoniesSoL.MOD_ID, locale);
	}

	@Override
	protected void addTranslations()
	{
		this.add(ModItems.FOOD_NOMICON.get(), "Citizen Food Nomicon");

		this.add("minecolonies_sol.gui.citizens_mode", "Citizens View");
		this.add("minecolonies_sol.gui.foods_mode", "Foods View");

		this.add("minecolonies_sol.gui.citizens", "Citizens\n(%1$s / %2$s)");
		this.add("minecolonies_sol.gui.ate_citizens", "Ate Citizens\n(%1$s / %2$s)");
		this.add("minecolonies_sol.gui.not_ate_citizens", "Not Ate Citizens\n(%1$s / %2$s)");
		this.add("minecolonies_sol.gui.foods", "Foods\n(%1$s / %2$s)");
		this.add("minecolonies_sol.gui.eaten_foods", "Eaten Foods\n(%1$s / %2$s)");
		this.add("minecolonies_sol.gui.not_eaten_foods", "Not Eaten Foods\n(%1$s / %2$s)");

		this.add("minecolonies_sol.gui.eaten_count", "Eaten Count : %s");
		this.add("minecolonies_sol.gui.ate_count", "Ate Citizens : %s");
		this.add("minecolonies_sol.gui.bonus_heart", "Bonus Hearts : %s");
		this.add("minecolonies_sol.gui.except_completed_citizen", "Except Milestone Completed");
		this.add("minecolonies_sol.gui.except_completed_food", "Except Everyone Ate");
	}

}
