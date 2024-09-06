
  
# Online Player Extractor  
  
A tool for downloading videos from online players like JWPlayer  
  
  
## Usage/Examples  
  
  
1. Download and install the program ([Releases page](https://github.com/julioreigen/online-player-extractor/releases))  
2. Open a tab in browser that has JW Player or a similar video player;  
3. Press F12 or right-click on the page and press “Inspect element”;  
4. Click on “Network” and click on “Media” in the list of filters; <br> ![how to enter network on dev panel](https://github.com/user-attachments/assets/8fb6d973-9897-433b-b84f-25ef944e37db)
5. Look for a file with status 206, usually the “time” of this file is slowly increasing; <br> ![Network tab](https://github.com/user-attachments/assets/2222b1c0-a648-402a-9dd2-7a9d23c78197)
6. Right-click on the file, go to Copy > Copy URL;  
7. Paste the URL into “Video URL”; <br> ![app UI](https://github.com/user-attachments/assets/bbb7db35-f691-4d0b-a528-03c976a19f5c)
8. Choose a location to save the .mp4 or .mkv file;  
9. Click on “Extract”  
Find your video in the folder and enjoy!

https://github.com/user-attachments/assets/90f30456-a492-4bfc-bdb9-d1a921c78ba9

  
## Players list (incomplete)

| Name       | File to catch | Filter    |
|------------|---------------|-----------|
| JWPlayer   | both          | both      |
| FileMoon   | master.m3u8   | fetch/XHR |
| VoeNetwork | master.m3u8   | fetch/XHR |
| StreamWish | master.m3u8   | fetch/XHR |
| MixDrop    | *.mp4         | media     |
| StreamTape | *.mp4         | media     |
| PlayerJs   | *.mp4         | media     |


  
## Run Locally  
  
Clone the project  
 
```bash  
 git clone https://github.com/julioreigen/online-player-extractor.git
 ```  
  
Open your IDE and go to the project directory  
  
```bash  
 cd online-player-extractor
 ```  
  
  
Run Launcher.java located in ***src/main/java/com/julioreigen/ope/onlineplayerextractor***
