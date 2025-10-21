package Iteration1.tests.object;


import org.junit.Test;
import static org.junit.Assert.*;

public class userObjectTest {
    //add user
    //change user info
    //delete user
    
    
    @Test
    public void testCreateRecipeWithoutTag() {
        Recipe recipe;

        System.out.println("\nStarting testCreateRecipeWithoutTag");

        recipe = new Recipe("Cookies",
                "These cookies are amazing",
                "Flour, Eggs, Butter",
                60,
                "",
                new RecipeTagSet(),
                true);

        assertEquals(0, recipe.getRecipeID());
        assertEquals("Cookies", recipe.getRecipeTitle());
        assertEquals("These cookies are amazing", recipe.getRecipeDescription());
        assertEquals("Flour, Eggs, Butter", recipe.getRecipeIngredients());
        assertEquals(60, recipe.getRecipeCookingTime());
        assertEquals("", recipe.getRecipeImages());
        assertEquals(0, recipe.getRecipeTagSet().size());
        assertEquals(true, recipe.getRecipeIsFavourite());

        System.out.println("Finished testCreateRecipeWithoutTag");
    }


    @Test
    public void testCreateRecipeWithATag() {
        Recipe recipe;

        System.out.println("\nStarting testCreateRecipeWithATag");

        recipe = new Recipe("Cheese Cake",
                "These cakes are amazing",
                "Flour, Eggs, Butter",
                330,
                "",
                new RecipeTagSet("sugar"),
                false);

        assertEquals("Cheese Cake", recipe.getRecipeTitle());
        assertEquals("These cakes are amazing", recipe.getRecipeDescription());
        assertEquals("Flour, Eggs, Butter", recipe.getRecipeIngredients());
        assertEquals(330, recipe.getRecipeCookingTime());
        assertEquals("", recipe.getRecipeImages());
        assertTrue(recipe.getRecipeTagSet().contains("sugar"));
        assertEquals(false, recipe.getRecipeIsFavourite());

        System.out.println("Finished testCreateRecipeWithATag");
    }

    @Test
    public void testCreateRecipeWithMultipleTag() {
        Recipe recipe;

        System.out.println("\nStarting testCreateRecipeWithMultipleTag");

        RecipeTagSet recipeTagSet = new RecipeTagSet();
        recipeTagSet.addTag("sugar");
        recipeTagSet.addTag("egg");

        recipe = new Recipe("Cheese Cake",
                "These cakes are amazing",
                "Flour, Eggs, Butter",
                320,
                "",
                recipeTagSet,
                false);

        assertEquals("Cheese Cake", recipe.getRecipeTitle());
        assertEquals("These cakes are amazing", recipe.getRecipeDescription());
        assertEquals("Flour, Eggs, Butter", recipe.getRecipeIngredients());
        assertEquals(320, recipe.getRecipeCookingTime());
        assertEquals("", recipe.getRecipeImages());
        assertTrue(recipe.getRecipeTagSet().contains("sugar"));
        assertTrue(recipe.getRecipeTagSet().contains("egg"));
        assertEquals(false, recipe.getRecipeIsFavourite());

        System.out.println("Finished testCreateRecipeWithMultipleTag");
    }

}
