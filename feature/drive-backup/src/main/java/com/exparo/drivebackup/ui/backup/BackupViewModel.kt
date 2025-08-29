package com.exparo.drivebackup.ui.backup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.exparo.drivebackup.data.model.BackupMetadata
import com.exparo.drivebackup.service.BackupManager
import com.exparo.drivebackup.service.GoogleDriveService
import com.exparo.drivebackup.service.UserNameUpdateService
import com.exparo.ui.ComposeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.collectAsState
import com.exparo.navigation.Navigation
import com.exparo.base.legacy.SharedPrefs

// Callback interface for Google Sign-In
interface GoogleSignInCallback {
    fun launchGoogleSignIn(onResult: (Boolean) -> Unit)
}

@Stable
@HiltViewModel
class BackupViewModel @Inject constructor(
    private val googleDriveService: GoogleDriveService,
    private val backupManager: BackupManager,
    private val navigation: Navigation,
    private val sharedPrefs: SharedPrefs,
    private val userNameUpdateService: UserNameUpdateService
) : ComposeViewModel<BackupState, BackupEvent>() {

    private val _viewState = MutableStateFlow(BackupState())
    val viewState = _viewState.asStateFlow()

    private var signInCallback: GoogleSignInCallback? = null

    init {
        checkSignInStatus()
        observeBackupProgress()
    }

    @Composable
    override fun uiState(): BackupState {
        return viewState.collectAsState().value
    }

    override fun onEvent(event: BackupEvent) {
        when (event) {
            is BackupEvent.SignIn -> signIn()
            is BackupEvent.SignOut -> signOut()
            is BackupEvent.CreateBackup -> createBackup(event.encrypt)
            is BackupEvent.RestoreBackup -> restoreBackup(event.backup)
            is BackupEvent.DeleteBackup -> deleteBackup(event.backup)
            is BackupEvent.SelectBackup -> selectBackup(event.backup)
            is BackupEvent.LoadBackups -> loadBackups()
            is BackupEvent.RefreshBackups -> refreshBackups()
            is BackupEvent.GoogleSignInResult -> handleGoogleSignInResult(event)
            is BackupEvent.CompleteOnboarding -> completeOnboarding()
        }
    }

    private fun observeBackupProgress() {
        viewModelScope.launch {
            backupManager.backupProgress.collectLatest { progress ->
                _viewState.update { it.copy(backupProgress = progress) }
                if (progress is BackupManager.BackupProgress.Completed) {
                    loadBackups()
                }
            }
        }
    }

    fun setSignInCallback(callback: GoogleSignInCallback) {
        signInCallback = callback
    }

    private fun checkSignInStatus() {
        viewModelScope.launch {
            val isSignedIn = googleDriveService.isSignedIn()
            if (isSignedIn) {
                // Get the current signed-in account to retrieve email
                val currentAccount = googleDriveService.getCurrentAccount()
                _viewState.update { 
                    it.copy(
                        isSignedIn = isSignedIn,
                        currentUserEmail = currentAccount?.email
                    ) 
                }
                loadBackups()
            } else {
                _viewState.update { 
                    it.copy(
                        isSignedIn = isSignedIn,
                        currentUserEmail = null
                    ) 
                }
            }
        }
    }

    private fun signIn() {
        signInCallback?.launchGoogleSignIn { /* Result handled by GoogleSignInResult event */ }
    }

    private fun handleGoogleSignInResult(result: BackupEvent.GoogleSignInResult) {
        viewModelScope.launch {
            if (result.success && result.account != null) {
                // Check if the user has granted the required Drive scope
                if (!googleDriveService.hasRequiredScopes(result.account)) {
                                    _viewState.update { 
                    it.copy(
                        isSignedIn = false,
                        errorMessage = "Google Drive access is required for backup functionality. Please sign in again and grant Drive access.",
                        currentUserEmail = null
                    )
                }
                    return@launch
                }
                
                // Initialize the Drive service with the signed-in account
                googleDriveService.initializeDriveService(result.account)

                // Update user name from Google account if available
                userNameUpdateService.updateUserNameFromGoogleAccount(googleDriveService)

                // Then, update the UI state
                _viewState.update { 
                    it.copy(
                        isSignedIn = true, 
                        errorMessage = null,
                        currentUserEmail = result.account.email
                    ) 
                }

                // NOW we can safely load the backups
                loadBackups()
            } else {
                _viewState.update { 
                    it.copy(
                        isSignedIn = false,
                        errorMessage = "Google Sign-In failed. Please try again.",
                        currentUserEmail = null
                    )
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            googleDriveService.signOut()
                    _viewState.update {
            it.copy(
                isSignedIn = false,
                backups = emptyList(),
                selectedBackup = null,
                currentUserEmail = null
            )
        }
        }
    }

    private fun createBackup(encrypt: Boolean) {
        viewModelScope.launch {
            if (_viewState.value.isSignedIn) {
                backupManager.createBackup(encrypt)
            }
        }
    }

    private fun restoreBackup(backup: BackupMetadata) {
        viewModelScope.launch {
            if (_viewState.value.isSignedIn) {
                backupManager.restoreBackup(backup)
            }
        }
    }

    private fun deleteBackup(backup: BackupMetadata) {
        viewModelScope.launch {
            val success = backupManager.deleteBackup(backup)
            if (success) {
                if (_viewState.value.selectedBackup?.id == backup.id) {
                    _viewState.update { it.copy(selectedBackup = null) }
                }
                loadBackups()
            }
        }
    }

    private fun selectBackup(backup: BackupMetadata?) {
        _viewState.update { it.copy(selectedBackup = backup) }
    }

    private fun loadBackups() {
        viewModelScope.launch {
            if (_viewState.value.isSignedIn) {
                try {
                    Log.d("BackupViewModel", "Loading backups...")
                    Log.d("BackupViewModel", "Current user ID: ${googleDriveService.getCurrentUserId()}")
                    Log.d("BackupViewModel", "Drive service available: ${googleDriveService.getDriveService() != null}")
                    
                    val backups = backupManager.getAllBackups()
                    Log.d("BackupViewModel", "Loaded ${backups.size} backups")
                    _viewState.update { it.copy(backups = backups, errorMessage = null) }
                } catch (e: Exception) {
                    Log.e("BackupViewModel", "Error loading backups", e)
                    _viewState.update { 
                        it.copy(
                            errorMessage = "Failed to load backups: ${e.message}",
                            backups = emptyList()
                        )
                    }
                }
            } else {
                Log.d("BackupViewModel", "User not signed in, skipping backup load")
            }
        }
    }
    
    fun refreshBackups() {
        loadBackups()
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            // For Google sign-in users, we need to navigate to onboarding and set the flag
            // to skip the Import CSV step and go directly to Currency Setup
            // We'll use shared preferences to communicate between modules
            
            // Set the flag in shared preferences
            sharedPrefs.putBoolean("is_from_google_signin", true)
            
            // Navigate to onboarding screen
            navigation.navigateTo(com.exparo.navigation.OnboardingScreen)
        }
    }
}