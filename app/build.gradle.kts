plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	id("com.google.gms.google-services")
	id("kotlin-kapt")
	id("dagger.hilt.android.plugin")
}

android {
	namespace = "net.medinnovatelabs.applecare"
	compileSdk = 34

	defaultConfig {
		applicationId = "net.medinnovatelabs.applecare"
		minSdk = 27
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
		mlModelBinding = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	// Add the dependency for the Google Play services library
	implementation("com.google.android.gms:play-services-auth:21.0.0")

	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)
	implementation(libs.navigation.compose)
	//Camera
	implementation("androidx.camera:camera-camera2:1.3.2")
	implementation("androidx.camera:camera-core:1.3.1")
	implementation("androidx.camera:camera-lifecycle:1.3.1")
	implementation("androidx.camera:camera-view:1.3.1")
	implementation("androidx.camera:camera-extensions:1.3.1")
	implementation("io.github.microutils:kotlin-logging:2.0.6")



	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	//	implementation(libs.androidx.material3)
	implementation("androidx.compose.material3:material3")
	implementation("io.coil-kt:coil-compose:2.6.0")
	implementation("androidx.compose.material3:material3-window-size-class:1.2.0")

	//Lottie
	implementation("com.airbnb.android:lottie-compose:6.0.0")
	implementation("androidx.core:core-splashscreen:1.0.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
	implementation ("io.insert-koin:koin-core:3.2.0")// or latest version
	implementation ("io.insert-koin:koin-android:3.2.0")

//	implementation(libs.firebase.auth)
	implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
	implementation("com.google.firebase:firebase-analytics")
	implementation(libs.tensorflow.lite.support)
	implementation(libs.tensorflow.lite.metadata)
	implementation(libs.tensorflow.lite.gpu)
	//Dagger - Hilt
	implementation("com.google.dagger:hilt-android:2.48")
	implementation(libs.firebase.auth.ktx)
	implementation(libs.firebase.auth)
	implementation(libs.androidx.lifecycle.runtime.compose)
	kapt ("com.google.dagger:hilt-android-compiler:2.48")

	implementation("androidx.hilt:hilt-work:1.2.0")
	kapt("androidx.hilt:hilt-compiler:1.2.0")
	implementation("androidx.work:work-runtime-ktx:2.8.1")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")
	implementation("androidx.compose.material:material-icons-extended:1.5.4")
	implementation ("androidx.compose.material:material:1.6.2")
	implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
	implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}
kapt {
	correctErrorTypes = true
}