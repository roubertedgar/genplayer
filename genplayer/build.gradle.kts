plugins {
    id(Plugins.androidLibrary)
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
    implementation(Dependencies.Androidx.constraintLayout)
    implementation(Dependencies.materialDesign)

    implementation("androidx.media:media:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    implementation(Dependencies.dagger)
    implementation(Dependencies.daggerAndroid)
    implementation(Dependencies.daggerSupport)
    kapt(Dependencies.daggerCompiler)
    kapt(Dependencies.daggerProcessor)

    implementation(Dependencies.ExoPlayer.core)
    implementation(Dependencies.ExoPlayer.ui)
    implementation(Dependencies.ExoPlayer.hls)
    implementation(Dependencies.ExoPlayer.dash)
    implementation(Dependencies.ExoPlayer.okhttp)
    implementation(Dependencies.ExoPlayer.mediaSession)
    implementation(Dependencies.ExoPlayer.cast)


    testImplementation(Dependencies.Test.jUnit)
    testImplementation(Dependencies.Test.androidxJunit)
    testImplementation(Dependencies.Test.robolectric)
    testImplementation(Dependencies.Test.mockitoInline)
    testImplementation(Dependencies.Test.mockitoKotlin)
    testImplementation("io.mockk:mockk:1.10.2")
    testImplementation(Dependencies.Test.assertJ)
    androidTestImplementation(Dependencies.Test.espresso)
}