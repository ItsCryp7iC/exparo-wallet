-dontwarn java.beans.**
-dontwarn javax.script.**
-dontwarn javax.servlet.**
-dontwarn org.apache.**
-dontwarn coil.**
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn com.google.errorprone.**
-dontwarn org.slf4j.impl.**

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-dontwarn io.grpc.InternalGlobalInterceptors

# JVM management classes not available on Android
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean

# Keep all classes in the drive-backup module to prevent obfuscation issues
-keep class com.exparo.drivebackup.** { *; }

# Keep Hilt WorkManager classes
-keep class dagger.hilt.android.internal.managers.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.CoroutineWorker

# Keep WorkManager and Hilt Worker related classes
-keep class androidx.work.** { *; }
-keep class androidx.hilt.work.** { *; }

# Keep Google Drive API classes
-keep class com.google.api.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Google API Client classes
-keep class com.google.api.client.** { *; }
-keep class com.google.api.client.googleapis.** { *; }
-keep class com.google.api.client.extensions.** { *; }
-keep class com.google.api.client.http.** { *; }
-keep class com.google.api.client.json.** { *; }
-keep class com.google.api.client.util.** { *; }

# Keep Google HTTP Client classes
-keep class com.google.http.client.** { *; }

# Keep Google Auth and Credential classes
-keep class com.google.auth.** { *; }
-keep class com.google.common.** { *; }

# Keep Google Drive Service specific classes
-keep class com.google.api.services.drive.** { *; }

# Keep JSON Factory and Transport classes
-keep class com.google.api.client.json.gson.** { *; }
-keep class com.google.api.client.extensions.android.** { *; }

# Keep OAuth2 and credential related classes
-keep class com.google.api.client.googleapis.extensions.android.gms.auth.** { *; }

# Keep Room classes for drive-backup
-keep class com.exparo.drivebackup.data.** { *; }

# Prevent obfuscation of classes that use @AssistedInject
-keep class * {
    @dagger.assisted.AssistedInject <init>(...);
}

# Keep all classes that extend CoroutineWorker or Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

# Keep Hilt EntryPoint interfaces  
-keep interface * extends dagger.hilt.EntryPoint

# Additional Hilt rules for release builds
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }

# Keep Jackson and JSON serialization classes
-keep class com.fasterxml.jackson.** { *; }
-keep class org.codehaus.jackson.** { *; }

# Keep Gson classes
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep reflection-based classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep all constructors for API classes
-keepclassmembers class com.google.api.** {
    <init>(...);
}

# Keep all constructors for HTTP client classes
-keepclassmembers class com.google.api.client.** {
    <init>(...);
}

# Keep specific Google Drive API classes used in GoogleDriveService
-keep class com.google.api.services.drive.** { *; }
-keep class com.google.api.services.drive.model.** { *; }
-keep class com.google.api.client.extensions.android.http.** { *; }
-keep class com.google.api.client.googleapis.extensions.android.gms.auth.** { *; }
-keep class com.google.api.client.json.gson.** { *; }

# Keep Google Sign-In classes
-keep class com.google.android.gms.auth.api.signin.** { *; }
-keep class com.google.android.gms.common.api.** { *; }

# Keep HTTP transport classes
-keep class com.google.api.client.http.** { *; }
-keep class com.google.http.client.** { *; }

# Keep credential and authentication classes
-keep class com.google.auth.** { *; }
-keep class com.google.api.client.googleapis.auth.** { *; }

# Keep serialization/deserialization classes
-keep class com.google.api.client.util.** { *; }
-keep class com.google.api.client.json.** { *; }

# Keep specific classes for File operations
-keep class com.google.api.services.drive.model.File { *; }
-keep class com.google.api.services.drive.model.FileList { *; }
-keep class com.google.api.client.http.FileContent { *; }

# Keep Google Play Services classes
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

# Additional rules for reflection-based APIs
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}

# Keep OAuth2 and credential related classes
-keep class com.google.api.client.googleapis.extensions.** { *; }
-keep class com.google.api.client.auth.** { *; }

# Keep transport and JSON factory classes
-keep class com.google.api.client.extensions.android.http.AndroidHttp { *; }
-keep class com.google.api.client.json.gson.GsonFactory { *; }

# Prevent obfuscation of classes that extend abstract classes
-keep class * extends com.google.api.client.util.GenericData { *; }
-keep class * extends com.google.api.client.json.GenericJson { *; }