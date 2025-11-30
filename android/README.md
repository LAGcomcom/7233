手动打包APK指南

Android Studio方式：
- 打开 `android/` 目录，等待 Gradle 同步
- 依次点击 `Build > Build Bundle(s)/APK(s) > Build APK(s)`
- 输出路径：`android/app/build/outputs/apk/debug/app-debug.apk` 或 `.../release/app-release.apk`

命令行方式：
- 若存在 Wrapper：
  - `cd android`
  - `.\u0067`  
  - `.\u0067 :app:assembleDebug` 或 `:app:assembleRelease`
- 若无 Wrapper：安装 Gradle 后执行：
  - `gradle :app:assembleDebug`

PowerShell脚本：
- `scripts/build-android.ps1` 默认生成 Debug 包；加入 `-Release` 生成 Release 包：
  - `powershell -ExecutionPolicy Bypass -File scripts/build-android.ps1`
  - `powershell -ExecutionPolicy Bypass -File scripts/build-android.ps1 -Release`
