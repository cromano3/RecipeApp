package com.example.bearrecipebookapp.oldstuff

/**text field search bar*/
//                        TextField(
//                            value =  uiState.currentInput,
//                            onValueChange =
//                            {
//                                topBarViewModel.updatePreview( it, it.text)
//                            },
//                            modifier = Modifier
//                                .focusRequester(focusRequester)
//                                .height(56.dp)
//                                .padding(top = 4.dp, bottom = 4.dp)
//                                .border(
//                                    width = 2.dp,
//                                    brush = (Brush.horizontalGradient(
//                                        colors = listOf(Color(0xFFd8af84), Color(0xFFb15f33)),
//                                        tileMode = TileMode.Mirror
//                                    )),
//                                    shape = RoundedCornerShape(25.dp)
//                                ),
//                            shape = RoundedCornerShape(25.dp),
//                            textStyle = TextStyle.Default.copy(color = Color(0xFF000000), fontSize = 14.sp),
//                            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF000000)) },
//                            trailingIcon =
//                            {
//                                if(uiState.currentInput.text.isNotEmpty()){
//                                    Surface(
//                                        color = Color.Transparent,
//                                        modifier = Modifier.clickable {  topBarViewModel.updatePreview( TextFieldValue(""), "") }
//                                    )
//                                    {
//                                        Icon(
//                                            Icons.Outlined.Close,
//                                            contentDescription = null,
//                                            tint = Color(0xFF000000)
//                                        )
//                                    }
//                                }
//                            },
//                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                            keyboardActions = KeyboardActions(
//                                onSearch = {
//                                    topBarViewModel.liveSearchForClick()
////                                searchScreenViewModel.searchFor(text)
//                                    focusManager.clearFocus()
//                                }),
//                            singleLine = true,
//                            colors = TextFieldDefaults.textFieldColors(
//                                disabledTextColor = Color.Transparent,
//                                backgroundColor = Color(0xFFd8af84),
//                                focusedIndicatorColor = Color.Transparent,
//                                unfocusedIndicatorColor = Color.Transparent,
//                                disabledIndicatorColor = Color.Transparent
//                            )
//                        )

/** How to pass a string along with the Nav controller to its destination.
 * This was ultimately not used as the variable is now written to the database.
 */
//            composable(route = "DetailsScreen/{recipeName}"){ it ->
//                val recipeName = it.arguments?.getString("recipeName")
//                recipeName?.let{NewDetailsScreen(recipeName = recipeName, onGoBackClick = {})}

/** Old Tutorial Alert */

//                                    Text(
//                                        " to add a recipe to your Menu and its ingredients to your" +
//                                                " Shopping List, or you can click on any of the recipe cards to see " +
//                                                "all the detailed information about the recipe and then click on the ",
//                                        color = Color(0xFF682300),
//                                        fontSize = 16.sp,
//                                        textAlign = TextAlign.Center
//                                    )

//                                    Surface(
//                                        modifier = Modifier
//                                            .padding(top = 8.dp, bottom = 8.dp)
//                                            .wrapContentSize()
//                                            .border(
//                                                width = 2.dp,
//                                                brush = (Brush.horizontalGradient(
//                                                    colors = listOf(
//                                                        Color(0xFFd8af84),
//                                                        Color(0xFFb15f33)
//                                                    ),
//                                                    tileMode = TileMode.Mirror
//                                                )),
//                                                shape = CircleShape
//                                            ),
//                                        shape = RoundedCornerShape(25.dp),
//                                        color = Color(0xFF682300),
//                                        elevation = 4.dp,
//                                    ) {
//                                        Row(
//                                            horizontalArrangement = Arrangement.Start,
//                                            modifier = Modifier.padding(
//                                                start = 12.dp,
//                                                end = 12.dp,
//                                                top = 12.dp,
//                                                bottom = 12.dp
//                                            )
//                                        )
//                                        {
//                                            Text(
//                                                text = "Add to Menu",
//                                                modifier = Modifier
//                                                    .align(Alignment.CenterVertically)
//                                                    .alpha(1f),
//                                                color = Color(0xFFd8af84),
//                                                fontSize = 18.sp,
//                                                textAlign = TextAlign.Center,
//                                                fontWeight = FontWeight.Bold
//                                            )
//                                        }
//                                    }
//
//                                    Text(
//                                        " button.",
//                                        color = Color(0xFF682300),
//                                        fontSize = 16.sp,
//                                    )