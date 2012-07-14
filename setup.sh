#!/bin/bash
SDK_VERSION=1.7.0
SDK_URL="http://googleappengine.googlecode.com/files/appengine-java-sdk-${SDK_VERSION}.zip"
SDK_ZIP=`basename $SDK_URL`
SDK_FOLDER=${SDK_ZIP%%.zip}

echo $SDK_ZIP
echo $SDK_FOLDER

mkdir temp
cd temp

echo "starting download of app engine sdk $SDK_VERSION"
wget $SDK_URL

if [ -f $SDK_ZIP ]; then
{
	echo "extracting sdk"
	unzip -q $SDK_ZIP
	
	if [ -d $SDK_FOLDER ]; then
	{
		echo "install libs in maven local repo"
		cd $SDK_FOLDER
		mvn install:install-file -Dfile=lib/appengine-tools-api.jar -DgroupId=com.google.appengine -DartifactId=appengine-tools -Dversion=$SDK_VERSION -Dpackaging=jar -DgeneratePom=true

		mvn install:install-file -Dfile=lib/shared/appengine-local-runtime-shared.jar -DgroupId=com.google.appengine -DartifactId=appengine-local-runtime-shared -Dversion=$SDK_VERSION -Dpackaging=jar -DgeneratePom=true

		mvn install:install-file -Dfile=lib/user/appengine-api-1.0-sdk-${SDK_VERSION}.jar -DgroupId=com.google.appengine -DartifactId=appengine-sdk-api -Dversion=${SDK_VERSION} -Dpackaging=jar -DgeneratePom=true
	
		mvn install:install-file -Dfile=lib/user/orm/datanucleus-appengine-1.0.10.final.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-appengine -Dversion=1.0.10.final -Dpackaging=jar -DgeneratePom=true

		mvn install:install-file -Dfile=lib/tools/orm/datanucleus-jpa-1.1.5.jar -DgroupId=com.google.appengine -DartifactId=datanucleus-jpa -Dversion=1.1.5 -Dpackaging=jar -DgeneratePom=true

		mvn install:install-file -Dfile=lib/tools/orm/jdo2-api-2.3-eb.jar -DgroupId=javax.jdo -DartifactId=jdo2-api -Dversion=2.3-eb -Dpackaging=jar -DgeneratePom=true
	}
	fi
}
fi
