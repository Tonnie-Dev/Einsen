package dev.spikeysanju.einsen.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.spikeysanju.einsen.R
import dev.spikeysanju.einsen.components.PulseEffect
import dev.spikeysanju.einsen.navigation.MainActions
import dev.spikeysanju.einsen.ui.theme.einsenColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(actions: MainActions) {

    // Animate state
    LaunchedEffect(key1 = true) {
        delay(2500L)
        actions.popBackStack.invoke()
        actions.gotoDashboard.invoke()
    }

    PulseEffect {
        // App logo with center align
        Box(
            modifier = Modifier.fillMaxSize().background(einsenColors.white),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.einsen_logo),
                contentDescription = "Einsen Logo",
                modifier = Modifier.size(100.dp)
            )
        }
    }
}