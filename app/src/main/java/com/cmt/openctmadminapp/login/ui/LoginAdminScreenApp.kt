package com.cmt.openctmadminapp.login.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.core.ui.home.LogoSection
import com.cmt.openctmadminapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openctmadminapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openctmadminapp.login.ui.viewmodel.LoginState
import com.cmt.openctmadminapp.login.ui.viewmodel.LoginViewModel

@Composable
fun LoginAdminScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var toast: Toast? = null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LogoSection(Modifier.weight(1f))
        RegisterSection(Modifier.weight(1f),
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onLoginClick = { viewModel.login(email, password) },
            focusManager
        )

        when (val state = loginState) {
            is LoginState.Loading -> {
                LoadingScreen()
            }
            is LoginState.Success -> {
                LaunchedEffect(Unit) {
                    navigationController.navigate(Routes.ResearchAdminScreen.route)
                }
            }
            is LoginState.Error -> {
                LaunchedEffect(state.message) {
                    toast?.cancel()
                    toast = Toast.makeText(context, state.message, Toast.LENGTH_LONG).apply { show() }
                }
            }
            else -> { /* Do nothing for Idle state */ }
        }
    }
}

@Composable
fun RegisterSection(
    modifier: Modifier,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    focusManager: FocusManager
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_description),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            EmailField(
                label = stringResource(id = R.string.login_email_field),
                email,
                onEmailChange,
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Next
                    )
                }
            )
            PasswordField(
                label = stringResource(id = R.string.login_pass_field),
                password,
                onPasswordChange
            ) { }
            MyButton(
                onLoginClick,
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
    onNext: () -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = label,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next), // para modificar el teclado y se adapte al fieldEmail
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onTertiary,
            focusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    var passwordStateVisibility by remember { mutableStateOf(false) }
    var showLastCharacter by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(value) {
        if (value.isNotEmpty()) {
            showLastCharacter = true;
            kotlinx.coroutines.delay(1000)
            showLastCharacter = false
        }
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = label,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), // para modificar el teclado y se adapte al fieldEmail
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDone()
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onTertiary,
            focusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onTertiaryContainer,
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
        visualTransformation = when {
            passwordStateVisibility -> VisualTransformation.None
            showLastCharacter && value.isNotEmpty() -> LastCharacterVisibleTransformation
            else -> PasswordVisualTransformation()
        }
    )
}

object LastCharacterVisibleTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = buildString {
            if (text.isNotEmpty()) {
                append("•".repeat(text.length - 1))
                append(text.last())
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset.coerceAtMost(transformedText.length)
            override fun transformedToOriginal(offset: Int): Int = offset.coerceAtMost(text.length)
        }
        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}