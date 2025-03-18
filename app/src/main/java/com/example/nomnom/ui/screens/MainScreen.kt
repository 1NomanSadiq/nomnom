package com.example.nomnom.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.library.components.clickablebutton.SomeSimpleButton
import com.example.nomnom.ui.screens.croppingscreen._3_CroppingScreen

class MainScreen : Screen{
    @Preview(showBackground = true)
    @Composable
    override fun Content() {
        Column( modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .systemBarsPadding()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val navigator = LocalNavigator.current
            val goto = { screen: Screen -> navigator?.push(screen) }
            SomeSimpleButton("Feature 3 Alltogether") { goto(_3_CroppingScreen()) }
            SomeSimpleButton("Feature 2 SimpleButton") { goto(_2_ButtonScreen()) }
            SomeSimpleButton("Feature 1 feature_name") { goto(_1_FeatureScreen()) }
        }
    }
}
