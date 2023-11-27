// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}
true // Needed to make the Suppress annotation work for the plugins block


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

