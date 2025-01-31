package steve_gall.minecolonies_solcarrot.core.common.entity;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenDataView;

import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;

public interface ICitizenDataViewExtensions extends ICitizenDataView
{
	@NotNull
	ICitizenFoodDataView minecolonies_sol$getFood();
}
