package demo.kiscode.fileshare.pojo;

/**
 * Description:
 * Author: keno
 * Date : 2020/9/21 15:36
 **/
public class ShareFileModel {
    private String name;
    private long size;

    public ShareFileModel() {
    }

    public ShareFileModel(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ShareFileModel{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}