# How To Enable Selenoid and Selenoid UI on Windows OS

### Download and install Docker

Please refer to official docker docs https://docs.docker.com/desktop/install/windows-install/ and Install Docker 

Give all neccessary permissions to Docker

Due to bug https://github.com/aerokube/selenoid/issues/1065 in Selenoid Library please  Uncheck "Use WSL 2 Based engine" checkbox in Docker settings

Go to a random folder and try to execute the docker command without sudo:
```
docker stats
```

The command should be executed without errors. 

### Download Configuration Manager to windows

Download Configuration Manager for your platform from releases page https://github.com/aerokube/cm/releases/tag/1.7.2.

Create new folder and Put *.exe file to this folder f.e C:\SelenoidConfigurationManager

Rename *.exe file to cm.exe for more simple usage

Add link to cm.exe to PATH in windows

Execute the next command:
```
cm.exe --help
```

### Download Selenoid using Configuration manager

Go to the **SelenoidConfigurationManager** folder and execute next 
```
cm.exe selenoid download
```

The latest selenoid image should be fetched

### Download Selenoid-UI using the Configuration manager

Go to the **SelenoidConfigurationManager**  folder and execute next 
```
cm.exe selenoid-ui download
```

The latest selenoid-ui image should be fetched

### Launch Selenoid using the Configuration manager

Clone project:
```
git clone git@github.rakops.com:DisplayQA/RXUI_Automation.git
```

Inside the cloned project find /selenoid folder and browsers.json file. Take a look on file content to see which browsers will be fetched

Run the next command to start selenoid with browsers specified in browsers.json file located in repo  
```
cm.exe selenoid start --vnc -enable-file-upload --browsers-json <absolute_path_to_UI_automation_repo>/selenoid/browsers.json --force
```

Configuration Manager will pull all browser images that were specified in the browsers.json file, will pull docker image for video recorder image.

After that Configuration manager will start selenoid locally with all configured docker images

After Selenoid is started, execute next command to see the selenoid status
 ```
cm.exe selenoid status
 ```

Check if selenoid container is up:
``` 
docker stats
```

You will see that selenoid-related containers are presented in the docker-command output
You can also check docker desktop to see if selenoid-related containers are presented
 
### Launch Selenoid-UI using Configuration Manager

Go to the **SelenoidConfigurationManager** folder and execute next 
```
cm.exe selenoid-ui start
``` 
 
Please open URL http://localhost:8080/#/ and see if Selenoid UI works as expected

Pay attention that the list of browsers should be the same as specified in browsers.json file

Pay attention that SSE and Selenoid should have the "connected" status

In case of errors please refer to logs of selenoid or selenoid-ui docker containers.

Now check all docker images:
```
docker images
```

Pay attention that selenoid-related mages are presented

Now Selenoid and Sleenoid UI are configured for local tests execution

To stop selenoid use command 
```
cm.exe selenoid stop
```

To stop selenoid-ui please execute the command 
```
cm.exe selenoid-ui stop
```
