package com.christopherromano.culinarycompanion.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LicensesScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
            .verticalScroll(rememberScrollState())){
        Text(
            text = "The following libraries and components are used in this app and are released under the following licenses:\n" +
                        "\n" +
                        "- Android Jetpack libraries, including androidx.core, androidx.compose, androidx.activity, androidx.lifecycle, and androidx.navigation: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                        "\n" +
                        "- Material Icons Extended library: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                        "\n" +
                        "- Accompanist Navigation Animation library: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                        "\n" +
                        "- javax.inject library: Common Development and Distribution License (CDDL) version 1.0 and GNU General Public License (GPL) version 2 with Classpath Exception (https://opensource.org/licenses/CDDL-1.0, https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)\n" +
                        "\n" +
                        "- Firebase libraries, including firebase-analytics-ktx, firebase-auth-ktx, firebase-firestore-ktx, and play-services-auth: Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0)\n" +
                        "\n" +
                        "- Coil library:Coil \n" +
                        "\n" +
                        "Copyright 2022 Coil Contributors\n" +
                        "\n" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   https://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License.\n" +
                        "\n" +
                        "Please see the corresponding license files in the individual libraries, or the licenses text file in this apps package, or visit the links above for the full text of the licenses.",
            color = Color(0xFF682300),
        )
    }
}