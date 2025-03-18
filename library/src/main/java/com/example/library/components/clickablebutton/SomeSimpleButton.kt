package com.example.library.components.clickablebutton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun SomeSimpleButton(title: String = "Shortcut for a clickable", modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(5.dp, 15.dp), contentAlignment = Alignment.Center){Text(text = title)}
    }
}