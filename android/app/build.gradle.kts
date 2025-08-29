import java.util.Properties
import java.io.FileInputStream
import org.gradle.api.GradleException

// -----------------------------
// Cargar propiedades del keystore
// -----------------------------
val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("key.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} else {
    throw GradleException("key.properties file not found in android/ directory!")
}

// Verificar keystore
val storeFilePath = keystoreProperties["storeFile"] as String?
println("Keystore path from key.properties: $storeFilePath")
val storeFileExists = storeFilePath?.let { file(it).exists() } ?: false
println("Does keystore file exist? $storeFileExists")
if (!storeFileExists) {
    throw GradleException("Keystore file not found. Check the path in key.properties!")
}

// -----------------------------
// Plugins
// -----------------------------
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
    id("com.google.gms.google-services") // debe ir al final
}

// -----------------------------
// Android configuration
// -----------------------------
android {
    namespace = "com.microbyte.adonay_driver"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    defaultConfig {
        applicationId = "com.microbyte.adonay_driver"
        minSdk = 23
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String?
            keyPassword = keystoreProperties["keyPassword"] as String?
            storePassword = keystoreProperties["storePassword"] as String?
            storeFile = storeFilePath?.let { file(it) } ?: throw GradleException(
                "Keystore file not found. Check key.properties path!"
            )
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

// -----------------------------
// Dependencies
// -----------------------------
dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}

// -----------------------------
// Flutter source
// -----------------------------
flutter {
    source = "../.."
}

// -----------------------------
// Fix Deep Links AAB issue
// -----------------------------
tasks.whenTaskAdded {
    if (name.contains("extractDeepLinksRelease")) {
        dependsOn("processReleaseGoogleServices")
    }
}
