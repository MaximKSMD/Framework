# How Enable Selenoid and Sleenoid UI on Mac OS

### Download and install Docker

Please refer to official docker docs https://docs.docker.com/desktop/install/mac-install/ and Install Docker 

Go to a random folder and try to execute the docker command without sudo:
```
docker stats
```

The command should be executed without errors. 

### Make directory for Configuration Manager 

Make new directory using commandline
```
mkdir ~/cm
```

Give execution permissions to binary:
```
chmod +x cm
```

Proceed to cm directory
```
cd ~/cm
```

### Download Configuration Manager to mac

Download Latest version of Configuration Manager
```
curl -s https://aerokube.com/cm/bash | bash
```

Rename Configuration Manager to **cm**

Add cm directory to path
```
export PATH="$HOME/cm:$PATH"
```

Execute the next command:
```
./cm --help
```

Optionally, you can configure the launching configuration manager not only from cm folder


### Download Selenoid using Configuration manager

Go to the **cm** folder and execute next 
```
./cm selenoid download
```

The latest selenoid image should be fetched

### Download Selenoid-UI using the Configuration manager

Go to the cm folder and execute next 
```
./cm selenoid-ui download
```

The latest selenoid-ui image should be fetched

### Launch Selenoid using the Configuration manager

Clone project:
git clone git@github.rakops.com:DisplayQA/RXUI_Automation.git

Inside the cloned project find /selenoid folder and browsers.json file.

Due to a bug in Selenoid Configuration Manager, use the next step before starting selenoid. Create a new .aerokube directory in <user>/home if this folder doesn't exist
```
cd ~/ && mkdir .aerokube
```
 
Copy browsers.json  file **seleniod** folder  in newly created  **~/.aerokube** folder:
```
rsync -a <link to fethced repo>/selenoid/browsers.json  ~/.aerokube/selenoid/
```

Go to the **cm** folder and Run the next command to start selenoid with browsers specified in browsers.json file located in repo  
```
./cm selenoid start --vnc -enable-file-upload --browsers-json ~/.aerokube/selenoid/browsers.json --force
```
<img width="1287" alt="Screenshot 2023-02-10 at 17 32 09" src="https://media.github.rakops.com/user/2845/files/33fe63d2-6841-466b-9903-84f3cf9e3553">

Configuration Manager will pull all browser images that were specified in the browsers.json file, will pull docker image for video recorder image.

After that Configuration manager will start selenoid locally with all configured docker images

After Selenoid is started, execute next command to see the selenoid status
 ```
./cm selenoid status
 ```

Check if selenoid container is up:
``` 
docker stats
```

You will see that selenoid-related containers are presented in the docker-command output
You can also check docker desktop to see if selenoid-related containers are presented
 
### Launch Selenoid-UI using Configuration Manager

Go to the **cm** folder and execute next 
```
./cm selenoid-ui start
``` 
 
Please open URL http://localhost:8080/#/ and see if Selenoid UI works as expected

Pay attention that the list of browsers should be the same as specified in browsers.json file

Pay attention that SSE and Selenoid should have the "connected" status

In case of errors please refer to logs of selenoid or selenoid-ui docker containers.

Now check all docker images:
```
docker images
```
<img width="1287" alt="Screenshot 2023-02-10 at 17 32 20" src="https://media.github.rakops.com/user/2845/files/1b0ef2e9-f374-4a22-935d-b6aff385dabf">

<img width="1287" alt="Screenshot 2023-02-10 at 17 33 21" src="https://media.github.rakops.com/user/2845/files/f6b59f9b-305f-4b0d-8eb1-c802884edaee">

Pay attention that selenoid-related images are presented

Now Selenoid and Sleenoid UI are configured for local tests execution

To stop selenoid use command 
./cm selenoid stop

To stop selenoid-ui please execute the command 
./cm selenoid-ui stop
