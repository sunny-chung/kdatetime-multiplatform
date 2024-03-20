---
title: Home
---

# KDateTime Multiplatform

![GitHub](https://img.shields.io/github/license/sunny-chung/kdatetime-multiplatform)
![Verification Test Status](https://github.com/sunny-chung/kdatetime-multiplatform/actions/workflows/run-test.yaml/badge.svg?branch=main)

![Android](https://img.shields.io/badge/Android-blue)
![JVM](https://img.shields.io/badge/JVM-blue)
![js](https://img.shields.io/badge/js-blue)
![iOS](https://img.shields.io/badge/iOS-blue)
![macOS](https://img.shields.io/badge/macOS-blue)
![watchOS](https://img.shields.io/badge/watchOS-blue)
![tvOS](https://img.shields.io/badge/tvOS-blue)

KDateTime is a Kotlin Multiplatform library to provide **regular date-time functionality needed with very minimal platform dependencies**. It means upgrading OS / platform SDK target versions or moving to another platform would not break your application. Same and consistent core API set is provided to all JVM, Apple, JS targets.

Comparing with the kotlinx one, this library additionally provides formatting and parsing from custom formatted string. As it has extremely few dependencies, it is very lightweight. The sum of all the artifacts among all the platforms, including source jars and javadocs, is less than 700 KB.

This library is going to be stable. Tests are executed against every push in the mainstream, and the status can be seen at the top of this page. All major features have been implemented, but suggestions and contributions are welcomed!

## Supported Platforms
- Android
- Non-Android JVM
- JS (Legacy compiler + IR compiler)
- iOS
- macOS
- watchOS
- tvOS

## Limitations
- KDateTime does not deal with timezones. It deals with timezone offsets. There are no DST, calendar shifting, date skipping, leap seconds (but leap years are supported), etc..
- So, only datetimes from year 1753 onwards are supported
- Only timestamps between year 1753 and 3999 are supported
- Minimum time unit is millisecond
- Only English is supported for locale-specific inputs and outputs (custom localized strings can be provided)
