# Афиша ИТ-Полигона

Стартовый проект практикума по состояниям интерфейса на Java и Android Views.

## Запуск

1. В Android Studio откройте **SDK Manager** и установите Android SDK Platform
   37.0, Build-Tools 36.0.0 и Platform-Tools.
2. Через **File → Open** откройте папку этого репозитория.
3. В настройках Gradle выберите встроенный JDK 25 Android Studio и дождитесь
   синхронизации. Проект использует AGP 9.2.0 и Gradle Wrapper 9.4.1.
4. Выберите эмулятор или подключённое устройство и нажмите **Run**.

Для первой сборки нужен доступ к репозиториям зависимостей. Приложение
работает офлайн.

## Сборка в PowerShell

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
.\gradlew.bat :app:assembleDebug
```

Укажите свои пути, если Android Studio или SDK установлены в другой папке.
