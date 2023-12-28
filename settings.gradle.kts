apply { from("flutter_settings.gradle") }
pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://repo.huaweicloud.com/repository/maven") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
//    # 三种模式解释
//    # FAIL_ON_PROJECT_REPOS → 工程或工程的插件设置了仓库，构建直接报错抛异常
//    # PREFER_PROJECT → 工程设置了仓库优先使用工程配置的，忽略settings
//    # PREFER_SETTINGS → 通过工程单独设置或插件设置的仓库，都会被忽略
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://repo.huaweicloud.com/repository/maven") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "OpenUnit"
include(":app")
include(":library:res")
include(":library:core")
include(":library:base")
include(":library:dialog")
include(":library:permission")
include(":library:router")
include(":library:recyclerview")
include(":library:image")
include(":library:serialization")
include(":library:net")

//native
include(":feature:pkg")

//compose
include(":feature:compose")

//flutter
include(":flutter_module")
project(":flutter_module").projectDir=File("../OpenUnit/feature/flutter_module")

