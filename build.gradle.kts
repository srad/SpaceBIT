// build.gradle.kts
// This file modernizes the old Groovy build.gradle to Kotlin DSL (build.gradle.kts)
// and updates the Kotlin version to 1.9.23 (latest stable as of this update).

plugins {
    // Apply standard Gradle plugins
    java // Provides Java compilation and packaging capabilities
    application // Provides tasks for building and running a JVM application
    idea // Provides tasks to generate IntelliJ IDEA project files
    // Apply the Kotlin JVM plugin with the specified version
    kotlin("jvm") version "2.2.0" // Specifies Kotlin version for the plugin and stdlib
}

// Define the group for your project's published artifacts
group = "com.github.srad.spacebit"
// Define the version of your project
version = "1.0-SNAPSHOT" // You might want to update this for releases

// Configure Java compilation settings
java {
    // Set the Java language version for compilation and runtime
    // This replaces sourceCompatibility and targetCompatibility for Java 11 and above
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

// Configure common options for Java compilation tasks
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8" // Ensure consistent encoding for Java source files
}

// Configure Kotlin compilation tasks
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    // Migrate from deprecated 'kotlinOptions.jvmTarget' to 'compilerOptions.jvmTarget' DSL
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24) // Set the JVM target version for Kotlin compiled bytecode
    }
}

// Define repositories where Gradle should look for dependencies
repositories {
    mavenCentral() // The primary repository for open-source libraries
    maven("https://jitpack.io") // JME often uses JitPack for some dependencies
    maven("https://dl.bintray.com/jmonkeyengine/maven/")
    // jcenter() is deprecated and has been removed.
    // If you still need the jmonkeyengine specific repository, uncomment and verify:
    // maven { url = uri("http://dl.bintray.com/jmonkeyengine/org.jmonkeyengine") }
}

// Define a constant for JMonkeyEngine version for easy management
val jme3Version = "3.2.4-stable"
// Define a constant for JMonkeyEngine group for easy management
val jme3Group = "org.jmonkeyengine"

// Configure the application plugin
application {
    // Specify the fully qualified name of the main class to run
    mainClass.set("com.github.srad.spacebit.main.SpaceBit")
}

// Configure source sets to define where source code and resources are located
sourceSets {
    main {
        java {
            srcDir("src") // Add 'src' as a source directory for Java files
        }
        resources {
            srcDirs("assets", "src") // Add 'assets' and 'src' for resource files
            exclude("*.blend") // Exclude .blend files from resources
        }
    }
}

// Define project dependencies
dependencies {
    // Kotlin Standard Library dependencies
    // 'implementation' is preferred over 'compile' for better build performance
    implementation(kotlin("stdlib-jdk8")) // Kotlin standard library for JDK 8+
    implementation(kotlin("reflect")) // Kotlin reflection library

    // Logging dependency
    implementation("org.apache.logging.log4j:log4j-core:2.13.0")

    // JMonkeyEngine dependencies
    // 'implementation' for compile-time and runtime dependencies
    // 'runtimeOnly' for dependencies only needed at runtime
    implementation("$jme3Group:jme3-core:$jme3Version")
    implementation("$jme3Group:jme3-effects:$jme3Version")
    implementation("$jme3Group:jme3-bullet:$jme3Version")
    implementation("$jme3Group:jme3-bullet-native:$jme3Version")
    implementation("$jme3Group:jme3-jogg:$jme3Version")
    runtimeOnly("$jme3Group:jme3-desktop:$jme3Version")
    runtimeOnly("$jme3Group:jme3-lwjgl:$jme3Version")
}
