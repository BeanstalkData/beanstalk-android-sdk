# Beanstalk Engage Android SDK


## Requirements

- Java 1.8+
- Android Studio 2.2+
- Android SDK 16+

## How To Add Dependency (from JitPack)

### Step 1

Add JitPack repository in your root build.gradle at the end of repositories section:

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

### Step 2

Add the dependency to relevant project module:

```groovy
dependencies {
    compile 'com.github.BeanstalkData:beanstalk-android-sdk:1.3.36'
}
```

**That's it!** The first time you request a project JitPack checks out the code, builds it and serves the build artifacts.

## Usage

Check out Sample module.

Build scripts are configured for retrieving information from properties files for security reason. Current configuration requires addition of the folder named ‘keystore’ that contains keystores for debug and release builds of sample application and corresponding properties files. Also it requires addition of ‘api.properties’ file inside library module folder with information relevant for API usage.

Structure of keystores properties files should be as following:

```
storeFile=<Path to keystore>
storePassword=<Keystore password>
keyAlias=<Key alias>
keyPassword=<Key password>
```

Structure of ‘api.properties’ file should contain following fields:

```
baseUrl=<Base URL to API>
company=<Company name>
appKey=<Application API key>
googleMapsKey=<Google Maps API key>
```

## License

Beanstalk Engage Android SDK is released under the MIT license. See LICENSE for details.
