package steve_gall.minecolonies_solcarrot.core.common.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;

public class MineColoniesSoLConfigServer
{
	public static final MineColoniesSoLConfigServer INSTANCE;
	public static final ForgeConfigSpec SPEC;

	static
	{
		var common = new ForgeConfigSpec.Builder().configure(MineColoniesSoLConfigServer::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}
	public final BooleanValue followCE;

	public final IntValue heartsPerMilestones;
	public final ConfigValue<List<? extends Integer>> milestones;

	public final ConfigValue<List<? extends String>> blacklist;
	public final ConfigValue<List<? extends String>> whitelist;
	public final IntValue minimumNutrition;

	public MineColoniesSoLConfigServer(ForgeConfigSpec.Builder builder)
	{
		builder.comment("Follows 'Spice of Life: Carrot Edition' config, if it installed.");
		this.followCE = builder.define("followCE", true);

		builder.push("milestones");
		this.heartsPerMilestones = builder.defineInRange("heartsPerMilestones", 2, 0, 1000);
		this.milestones = builder.defineList("milestones", () -> Lists.newArrayList(5, 10, 15, 20, 25), e -> e instanceof Integer);
		builder.pop();

		builder.push("filtering");
		this.blacklist = builder.defineList("blacklist", () -> Lists.newArrayList(), e -> e instanceof String);
		this.whitelist = builder.defineList("whitelist", () -> Lists.newArrayList(), e -> e instanceof String);
		this.minimumNutrition = builder.defineInRange("minimumNutrition", 1, 0, 1000);
		builder.pop();
	}

	public boolean shouldCount(Item item)
	{
		var food = item.getDefaultInstance().getFoodProperties(null);
		return food.getNutrition() >= this.minimumNutrition.get() && this.isAllowed(item);
	}

	public boolean isAllowed(Item item)
	{
		var id = ForgeRegistries.ITEMS.getKey(item);

		if (id == null)
		{
			return false;
		}

		var idStr = id.toString();

		if (this.whitelist.get().size() > 0)
		{
			return this.whitelist.get().contains(idStr);
		}
		else
		{
			return !this.blacklist.get().contains(idStr);
		}

	}

}
