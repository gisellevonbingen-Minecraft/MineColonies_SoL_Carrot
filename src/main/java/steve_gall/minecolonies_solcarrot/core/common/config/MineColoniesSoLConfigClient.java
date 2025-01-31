package steve_gall.minecolonies_solcarrot.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MineColoniesSoLConfigClient
{
	public static final MineColoniesSoLConfigClient INSTANCE;
	public static final ForgeConfigSpec SPEC;

	static
	{
		var common = new ForgeConfigSpec.Builder().configure(MineColoniesSoLConfigClient::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public MineColoniesSoLConfigClient(ForgeConfigSpec.Builder builder)
	{

	}

}
