import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadThread extends Thread{

    private FileInfo file;
    dmController manager;

    public DownloadThread(FileInfo file, dmController manager)
    {
        this.file = file;
        this.manager = manager;
    }

    @Override
    public void run()
    {
        this.file.setStatus("DOWNLOADING");
        this.manager.updateUI(file);

        //Download logic
        try {
            Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
            this.file.setStatus("COMPLETED");
        } catch (Exception e) {
            this.file.setStatus("FAILED");
            e.printStackTrace();
        }

        this.manager.updateUI(file);

    }   
}
