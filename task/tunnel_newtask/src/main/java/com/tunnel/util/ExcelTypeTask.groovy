package com.tunnel.util

/**
 * Created by Ness on 2016/11/5.
 */
class ExcelTypeTask extends TimerTask {

    private String sessionId

    public ExcelTypeTask(String saveSessionId) {
        this.sessionId = saveSessionId
    }

    @Override
    void run() {
        DownloadExcel.downloadExcelType.get(sessionId).clear()
    }


}
