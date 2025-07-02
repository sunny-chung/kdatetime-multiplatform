# Maven Central Publication Steps

Last Update: 2025-07-02

1. Run the Gradle task `publishAllPublicationsToSonatypeRepository`.
2. Call the API https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/io.github.sunny-chung?publishing_type=user_managed
3. Visit Maven Central to review and approve the deployment. In particular, review the *.kotlin_module file inside *.jar files and the POM files.
