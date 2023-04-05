@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spotless)
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

spotless {
    isEnforceCheck = false
    java {
        palantirJavaFormat()
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "perceptual"
            from(components["java"])
            pom {
                name.set("Perceptual")
                description.set("A smarter volume slider scale")
                url.set("https://www.github.com/fourlastor/perceptual-gdx")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/fourlastor/perceptual-gdx/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("fourlastor")
                        name.set("Daniele Conti")
                    }
                }
                scm {
                    connection.set("scm:git:https://www.github.com/fourlastor/perceptual-gdx.git")
                    developerConnection.set("scm:git:https://www.github.com/fourlastor/perceptual-gdx.git")
                    url.set("https://www.github.com/fourlastor/perceptual-gdx")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
