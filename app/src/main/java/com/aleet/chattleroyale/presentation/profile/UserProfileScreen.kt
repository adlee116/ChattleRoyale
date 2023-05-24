package com.aleet.chattleroyale.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.presentation.theme.h4
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "User Profile", style = h4)

        user?.let {
            ProfilePicture(user = it)
//            com.aleet.chattleroyale.presentation.profile.DisplayName(user = it, onNameChange = viewModel::onNameChange)
//            com.aleet.chattleroyale.presentation.profile.PasswordChange(onPasswordChange = viewModel::onPasswordChange)
//            com.aleet.chattleroyale.presentation.profile.NotificationSettings(user = it, onNotificationChange = viewModel::onNotificationChange) com.aleet.chattleroyale.presentation.profile.DisplayName(user = it, onNameChange = viewModel::onNameChange)
            DisplayName(user = it)
            PasswordChange()
            NotificationSettings(user = it)
        }
    }
}

@Composable
fun ProfilePicture(user: User) {
    // TODO: Implement profile picture change
    Text(text = "Profile Picture: ${user.profilePicture}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//fun com.aleet.chattleroyale.presentation.profile.DisplayName(user: User, onNameChange: (String) -> Unit) {
fun DisplayName(user: User) {
    var name by remember { mutableStateOf(user.userName) }
    TextField(
        value = name ?: "",
        onValueChange = { newName ->
            name = newName
//            onNameChange(newName)
        },
        label = { Text("Display Name") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//fun com.aleet.chattleroyale.presentation.profile.PasswordChange(onPasswordChange: (String) -> Unit) {
fun PasswordChange() {
    var password by remember { mutableStateOf("") }
    TextField(
        value = password,
        onValueChange = { newPassword ->
            password = newPassword
//            onPasswordChange(newPassword)
        },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
//fun com.aleet.chattleroyale.presentation.profile.NotificationSettings(user: User, onNotificationChange: (Boolean) -> Unit) {
fun NotificationSettings(user: User) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    //TODO need to set this to an enabled thing somewhere
    Switch(
        checked = notificationsEnabled,
        onCheckedChange = { newStatus ->
            notificationsEnabled = newStatus
//            onNotificationChange(newStatus)
        }
    )
    Text(text = if (notificationsEnabled) "Notifications Enabled" else "Notifications Disabled")
}
