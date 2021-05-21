package demo.kiscode.fileshare.pojo;

import java.io.Serializable;

import demo.kiscode.fileshare.contants.PathType;

/**
 * Description:
 * Author: keno
 * Date : 2021/5/21 13:16
 **/
public class FileModel implements Serializable {
    private String name;
    private PathType pathType;
    private long createTime;
    private long size;

    public FileModel(String name, PathType pathType, long size, long createTime) {
        this.name = name;
        this.pathType = pathType;
        this.createTime = createTime;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PathType getPathType() {
        return pathType;
    }

    public void setPathType(PathType pathType) {
        this.pathType = pathType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}