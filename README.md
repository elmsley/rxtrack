RxTrack
=======

> Lightweight Drug Dosage Software



Installation:
-------------

1.  Ensure you have Microsoft Excel (2003 or later) (For Reporting)

2.  Install QL-570 Printer driver

Find the file and double-click it: qd570w510a2kus.exe
Execute qd570w510a2kus\D_SETUP.EXE and follow directions.  You must have a printer connected

3.  Install bpac client

Find the file and double-click bcciw30010.msi
Follow installation wizard prompts

4.  Install RXTrack software

Double-click on RxTrack3.0Installer.exe and follow instructions


--
Building notes:
1. Open Product configuration (prodconfiguration.product) and export an exe structure in e:\rxtrack
2. Install NSIS and run the script rxtrack.nsi
3. Clear out c:\temp
4. Run RxTrack3.0Installer.exe