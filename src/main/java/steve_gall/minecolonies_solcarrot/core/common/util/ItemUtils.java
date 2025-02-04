package steve_gall.minecolonies_solcarrot.core.common.util;

import java.util.Collection;
import java.util.function.IntFunction;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ItemUtils
{
	public static <COLLECTION extends Collection<Item>> COLLECTION decodeItems(IntFunction<COLLECTION> func, FriendlyByteBuf buffer)
	{
		var size = buffer.readInt();
		var collection = func.apply(size);

		for (var i = 0; i < size; i++)
		{
			var id = buffer.readResourceLocation();
			collection.add(BuiltInRegistries.ITEM.get(id));
		}

		return collection;
	}

	public static void encodeItems(FriendlyByteBuf buffer, Collection<Item> collection)
	{
		var size = collection.size();
		buffer.writeInt(size);

		for (var item : collection)
		{
			var id = BuiltInRegistries.ITEM.getKey(item);
			buffer.writeResourceLocation(id);
		}

	}

	public static void deserializeItems(Collection<Item> collection, ListTag listTag)
	{
		collection.clear();

		for (var i = 0; i < listTag.size(); i++)
		{
			var id = ResourceLocation.tryParse(listTag.getString(i));

			if (id == null)
			{
				continue;
			}

			var item = BuiltInRegistries.ITEM.get(id);

			if (item == null || item == Items.AIR)
			{
				continue;
			}

			collection.add(item);
		}

	}

	public static ListTag serializeItems(Collection<Item> collection)
	{
		var listTag = new ListTag();

		for (var item : collection)
		{
			var id = BuiltInRegistries.ITEM.getKey(item);

			if (id == null)
			{
				continue;
			}

			listTag.add(StringTag.valueOf(id.toString()));
		}

		return listTag;
	}

	private ItemUtils()
	{

	}

}
