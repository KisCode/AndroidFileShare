package demo.kiscode.fileshare.contants;

/**
 * Description:
 * Author: keno
 * Date : 2021/5/18 15:44
 **/
public enum FileMIME {
    IMAGE("image/*")
    , AUDIO("audio/*")
    , VIDEO("video/*")
    , PDF("application/pdf")
    , WORD("application/msword")
    , PPT("application/vnd.ms-powerpoint")
    , EXCEL("application/vnd.ms-excel")
    , ZIP("application/x-gzip")
    , HTML("text/html")
    , TEXT("text/plain")
    , CHM("application/x-chm")
    , APK("application/vnd.android.package-archive")
    , UNKWNO("*/*");

    private String value;

    FileMIME(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}