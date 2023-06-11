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
    archiveFileName.set("$projectName-$version")
    finalizedBy("buyThePlugins")
}

tasks.withType<JavaCompile> {
    options.encoding = Charsets.UTF_8.name()
    options.isIncremental = true
}

tasks.register("buyThePlugins") {
    dependsOn

    doLast {
        println("If you like the CustomBot, please consider buying it!")
    }
}

tasks.test {
    useJUnitPlatform()
}