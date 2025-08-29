package com.exparo.drivebackup.service

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // This will hold our active Drive service instance
    private var driveService: Drive? = null

    // This function will now be called right after a successful sign-in
    fun initializeDriveService(account: GoogleSignInAccount) {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_APPDATA)
        )
        credential.selectedAccount = account.account
        driveService = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        ).setApplicationName("Exparo Wallet").build()
    }

    fun getDriveService(): Drive? {
        // If the service isn't initialized, try to initialize it with the last signed-in account
        if (driveService == null) {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account != null) {
                initializeDriveService(account)
            }
        }
        return driveService
    }

    fun getSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        return client.signInIntent
    }

    suspend fun handleSignInResult(account: GoogleSignInAccount): Boolean {
        // Check if the user has granted the required Drive scope
        val hasDriveScope = GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))
        if (!hasDriveScope) {
            // Request the missing scope
            val signInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
            val signInIntent = signInClient.signInIntent
            // Note: This would need to be handled in the UI to show the scope request dialog
            return false
        }
        
        initializeDriveService(account)
        return true
    }

    fun getCurrentUserId(): String? {
        return GoogleSignIn.getLastSignedInAccount(context)?.id
    }

    fun isSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null && hasRequiredScopes(account)
    }
    
    fun hasRequiredScopes(account: GoogleSignInAccount): Boolean {
        return GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))
    }
    
    fun getCurrentAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
    
    fun getCurrentUserName(): String? {
        val account = getCurrentAccount()
        return when {
            account?.displayName?.isNotBlank() == true -> account.displayName
            account?.givenName?.isNotBlank() == true && account.familyName?.isNotBlank() == true -> 
                "${account.givenName} ${account.familyName}"
            account?.givenName?.isNotBlank() == true -> account.givenName
            account?.familyName?.isNotBlank() == true -> account.familyName
            else -> null
        }
    }

    fun signOut() {
        // When signing out, clear our stored Drive service instance
        driveService = null
        val signInClient = GoogleSignIn.getClient(context, com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
        signInClient.signOut()
    }
}