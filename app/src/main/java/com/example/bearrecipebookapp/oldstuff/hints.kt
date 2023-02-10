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