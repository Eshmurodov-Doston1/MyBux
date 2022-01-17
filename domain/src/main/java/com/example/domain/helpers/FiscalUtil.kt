package com.example.domain.helpers

import java.util.ArrayList

class  FiscalUtil {
    var TerminalID = "11"
    var Version = "11"
    var Type = "11"
    var CurrentReceiptSeq = "11"
    var CurrentTime = "11"
    var FirstReceiptTime = "11"
    var ReceiptMaxCount = "11"
    var ReceiptCount = "11"
    var ZReportMaxCount = "11"
    var ZReportCount = "11"
    var AvailableDeselectMemory = "11"
    var AvailableResetMemory = "11"
    var AvailablePersistentMemory = "11"

    constructor(str: String) {
        AvailablePersistentMemory = str.substring(0, 6)
        AvailableResetMemory = str.substring(6, 12)
        AvailableDeselectMemory = str.substring(12, 18)
        ZReportCount = str.substring(18, 22)
        ZReportMaxCount = str.substring(22, 26)
        ReceiptCount = str.substring(26, 30)
        ReceiptMaxCount = str.substring(30, 34)
        FirstReceiptTime = str.substring(34, 50)
        CurrentTime = str.substring(50, 66)
        CurrentReceiptSeq = str.substring(66, 82)
        Type = str.substring(82, 84)
        Version = str.substring(84, 88)
        TerminalID = str.substring(88, 104)
    }



    fun getList(): List<InfoModel>? {
        val infoModels: MutableList<InfoModel> = ArrayList<InfoModel>()
        infoModels.add(InfoModel("TerminalID", TerminalID))
        infoModels.add(InfoModel("Version", Version))
        infoModels.add(InfoModel("Type", Type))
        infoModels.add(InfoModel("CurrentReceiptSeq", CurrentReceiptSeq))
        infoModels.add(InfoModel("CurrentTime", CurrentTime))
        infoModels.add(InfoModel("FirstReceiptTime", FirstReceiptTime))
        infoModels.add(InfoModel("ReceiptMaxCount", ReceiptMaxCount))
        infoModels.add(InfoModel("ReceiptCount", ReceiptCount))
        infoModels.add(InfoModel("ZReportMaxCount", ZReportMaxCount))
        infoModels.add(InfoModel("ZReportCount", ZReportCount))
        infoModels.add(InfoModel("AvailableDeselectMemory", AvailableDeselectMemory))
        infoModels.add(InfoModel("AvailableResetMemory", AvailableResetMemory))
        infoModels.add(InfoModel("AvailablePersistentMemory", AvailablePersistentMemory))
        return infoModels
    }
}