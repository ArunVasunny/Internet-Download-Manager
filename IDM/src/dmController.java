import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class dmController implements Initializable{
    
    @FXML
    private TableView<FileInfo> tableView;

    @FXML
    private Button downloadButton;
    
    @FXML
    private TextField urlTextField;

    public int index = 0; //0

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo,String> srno = (TableColumn<FileInfo,String>) this.tableView.getColumns().get(0);
        srno.setCellValueFactory(p -> {
            return p.getValue().indexProperty();
        });

        TableColumn<FileInfo,String> filename = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(1);
        filename.setCellValueFactory(p -> {
            return p.getValue().nameProperty();
        });

        TableColumn<FileInfo,String> fileurl = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(2);
        fileurl.setCellValueFactory(p -> {
            return p.getValue().urlProperty();
        });

        TableColumn<FileInfo,String> status = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(3);
        status.setCellValueFactory(p -> {
            return p.getValue().statusProperty();
        });

        TableColumn<FileInfo, String> size = (TableColumn<FileInfo, String>)this.tableView.getColumns().get(4);
        size.setCellValueFactory(p->{
            SimpleStringProperty simppleStringProperty = new SimpleStringProperty();
            simppleStringProperty.set(p.getValue().getSize());
            return simppleStringProperty;
        });

        TableColumn<FileInfo,String> percentage = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(5);
        percentage.setCellValueFactory(p -> {
            //To display symbol
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.set(p.getValue().getPercent() + " %");
            return simpleStringProperty;
        });

    }
    
    @FXML
    void downloadButtonClicked(ActionEvent event) {

        String url = urlTextField.getText().trim();
        String filename = url.substring(url.lastIndexOf("/")+1);
        // System.out.println(filename);
        String status = "STARTING";
        //SIZE
        String size = getSizeFromURL(url);
        String path = location.DOWNLOAD_PATH + File.separator+filename;
        FileInfo file = new FileInfo((index+1) +"", filename, url, status, size, path,"0"); //1
        this.index = index + 1; //2
        DownloadThread thread = new DownloadThread(file, this);
        this.tableView.getItems().add(Integer.parseInt(file.getIndex()) - 1,file); //2-1 = 1
        thread.start();
        this.urlTextField.setText("");
        System.out.println("File Downloaded Successfully");
        
    }

    public void updateUI(FileInfo metaFile)
    {
        FileInfo fileInfo = this.tableView.getItems().get(Integer.parseInt(metaFile.getIndex())-1); //1-1 = 0
        fileInfo.setStatus(metaFile.getStatus());
        DecimalFormat decimcalFormat = new DecimalFormat("0.0");
        fileInfo.setPercent(decimcalFormat.format(Double.parseDouble(metaFile.getPercent())));
        this.tableView.refresh();
        System.out.println(metaFile);
    }

    public String getSizeFromURL(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            int fileSize = connection.getContentLength();
            return formatFileSize(fileSize);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public String formatFileSize(long size)
    {
        if (size <= 0)
        {
            return "0 B";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
