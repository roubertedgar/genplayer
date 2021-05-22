import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

typealias App = com.android.build.gradle.AppPlugin
typealias Library = com.android.build.gradle.LibraryPlugin
typealias AndroidExtension = com.android.build.gradle.TestedExtension

buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Path.androidGradle)
        classpath(Path.kotlinGradle)
        classpath(Path.playServices)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    plugins.whenPluginAdded {
        if (this is App || this is Library) {
            extensions.findByType<AndroidExtension>()?.applyCommonConfigs()
        }
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.freeCompilerArgs += "-Xallow-result-return-type"
        kotlinOptions.jvmTarget = "1.8"
    }
}


fun AndroidExtension.applyCommonConfigs() {
    println("Applying common configs to android modules")

    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        val sharedTest = "src/sharedTest"
        getByName("test") {
            java.srcDir("$sharedTest/kotlin")
            resources.srcDirs("$sharedTest/resources")
        }
        getByName("androidTest") {
            java.srcDir("$sharedTest/kotlin")
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

//    variantFilter {
//        if (buildType.name.contains("release") || buildType.name.contains("debug")) {
//            ignore = true
//        }
//    }
}

tasks.register("clean") {
    delete(project.buildDir)
}