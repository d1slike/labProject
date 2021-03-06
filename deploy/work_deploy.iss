; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{0A458551-D515-4A5E-BB67-3FB8258B5DAE}
AppName=RCalc
AppVersion=1.0.0
;AppVerName=RCalc 1.0.0
AppPublisher=Yan Comissarov
AppPublisherURL=www.stankin.ru
AppSupportURL=eldustru@yandedx.ru
AppUpdatesURL=www.stankin.ru
DefaultDirName={pf}\RCalc
DefaultGroupName=RCalc
AllowNoIcons=yes
OutputDir=C:\Workspace\labProject\deploy
OutputBaseFilename=RCalc_RC1_x86
SetupIconFile=C:\Workspace\labProject\deploy\package\windows\RCalc.ico
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "russian"; MessagesFile: "compiler:Languages\Russian.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Workspace\labProject\target\jfx\native\RCalc\RCalc.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Workspace\labProject\target\jfx\native\RCalc\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\RCalc"; Filename: "{app}\RCalc.exe"
Name: "{group}\{cm:UninstallProgram,RCalc}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\RCalc"; Filename: "{app}\RCalc.exe"; Tasks: desktopicon

[Run]
Filename: "{app}\RCalc.exe"; Description: "{cm:LaunchProgram,RCalc}"; Flags: nowait postinstall skipifsilent

