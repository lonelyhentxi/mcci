package com.evernightfireworks.mcci.service.parser;

import com.evernightfireworks.mcci.service.core.CLinkType;
import com.evernightfireworks.mcci.service.core.CNode;
import com.evernightfireworks.mcci.service.core.CNodeType;
import com.evernightfireworks.mcci.service.core.CraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeParser {
    CraftingManager manager;

    private final Logger logger = LogManager.getFormatterLogger("mcci:server:parser:recipe_parser");

    public RecipeParser(CraftingManager manager) {
        this.manager = manager;
    }

    CNode parseInGradientItem(ItemStack stack) {
        return this.manager.getOrCreateGlobalNode(Registry.ITEM.getId(stack.getItem()), CNodeType.item);
    }

    void parseInGradients(Recipe<?> recipe, Identifier recipeId) {
        ItemStack outputItem = recipe.getOutput();
        CNode outputNode = this.manager.getOrCreateGlobalNode(Registry.ITEM.getId(outputItem.getItem()), CNodeType.item);
        var inputs = recipe.getPreviewInputs();
        for (Ingredient i : inputs) {
            ItemStack[] entries = i.getMatchingStacksClient();
            for (ItemStack e : entries) {
                CNode node = this.parseInGradientItem(e);
                this.manager.createGlobalSingleLink(outputNode, node, recipe.toString(), recipeId, CLinkType.recipe);
            }
        }
    }

    void parseCraftingShapeless(ShapelessRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    // @TODO
    void parseCraftingSpecial(
            @SuppressWarnings("unused") SpecialCraftingRecipe recipe,
            @SuppressWarnings("unused") Identifier recipeId) {
    }

    void parseCraftingShaped(ShapedRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    void parseCrafting(CraftingRecipe recipe, Identifier recipeId) {
        if (recipe instanceof SpecialCraftingRecipe) {
            this.parseCraftingSpecial((SpecialCraftingRecipe) recipe, recipeId);
        } else if(recipe instanceof ShapedRecipe) {
            this.parseCraftingShaped((ShapedRecipe)recipe, recipeId);
        } else if(recipe instanceof ShapelessRecipe) {
            this.parseCraftingShapeless((ShapelessRecipe) recipe, recipeId);
        }
    }

    void parseBlasting(BlastingRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    void parseCampfireCooking(CampfireCookingRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    void parseSmelting(SmeltingRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    void parseSmoking(SmokingRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    void parseStoneCutting(StonecuttingRecipe recipe, Identifier recipeId) {
        this.parseInGradients(recipe, recipeId);
    }

    public void parseRecipes(RecipeManager recipes) {
        recipes
                .values()
                .forEach((r) -> {
                    var type = r.getType();
                    var i = r.getId();
                    if(type==RecipeType.BLASTING) {
                        parseBlasting((BlastingRecipe) r, i);
                    } else if(type==RecipeType.CRAFTING) {
                        parseCrafting((CraftingRecipe) r, i);
                    } else if(type==RecipeType.CAMPFIRE_COOKING) {
                        parseCampfireCooking((CampfireCookingRecipe) r, i);
                    } else if(type==RecipeType.SMELTING) {
                        parseSmelting((SmeltingRecipe) r, i);
                    } else if(type==RecipeType.SMOKING) {
                        parseSmoking((SmokingRecipe) r, i);
                    } else if(type==RecipeType.STONECUTTING) {
                        parseStoneCutting((StonecuttingRecipe) r, i);
                    }
                });
    }
}
