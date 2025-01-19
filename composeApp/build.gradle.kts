import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.ksp)

    kotlin("plugin.serialization") version "1.9.22"
}

kotlin {
    sourceSets {
        all {
            languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
        }
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.navigation.compose)
            implementation(libs.koin.core)
            //implementation(libs.datastore)
            //implementation(libs.datastore.preferences)
            implementation(libs.kotlinx.serialization.json)
//            implementation(libs.androidx.ui.tooling)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    task("testClasses")
}

dependencies {
    ksp(libs.room.compiler)
}

android {
    namespace = "com.github.catomon.yukinotes"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.catomon.yukinotes"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 4
        versionName = "1.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.github.catomon.yukinotes.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi) //TargetFormat.Dmg, TargetFormat.Deb
            packageName = "YukiNotes"
            packageVersion = "1.3.0"

            //modules("java.compiler", "java.instrument", "java.naming", "java.scripting", "java.security.jgss", "java.sql", "jdk.management", "jdk.unsupported")

            buildTypes.release.proguard {
                configurationFiles.from(project.file("compose-desktop.pro"))
                isEnabled = false
            }

            windows {
                iconFile.set(project.file("app.ico"))
                shortcut = true
            }
        }
    }
}
