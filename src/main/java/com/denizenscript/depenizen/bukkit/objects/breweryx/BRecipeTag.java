package com.denizenscript.depenizen.bukkit.objects.breweryx;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ColorTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.dre.brewery.recipe.BEffect;
import com.dre.brewery.recipe.BRecipe;
import com.dre.brewery.recipe.RecipeItem;
import com.dre.brewery.utility.Tuple;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.Optional;

public class BRecipeTag implements ObjectTag {

    // <--[ObjectType]
    // @name BRecipeTag
    // @prefix brecipe
    // @base ElementTag
    // @format
    // The identity format for brewery is <recipe_name>
    // For example, 'brecipe@my_recipe'.
    //
    // @plugin Depenizen, BreweryX
    // @description
    // A BRecipeTag represents a Brewery recipe.
    //
    // -->


    @Fetchable("brecipe")
    public static BRecipeTag valueOf(String string, TagContext context) {
        if (string.startsWith("brecipe@")) {
            string = string.substring("brecipe@".length());
        }

        BRecipe recipe = BRecipe.get(string);
        if (recipe == null) {
            return null;
        }
        return new BRecipeTag(recipe);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("brecipe@", "");
        return BRecipe.get(arg) != null;
    }

    BRecipe bRecipe;

    public BRecipeTag(BRecipe bRecipe) {
        this.bRecipe = bRecipe;
    }

    String prefix = "brecipe";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "brecipe@" + bRecipe.getRecipeName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public ObjectTag setPrefix(String aString) {
        prefix = aString;
        return this;
    }



    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <BRecipeTag.id>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the ID of the recipe as specified in the config.
        // -->
        if (attribute.startsWith("id")) {
            /*
            TODO: This being optional was infrastructure added by the original authors and is not used
            in Brewery. It will be deprecated and replaced soon.
             */
            Optional<String> id = bRecipe.getOptionalID();
            if (id.isPresent()) {
                return new ElementTag(id.get()).getObjectAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <BRecipeTag.name>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the name of the recipe at it's highest quality.
        // -->
        else if (attribute.startsWith("name")) {
            return new ElementTag(bRecipe.getRecipeName()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.ingredients>
        // @returns ListTag(ItemTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of ItemTags that are the ingredients of the recipe.
        // -->
        else if (attribute.startsWith("ingredients")) {
            ListTag ingredients = new ListTag();
            for (RecipeItem recipeItem : bRecipe.getIngredients()) {
                if (recipeItem.getMaterials() == null) {
                    continue;
                }
                for (Material material : recipeItem.getMaterials()) {
                    ingredients.addObject(new ItemTag(material, recipeItem.getAmount()));
                }
            }
            return ingredients.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.difficulty>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the difficulty of the recipe.
        // -->
        else if (attribute.startsWith("difficulty")) {
            return new ElementTag(bRecipe.getDifficulty()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.cooking_time>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the cooking time of the recipe.
        // -->
        else if (attribute.startsWith("cooking_time")) {
            return new ElementTag(bRecipe.getCookingTime()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.distill_runs>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the distill runs of the recipe
        // -->
        else if (attribute.startsWith("distill_runs")) {
            return new ElementTag(bRecipe.getDistillRuns()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.distill_time>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of time each distill run takes.
        // -->
        else if (attribute.startsWith("distill_time")) {
            return new ElementTag(bRecipe.getDistillTime()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.wood>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the type of wood used in the recipe (by number, Ex: 0 = Any, 1 = Oak).
        // -->
        else if (attribute.startsWith("wood")) {
            return new ElementTag(bRecipe.getWood()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.age>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of minecraft days the potion must age in a Brewery barrel.
        // -->
        else if (attribute.startsWith("age")) {
            return new ElementTag(bRecipe.getAge()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.color>
        // @returns ColorTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the color of the distilled/finished potion.
        // -->
        else if (attribute.startsWith("color")) {
            Color color = bRecipe.getColor().getColor();
            return new ColorTag(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.alcohol>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of alcohol in a perfect potion.
        // -->
        else if (attribute.startsWith("alcohol")) {
            return new ElementTag(bRecipe.getAlcohol()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.lore>
        // @returns ListTag(ElementTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of the lore of the recipe (displayed on potion).
        // -->
        else if (attribute.startsWith("lore")) {
            if (bRecipe.getLore() == null) {
                return null;
            }
            ListTag lore = new ListTag();
            for (Tuple<Integer, String> tuple : bRecipe.getLore()) {
                lore.addObject(new ElementTag(tuple.second()));
            }
            return lore.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.custom_model_data>
        // @returns ListTag(ElementTag(Number))
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of the 3 possible custom model data's for each varied quality of the recipe/potion.
        // -->
        else if (attribute.startsWith("custom_model_data")) {
            ListTag cmDatas = new ListTag();
            for (int cmData : bRecipe.getCmData()) {
                cmDatas.addObject(new ElementTag(cmData));
            }
            return cmDatas.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.effects>
        // @returns ListTag(ElementTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of potion effects of as their names (Example: SLOW_FALLING).
        // -->
        else if (attribute.startsWith("effects")) {
            ListTag effects = new ListTag();
            for (BEffect bEffect : bRecipe.getEffects()) {
                effects.addObject(new ElementTag(bEffect.getType().toString()));
            }
            return effects.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.player_commands>
        // @returns ListTag(ElementTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of commands that are run by the player when the potion is drunk.
        // -->
        else if (attribute.startsWith("player_commands")) {
            if (bRecipe.getPlayercmds() == null) {
                return null;
            }
            ListTag cmds = new ListTag();
            for (Tuple<Integer, String> tuple : bRecipe.getPlayercmds()) {
                cmds.addObject(new ElementTag(tuple.second()));
            }
            return cmds.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.server_commands>
        // @returns ListTag(ElementTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of commands that are run by the server when the potion is drunk.
        // -->
        else if (attribute.startsWith("server_commands")) {
            if (bRecipe.getServercmds() == null) {
                return null;
            }
            ListTag cmds = new ListTag();
            for (Tuple<Integer, String> tuple : bRecipe.getServercmds()) {
                cmds.addObject(new ElementTag(tuple.second()));
            }
            return cmds.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.message>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the message sent to the player when the potion is drunk.
        // -->
        else if (attribute.startsWith("message")) {
            if (bRecipe.getDrinkMsg() == null) {
                return null;
            }
            return new ElementTag(bRecipe.getDrinkMsg()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BRecipeTag.title>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the title message sent to the player when the potion is drunk.
        // -->
        else if (attribute.startsWith("title")) {
            if (bRecipe.getDrinkTitle() == null) {
                return null;
            }
            return new ElementTag(bRecipe.getDrinkTitle()).getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);
    }
}
