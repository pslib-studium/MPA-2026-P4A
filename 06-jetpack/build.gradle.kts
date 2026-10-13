// AGP 9 obsahuje podporu Kotlinu. Zde volíme novější KGP ze stejného katalogu
// jako Compose compiler, aby oba používaly stejnou verzi Kotlinu.
buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

// apply false pouze zpřístupní plugin; pro modul app ho zapneme v app/build.gradle.kts.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
