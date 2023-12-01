// Top-level build file where you can add configuration options common to all sub-projects/modules.
apply { from("flutter_build.gradle") }
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false


}
true

// 对app 模块配置
gradle.rootProject {
    buildDir =  File(rootDir, "build-gradle/${path.replace(':', '/')}")
}


// 对子模块进行配置
subprojects{
    beforeEvaluate{
        buildDir =  File(rootDir, "build-gradle/${path.replace(':', '/')}")
    }

}

