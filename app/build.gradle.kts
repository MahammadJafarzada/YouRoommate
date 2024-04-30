plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.kotlinKapt)
    id(Plugins.parcelize)
    id(Plugins.safeArgs)
    id("com.google.gms.google-services")
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
    signingConfigs{
        getByName("debug") {
            storeFile =
                file("C:\\Users\\Orkhan\\AndroidStudioProjects\\YouRoommate\\certficates\\DebugKeyStore.jks")
            storePassword = "room159"
            keyAlias = "key0"
            keyPassword = "room159"
        }
        create("release"){
            keyAlias = "key0"
            keyPassword = "room159"
            storeFile = file("../certficates/ReleaseKeyStore.jks")
            storePassword = "room159"
        }
        create("dev"){
            keyAlias = "key0"
            keyPassword = "room159"
            storeFile = file("../certficates/DebugKeyStore.jks")
            storePassword = "room159"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("dev")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(project(":features:login"))
    implementation(project(":features:register"))
    implementation(project(":features:splash"))
    implementation(project(":features:account"))
    implementation(project(":features:favourite"))
    implementation(project(":features:homeScreen"))
    implementation(project(":features:chat"))
    implementation(project(":features:addCard"))


    implementation(Libs.Splash.splash)

    implementation(Libs.NAV.navigationUI)
    implementation(Libs.NAV.navigationFragment)

    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    implementation("com.facebook.android:facebook-login:latest.release")
    implementation("com.facebook.android:facebook-android-sdk:15.1.0")


    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation(Libs.HILT.hilt)
    implementation(libs.firebase.database)
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