package steve_gall.minecolonies_solcarrot.core.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MineColoniesSoLConfigCommon
{
	public static final MineColoniesSoLConfigCommon INSTANCE;
	public static final ModConfigSpec SPEC;

	static
	{
		var common = new ModConfigSpec.Builder().configure(MineColoniesSoLConfigCommon::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public MineColoniesSoLConfigCommon(ModConfigSpec.Builder builder)
	{

	}

}
