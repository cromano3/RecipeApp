package com.example.bearrecipebookapp.oldstuff

class DataSource {
//    fun loadRecipeList(): List<Recipe>{
//       return listOf<Recipe>(
//            Recipe(
//                recipeName = R.string.recipe1_name,
//                ingredientsList = R.string.recipe1_ingredients,
//                //ingredientsList = listOf<String>("flour", "water"),
//                instructions = R.string.recipe1_instructions,
//                imageRes = arrayOf( R.drawable.bagel, R.drawable.bagel2)
//            ),
//            Recipe(
//                recipeName = R.string.recipe2_name,
//                ingredientsList = R.string.recipe2_ingredients,
//                //ingredientsList = listOf<String>("flour", "sugar"),
//                instructions = R.string.recipe2_instructions,
//                imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//            ),
//
//
//           //Demo Stuff
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.bagel2, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.bagel, R.drawable.bagel2)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//           ),
//           Recipe(
//               recipeName = R.string.demo,
//               ingredientsList = R.string.recipe2_ingredients,
//               //ingredientsList = listOf<String>("flour", "sugar"),
//               instructions = R.string.recipe2_instructions,
//               imageRes = arrayOf( R.drawable.garlic2, R.drawable.garlic)
//           )
//
//
//        )
//    }
    fun loadPantryList(): List<List<String>>{

        var categoryAndItemNamesCI = listOf(
            listOf<String>("Vegetables", "Onion","Garlic","Cauliflower","Broccoli","Zucchini","Sweet Potatoes","Spinach","Garbanzo","Red Pepper","Mushroom","Potatoes","Carrots"),
            listOf<String>("Fruits", "Lemon","Lime","Apple","Banana","Tomato","Plantain"),
            listOf<String>("Milk, Cream, Oil, and Liquids", "Vegetable Milk","Coconut Milk","Soy Sauce","Sesame Oil","Olive Oil","Vegetable Broth"),
            listOf<String>("Nuts", "Walnuts","Cashews","Pecans"),
            listOf<String>("Rice, Pasta, and Legumes", "Rice","Brown Rice","Lentils","Pasta"),
            listOf<String>("Herbs", "Basil","Parsley","Cilantro","Rosemary","Thyme"),
            listOf<String>("Spices and Powders", "Garlic Powder","Paprika","Cajun Powder","Curry Powder","Cumin"),
            listOf<String>("Baking", "Flour","Yeast","Vegetable Butter"),
            listOf<String>("Store Bought", "Tortillas","Peanut Butter","Tofu","Nutritional Yeast")
        )


        
//        categoryAndItemNamesCI. = "Vegetables"
//        ""1][0] = "Fruits"
//        ""2][0] = "Milk, Cream, Oil, and Liquids"
//        ""3][0] = "Nuts"
//        ""4][0] = "Rice, Pasta, and Legumes"
//        ""5][0] = "Herbs"
//        ""6][0] = "Spices and Powders"
//        ""7][0] = "Baking"
//        ""8][0] = "Pre-Made or Store Bought"
//
//        
//        ""0][0] = "Onion"
//        ""0][1] = "Garlic"
//        ""0][2] = "Cauliflower"
//        ""0][3] = "Broccoli"
//        ""0][4] = "Zucchini"
//        ""0][5] = "Sweet Potatoes"
//        ""0][6] = "Spinach"
//        ""0][7] = "Garbanzo"
//        ""0][8] = "Red Pepper"
//        ""0][9] = "Mushroom"
//        ""0][10] = "Potatoes"
//        ""0][11] = "Carrots"
//        
//        ""1][0] = "Lemon"
//        ""1][1] = "Lime"
//        ""1][2] = "Apple"
//        ""1][3] = "Banana"
//        ""1][4] = "Tomato"
//        ""1][5] = "Plantain"
//
//        ""2][0] = "Vegetable Milk"
//        ""2][1] = "Coconut Milk"
//      //  ""2][2] = "Honey"
//        ""2][2] = "Soy Sauce"
//        ""2][3] = "Sesame Oil"
//        ""2][4] = "Olive Oil"
//        ""2][5] = "Vegetable Broth"
//
//        ""3][0] = "Walnuts"
//        ""3][1] = "Cashews"
//        ""3][2] = "Pecans"
//
//        ""4][0] = "Rice"
//        ""4][1] = "Brown Rice"
//        ""4][2] = "Lentils"
//        ""4][3] = "Pasta"
//
//        //herbs
//        ""5][0] = "Basil"
//        ""5][1] = "Parsley"
//        ""5][2] = "Cilantro"
//        ""5][3] = "Rosemary"
//        ""5][4] = "Thyme"
//
//        //spices and powders
//        ""6][0] = "Garlic Powder"
//        ""6][1] = "Paprika"
//        ""6][2] = "Cajun Powder"
//        ""6][3] = "Curry Powder"
//        ""6][4] = "Cumin"
//        
//        //Baking
//        ""7][0] = "Flour"
//        ""7][1] = "Yeast"
//        ""7][2] = "Vegetable Butter"
//        
//        //Store Bought
//        ""8][0] = "Tortillas"
//        ""8][1] = "Peanut Butter"
//        ""8][2] = "Tofu"
//        ""8][3] = "Nutritional Yeast"
        
        

        return categoryAndItemNamesCI

        
    }
}