# Word-Formed Release Process

The project is configured with a fully automated CI/CD pipeline using **GitHub Actions**.

## CI/CD Pipeline (`.github/workflows/android.yml`)

The workflow is triggered by:
- **Push to `master` branch:** Runs unit tests, builds the debug APK, and signs the release bundle (AAB) as an artifact.
- **Tag push (`v*`):** Performs all build steps AND automatically deploys the signed AAB to the **Google Play Store**.

### Automated Steps

1.  **Validation:** Runs standard Kotlin/Android unit tests via `./gradlew test`.
2.  **Build:** Compiles the release-ready App Bundle (AAB).
3.  **Signing:**
    - Decodes the Base64 `SIGNING_KEY` stored in GitHub Secrets.
    - Uses `jarsigner` (native JDK tool) to sign the AAB.
    - Sets the `signed_bundle` output for subsequent steps.
4.  **Artifact Upload:** Uploads the signed AAB to GitHub as a `signed-app-bundle` artifact.
5.  **Deployment (Tagged Releases only):**
    - Uses `r0adkll/upload-google-play` action.
    - Deploys the AAB to the **Internal Track**.
    - Sets release status to **`draft`** (to accommodate initial app draft limitations).
    - Automatically uploads release notes from `distribution/whatsnew/`.

## Manual Requirements

To enable the automated deployment, the following **GitHub Secrets** must be configured:

| Secret | Description |
| :--- | :--- |
| `SIGNING_KEY` | Base64 encoded `.jks` keystore file. |
| `ALIAS` | The alias of the signing key. |
| `KEYSTORE_PASSWORD` | Password for the keystore file. |
| `KEY_PASSWORD` | Password for the specific signing key. |
| `SERVICE_ACCOUNT_JSON` | Raw JSON content of the Google Play Service Account key. |

## Versioning Policy

-   **Version Code:** Must be incremented manually in `app/build.gradle.kts` for every release.
-   **Version Name:** Follows Semantic Versioning (e.g., `1.1.2`).
