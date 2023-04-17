# How Enable Selenoid and Sleenoid UI on Ubuntu OS

### Download and install Docker

Update the package lists
```
sudo apt update
```

Next, install the dependencies that are required by Docker to function as expected:
```
sudo apt install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
```

Thereafter, add the GPG key as shown:
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

Next, add the Docker repository as shown:
```
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

Once you are done adding the repository, it’s time now to install Docker. To check available versions that are available from docker repository, run the command:
```
apt list -a docker-ce
```

Install Docker version 20.10.5
```
sudo apt install docker-ce=5:20.10.5~3-0~ubuntu-focal docker-ce-cli=5:20.10.5~3-0~ubuntu-focal containerd.io
```

Once you have installed Docker, you can confirm its status by running the command:
```
sudo systemctl status docker
```
To prevent Docker from being updated and maintain using the current version, run the command:
```
sudo apt-mark hold docker-ce
```

Add new group docker:
```
sudo groupadd docker
```

Add your user to the docker group:
```
sudo usermod -aG docker $USER
```

Relogin your user  using the command:
```
su ${USER}
```

Go to a random folder and try to execute the docker command without sudo:
```
docker stats
```

The command should be executed without errors. 

### Download Configuration Manager for your platform from the releases page

Go to https://github.com/aerokube/cm/releases/tag/1.7.2 and download corresponded image for your system 

Create a new folder for Configuration Manager and put the image of Configuration manager into it (selenoid_cm)

Rename Configuration Manager file to 'cm'

Open a terminal and go to the selenoid_cm folder. Give execution permissions to binary:
```
chmod +x cm
```

Add selenoid_cm directory to path
```
export PATH="$HOME/selenoid_cm:$PATH"
```

Go to some random folder (f.e  - / home) and execute the next command:
```
./cm --help
```

Optionally, you can configure the launching configuration manager not only from selenoid_cm folder

Execute next command:
```
reboot
```

### Download Selenoid using Configuration manager

Go to the selenoid_cm folder and execute next 
```
sudo ./cm selenoid download
```

The latest selenoid image should be fetched

### Download Selenoid-UI using the Configuration manager

Go to the selenoid_cm folder and execute next 
```
sudo ./cm selenoid-ui download
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

Go to the selenoid_cm folder and Run the next command to start selenoid with browsers specified in browsers.json file located in repo  
```
sudo ./cm selenoid start --vnc -enable-file-upload --browsers-json ~/.aerokube/selenoid/browsers.json --force
```

Configuration Manager will pull all browser images that were specified in the browsers.json file, will pull docker image for video recorder image.

After that Configuration manager will start selenoid locally with all configured docker images

After Selenoid is started, execute next command to see the selenoid status
 ```
./cm selenoid status
 ```

Check if selenoid container is up:
docker stats

You will see that selenoid-related containers are presented in the docker-command output
 
### Launch Selenoid-UI using Configuration Manager

Go to the selenoid_cm folder and execute next 
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

Pay attention that selenoid-related mages are presented

Now Selenoid and Sleenoid UI are configured for local tests execution

To stop selenoid use command 
./cm selenoid stop

To stop selenoid-ui please execute the command 
./cm selenoid-ui stop
