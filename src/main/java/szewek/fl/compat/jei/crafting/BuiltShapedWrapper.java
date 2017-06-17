package szewek.fl.compat.jei.crafting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import szewek.fl.compat.jei.FLJEI;
import szewek.fl.recipes.BuiltShapedRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class BuiltShapedWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
	private final BuiltShapedRecipe recipe;

	public BuiltShapedWrapper(BuiltShapedRecipe bsr) {
		recipe = bsr;
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingr) {
		IStackHelper ish = FLJEI.getHelpers().getStackHelper();
		ingr.setInputLists(ItemStack.class, ish.expandRecipeItemStackInputs(Arrays.asList(recipe.getCached())));
		ingr.setOutput(ItemStack.class, recipe.getRecipeOutput());
	}

	@Override
	public int getWidth() {
		return recipe.width;
	}

	@Override
	public int getHeight() {
		return recipe.height;
	}
}
