package steve_gall.minecolonies_solcarrot.core.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;

public class ModCommands
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		var roots = new String[]{MineColoniesSoL.MOD_ID, "sol_mc"};

		for (var root : roots)
		{
			dispatcher.register(register(root));
		}

	}

	public static LiteralArgumentBuilder<CommandSourceStack> register(String root)
	{
		var command = Commands.literal(root);
		command.then(CitizenCommands.register());

		return command;
	}

}
