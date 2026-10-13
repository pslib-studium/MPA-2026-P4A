plugins {
    alias(libs.plugins.android.application)
    // Kotlin je součástí AGP 9, Compose navíc potřebuje svůj compiler plugin.
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "cz.pslib.jetpack"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "cz.pslib.jetpack"
        minSdk = 30 // Nejstarší podporovaný systém: Android 11.
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        // Cílový bytecode aplikace je Java 11; Gradle přitom běží na JVM 25.
        // Vestavěný Kotlin přebírá JVM target z targetCompatibility.
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true // Zapne podporu Compose také v Android Studiu.
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // BOM sjednotí verze Compose knihoven. Samotné knihovny musíme přidat zvlášť.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
