plugins {
    `application`
}

repositories {
    jcenter()
}

application {
    mainClassName = "com.mp4box.gui.Main"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
