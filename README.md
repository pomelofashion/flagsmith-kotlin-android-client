<img width="100%" src="https://github.com/Flagsmith/flagsmith/raw/main/static-files/hero.png"/>

# Flagsmith Kotlin Android Client

The SDK client for Kotlin based Android applications for [https://www.flagsmith.com/](https://www.flagsmith.com/). Flagsmith allows you to manage feature flags and remote config across multiple projects, environments and organisations.

## Adding to your project

For full documentation visit [https://docs.flagsmith.com/clients/android](https://docs.flagsmith.com/clients/android)

## Resources

- [Website](https://www.flagsmith.com/)
- [Documentation](https://docs.flagsmith.com/)
- If you have any questions about our projects you can email [support@flagsmith.com](mailto:support@flagsmith.com)

## Development

To run the integration tests and develop using this repository you'll need to set your environment key using the environment variable `ENVIRONMENT_KEY`. E.g. to run the integration tests:

```bash
ENVIRONMENT_KEY=F5X.... ./gradlew clean :FlagsmithClient:testDebugUnitTest
```

## To publish artifact to mobile-android repository
1. Change the version name, the major, minor and patch version should follow the base version of original repository we rely on
2. Run command to publish the package
```bash
./gradlew assembleRelease publish
```
