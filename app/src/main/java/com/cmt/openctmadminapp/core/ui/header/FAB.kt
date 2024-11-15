package com.cmt.openctmadminapp.core.ui.header

import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cmt.openctmadminapp.core.di.ThemePreferenceManager

@Composable
fun FAB(isDarkTheme: Boolean, onThemeChange: (Int) -> Unit, onMainFabClick: () -> Unit) {
    var showOptions by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                showOptions = !showOptions
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(40.dp),
            shape = CircleShape,
            containerColor = Color(0xFF5CE1E6)
        ) {
            Icon(
                Icons.Default.Accessibility,
                contentDescription = "icono de accesibilidad",
                tint = Color.Black
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 80.dp,
                    end = 24.dp
                ) // Posiciona las opciones debajo del FAB principal
        ) {
            AnimatedVisibility(
                visible = showOptions,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    FloatingActionButton(
                        onClick = {
                            val newTheme = if (isDarkTheme) {
                                AppCompatDelegate.MODE_NIGHT_NO
                            } else {
                                AppCompatDelegate.MODE_NIGHT_YES
                            }
                            onThemeChange(newTheme)
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(40.dp),
                        shape = CircleShape,
                        containerColor = Color.White
                    ) {
                        Icon(
                            if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar tema",
                            tint = Color.Black
                        )
                    }

                    FloatingActionButton(
                        onClick = { /* Acción para la opción 2 */ },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(40.dp),
                        shape = CircleShape,
                        containerColor = Color.White
                    ) {
                        Icon(
                            Icons.Default.FormatSize,
                            contentDescription = "icono de tamaño de texto",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}