# Income Tracker — Android App

Track your monthly income across two workplaces, with individual shifts by date.

## Features
- **Workplace A** — variable hourly rate: ₪40 / ₪60 / ₪80 (choose per shift)
- **Workplace B** — fixed rate ₪40/h
- Add shifts with a date, hours worked, and an optional note
- Month-by-month navigation (← →)
- Live summary: per-workplace totals + grand total
- Delete any shift with a tap
- Data stored locally on your phone (Room/SQLite)

---

## How to build & install

### Option 1 — Android Studio (easiest)
1. Download and install [Android Studio](https://developer.android.com/studio)
2. Open Android Studio → **Open** → select the `IncomeTracker` folder
3. Wait for Gradle sync to finish (first time downloads dependencies ~2 min)
4. Plug in your Android phone via USB (enable USB debugging in Developer Options)
5. Click the green **▶ Run** button — the app installs and launches on your phone

### Option 2 — Command line
```bash
# Make sure ANDROID_HOME is set (e.g. ~/Android/Sdk)
export ANDROID_HOME=~/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools

cd IncomeTracker
./gradlew assembleDebug

# APK will be at:
# app/build/outputs/apk/debug/app-debug.apk

# Install on connected phone:
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Option 3 — Copy APK after building
After building with either method above, copy `app-debug.apk` to your phone
and open it to install (you may need to enable "Install from unknown sources" in Settings).

---

## Customizing workplace names
Edit `app/src/main/res/values/strings.xml`:
```xml
<string-array name="workplaces">
    <item>Hospital</item>
    <item>Clinic</item>
</string-array>
```

## Changing tariff options
Edit `AddShiftActivity.kt` — the `RadioGroup` with `rb40`, `rb60`, `rb80`.
