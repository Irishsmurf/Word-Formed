# Manual Release Playbook

This playbook provides instructions for manually building, signing, and deploying a release of Word-Formed when the CI/CD pipeline is unavailable or for troubleshooting purposes.

## 📋 Prerequisites

- **Android Studio** (with SDK and Build Tools installed).
- **Keystore file** (`.jks`) and credentials.
- **Google Play Console** access.
- **Java Development Kit (JDK) 17+**.

---

## 🛠 Step 1: Prepare the Version

1. Open `app/build.gradle.kts`.
2. Increment the `versionCode` (must be higher than the current version in Play Store).
3. Update the `versionName` as appropriate.
4. Update `distribution/whatsnew/whatsnew-en-US` with your release notes.

---

## 🏗 Step 2: Build the Release Bundle

From the root of the project, run:

```bash
./gradlew bundleRelease
```

The output AAB will be located at:
`app/build/outputs/bundle/release/app-release.aab`

---

## ✍️ Step 3: Sign the Bundle

You can sign the bundle manually using `jarsigner`:

```bash
jarsigner -keystore <path_to_keystore.jks> \
  -storepass <keystore_password> \
  -keypass <key_password> \
  app/build/outputs/bundle/release/app-release.aab \
  <key_alias>
```

Alternatively, you can configure your `signingConfigs` in `app/build.gradle.kts` and use:
```bash
./gradlew bundleRelease
```
(If configured correctly, the output will already be signed).

---

## 🚀 Step 4: Manual Upload to Play Store

1. Log in to the [Google Play Console](https://play.google.com/console).
2. Select **Word-Formed**.
3. Go to **Testing** -> **Internal testing**.
4. Click **Create new release**.
5. Upload the signed `app-release.aab` file.
6. Copy the contents of `distribution/whatsnew/whatsnew-en-US` into the release notes section.
7. Click **Save** and then **Review release**.
8. Finally, click **Start rollout to Internal testing**.

---

## 🏁 Step 5: Post-Release

1. Tag the release in Git:
   ```bash
   git tag -a v1.x.x -m "Release v1.x.x"
   git push origin v1.x.x
   ```
2. Monitor crash reports and user feedback in the Play Console.
