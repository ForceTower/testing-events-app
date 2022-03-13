plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "dev.forcetower.events"
        minSdk = 19
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"
        multiDexEnabled = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }

        testInstrumentationRunner = "dev.forcetower.events.EventAppRunner"
    }
    flavorDimensions += "api"
    productFlavors {
        val verCode = android.defaultConfig.versionCode ?: 0
        create("minApi19") {
            dimension = "api"
            minSdk = 19
            versionCode = 190000 + verCode
            versionNameSuffix = "-minApi19"
        }
        create("minApi21") {
            dimension = "api"
            minSdk = 21
            versionCode = 210000 + verCode
            versionNameSuffix = "-minApi21"
        }
    }

    signingConfigs {
        create("release") {
            var password = System.getenv("KEYSTORE_PASSWORD")
            if (password == null)
                password = "android"

            var alias = System.getenv("KEYSTORE_ALIAS")
            if (alias == null)
                alias = "androiddebugkey"

            var keyPass = System.getenv("KEYSTORE_KEY_PASSWORD")
            if (keyPass == null)
                keyPass = "android"

            var signFile = rootProject.file("signing.jks")
            if (!signFile.exists())
                signFile = rootProject.file("debug.keystore")

            storeFile = signFile
            storePassword = password
            keyAlias = alias
            keyPassword = keyPass
        }
        getByName("debug") {
            val password = "android"
            val alias = "androiddebugkey"
            val keyPass = "android"
            val signFile = rootProject.file("debug.keystore")

            storeFile = signFile
            storePassword = password
            keyAlias = alias
            keyPassword = keyPass
        }
    }

    lint {
        checkReleaseBuilds = false
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        dataBinding = true
    }
    kapt {
        correctErrorTypes = true
        javacOptions {
            option("-Xmaxerrs", 1000)
        }
    }
    sourceSets {
        getByName("test").java.srcDirs("src/test-common/java")
        getByName("androidTest").java.srcDirs("src/test-common/java")
    }
}

val minApi21Implementation by configurations
val minApi19Implementation by configurations

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    // Some colors are marked as private, but they're public
    // https://github.com/material-components/material-components-android/issues/2538
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.1")

    implementation("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")

    minApi19Implementation("com.squareup.okhttp3:okhttp") {
        version { strictly("3.12.1") }
    }
    minApi21Implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.google.dagger:hilt-android:2.41")
    kapt("com.google.dagger:hilt-android-compiler:2.41")

    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    implementation("com.github.bumptech.glide:glide:4.13.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")

    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.41")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.41")
}
