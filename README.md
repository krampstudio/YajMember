#YajMember

_Yet Another Jug Member App_

Small app to manage our java user group users

## Releases

_still under dev (0.1 comming soon)_

## Development

To start coding, you need:

* Git as well and the repo cloned
* Gradle version > 1.0 in your path
* A relationnal database (I use H2 in dev, but you can use your favorite)

If you select an other database, please change the settings in the ```src/main/resources/persistence.xml```
and add the driver to the gradle dependencies (same infos than Maven).


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

