package download.manager;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

public class Download extends Observable implements Runnable{

	private static final int MAX_SIZE_OF_BUFFER = 1024;
	public static final String Status[] = 
		{"Downloading", "Paused", "Complete", "Canceled" ,"Error"};
	//Коды команд
	public static final int DOWNLODING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELED = 3;
	public static final int ERROR = 4;
	
	
	private URL url;
	private int downloded;
	private int size;
	private int status;
	
	
	
	public Download(URL url)
	{
		this.url = url;
		size = -1;
		downloded = 0;
		status = DOWNLODING;
		download();
	}
	
	private void download()
	{
		Thread thread = new Thread(this);
		thread.start();
		
	}
	public void pause(){
		status = PAUSED;
		stateChanged();
	}
	public void resume(){
		status = DOWNLODING;
		stateChanged();
		download();
	}
	public void cancel(){
		status = CANCELED;
		stateChanged();
		
	}
	private void error(){
		status = ERROR;
		stateChanged();
	}
	
	public String getURL(){
		return url.toString();
		
	}
	
	public int getSize(){
		return size;
	}
	
	public int getStatus()
	{
		return status;
	}
	public float getProgress(){
		return ((float)downloded/size) * 100;
	}
	
	private String getFileName(URL url){
		
		String fileName = url.getFile();
		
		return fileName.substring(fileName.lastIndexOf('/')+1);
		
		
		
	}
	
	
	@Override
	public void run() {
		
		RandomAccessFile file = null;
		InputStream stream = null;
		try{
			HttpURLConnection connection = 
					(HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Range", "bytes = "+downloded+"-");
			connection.connect();
			if(connection.getResponseCode() != 200)
			{
				error();
				
			}
			int contentLenght = connection.getContentLength();
			if(contentLenght < 1)
			{
				error();
			}
			
			
			if(size == -1)
			{
				size = contentLenght;
				stateChanged();
			}
			
			
			file = new RandomAccessFile(getFileName(url), "rw");
			file.seek(downloded);
			stream = connection.getInputStream();
			while(status == DOWNLODING)
			{
				
				byte buffer[];
				if(size - downloded > MAX_SIZE_OF_BUFFER)
				{
					buffer = new byte[MAX_SIZE_OF_BUFFER];
					
				}else {
					buffer = new byte[size - downloded];
				}
				int read = stream.read(buffer);
				if(read == -1)
					break;
				file.write(buffer, 0, read);
				downloded += read;
				stateChanged();
				
			}
			
			if(status == DOWNLODING)
			{
				status = COMPLETE;
				stateChanged();
				
			}
			
		}catch(Exception e)
		{
			error();
			
		}
		finally{
			
			if(file != null){
				
				try{
					file.close();
				}catch(Exception e){
					
				}
				
				if(stream != null){
					try{
						stream.close();
					}catch(Exception e)
					{
						
					}
				}
			}
			
		}
		
		
		
	}
	
	private void stateChanged(){
		setChanged();
		notifyObservers();
	}
	
	

}
