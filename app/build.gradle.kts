plugins {
    id("com.android.application")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("/home/zeon/android.key.jks")
            keyPassword = "147258.as"
            storePassword = "147258.as"
            keyAlias = "key0"
        }
    }
    namespace = "com.cppzeal.rdavatar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cppzeal.rdavatar"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0 b"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("org.apache.commons:commons-lang3:3.12.0")
    compileOnly("de.robv.android.xposed:api:82") // 这里的版本号可能会有变化
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.11.0")

//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation ("com.google.code.gson:gson:2.8.8")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}