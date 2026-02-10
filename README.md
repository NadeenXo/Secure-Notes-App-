# 🔐 Secure Notes App (Android – Kotlin)

A simple Android app to **create, view, edit, and delete notes securely**.  
Notes are stored **locally only** and **never persisted in plaintext**.

## ✅ How to Run
1. Clone the repo
2. Open in **Android Studio**
3. Let **Gradle sync**
4. Run on an emulator/device

## 🧰 Android Version & Tools Used
- Language: **Kotlin**
- UI: **Jetpack Compose**
- Architecture: **MVVM + Repository**
- Navigation: **Navigation-Compose**
- Database: **Room**
- Dependency Injection: **Koin**
- Encryption: **AES-256 GCM** with keys stored in **Android Keystore**
- Min SDK: **26+**
- Target SDK: **Latest stable**

## 🧠 What Influenced My Decisions
- **Security-first storage**: note content is encrypted before writing to disk; keys are generated/stored in **Android Keystore** (never hardcoded / never logged).
- **Offline-only by design**: no backend, no cloud sync → smaller attack surface + matches requirements.
- **Maintainable structure**: MVVM + Repository keeps UI, business logic, and data handling separated.

## ⭐ Bonus

### Threat Model (What I’m Protecting Against)
- Plaintext note exposure via:
  - filesystem inspection
  - backup extraction
  - accidental logging of sensitive content

### Known Limitations
- Notes are decrypted **in memory** when displayed.
- No biometric/app lock layer.
- Device-level compromise (malware with runtime access) is out of scope.
