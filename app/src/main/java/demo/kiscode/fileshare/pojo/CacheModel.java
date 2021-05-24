package demo.kiscode.fileshare.pojo;

import java.io.Serializable;

import demo.kiscode.fileshare.contants.PathType;

/**
 * Description:
 * Author: keno
 * Date : 2021/5/21 11:21
 **/
public class CacheModel implements Serializable {

    //路径类型
    private PathType pathType;
    //绝对路径
    private String pathAbsolute;
    //路径下全部文件大小总和
    private long totalSize;

    public CacheModel(PathType pathType, String pathAbsolute, long totalSize) {
        this.pathType = pathType;
        this.pathAbsolute = pathAbsolute;
        this.totalSize = totalSize;
    }

    public PathType getPathType() {
        return pathType;
    }

    public void setPathType(PathType pathType) {
        this.pathType = pathType;
    }

    public String getPathAbsolute() {
        return pathAbsolute;
    }

    public void setPathAbsolute(String pathAbsolute) {
        this.pathAbsolute = pathAbsolute;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}