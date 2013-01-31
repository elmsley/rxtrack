; rxtrack.nsi
;
; This script is based on example1.nsi, but it remember the directory, 
; has uninstall support and (optionally) installs start menu shortcuts.
;
; It will install rxtrack.nsi into a directory that the user selects,

;--------------------------------

; The name of the installer
Name "RxTrackInstaller"

; The file to write
OutFile "RxTrack3.0Installer.exe"

; The default installation directory
InstallDir $LOCALAPPDATA\RxTrack

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKCU "Software\RxTrack" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel user

!define WEBSITE_LINK "https://github.com/elmsley/rxtrack"

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The stuff to install
Section "RxTrack (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File /r E:\RxTrack\*.*
  
  ; Write the installation path into the registry
  WriteRegStr HKCU SOFTWARE\NSIS_RxTrack "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
 !define REG_UNINSTALL "Software\Microsoft\Windows\CurrentVersion\Uninstall\RxTrack"
  WriteRegStr HKCU "${REG_UNINSTALL}" "DisplayName" "RxTrack"
  WriteRegStr HKCU "${REG_UNINSTALL}" "DisplayIcon" "$\"$INSTDIR\RxTrack.exe$\""
  WriteRegStr HKCU "${REG_UNINSTALL}" "Publisher" "AJ Solutions"
  WriteRegStr HKCU "${REG_UNINSTALL}" "DisplayVersion" "3.0"
  WriteRegStr HKCU "${REG_UNINSTALL}" "HelpLink" "${WEBSITE_LINK}"
  WriteRegStr HKCU "${REG_UNINSTALL}" "URLInfoAbout" "${WEBSITE_LINK}"
  WriteRegStr HKCU "${REG_UNINSTALL}" "InstallLocation" "$\"$INSTDIR$\""
  WriteRegStr HKCU "${REG_UNINSTALL}" "InstallSource" "$\"$EXEDIR$\""
  WriteRegStr HKCU "${REG_UNINSTALL}" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWord HKCU "${REG_UNINSTALL}" "EstimatedSize" 30000 ;KB
  WriteRegDWORD HKCU "${REG_UNINSTALL}" "NoModify" 1
  WriteRegDWORD HKCU "${REG_UNINSTALL}" "NoRepair" 1
  WriteRegStr HKCU "${REG_UNINSTALL}" "Comments" "Uninstalls RxTrack."
   WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\RxTrack"
  CreateShortCut "$SMPROGRAMS\RxTrack\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\RxTrack\RxTrack.lnk" "$INSTDIR\RxTrack.exe" "" "$INSTDIR\RxTrack.exe" 0
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKCU "${REG_UNINSTALL}"
  DeleteRegKey HKCU SOFTWARE\NSIS_RxTrack

  ; Remove files and uninstaller
  Delete $INSTDIR\RxTrack.nsi
  Delete $INSTDIR\uninstall.exe

  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\RxTrack\*.*"

  ; Remove directories used
  RMDir "$SMPROGRAMS\RxTrack"
  RMDir "$INSTDIR"
  
SectionEnd
