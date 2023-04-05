@file:Suppress("UnstableApiUsage")

include(":perceptual")

dependencyResolutionManagement {
    versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }
}
