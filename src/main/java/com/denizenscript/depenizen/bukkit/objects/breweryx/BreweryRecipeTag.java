package com.denizenscript.depenizen.bukkit.objects.breweryx;

import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ColorTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.dre.brewery.recipe.BEffect;
import com.dre.brewery.recipe.BRecipe;
import com.dre.brewery.recipe.RecipeItem;
import com.dre.brewery.utility.Tuple;
import org.bukkit.Color;

public class BreweryRecipeTag implements ObjectTag {

    // <--[ObjectType]
    // @name BreweryRecipeTag
    // @prefix breweryrecipe
    // @base ElementTag
    // @format
    // The identity format for brewery is <recipe_name>
    // For example, 'breweryrecipe@example_recipe'.
    //
    // @plugin Depenizen, BreweryX
    // @description
    // A BreweryRecipeTag represents a Brewery recipe.
    //
    // -->

    @Fetchable("breweryrecipe")
    public static BreweryRecipeTag valueOf(String string, TagContext context) {
        if (string.startsWith("breweryrecipe@")) {
            string = string.substring("breweryrecipe@".length());
        }

        BRecipe recipe = BRecipe.get(string);
        if (recipe == null) {
            return null;
        }
        return new BreweryRecipeTag(recipe);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("breweryrecipe@", "");
        return BRecipe.get(arg) != null;
    }

    BRecipe bRecipe;

    public BreweryRecipeTag(BRecipe bRecipe) {
        this.bRecipe = bRecipe;
    }

    String prefix = "breweryrecipe";

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
        return "breweryrecipe@" + bRecipe.getId();
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

