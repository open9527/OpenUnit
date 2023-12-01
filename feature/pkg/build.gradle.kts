@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.open.pkg"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        buildConfigField("String", "HOST_URL", "\"https://www.wanandroid.com/\"")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    api(libs.appcompat)
    api(libs.bundles.ktx)
    api(libs.bundles.ui)
    api(libs.bundles.coil)
    api(libs.bundles.serialization)
    api(libs.bundles.net)
    api(libs.bundles.refresh)
    api(project(path = ":library:core"))
    api(project(path = ":library:base"))
    api(project(path = ":library:dialog"))
    api(project(path = ":library:permission"))
    api(project(path = ":library:router"))
    api(project(path = ":library:recyclerview"))
    api(project(path = ":library:image"))
    api(project(path = ":library:serialization"))
    api(project(path = ":library:net"))
}