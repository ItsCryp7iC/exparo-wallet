package com.exparo.drivebackup.ui.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp // <-- FIX 1: Missing import added
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exparo.design.l0_system.UI
import com.exparo.design.l0_system.style
import com.exparo.design.l1_buildingBlocks.ExparoIconScaled
import com.exparo.design.l1_buildingBlocks.IconScale
import com.exparo.design.utils.thenIf
import com.exparo.drivebackup.data.model.BackupMetadata
import com.exparo.drivebackup.service.BackupManager
import com.exparo.legacy.utils.drawColoredShadow
import com.exparo.navigation.DriveBackupScreen
import com.exparo.navigation.ScheduledBackupSettings
import com.exparo.navigation.navigation
import com.exparo.navigation.screenScopedViewModel
import com.exparo.ui.R
import com.exparo.wallet.ui.theme.*
import com.exparo.wallet.ui.theme.components.ExparoToolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.getValue // Make sure you have this import


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxWithConstraintsScope.BackupScreen(
    screen: DriveBackupScreen,
    modifier: Modifier = Modifier
) {
    val viewModel: BackupViewModel = screenScopedViewModel()
    val uiState by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.onEvent(BackupEvent.GoogleSignInResult(success = true, account = account))
            } catch (e: ApiException) {
                viewModel.onEvent(BackupEvent.GoogleSignInResult(success = false, account = null))
            }
        }
    )

    val signInCallback = remember {
        object : GoogleSignInCallback {
            override fun launchGoogleSignIn(onResult: (Boolean) -> Unit) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)

                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setSignInCallback(signInCallback)
    }

    BackupScreenContent(
        modifier = modifier,
        screen = screen,
        backups = uiState.backups,
        isSignedIn = uiState.isSignedIn,
        backupProgress = uiState.backupProgress,
        selectedBackup = uiState.selectedBackup,
        errorMessage = uiState.errorMessage,
        currentUserEmail = uiState.currentUserEmail,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun BackupScreenContent(
    screen: DriveBackupScreen,
    backups: List<BackupMetadata>,
    isSignedIn: Boolean,
    backupProgress: BackupManager.BackupProgress,
    selectedBackup: BackupMetadata?,
    errorMessage: String?,
    currentUserEmail: String?,
    onEvent: (BackupEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val nav = navigation()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("backup_lazy_column")
    ) {
        item {
            ExparoToolbar(
                onBack = { nav.onBackPressed() },
            ) {
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = stringResource(R.string.backup_to_google_drive),
                style = UI.typo.h2.style(
                    fontWeight = FontWeight.Black
                )
            )
            Spacer(Modifier.height(24.dp))
        }

        if (isSignedIn) {
            item {
                // Account info section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Google Drive Connected",
                            style = UI.typo.b1.style(
                                color = UI.colors.gray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Signed in with ${currentUserEmail ?: "Google Account"}",
                            style = UI.typo.nB2.style(
                                color = UI.colors.gray
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                                         IconButton(
                         onClick = { onEvent(BackupEvent.SignOut) }
                     ) {
                         Icon(
                             imageVector = Icons.AutoMirrored.Filled.Logout,
                             contentDescription = "Sign Out",
                             tint = UI.colors.gray,
                             modifier = Modifier.size(24.dp)
                         )
                     }
                }
                Spacer(Modifier.height(16.dp))
                SettingsPrimaryButton(
                    icon = R.drawable.ic_vue_security_shield,
                    text = "Create Backup",
                    onClick = { onEvent(BackupEvent.CreateBackup()) },
                    backgroundGradient = GradientGreen
                )
                Spacer(Modifier.height(12.dp))
                SettingsDefaultButton(
                    icon = R.drawable.ic_custom_calendar_m,
                    text = "Scheduled Backups",
                    description = "Configure automatic daily/weekly backups",
                    onClick = {
                        nav.navigateTo(ScheduledBackupSettings)
                    }
                )
                Spacer(Modifier.height(12.dp))
                when (backupProgress) {
                    is BackupManager.BackupProgress.InProgress -> {
                        LinearProgressIndicator(
                            progress = backupProgress.progress.toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 8.dp)
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            text = backupProgress.message,
                            style = UI.typo.nB2.style(
                                color = UI.colors.gray
                            )
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    is BackupManager.BackupProgress.Completed -> {
                        Text(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            text = "Backup completed successfully!",
                            style = UI.typo.b2.style(
                                color = UI.colors.gray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    is BackupManager.BackupProgress.Error -> {
                        Text(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            text = "Error: ${backupProgress.message}",
                            style = UI.typo.b2.style(
                                color = Red,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    else -> {}
                }
            }

            if (backups.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Backups",
                            style = UI.typo.b1.style(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        IconButton(
                            onClick = { onEvent(BackupEvent.RefreshBackups) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh backups",
                                tint = UI.colors.gray
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
                items(backups) { backup ->
                    BackupItem(
                        backup = backup,
                        isSelected = backup.id == selectedBackup?.id,
                        onEvent = onEvent
                    )
                    Spacer(Modifier.height(8.dp))
                }
            } else {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "No backups found",
                        style = UI.typo.nB2.style(
                            color = UI.colors.gray
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            // Complete button at the bottom - only show during onboarding
            if (screen.isFromOnboarding) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    SettingsPrimaryButton(
                        icon = R.drawable.ic_done,
                        text = "Complete",
                        onClick = { 
                            // Complete onboarding and navigate to main app
                            onEvent(BackupEvent.CompleteOnboarding)
                        },
                        backgroundGradient = GradientGreen,
                        textColor = White,
                        hasShadow = true
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        } else {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = "Connect Google Drive",
                    style = UI.typo.b1.style(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(16.dp))
                
                // Display error message if present
                errorMessage?.let { message ->
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = message,
                        style = UI.typo.nB2.style(
                            color = Red,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                }
                
                SettingsPrimaryButton(
                    icon = R.drawable.ic_vue_security_shield,
                    text = "Sign in with Google",
                    onClick = { onEvent(BackupEvent.SignIn) },
                    backgroundGradient = Gradient.solid(Blue)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = "Sign in with your Google account to enable automatic backups to Google Drive.",
                    style = UI.typo.nB2.style(
                        color = UI.colors.gray
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun BackupItem(
    backup: BackupMetadata,
    isSelected: Boolean,
    onEvent: (BackupEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsButtonRow(
        modifier = modifier,
        onClick = { onEvent(BackupEvent.SelectBackup(backup)) },
        backgroundGradient = if (isSelected) Gradient.solid(UI.colors.primary) else Gradient.solid(UI.colors.medium)
    ) {
        Spacer(Modifier.width(12.dp))
        ExparoIconScaled(
            icon = R.drawable.ic_vue_security_shield,
            tint = if (isSelected) White else UI.colors.pureInverse,
            iconScale = IconScale.M,
            padding = 0.dp
        )
        Spacer(Modifier.width(8.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = backup.fileName,
                style = UI.typo.b2.style(
                    color = if (isSelected) White else UI.colors.pureInverse,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Size: ${formatFileSize(backup.fileSize)}",
                style = UI.typo.nB2.style(
                    color = if (isSelected) White.copy(alpha = 0.8f) else Gray,
                    fontWeight = FontWeight.Normal
                ).copy(fontSize = 14.sp)
            )
            Text(
                text = "Created: ${formatDate(backup.createdDate)}",
                style = UI.typo.nB2.style(
                    color = if (isSelected) White.copy(alpha = 0.8f) else Gray,
                    fontWeight = FontWeight.Normal
                ).copy(fontSize = 14.sp)
            )
        }
        Row {
            IconButton(
                onClick = { onEvent(BackupEvent.RestoreBackup(backup)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Restore,
                    contentDescription = "Restore",
                    tint = if (isSelected) White else UI.colors.pureInverse
                )
            }
            IconButton(
                onClick = { onEvent(BackupEvent.DeleteBackup(backup)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = if (isSelected) White else Red
                )
            }
        }
    }
}

// --- FIX 2 & 3: Parameters reordered to match Compose conventions ---
@Composable
private fun SettingsPrimaryButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasShadow: Boolean = false,
    backgroundGradient: Gradient = Gradient.solid(UI.colors.medium),
    textColor: Color = White,
    iconPadding: Dp = 0.dp,
    description: String? = null
) {
    SettingsButtonRow(
        onClick = onClick,
        modifier = modifier,
        hasShadow = hasShadow,
        backgroundGradient = backgroundGradient
    ) {
        Spacer(Modifier.width(12.dp))
        ExparoIconScaled(
            icon = icon,
            tint = textColor,
            iconScale = IconScale.M,
            padding = iconPadding
        )
        Spacer(Modifier.width(8.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = text,
                style = UI.typo.b2.style(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
            )
            if (!description.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = description,
                    style = UI.typo.nB2.style(
                        color = Gray,
                        fontWeight = FontWeight.Normal
                    ).copy(fontSize = 14.sp)
                )
            }
        }
    }
}

@Composable
private fun SettingsDefaultButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    SettingsButtonRow(
        onClick = onClick,
        modifier = modifier
    ) {
        Spacer(Modifier.width(12.dp))
        ExparoIconScaled(
            icon = icon,
            tint = UI.colors.pureInverse,
            iconScale = IconScale.M,
            padding = 0.dp
        )
        Spacer(Modifier.width(8.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 20.dp, end = 8.dp)
        ) {
            Text(
                text = text,
                style = UI.typo.b2.style(
                    color = UI.colors.pureInverse,
                    fontWeight = FontWeight.Bold
                )
            )
            if (!description.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = description,
                    style = UI.typo.nB2.style(
                        color = Gray,
                        fontWeight = FontWeight.Normal
                    ).copy(fontSize = 14.sp)
                )
            }
        }
    }
}

@Composable
private fun SettingsButtonRow(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    hasShadow: Boolean = false,
    backgroundGradient: Gradient = Gradient.solid(UI.colors.medium),
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .thenIf(hasShadow) {
                drawColoredShadow(color = backgroundGradient.startColor)
            }
            .fillMaxWidth()
            .clip(UI.shapes.r4)
            .background(backgroundGradient.asHorizontalBrush(), UI.shapes.r4)
            .thenIf(onClick != null) {
                clickable {
                    onClick?.invoke()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${(size / 1024)} KB"
        size < 1024 * 1024 * 1024 -> "${(size / (1024 * 1024))} MB"
        else -> "${(size / (1024 * 1024 * 1024))} GB"
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}