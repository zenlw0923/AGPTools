import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.11.2")
}

gradlePlugin {
    plugins {
        create("hidePlugin") {
            id = "com.agptools.hide"
            implementationClass = "HidePlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
            version = "1.0.0"
        }
    }
}
