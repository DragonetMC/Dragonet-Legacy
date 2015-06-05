package org.bukkit.inventory;

import org.apache.commons.lang.Validate;

/**
 * Represents a dynamic recipe.
 * These are recipes that have can have different results, depending on the inputs used, rather than a simple matching algorithm.
 *
 * <p>Used for recipes such as Banners, which require item metadata to be copied to an item, along with having a semi-shaped recipe.</p>
 */
public class DynamicRecipe implements Recipe {

    private ItemMatcher matcher;
    private ItemStack result;

    public DynamicRecipe() {
        setMatcher(new ItemMatcher());
    }

    public DynamicRecipe(ItemMatcher matcher) {
        setMatcher(matcher);
    }

    /**
     * Sets the {@link ItemMatcher} to be used with this recipe.
     * @param matcher ItemMatcher to use. Must not be null.
     */
    public void setMatcher(ItemMatcher matcher) {
        Validate.notNull(matcher, "Matcher cannot be null.");
        this.matcher = matcher;
    }

    /**
     * Checks to see if the recipe will match a crafting matrix. This method also prepares {@link #getResult()}
     * to return the correct item (including all metadata) for the input.
     * @param matrix Items on the crafting grid
     * @return Whether the recipe matches the inputs
     */
    public boolean matches(ItemStack[] matrix) {
        result = matcher.getResult(matrix);
        return result != null;
    }

    /**
     * Gets the result of this recipe, given the input of {@link #matches(ItemStack[])}.
     * @return The result of the recipe, or null if it does not match
     */
    @Override
    public ItemStack getResult() {
        return result;
    }
}
