import javafx.beans.property.SimpleStringProperty;

public class FileInfo {
    //Initialize karna badmey
    private SimpleStringProperty index = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty url = new SimpleStringProperty();
    private SimpleStringProperty status = new SimpleStringProperty();
    //"Downloading, Starting, Completed"
    private SimpleStringProperty size = new SimpleStringProperty();
    private SimpleStringProperty path = new SimpleStringProperty();
    private SimpleStringProperty percent = new SimpleStringProperty();

    public FileInfo(String index, String name, String url, String status, String size, String path, String percent)
    {
        this.index.set(index);
        this.name.set(name);
        this.url.set(url);
        this.status.set(status);
        this.size.set(size);
        this.path.set(path);
        this.percent.set(percent);
    }

    public String getIndex()
    {
        return index.get();
    }

    public SimpleStringProperty indexProperty()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index.set(index);
    }

    public String getName()
    {
        return name.get();
    }

    public SimpleStringProperty nameProperty()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getSize()
    {
        return size.get();
    }
    
    public SimpleStringProperty sizeProperty()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size.set(size);
    }

    public String getPath()
    {
        return path.get();
    }

    public SimpleStringProperty pathProperty()
    {
        return path;
    }

    public void setPath(String path)
    {
       this.path.set(path);
    }

    public String getPercent()
    {
        return percent.get();
    }

    public SimpleStringProperty percentProperty()
    {
        return percent;
    }

    public void setPercent(String percent)
    {
        this.percent.set(percent);
    }



    @Override 
    public String toString() {
        return "FileInfo [index=" + index + ", name=" + name + ", url=" + url + ", status=" + status + ", size=" + size + ", path=" + path + "percent=" + percent +"]";
    }
    
        
}
