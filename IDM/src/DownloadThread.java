import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadThread extends Thread{

    private static final long UPDATE_INTERVAL_MS = 1000;
    private FileInfo file;
    dmController manager;
    private long lastUpdateTime;
    private long bytesDownloaded;
    private volatile boolean paused = false;

    public DownloadThread(FileInfo file, dmController manager)
    {
        this.file = file;
        this.manager = manager;
        this.lastUpdateTime = System.currentTimeMillis();
        this.bytesDownloaded = 0;
        
    }

    private FileOutputStream fileOutputStream;
    private BufferedInputStream bufferedInputStream;

    public void closeFile()
    {
        try 
        {
            if (fileOutputStream != null)
            {
                fileOutputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseDownload()
    {
        paused = true;
    }

    public void resumeDownload()
    {
        paused = false;
        synchronized(this)
        {
            notify(); // this will tell to resume
            file.setStatus("DOWNLOADING");
        }
    }

    @Override
    public void run()
    {
        this.file.setStatus("DOWNLOADING");
        this.manager.updateUI(file);
        //Download logic
        try {
            // We cant retrieve Download infomation using this method so ill implement an alternate method
            // Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));

            URL url = new URL(this.file.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();
            // System.out.println("FileSIze = " +fileSize);

            int countByte = 0;
            double percent = 0.0;
            double byteSum = 0.0;
            bufferedInputStream = new BufferedInputStream(url.openStream());

            fileOutputStream = new FileOutputStream(this.file.getPath());
            byte data[] = new byte[1024];

            while (true) {

                if(paused)
                {
                    synchronized(this)
                    {
                        file.setStatus("PAUSED");
                        try {
                            wait();
                        } 
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                countByte = bufferedInputStream.read(data,0,1024);
                if(countByte == -1)
                {
                    break;
                }

                fileOutputStream.write(data,0,countByte);

                byteSum = byteSum+countByte;
                bytesDownloaded = bytesDownloaded+countByte;

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastUpdateTime;
                if (elapsedTime >= UPDATE_INTERVAL_MS) {
                    double downloadSpeed = (double) bytesDownloaded / elapsedTime * 1000; // bytes per second
                    file.setSpeed(formatSpeed(downloadSpeed));
                    // this.manager.updateUI(file); 
                    // Reset values for next interval
                    lastUpdateTime = currentTime;
                    bytesDownloaded = 0;
                }

                if(fileSize>0){
                    percent=(byteSum/fileSize * 100);
                    // System.out.println(percent);
                    this.file.setPercent(percent+ "");
                    this.manager.updateUI(file);
                }
            }   

            fileOutputStream.close();
            bufferedInputStream.close();

            this.setName(100 + "");
            this.file.setStatus("COMPLETED");

        } catch (Exception e) {
            this.file.setStatus("FAILED");
            e.printStackTrace();
        }

        this.file.setSpeed("0");
        ConnectionClass.update(file); //This will update file when the download gets complete
        this.manager.updateUI(file);

    }  
    
    private String formatSpeed(double speed) {
        if (speed < 1024) {
            return String.format("%.2f B/s", speed);
        } else if (speed < 1024 * 1024) {
            return String.format("%.2f KB/s", speed / 1024);
        } else {
            return String.format("%.2f MB/s", speed / (1024 * 1024));
        }
    }

}
