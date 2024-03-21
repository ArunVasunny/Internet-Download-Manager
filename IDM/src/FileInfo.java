import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FileInfo {
    //Initialize karna badmey
    private SimpleIntegerProperty index;
    private SimpleStringProperty name;
    private SimpleStringProperty url;
    private SimpleStringProperty status;
    //"Downloading, Starting, Completed"
    private SimpleStringProperty action;

    public FileInfo(int index, String name, String url, String status, String action)
    {
        this.index.set(index);
        this.name.set(name);
        this.url.set(url);
        this.status.set(status);
    }

    public int getIndex()
    {
        return index.get();
    }

    public SimpleIntegerProperty indexProperty()
    {
        return index;
    }

    public void setIndex(int index)
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

    public String getAction()
    {
        return action.get();
    }
    
    public SimpleStringProperty actionProperty()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action.set(action);
    }
        
}
