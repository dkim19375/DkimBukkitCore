import me.dkim19375.dkimgradle.data.pom.DeveloperData
import me.dkim19375.dkimgradle.data.pom.LicenseData
import me.dkim19375.dkimgradle.data.pom.SCMData
import me.dkim19375.dkimgradle.enums.mavenAll
import me.dkim19375.dkimgradle.util.addKotlinKDocSourcesJars
import me.dkim19375.dkimgradle.util.setupJava
import me.dkim19375.dkimgradle.util.setupPublishing
import me.dkim19375.dkimgradle.util.spigotAPI

plugins {
    signing
    `java-library`
    `maven-publish`
    alias(libs.plugins.dkim.gradle)
    alias(libs.plugins.dokkatoo)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.licenser)
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.spotless)
}

group = "me.dkim19375"
version = "3.5.1"

setupJava(javaVersion = JavaVersion.VERSION_11)

repositories {
    mavenCentral()
    mavenAll()
}

dependencies {
    api(libs.dkimcore)
    api(libs.kotlin.stdlib)

    compileOnly(spigotAPI("1.8.8"))
    compileOnly(libs.placeholderapi)
    compileOnly(libs.vault.api)

    // testing libs
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.kotlin)
}

private object LibInfo {
    const val ARTIFACT_ID = "dkim-bukkit-core"
    const val DESCRIPTION = "A kotlin library used to making spigot plugins easier"
    const val VCS_USERNAME = "dkim19375"
    const val VCS_REPOSITORY = "DkimBukkitCore"
    const val VCS = "github.com/$VCS_USERNAME/$VCS_REPOSITORY"
}

val artifacts = addKotlinKDocSourcesJars()

setupPublishing(
    groupId = "io.github.dkim19375",
    artifactId = LibInfo.ARTIFACT_ID,
    description = LibInfo.DESCRIPTION,
    url = "https://${LibInfo.VCS}",
    licenses = listOf(LicenseData.MIT),
    developers = listOf(
        DeveloperData(
            id = "dkim19375",
            roles = listOf("developer"),
            timezone = "America/New_York",
            url = "https://github.com/dkim19375",
        )
    ),
    scm = SCMData.generateGit(
        username = LibInfo.VCS_USERNAME,
        repository = LibInfo.VCS_REPOSITORY,
        developerSSH = true,
    ),
    publicationName = "mavenKotlin",
    verifyMavenCentral = true,
    artifacts = artifacts.javadocJarTasks.values.map(TaskProvider<Jar>::get) + artifacts.sourcesJarTask.get(),
    setupNexusPublishing = System.getenv("GITHUB_ACTIONS") != "true",
)

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
