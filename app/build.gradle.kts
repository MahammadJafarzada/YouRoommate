plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.kotlinKapt)
    id(Plugins.safeArgs)
}

android {
    namespace = "com.mahammadjafarzade.youroommate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mahammadjafarzade.youroommate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":features:login"))
    implementation(project(":features:register"))
    implementation(project(":features:splash"))

    implementation(Libs.NAV.navigationUI)
    implementation(Libs.NAV.navigationFragment)

    implementation(Libs.HILT.hilt)
    kapt(Libs.HILT.hiltKapt)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}