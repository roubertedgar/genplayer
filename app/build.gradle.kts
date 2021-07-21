plugins {
    id(Plugins.androidApplication)
    id(Plugins.Kotlin.android)
    id(Plugins.Kotlin.androidExtensions)
    id(Plugins.Kotlin.kapt)
    jacoco
}

jacoco{
    toolVersion = "0.8.4"
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug"){
            isTestCoverageEnabled = true
        }
    }
}

dependencies {

    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.coroutinesAndroid)

    implementation(Dependencies.Androidx.core)
    implementation(Dependencies.Androidx.appCompat)
    implementation(Dependencies.materialDesign)

    implementation(Dependencies.Androidx.constraintLayout)
    implementation(Dependencies.Androidx.activity)
    implementation(Dependencies.Androidx.fragment)

    implementation(Dependencies.Androidx.navigationFragment)
    implementation(Dependencies.Androidx.navigationUI)

    implementation(Dependencies.dagger)
    implementation(Dependencies.daggerAndroid)
    implementation(Dependencies.daggerSupport)
    kapt(Dependencies.daggerCompiler)
    kapt(Dependencies.daggerProcessor)

    implementation(project(":genplayer"))

    implementation(Dependencies.ExoPlayer.cast)

    androidTestImplementation(Dependencies.Test.jUnit)

    debugImplementation(Dependencies.Test.androidxCore)
    debugImplementation(Dependencies.Test.fragment)
    debugImplementation(Dependencies.Test.navigation)
    debugImplementation(Dependencies.Test.assertJ)

    testImplementation(Dependencies.Test.androidxJunit)
    testImplementation(Dependencies.Test.runner)
    testImplementation(Dependencies.Test.espresso)
    testImplementation(Dependencies.Test.robolectric)

    androidTestImplementation(Dependencies.Test.androidxJunit)
    androidTestImplementation(Dependencies.Test.navigation)
    androidTestImplementation(Dependencies.Test.runner)
    androidTestImplementation(Dependencies.Test.espresso)
    androidTestImplementation(Dependencies.Test.robolectricAnnotations)
    kaptTest(Dependencies.daggerCompiler)
}