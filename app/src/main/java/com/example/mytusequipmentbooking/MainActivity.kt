
package com.example.mytusequipmentbooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mytusequipmentbooking.ui.theme.MyTUSEquipmentBookingTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences(
            "app_preferences",
            MODE_PRIVATE
        )

        setContent {

            var darkTheme by remember {
                mutableStateOf(
                    preferences.getBoolean("dark_theme", false)
                )
            }

            MyTUSEquipmentBookingTheme(
                darkTheme = darkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(
                        darkTheme = darkTheme,
                        onThemeChange = { enabled ->

                            darkTheme = enabled

                            preferences.edit()
                                .putBoolean("dark_theme", enabled)
                                .apply()
                        }
                    )
                }
            }
        }
    }
}
