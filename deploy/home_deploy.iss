; Скрипт создан при помощи мастера создания скриптов.
; СМ. ДОКУМЕНТАЦИЮ ДЛЯ ИЗУЧЕНИЯ ДЕТАЛЕЙ ОТНОСИТЕЛЬНО СОЗДАНИЯ ФАЙЛОВ СКРИПТА INNO SETUP!

#define MyAppName "RCalc"
#define MyAppVerName "RCalc 1.0.0"
#define MyAppPublisher "Yan Comissarov"
#define MyAppURL "eldustru@yandex.ru"
#define MyAppExeName "RCalc.exe"

[Setup]
AppName={#MyAppName}
AppVerName={#MyAppVerName}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
OutputDir=C:\Work\LabProjectReleased\deploy
OutputBaseFilename=RCalc_Setup
SetupIconFile=C:\Work\LabProjectReleased\target\jfx\native\RCalc\RCalc.ico
Compression=lzma
SolidCompression=yes

[Languages]
Name: "russian"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Work\LabProjectReleased\target\jfx\native\RCalc\RCalc.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Work\LabProjectReleased\target\jfx\native\RCalc\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; ОТМЕТЬТЕ: Не используйте "Флажки: Проигнорировать версию" на любых общедоступных системных файлах

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#MyAppName}}"; Flags: nowait postinstall skipifsilent

