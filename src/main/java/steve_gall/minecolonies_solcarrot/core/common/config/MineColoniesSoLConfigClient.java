package steve_gall.minecolonies_solcarrot.core.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MineColoniesSoLConfigClient
{
	public static final MineColoniesSoLConfigClient INSTANCE;
	public static final ModConfigSpec SPEC;

	static
	{
		var common = new ModConfigSpec.Builder().configure(MineColoniesSoLConfigClient::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public MineColoniesSoLConfigClient(ModConfigSpec.Builder builder)
	{

	}

}
