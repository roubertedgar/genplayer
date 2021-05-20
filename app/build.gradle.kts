plugins {
    id(Plugins.androidApplication)
    id(Plugins.Kotlin.android)
    id(Plugins.Kotlin.androidExtensions)
    id(Plugins.Kotlin.kapt)
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

    debugImplementation("androidx.fragment:fragment-testing:1.4.0-alpha01")
    debugImplementation("androidx.test:core:1.4.0-beta01")

    testImplementation("androidx.test:core:1.4.0-beta01")
    testImplementation("androidx.test.ext:junit:1.1.3-beta01")
    testImplementation("androidx.test:runner:1.4.0-beta01")
    testImplementation("androidx.test.espresso:espresso-core:3.4.0-beta01")
    testImplementation("org.robolectric:annotations:4.5.1")
    testImplementation(Dependencies.Test.robolectric)

    androidTestImplementation("androidx.test:core:1.4.0-beta01")
    androidTestImplementation("androidx.test.ext:junit:1.1.3-beta01")
    androidTestImplementation("androidx.test:runner:1.4.0-beta01")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0-beta01")
    androidTestImplementation("org.robolectric:annotations:4.5.1")
    kaptTest(Dependencies.daggerCompiler)
}