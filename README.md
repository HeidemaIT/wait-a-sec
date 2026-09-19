# Wait a Sec

Monorepo with two Android apps that share the calm Material 3 design from Ninety Seconds.

| App | Path | Package |
| --- | --- | --- |
| **Wait a Second** | `apps/wait-a-sec/android` | `com.waitasecond.app` |
| **Learn from Silence** | `apps/learn-from-silence/android` | `com.learnfromsilence.app` |

## Run

```bash
# Wait a Second
cd apps/wait-a-sec/android && ./gradlew assembleDebug

# Learn from Silence (1-minute silence sit)
cd apps/learn-from-silence/android && ./gradlew assembleDebug
```

Debug APKs land at `app/build/outputs/apk/debug/app-debug.apk` inside each Android project.

**Learn from Silence** keeps the sit on the home screen. After each sit, smileys ask if the length was too short / enough / too long and adjust the next duration by ±30s (minimum 30s).

## CI

Each app has its own GitHub Actions pipeline (path-filtered):

- [Wait a Second — Android CI](.github/workflows/wait-a-sec-android-ci.yml)
- [Learn from Silence — Android CI](.github/workflows/learn-from-silence-android-ci.yml)
