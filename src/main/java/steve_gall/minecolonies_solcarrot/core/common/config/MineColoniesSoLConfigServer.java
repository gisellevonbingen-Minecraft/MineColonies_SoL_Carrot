package steve_gall.minecolonies_solcarrot.core.common.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class MineColoniesSoLConfigServer
{
	public static final MineColoniesSoLConfigServer INSTANCE;
	public static final ModConfigSpec SPEC;

	static
	{
		var common = new ModConfigSpec.Builder().configure(MineColoniesSoLConfigServer::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}
	public final BooleanValue followCE;

	public final IntValue heartsPerMilestones;
	public final ConfigValue<List<? extends Integer>> milestones;

	public final ConfigValue<List<? extends String>> blacklist;
	public final ConfigValue<List<? extends String>> whitelist;
	public final IntValue minimumNutrition;

	public MineColoniesSoLConfigServer(ModConfigSpec.Builder builder)
	{
		builder.comment("Follows 'Spice of Life: Carrot Edition' config, if it installed.");
		this.followCE = builder.define("followCE", true);

		builder.push("milestones");
		this.heartsPerMilestones = builder.defineInRange("heartsPerMilestones", 2, 0, 1000);
		this.milestones = builder.defineList("milestones", () -> Lists.newArrayList(5, 10, 15, 20, 25), () -> 0, e -> e instanceof Integer);
		builder.pop();

		builder.push("filtering");
		this.blacklist = builder.defineListAllowEmpty("blacklist", () -> Lists.newArrayList(), () -> "", e -> e instanceof String);
		this.whitelist = builder.defineListAllowEmpty("whitelist", () -> Lists.newArrayList(), () -> "", e -> e instanceof String);
		this.minimumNutrition = builder.defineInRange("minimumNutrition", 1, 0, 1000);
		builder.pop();
	}

	public boolean shouldCount(Item item)
	{
		var food = item.getDefaultInstance().getFoodProperties(null);
		return food.nutrition() >= this.minimumNutrition.get() && this.isAllowed(item);
	}

	public boolean isAllowed(Item item)
	{
		var id = BuiltInRegistries.ITEM.getKey(item);

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
