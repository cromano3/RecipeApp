package com.christopherromano.culinarycompanion.datamodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.ui.graphics.vector.ImageVector

data class ProfileScreenStarsDataModel(
    val star1State: ImageVector = Icons.Outlined.Grade,
    val star2State: ImageVector = Icons.Outlined.Grade,
    val star3State: ImageVector = Icons.Outlined.Grade,
    val star4State: ImageVector = Icons.Outlined.Grade,
    val star5State: ImageVector = Icons.Outlined.Grade,
    val star6State: ImageVector = Icons.Outlined.Grade,
    val star7State: ImageVector = Icons.Outlined.Grade,
    val star8State: ImageVector = Icons.Outlined.Grade,
    val star9State: ImageVector = Icons.Outlined.Grade,
    val star10State: ImageVector = Icons.Outlined.Grade,
    val starList: List<ImageVector> = listOf<ImageVector>(
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade,
        Icons.Outlined.Grade)
    )
