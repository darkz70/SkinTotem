pluginManagement {
    repositories {
        maven { name = "NeoForge"; url = uri("https://maven.neoforged.net/releases") }
        maven { name = "Kikugie Snapshots"; url = uri("https://maven.kikugie.dev/snapshots") }
        maven { name = "Kikugie Releases"; url = uri("https://maven.kikugie.dev/releases") }
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

stonecutter {
    shared {
        fun mc(version: String, vararg paths: String) =
            vers(version, *paths)

        mc("1.20.1", "versions/1.20.1")
        mc("1.21",   "versions/1.21")
        mc("1.21.1", "versions/1.21.1")
        mc("1.21.4", "versions/1.21.4")
        mc("1.21.5", "versions/1.21.5")
        mc("1.21.6", "versions/1.21.6")
        mc("1.21.7", "versions/1.21.7")
        mc("1.21.8", "versions/1.21.8")
        mc("1.21.9", "versions/1.21.9")
        mc("1.21.10","versions/1.21.10")
        mc("1.21.11","versions/1.21.11")
    }
    centralScript = "build.gradle"
    automaticPlatformConstants = true
}