    public static void register() {
        // <--[tag]
        // @attribute <BreweryRecipeTag.id>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the ID of the recipe.
        // -->
        tagProcessor.registerTag(ElementTag.class, "id", (attribute, object) -> {
            String id = object.bRecipe.getId();
            return new ElementTag(id, true);
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.name>[<quality>]
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the name of the recipe based on the provided quality number.
        // -->
        tagProcessor.registerTag(ElementTag.class, ElementTag.class, "name", (attribute, object, quality) -> {
            return new ElementTag(object.bRecipe.getName(quality.asInt()), true);
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.full_name>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the full name of the recipe as declared in the recipes file.
        // -->
        tagProcessor.registerTag(ElementTag.class, "full_name", (attribute, object) -> {
            return new ElementTag(String.join("/", object.bRecipe.getName()), true);
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.ingredients>
        // @returns ListTag(ItemTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of ItemTags that are the ingredients of the recipe.
        // -->
        tagProcessor.registerTag(ListTag.class, "ingredients", (attribute, object) -> {
            ListTag ingredients = new ListTag();
            for (RecipeItem recipeItem : object.bRecipe.getIngredients()) {
                ingredients.add(recipeItem.toConfigString());
            }
            return ingredients;
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.difficulty>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the difficulty of the recipe.
        // -->
        tagProcessor.registerTag(ElementTag.class, "difficulty", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getDifficulty());
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.cooking_time>
        // @returns DurationTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the cooking time of the recipe.
        // -->
        tagProcessor.registerTag(DurationTag.class, "cooking_time", (attribute, object) -> {
            return new DurationTag(object.bRecipe.getCookingTime() * 60); // Brewery returns value in minutes
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.distill_runs>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the distill runs of the recipe.
        // -->
        tagProcessor.registerTag(ElementTag.class, "distill_runs", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getDistillruns());
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.distill_time>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of time each distill run takes.
        // -->
        tagProcessor.registerTag(ElementTag.class, "distill_time", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getDistillTime());
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.wood>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the type of wood used in the recipe (by number, Ex: 0 = Any, 1 = Oak).
        // -->
        tagProcessor.registerTag(ElementTag.class, "wood", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getWood());
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.age>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of minecraft days the potion must age in a Brewery barrel.
        // -->
        tagProcessor.registerTag(ElementTag.class, "age", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getAge());
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.color>
        // @returns ColorTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the color of the distilled/finished potion.
        // -->
        tagProcessor.registerTag(ColorTag.class, "color", ((attribute, object) -> {
            Color color = object.bRecipe.getColor().getColor();
            return new ColorTag(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }));

        // <--[tag]
        // @attribute <BreweryRecipeTag.alcohol>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the absolute amount of alcohol 0-100 in a perfect potion (will be added directly to the player, where 100 means fainting).
        // -->
        tagProcessor.registerTag(ElementTag.class, "alcohol", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getAlcohol());
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.lore>
        // @returns ListTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of the lore of the recipe (displayed on potion).
        // -->
        tagProcessor.registerTag(ListTag.class, "lore", (attribute, object) -> {
            if (object.bRecipe.getLore() == null) {
                return null;
            }
            ListTag lore = new ListTag();
            for (Tuple<Integer, String> tuple : object.bRecipe.getLore()) {
                lore.addObject(new ElementTag(tuple.second(), true));
            }
            return lore;
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.custom_model_data>
        // @returns ListTag(ElementTag(Number))
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of the 3 possible custom model data's for each varied quality of the recipe/potion.
        // -->
        tagProcessor.registerTag(ListTag.class, "custom_model_data", (attribute, object) -> {
            ListTag cmDatas = new ListTag();
            for (int cmData : object.bRecipe.getCmData()) {
                cmDatas.addObject(new ElementTag(cmData));
            }
            return cmDatas;
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.effects>
        // @returns ListTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of potion effects of as their names (Example: SLOW_FALLING).
        // -->
        tagProcessor.registerTag(ListTag.class, "effects", (attribute, object) -> {
            ListTag effects = new ListTag();
            for (BEffect bEffect : object.bRecipe.getEffects()) {
                effects.addObject(new ElementTag(bEffect.getType().toString(), true));
            }
            return effects;
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.player_commands>
        // @returns ListTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of commands that are run by the player when the potion is drunk.
        // -->
        tagProcessor.registerTag(ListTag.class, "player_commands", (attribute, object) -> {
            if (object.bRecipe.getPlayercmds() == null) {
                return null;
            }
            ListTag cmds = new ListTag();
            for (Tuple<Integer, String> tuple : object.bRecipe.getPlayercmds()) {
                cmds.addObject(new ElementTag(tuple.second(), true));
            }
            return cmds;
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.server_commands>
        // @returns ListTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of commands that are run by the server when the potion is drunk.
        // -->
        tagProcessor.registerTag(ListTag.class, "server_commands", (attribute, object) -> {
            if (object.bRecipe.getServercmds() == null) {
                return null;
            }
            ListTag cmds = new ListTag();
            for (Tuple<Integer, String> tuple : object.bRecipe.getServercmds()) {
                cmds.addObject(new ElementTag(tuple.second(), true));
            }
            return cmds;
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.message>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the message sent to the player when the potion is drunk.
        // -->
        tagProcessor.registerTag(ElementTag.class, "message", (attribute, object) -> {
            if (object.bRecipe.getDrinkMsg() == null) {
                return null;
            }
            return new ElementTag(object.bRecipe.getDrinkMsg(), true);
        });

        // <--[tag]
        // @attribute <BreweryRecipeTag.title>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the title message sent to the player when the potion is drunk.
        // -->
        tagProcessor.registerTag(ElementTag.class, "title", ((attribute, object) -> {
            if (object.bRecipe.getDrinkTitle() == null) {
                return null;
            }
            return new ElementTag(object.bRecipe.getDrinkTitle(), true);
        }));

        // <--[tag]
        // @attribute <BreweryRecipeTag.has_glint>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns if the recipe, once created into a brew, has a glint effect.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_glint", ((attribute, object) -> {
            return new ElementTag(object.bRecipe.isGlint());
        }));


        // <--[tag]
        // @attribute <BreweryRecipeTag.is_valid>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns if the recipe is valid and can be created.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_valid", ((attribute, object) -> {
            return new ElementTag(object.bRecipe.isValid());
        }));
    }

    public static ObjectTagProcessor<BreweryRecipeTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }
}
