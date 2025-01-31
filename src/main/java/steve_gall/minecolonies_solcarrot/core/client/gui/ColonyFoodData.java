package steve_gall.minecolonies_solcarrot.core.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.IColonyView;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_solcarrot.api.common.IMineColoniesSoLAPI;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;

public class ColonyFoodData
{
	private final IColonyView colony;

	private final Map<ICitizenDataView, ICitizenFoodDataView> citizenFoodMap = new HashMap<>();
	private final Map<Item, Set<ICitizenDataView>> foodCitizensMap = new HashMap<>();

	private final List<ItemStack> allFoods;
	private final Map<Item, String> nameCache = new HashMap<>();

	public ColonyFoodData(IColonyView colony)
	{
		this.colony = colony;
		var allFoods = new ArrayList<ItemStack>();
		ForgeRegistries.ITEMS.getValues().stream().filter(IMineColoniesSoLAPI.instance()::shouldCount).map(ItemStack::new).forEach(allFoods::add);
		allFoods.forEach(i -> this.nameCache.put(i.getItem(), i.getHoverName().getString()));
		allFoods.sort(this::compareFood);
		this.allFoods = ImmutableList.copyOf(allFoods);

		var foodCitizensMap = new HashMap<Item, Set<ICitizenDataView>>();

		for (var stack : allFoods)
		{
			foodCitizensMap.put(stack.getItem(), new HashSet<>());
		}

		for (var citizen : colony.getCitizens().values())
		{
			var foodData = IMineColoniesSoLAPI.instance().getFoodDataView(citizen);
			this.citizenFoodMap.put(citizen, foodData);

			for (var item : foodData.getEatens())
			{
				foodCitizensMap.get(item).add(citizen);
			}

		}

		for (var entry : foodCitizensMap.entrySet())
		{
			this.foodCitizensMap.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
		}

	}

	public IColonyView getColony()
	{
		return this.colony;
	}

	public int compareFood(ItemStack stack1, ItemStack stack2)
	{
		var name1 = this.nameCache.get(stack1.getItem());
		var name2 = this.nameCache.get(stack2.getItem());
		return name1.compareTo(name2);
	}

	public boolean isCompletedFood(Item item)
	{
		return this.foodCitizensMap.get(item).size() >= this.getCitizenCount();
	}

	public int getCitizenCount()
	{
		return this.citizenFoodMap.size();
	}

	public ICitizenFoodDataView getFoodData(ICitizenDataView citizen)
	{
		return this.citizenFoodMap.get(citizen);
	}

	public Set<ICitizenDataView> getEatenCitizens(Item item)
	{
		return this.foodCitizensMap.get(item);
	}

	public List<ItemStack> getAllFoods()
	{
		return this.allFoods;
	}

	public Set<Entry<ICitizenDataView, ICitizenFoodDataView>> getCitizenFoodEntrySet()
	{
		return this.citizenFoodMap.entrySet();
	}

	public Set<Entry<Item, Set<ICitizenDataView>>> getFoodCitizensEntrySet()
	{
		return this.foodCitizensMap.entrySet();
	}

}
