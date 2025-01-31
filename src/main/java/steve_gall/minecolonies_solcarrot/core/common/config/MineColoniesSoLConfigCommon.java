package steve_gall.minecolonies_solcarrot.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MineColoniesSoLConfigCommon
{
	public static final MineColoniesSoLConfigCommon INSTANCE;
	public static final ForgeConfigSpec SPEC;

	static
	{
		var common = new ForgeConfigSpec.Builder().configure(MineColoniesSoLConfigCommon::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public MineColoniesSoLConfigCommon(ForgeConfigSpec.Builder builder)
	{

	}

}
