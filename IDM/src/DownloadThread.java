import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
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
            // We cant retrieve Download infomation using this method so ill implement an alternate method
            // Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));

            URL url = new URL(this.file.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();
            System.out.println("FileSIze = " +fileSize);

            int countByte = 0;
            double percent = 0.0;
            double byteSum = 0.0;
            BufferedInputStream bInputStream = new BufferedInputStream(url.openStream());

            FileOutputStream fOutputStream = new FileOutputStream(this.file.getPath());
            byte data[] = new byte[1024];

            while (true) {
                
                countByte = bInputStream.read(data,0,1024);
                if(countByte == -1)
                {
                    break;
                }

                fOutputStream.write(data,0,countByte);

                byteSum = byteSum+countByte;

                if(fileSize>0){
                    percent=(byteSum/fileSize * 100);
                    // System.out.println(percent);
                    this.file.setPercent(percent+ "");
                    this.manager.updateUI(file);
                }
            }   

            fOutputStream.close();
            bInputStream.close();

            this.setName(100 + "");
            this.file.setStatus("COMPLETED");
        } catch (Exception e) {
            this.file.setStatus("FAILED");
            e.printStackTrace();
        }

        this.manager.updateUI(file);

    }   
}
