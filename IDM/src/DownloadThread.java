import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

        //Dont forget to handle file already exists

        // String downloadPath = file.getPath();
        // Path path = Paths.get(downloadPath);
        // if (Files.exists(path)) {
        //     // File already exists, handle this situation accordingly
        //     System.out.println("File already exists: " + downloadPath);
        //     file.setStatus("FAILED"); // Update file status to indicate failure
        //     manager.updateUI(file); // Update UI to reflect the status change
        //     return; // Exit run() method
        // }

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
