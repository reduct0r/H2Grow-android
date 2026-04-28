# H2Grow Android

- H2Grow - Android-клиент.
- Сервер: [H2Grow](https://github.com/reduct0r/H2Grow)
- Назначение: управление/мониторинг/интерактив умных устройств на базе ESP32.
***
>Android-приложение используется как клиент сервера для управления устройствами: реализована регистрация и авторизация с использованием JWT, настроен сетевой слой с использованием interceptor.
В приложении реализована базовая логика взаимодействия с сервером и управление устройствами, в том числе отправка команд и получение их состояния/сводки. Настройка сценариев и таймеров.
В данный момент продолжаю развивать проект: добавляю автоматическое обнаружение устройств в локальной сети и расширяю UI для управления.
Практическая направленность проекта заключается в создании расширяемой системы для управления кастомными устройствами умного дома.
***

- Минимальные требования:
  - Android 8.0+ (API 26+)
  - Свободное место: 50 MB

  Основные экраны:
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/fac858a9-38bb-4001-aa7a-b3f55eba4442" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/0d556541-8a0e-4f5d-85bd-74658ec50f38" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/363d7669-4a8f-452b-8eed-6b751c8b071f" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/c95d2bfc-bdec-44b0-bb0f-b8af7ecf953b" width="200"/></td>
  </tr>
</table>

Поддерживаемое быстрое управление:
<table>
  <tr>
   <td><img width="359" height="324" alt="image" src="https://github.com/user-attachments/assets/a922c9f3-3c39-44ba-ae60-423dd78f4705" />
  </tr>
</table>


**Для разработчиков**
- Клонирование репозитория:
```powershell
git clone <repo-url>
cd H2Grow
```

- Настройка локальных свойств:
  - Добавьте секреты/API-ключи в защищённый файл.

- Сборка и запуск (на Windows/PowerShell):
```powershell
# Сборка debug
.\gradlew.bat assembleDebug
# Установка на подключённое устройство
.\gradlew.bat installDebug
```

- Архитектура проекта:
  - Модуль `app/` — основной Android-модуль
  - Используется: Retrofit, Hilt/Dagger,
  - Паттерн: MVVM (ViewModel + LiveData/StateFlow)

- CI / Pull Requests:
  - Создавайте ветки по фичам: `feature/<name>`
  - Открывайте PR в `develop` или `main` в соответствии с workflow.
  - В PR указывайте описание, тесты, шаги проверки, скриншоты UI (если есть изменения UI).
