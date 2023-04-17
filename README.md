# Java+ Selenide + RestAssured + Allure Selenoid UI Automation Testing Framework

UI Automation Framework for Media SSP Platform

## Getting Started

### List of libraries that are used in the framework
_For UI Automation we have clarified to use approach with next technological stack:_
- **Java 11** - the main programming language for implementation of tests and infrastructure
- **Selenide** - open source wrapper around selenium
- **Docker** - virtualization solution for building isolated environment
- **Selenoid** - Docker-based open-source solution that we will use for running tests inside Selenoid locally
- **Maven** - project building management tool
- **testNG** - simple test runner for executing tests, comparing of Expected result with Actual result, and assurance of parallel execution
- **Jackson** -  data-processing library for Java
- **SnakeYaml** - open-source solution for reading configuration from *.yaml files
- **KeycloakAPI** - library for user-management
- **RestAssured** - open-source wrapper across HTTP Client for building API precondition
- **Lombok** - open-source library for making code more simple and more clear
- **JavaFaker** - open-source library for generation data for test needs
- **Awaitility** - open-source solution for waiters
- **Allure** - open-source library for building pretty, beautified reports

### UI Automation framework structure

_Source code contains next packages:_
- **api** - package with API Services and Data transfer Objects and entities
- **configuration** - package with configuration loader staff  
- **managers** - package with WebDriverManager
- **pages** - package with Page Objects
- **widgets** - package with Page Widgets
- **zutils** - package with useful utilities 

![]<img width="430" alt="Screenshot at Feb 07 20-19-55" src="https://media.github.rakops.com/user/2841/files/6b77000a-c04e-4267-8b8b-0336ac6247f3">

_There are 2 other folders in UI Automation Framework:_
- **.circleci** - placeholder of config.yaml file that are needed for IU CircleCi jobs
- **selenoid** - placeholder for  file browsers.json for selenoid configuration manager


## How to set up UI Automation Framework on your working station


### Setup JDK and maven

- Install JDK 11
- Add JAVA_HOME to as environment variable and add %JAVA_HOME%/bin to java to Path
- Install Apache Maven 3.5.4
- Specify MAVEN_HOME
- Add %MAVEN_HOME%/bin to Path
- Check that JDK is installed correctly
```
$ java --version
```
- Check that maven is installed correctly
```
$ mvn --version
```

### Add personal configs to *.application-local.yml file and into framework

- Go to the "resources " folder and copy application-rx.yml file

<img width="280" alt="Screenshot at Feb 08 20-38-32" src="https://media.github.rakops.com/user/2841/files/8c15578d-cc67-493a-899a-c10465e11be1">

- Paste  application-rx.yml file to the "resources" directory. Rename it to application-local.yml

<img width="280" alt="Screenshot at Feb 08 20-39-35" src="https://media.github.rakops.com/user/2841/files/2f8cd1fa-dccc-4c4d-8b3c-bdc54a3535da">

- Get the name of your operation system
- Find "ActiveSystemProfileResolver. java" class  in "configurations" folder and open it
- Add public static final String userN = <your_system_name>

<img width="530" alt="Screenshot at Feb 08 19-30-31" src="https://media.github.rakops.com/user/2841/files/80bf20b6-1a0a-49cb-9dc0-f331d6db6c16">

### Enable Selenoid and Selenoid UI on your workstation 

**[See how Enable Selenoid and Sleenoid UI on Mac OS](selenoid/selenoid-mac.md)**

**[See how Enable Selenoid and Sleenoid UI on Ubuntu OS](selenoid/selenoid-ubuntu.md)**

**[See how Enable Selenoid and Sleenoid UI on Windows OS](selenoid/selenoid-windows.md)**


## How to use UI Automation framework

Now you can launch tests inside selenoid or directly in the browser using command line interface  or build-in IDEa test launcher.
There are 2 options to execute the tests locally: inside the selenoid container and without selenoid

### Execute test inside selenoid

Launch selenoid 
```
./cm selenoid start --vnc -enable-file-upload --browsers-json <absolute path to \tr-reg-ui\seleniod\browsers.json>
```
 
Launch selenoid UI
```
./cm selenoid-ui  start
```
 
Now Open the browser and go to http://localhost:8080/#/ and see if the session becomes available

<img width="500" alt="Screenshot at Feb 09 16-44-47" src="https://media.github.rakops.com/user/2841/files/f18c7de6-21f0-409a-b20d-458831ee0a87">


If you will click on session on UI, you can observe test execution in the current time

<img width="500" alt="Screenshot at Feb 09 16-45-25" src="https://media.github.rakops.com/user/2841/files/10fd7ac7-7b16-4e58-811e-5e4a9b8af3b2">

### Execute test in local crome browser

Let's see how to execute tests locally outside selenoid
First of all, you need to disable selenoid in UI Automation framework by editing "**enableSelenoid**" config to **false** in application-local.yml file 

<img width="500" alt="Screenshot at Feb 09 17-02-53" src="https://media.github.rakops.com/user/2841/files/8228fe30-0ad4-4959-90ed-1127b58502e6">

If you want, you can stop  selenoid 
 
```
./cm selenoid  stop
```
 
If you want, you can stop  selenoid UI
 
```
./cm selenoid-ui  stop
```

