
  
# Online Player Extractor  
  
A tool for downloading videos from online players like JWPlayer  
  
  
## Usage/Examples  
  
  
1. Download and install the program ([Releases page](https://github.com/julioreigen/online-player-extractor/releases))  
2. Open a tab in browser that has JW Player or a similar video player;  
3. Press F12 or right-click on the page and press “Inspect element”;  
4. Click on “Network” and click on “Media” in the list of filters; <br> ![how to enter network on dev panel](https://github.com/julioreigen/online-player-extractor/blob/main/img/network.png?raw=true)
5. Look for a file with status 206, usually the “time” of this file is slowly increasing; <br> ![Network tab](https://github.com/julioreigen/online-player-extractor/blob/main/img/copyURL.png?raw=true)
6. Right-click on the file, go to Copy > Copy URL;  
7. Paste the URL into “Video URL”; <br> ![app UI](https://github.com/julioreigen/online-player-extractor/blob/main/img/appUI.png?raw=true)
8. Choose a location to save the .mp4 or .mkv file;  
9. Click on “Extract”  
Find your video in the folder and enjoy!
  
  
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