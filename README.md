#YajMember

_Yet Another Jug Member App_

Small app to manage our java user group users

## Releases

_still under dev (0.1 comming soon)_

## Development

To start coding, you need the following softwares:

* [Git](http://git-scm.com/downloads) as well and the repo cloned
* [Gradle](http://www.gradle.org/downloads) version **>1.0** in your path
* [Node.js](http://nodejs.org/download/) **>=0.8** to be able to use the javascript tooling and package repository
* JDK **>=7**
* [MongoDB](http://www.mongodb.org/downloads) **>=2** as data store server

Then set up the project:

* Update the file `gradle.properties` (the _binaries_ and _proxy_ section) 
* Install Grunt: 

```bash
$> npm install -g grunt-cli
```

* Run the command to fetch the javascript dependencies:

```bash
$> gradle install
```

* To set up the project configuration:
 1. Copy the config file from `src/main/config/config.propertie` to somewhere in the classpath, ie. `src/main/resources`
 2. Update the mongo connection settings
 3. Disable the authentication in the config file (`auth.enabled=false`) or ask me for the Google API identifier.
* Import the data into mongo (the data are contained into CSV files under `src/main/scripts`), by running: 

```bash
$> gradle bulk
```

* Once the data is imported, you can start the development server:

```bash
$> gradle jettyRun
```




## Thank you to the blocks that I have assembled to build this app

###Build

* [Gradle](http://www.gradle.org/) : the best java build tool (from far!)
* [Grunt](http://gruntjs.org) : the best javascript build tool (too)
* [Bower](http://twitter.github.com/bower/) : client side dependency management

###Server side smells

* [OpenJDK 7](http://openjdk.java.net/projects/jdk7/) : yes, java 6 isn't the stable version anymore... 
* [Jetty](http://www.eclipse.org/jetty/) : invisible web server has it should be
* [Mongo Java Driver](https://github.com/mongodb/mongo-java-driver) : just because I don't like SQL
* [Guice](http://code.google.com/p/google-guice/) : taste it and you will become dependent to injections
* [Guava](http://code.google.com/p/guava-libraries/) : some dynamism is always great
* [Jersey](http://jersey.java.net/) : REST as the only way to do HTTP
* [Gson](http://code.google.com/p/google-gson/) : serializer for lazy people
* [SuperCSV](http://supercsv.sourceforge.net/) : everything may not be in XML
* [Sclar](http://www.thebuzzmedia.com/software/imgscalr-java-image-scaling-library/) : I don't want to screw up images
* [Google Web APIs](http://code.google.com/p/google-api-java-client/) : OAuth from Google App to Yajug board members 
* [Test NG](http://testng.org/) : The best java unit and integration tests framework

###Client side flavours

* [RequireJs](http://requirejs.org/): don't need a skeleton framework, AMD's webmodules are enough
* [jQuery](http://jquery.com/) : what else ?
* [jQueryUi](http://jqueryui.com/) : simple components, cool theming, everything to build an UI 
* [Gridy](https://github.com/wbotelhos/gridy) : but I'm too lazy to build my own _listgrid_
* [Noty](http://needim.github.com/noty/) : because communication is the key
* [Chart.js](http://www.chartjs.org/) : a canvas based chart library, that rocks with a few hacks
* {Epiceditor](http://oscargodson.github.io/EpicEditor/) : markdown instead of wysiwig, because we are harckers


## There is still a lot to do

Check my personnal [TODO List](TODO.md) or the [issues list](https://github.com/krampstudio/YajMember/issues)
