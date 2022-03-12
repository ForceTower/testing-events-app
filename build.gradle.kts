buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.1")
    }
}

plugins {
    id("com.diffplug.spotless") version "6.3.0"
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        val ktLintVersion = "0.44.0"
        kotlin {
            target("**/*.kt")
            ktlint(ktLintVersion).userData(
                mapOf(
                    "disabled_rules" to "import-ordering"
                )
            )
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(ktLintVersion)
        }
    }

    tasks.whenTaskAdded {
        if (name == "preBuild") {
            mustRunAfter("spotlessCheck")
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = "1.8"
        }
    }
}