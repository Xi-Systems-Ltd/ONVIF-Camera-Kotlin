import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.vanniktech.maven.publish.base) apply false
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }

    // Credentials must be added to ~/.gradle/gradle.properties per
    // https://vanniktech.github.io/gradle-maven-publish-plugin/central/#secrets
    plugins.withId("com.vanniktech.maven.publish.base") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "githubPackages"
                    url = uri("https://maven.pkg.github.com/Xi-Systems-Ltd/ONVIF-Camera-Kotlin")
                    // username and password (a personal Github access token) should be specified
                    // as gpr.user` and `gpr.key` Gradle properties or alternatively
                    // as `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables
                    credentials {
                        username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String? ?: ""
                        password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.key") as String? ?: ""
                    }
                }
            }
        }
        configure<MavenPublishBaseExtension> {
            pom {
                name.set("ONVIF Camera Kotlin")
                description.set("A Kotlin library to interact with ONVIF cameras.")
                url.set("https://github.com/Xi-Systems-Ltd/ONVIF-Camera-Kotlin/")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/Xi-Systems-Ltd/ONVIF-Camera-Kotlin/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("sproctor")
                        name.set("Sean Proctor")
                        email.set("sproctor@gmail.com")
                    }
                    developer {
                        id.set("PHernimanXiSystems")
                        name.set("Peter Herniman")
                        email.set("pete.herniman@xisystems.co.uk")
                    }
                }
                scm {
                    url.set("https://github.com/Xi-Systems-Ltd/ONVIF-Camera-Kotlin/tree/master")
                }
            }
        }
    }
}

tasks.wrapper {
    gradleVersion = "8.14.3"
}
