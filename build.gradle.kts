plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ua.mani123"
version = findProperty("version")!!
var projectName = findProperty("projectName")

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")


    //implementation("com.electronwill.night-config:toml:3.6.6")

    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("net.dv8tion:JDA:5.0.0-beta.10")

    implementation(files(fileTree("lib") {
        include("*.jar")
    }))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.shadowJar {
    dependsOn
    manifest {
        attributes(
                mapOf(
                        "Implementation-Title" to projectName,
                        "Implementation-Version" to version,
                        "Main-Class" to "ua.mani123.CustomBot"
                )
        )
    }
    project.setProperty("mainClassName", "ua.mani123.CustomBot")
    archiveFileName.set("$projectName-$version.jar")
    finalizedBy("buyThePlugins")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = Charsets.UTF_8.name()
    options.isIncremental = true
}

tasks.register("buyThePlugins") {

    doLast {
        println("If you like the CustomBot, please consider buying or support it!")
    }
}

tasks.test {
    useJUnitPlatform()
}