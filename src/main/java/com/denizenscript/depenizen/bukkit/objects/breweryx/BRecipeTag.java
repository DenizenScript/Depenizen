package com.denizenscript.depenizen.bukkit.objects.breweryx;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ColorTag;
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

    public static void register() {

        // <--[tag]
        // @attribute <BRecipeTag.id>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the ID of the recipe as specified in the config.
        // -->
        tagProcessor.registerTag(ElementTag.class, "id", (attribute, object) -> {
            /*
            This being optional was infrastructure added by the original authors and is not used
            in Brewery. It will be deprecated and replaced soon.
            */
            Optional<String> id = object.bRecipe.getOptionalID();
            if (id.isPresent()) {
                return new ElementTag(id.get());
            }
            return null;
        });

        // <--[tag]
        // @attribute <BRecipeTag.name>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the name of the recipe at it's highest quality.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getRecipeName());
        });

        // <--[tag]
        // @attribute <BRecipeTag.ingredients>
        // @returns ListTag(ItemTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of ItemTags that are the ingredients of the recipe.
        // -->
        tagProcessor.registerTag(ListTag.class, "ingredients", (attribute, object) -> {
            ListTag ingredients = new ListTag();
            for (RecipeItem recipeItem : object.bRecipe.getIngredients()) {
                if (recipeItem.getMaterials() == null) {
                    continue;
                }
                for (Material material : recipeItem.getMaterials()) {
                    ingredients.addObject(new ItemTag(material, recipeItem.getAmount()));
                }
            }
            return ingredients;
        });

        // <--[tag]
        // @attribute <BRecipeTag.difficulty>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the difficulty of the recipe.
        // -->
        tagProcessor.registerTag(ElementTag.class, "difficulty", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getDifficulty());
        });

        // <--[tag]
        // @attribute <BRecipeTag.cooking_time>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the cooking time of the recipe.
        // -->
        tagProcessor.registerTag(ElementTag.class, "cooking_time", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getCookingTime());
        });

        // <--[tag]
        // @attribute <BRecipeTag.distill_runs>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the distill runs of the recipe
        // -->
        tagProcessor.registerTag(ElementTag.class, "distill_runs", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getDistillRuns());
        });

        // <--[tag]
        // @attribute <BRecipeTag.distill_time>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of time each distill run takes.
        // -->
        tagProcessor.registerTag(ElementTag.class, "distill_time", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getDistillTime());
        });

        // <--[tag]
        // @attribute <BRecipeTag.wood>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the type of wood used in the recipe (by number, Ex: 0 = Any, 1 = Oak).
        // -->
        tagProcessor.registerTag(ElementTag.class, "wood", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getWood());
        });

        // <--[tag]
        // @attribute <BRecipeTag.age>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of minecraft days the potion must age in a Brewery barrel.
        // -->
        tagProcessor.registerTag(ElementTag.class, "age", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getAge());
        });

        // <--[tag]
        // @attribute <BRecipeTag.color>
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
        // @attribute <BRecipeTag.alcohol>
        // @returns ElementTag(Number)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the amount of alcohol in a perfect potion.
        // -->
        tagProcessor.registerTag(ElementTag.class, "alcohol", (attribute, object) -> {
            return new ElementTag(object.bRecipe.getAlcohol());
        });

        // <--[tag]
        // @attribute <BRecipeTag.lore>
        // @returns ListTag(ElementTag)
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
                lore.addObject(new ElementTag(tuple.second()));
            }
            return lore;
        });

        // <--[tag]
        // @attribute <BRecipeTag.custom_model_data>
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
        // @attribute <BRecipeTag.effects>
        // @returns ListTag(ElementTag)
        // @plugin Depenizen, BreweryX
        // @description
        // Returns a ListTag of potion effects of as their names (Example: SLOW_FALLING).
        // -->
        tagProcessor.registerTag(ListTag.class, "effects", (attribute, object) -> {
            ListTag effects = new ListTag();
            for (BEffect bEffect : object.bRecipe.getEffects()) {
                effects.addObject(new ElementTag(bEffect.getType().toString()));
            }
            return effects;
        });

        // <--[tag]
        // @attribute <BRecipeTag.player_commands>
        // @returns ListTag(ElementTag)
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
                cmds.addObject(new ElementTag(tuple.second()));
            }
            return cmds;
        });

        // <--[tag]
        // @attribute <BRecipeTag.server_commands>
        // @returns ListTag(ElementTag)
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
                cmds.addObject(new ElementTag(tuple.second()));
            }
            return cmds;
        });

        // <--[tag]
        // @attribute <BRecipeTag.message>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the message sent to the player when the potion is drunk.
        // -->
        tagProcessor.registerTag(ElementTag.class, "message", (attribute, object) -> {
            if (object.bRecipe.getDrinkMsg() == null) {
                return null;
            }
            return new ElementTag(object.bRecipe.getDrinkMsg());
        });

        // <--[tag]
        // @attribute <BRecipeTag.title>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the title message sent to the player when the potion is drunk.
        // -->
        tagProcessor.registerTag(ElementTag.class, "title", ((attribute, object) -> {
            if (object.bRecipe.getDrinkTitle() == null) {
                return null;
            }
            return new ElementTag(object.bRecipe.getDrinkTitle());
        }));
    }

    public static ObjectTagProcessor<BRecipeTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }
}
