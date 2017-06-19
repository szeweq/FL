package szewek.fl.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import szewek.fl.compat.jei.crafting.BuiltShapedWrapper;
import szewek.fl.recipes.BuiltShapedRecipe;

@JEIPlugin
public class FLJEI extends BlankModPlugin {
	private static IJeiHelpers helpers;

	@Override
	public void register(IModRegistry reg) {
		helpers = reg.getJeiHelpers();
		reg.handleRecipes(BuiltShapedRecipe.class, BuiltShapedWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
	}

	public static IJeiHelpers getHelpers() {
		return helpers;
	}
}
