
package com.example.mytusequipmentbooking.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// TUS-inspired gold and black colours
private val TUSGold = Color(0xFFFFC629)
private val TUSWhite = Color(0xFFFFFFFF)
private val TUSLightGrey = Color(0xFFF5F5F5)
private val TUSDarkGrey = Color(0xFF292929)

private val LightColorScheme = lightColorScheme(
    primary = TUSGold,
    secondary = TUSDarkGrey,
    tertiary = TUSGold,
    background = TUSWhite,
    surface = TUSWhite,
    onPrimary = TUSDarkGrey,
    onSecondary = Color.White,
    onBackground = TUSDarkGrey,
    onSurface = TUSDarkGrey
)

private val DarkColorScheme = darkColorScheme(
    primary = TUSGold,
    secondary = TUSDarkGrey,
    tertiary = TUSGold,
    background = TUSDarkGrey,
    surface = TUSDarkGrey,
    onPrimary = TUSDarkGrey,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)
@Composable
fun MyTUSEquipmentBookingTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
