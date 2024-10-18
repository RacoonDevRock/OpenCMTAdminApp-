package com.cmt.openctmadminapp.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openctmadminapp.core.ui.home.LogoSection

//@Preview(showSystemUi = true)
@Composable
fun LoginAdminScreen(modifier: Modifier, navigationController: NavHostController) {
    val navigateToResearch = remember { Routes.ResearchAdminScreen.route }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LogoSection(Modifier.weight(1f))
        RegisterSection(Modifier.weight(1f)) { navigationController.navigate(navigateToResearch) }
    }
}

@Composable
fun RegisterSection(modifier: Modifier, navigate: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.login_description),
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 50.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            EmailField(label = stringResource(id = R.string.login_email_field), email) {
                email = it
            }
            Spacer(modifier = Modifier.height(25.dp))
            PasswordField(label = stringResource(id = R.string.login_pass_field), password) {
                password = it
            }
            Spacer(modifier = Modifier.height(25.dp))
            MyButton(
                navigate,
                stringResource(id = R.string.login_button),
                Icons.AutoMirrored.Filled.ArrowForwardIos
            )
        }
    }
}

@Composable
fun EmailField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.width(310.dp),
        placeholder = {
            Text(
                text = label,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // para modificar el teclado y se adapte al fieldEmail
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var passwordStateVisibility by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.width(310.dp),
        placeholder = {
            Text(
                text = label,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // para modificar el teclado y se adapte al fieldEmail
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        trailingIcon = {
            val imagen = if (passwordStateVisibility) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = { passwordStateVisibility = !passwordStateVisibility }) {
                Icon(imageVector = imagen, contentDescription = "show password")
            }
        },
        shape = RoundedCornerShape(25.dp),
        visualTransformation = if (passwordStateVisibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}