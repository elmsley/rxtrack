Sub DoPrint2(flag, strExport, filename, labelToUse, projectName, flname, sig, qty, dosage, date, bin, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
    Set ObjDoc = Nothing
	Set ObjDoc = CreateObject("BrssCom.Document")
	' msgbox labelTouse
	bRet = ObjDoc.Open(labelToUse)
	If (bRet <> False) Then
		ObjDoc.SetText 0, projectName
		ObjDoc.SetText 1, flname
		arrList = Split(sig, ". ")
		myLength = UBound(arrList) 
		ObjDoc.SetText 2, arrList(0)
		if (myLength >= 1) then
		  ObjDoc.SetText 3, arrList(1)
		end if
		ObjDoc.SetText 4, qty
		ObjDoc.SetText 5, dosage
		ObjDoc.SetText 6, date
		ObjDoc.SetText 7, bin
		'ObjDoc.SetText 8, p0
		'ObjDoc.SetText 9, p1
		'ObjDoc.SetText 10, p2
		'ObjDoc.SetText 11, p3
		'ObjDoc.SetText 12, p4
		'ObjDoc.SetText 13, p5
		'ObjDoc.SetText 14, p6
		'ObjDoc.SetText 15, p7
		'ObjDoc.SetText 16, p8
		ObjDoc.SetText 17, bin
		ObjDoc.ReplaceImageFile 0, p0, 4
		ObjDoc.ReplaceImageFile 1, p1, 4
		ObjDoc.ReplaceImageFile 2, p2, 4
		ObjDoc.ReplaceImageFile 3, p3, 4
		ObjDoc.ReplaceImageFile 4, p4, 4
		ObjDoc.ReplaceImageFile 5, p5, 4
		ObjDoc.ReplaceImageFile 6, p6, 4
		ObjDoc.ReplaceImageFile 7, p7, 4
		ObjDoc.ReplaceImageFile 8, p8, 4
		ObjDoc.ReplaceImageFile 9, p9, 4
		if (flag = "e" or flag = "b") then
		  ObjDoc.Export 2, strExport, 180	'Export
		  'msgbox "Exported to " + strExport
		end if
		if (flag = "p" or flag = "b") then
		  ObjDoc.DoPrint 0, filename
		  'msgbox "Printed as job: " + filename
		end if
	End If
End Sub

Set args = WScript.Arguments
arg0 = args.Item(0)
arg1 = args.Item(1)
arg2 = args.Item(2)
arg3 = args.Item(3)
arg4 = args.Item(4)
arg5 = args.Item(5)
arg6 = args.Item(6)
arg7 = args.Item(7)
arg8 = args.Item(8)
arg9 = args.Item(9)
arg10 = args.Item(10)
arg11 = args.Item(11)
arg12 = args.Item(12)
arg13 = args.Item(13)
arg14 = args.Item(14)
arg15 = args.Item(15)
arg16 = args.Item(16)
arg17 = args.Item(17)
arg18 = args.Item(18)
arg19 = args.Item(19)
arg20 = args.Item(20)

DoPrint2 arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20
