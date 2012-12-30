#YajMember

_Yet Another Jug Member App_

Small app to manage our java user group users

## Releases

_still under dev (0.1 comming soon)_

## Development

To start coding, you need:

* Git as well and the repo cloned
* Gradle version **>1.0** in your path
* JDK **>=7**
* A relationnal database (I use H2 in dev, but you can use your favorite)

Copy the config file from ̀```src/main/config/config.propertie``` to somewhere in the classpath, ie. ```src/main/resources``̀`.
Disable the authentication in the config file (```auth.enabled=false``̀ ) or ask me for the Google API identifier.

If you select an other database than H2, please change the settings in the ```src/main/resources/META-INF/persistence.xml```
and add the driver to the gradle dependencies (same infos than Maven).

Once the database is started, the first step is to add some data and by the way to generate the database schema. 
The ```bulk``` task will help you to do this step. You can import the default data set, or edit the content of the CSV files 
located under ```src/main/scripts```

Then, just run:

```bash
$ gradle bulk -Ptarget=event
```

and if the previous task complete succesfully:

```bash
$ gradle bulk -Ptarget=member
```

Once the data is imported, you can start the web app:


```bash
$ gradle jettyRun
```


## Thank you to the blocks that I have assembled to build this app

###Build

* [Gradle](http://www.gradle.org/) : the best java build tool (from far!)

###Server side smells

* [OpenJDK 7](http://openjdk.java.net/projects/jdk7/) : yes, java 6 isn't the stable version anymore... 
* [Jetty](http://www.eclipse.org/jetty/) : invisible web server has it should be
* [OpenJPA](http://openjpa.apache.org/) : just because I don't like SQL
* [Bean Validation - bval](http://openjpa.apache.org/bean-validation-primer.html) : fits well with openjpa
* [Guice](http://code.google.com/p/google-guice/) : taste it and you will become dependent to injections
* [Jersey](http://jersey.java.net/) : REST as the only way to do HTTP
* [Gson](http://code.google.com/p/google-gson/) : serializer for lazy people
* [SuperCSV](http://supercsv.sourceforge.net/) : everything may not be in XML

###Client side flavours

* [RequireJs](http://requirejs.org/): don't need a skeleton framework, webmodules are enough
* [jQuery](http://jquery.com/) : what else ?
* [jQueryUi](http://jqueryui.com/) : simple component, cool theming, everything to build an UI 
* [Gridy](https://github.com/wbotelhos/gridy) : but I'm too lazy to build my own _listgrid_
* [Noty](http://needim.github.com/noty/) : because communication is the key

