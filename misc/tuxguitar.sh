#! /bin/sh
#@ident "$Id: tuxguitar.sh,v 1.3 2008/04/16 17:04:50 rzr Exp $"
#@Author: www.philippe.coval.online.fr -- revision: $Author: rzr $
#@Licence: LGPL
#@Description: Wrapper script for starting java application tuxguitar
###############################################################################

[ ! -z $DEBUG ] && set -e
[ ! -z $DEBUG ] && set -x

#/// guess JAVA_HOME if undefined
java_guess_()
{
    d="/opt/java/"
    [ -d "$t" ] && d="$t"
    d="/usr/local/opt/java/"
    [ -d "$t" ] && d="$t"
# windows
    t="C:\\Program\ Files\\Java\\jre1.6.0_03/"
    [ -d "$t" ] && d="$t"
    t="\\Program\ Files\\Java\\jre1.6.0_03/"
    [ -d "$t" ] && d="$t"
    t="/Program\ Files/Java/jre1.6.0_03/"
    [ -d "$t" ] && d="$t"
# macosx
    t="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home/"
    [ -d "$t" ] && d="$t"
# opensuse
    t="/etc/profile.d/alljava.sh"
    [ -r $t ] && . $t && echo ${JAVA_HOME} && return
    t="/usr/lib/jvm/java"
    [ -d "$t" ] && d="$t"
    t="/usr/lib64/jvm/java"
    [ -d "$t" ] && d="$t"
# mandriva
    t="/usr/lib/jvm/jre-1.6.0-sun/bin/../"
    [ -d "$t" ] && d="$t"
# gentoo
    t="/opt/sun-jdk-1.5.0.14/"
    [ -d "$t" ] && d="$t"
    t="/opt/sun-jdk-1.6.0.04/"
    [ -d "$t" ] && d="$t"
# debian
if [ -r /etc/debian_version  ]; then
    t="/usr/lib/jvm/java-gcj/jre/bin/../../"
    [ -d "$t" ] && d="$t"
    t="/usr/lib/jvm/java-6-openjdk/jre/bin/../../"
    [ -d "$t" ] && d="$t"
    t="/usr/lib/jvm/java-1.5.0-sun/jre/bin/../../"
    [ -d "$t" ] && d="$t"
    t="/usr/lib/jvm/java-6-sun/jre/bin/../../"
    [ -d "$t" ] && d="$t"
fi
# results
    [ -d "$d" ] && echo "$d"
}

#/// org.eclipse.swt.SWTError: No more handles
#/// [Unknown Mozilla path (MOZILLA_FIVE_HOME not set)]
mozilla_guess_()
{
    t="/usr/lib/mozilla"
    test -r "$t/libxpcom.so" && d="$t"
    t="/usr/lib/firefox/"
    test -r "$t/libxpcom.so" && d="$t"
    t="/usr/lib/iceweasel"
    test -r "$t/libxpcom.so" && d="$t"
    echo "$d"
}

# 
env_()
{
# java
    [ -z ${JAVA_HOME} ] && t=$(java_guess_) && [ -d "$t" ] && JAVA_HOME=$t
    if [ -d "${JAVA_HOME}" ] ; then
        export JAVA_HOME
        JAVA=${JAVA:=${JAVA_HOME}/jre/bin/java}
        [ -x ${JAVA} ] && export JAVA
    else
        JAVA=${JAVA:=java}
        export JAVA
    fi
    
# mozilla
    [ -z ${MOZILLA_FIVE_HOME} ] && t=$(mozilla_guess_) && [ -d "$t" ] && MOZILLA_FIVE_HOME=$t
    if [ -d "$MOZILLA_FIVE_HOME" ] ; then
        export MOZILLA_FIVE_HOME
        export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$MOZILLA_FIVE_HOME
    fi

    [ ! -z ${DEBUG} ] && echo "# MOZILLA_FIVE_HOME=${MOZILLA_FIVE_HOME}"
    [ ! -z ${DEBUG} ] && echo "# JAVA_HOME=${JAVA_HOME}"}
}

tuxguitar_()
{
    PACKAGE=${PACKAGE:=tuxguitar}
    PACKAGE_HOME=${PACKAGE_HOME:=/usr/share/${PACKAGE}/}
    PACKAGE_MAIN=${PACKAGE_MAIN:=org.herac.tuxguitar.gui.TGMain}
    PACKAGE_LIB=${PACKAGE_LIB:=/usr/lib/jni}
# java env
    JAVA=${JAVA:=java}
    CLASSPATH=${CLASSPATH}
    t="${PACKAGE_HOME}"
    [ -d $t ] && CLASSPATH=${CLASSPATH}:$t
    t="${PACKAGE_HOME}/${PACKAGE}.jar"
    [ -r $t ] && CLASSPATH=${CLASSPATH}:$t
    t="/usr/share/java/swt.jar"
    [ -r $t ] && CLASSPATH=${CLASSPATH}:$t
    t="/usr/share/java/itext.jar"
    [ -r $t ] && CLASSPATH=${CLASSPATH}:$t
    JAVA_FLAGS=${JAVA_FLAGS:="-Xms128m -Xmx128m"}
    JAVA_FLAGS="${JAVA_FLAGS}\
 -Djava.library.path=${PACKAGE_LIB} \
 -cp ${CLASSPATH}:${PACKAGE_CLASSPATH}"
# run java
    args="$@"
    t="/usr/share/tuxguitar/tuxguitar.tg"
    [ -z $1 ] && args=$t
    [ ! -z ${DEBUG} ] && ${JAVA} -version
    [ ! -z ${DEBUG} ] && ${JAVA} ${JAVA_FLAGS} ${PACKAGE_MAIN} --version
    [ ! -z ${DEBUG} ] && echo "# ${JAVA} ${JAVA_FLAGS} ${MAINCLASS} $args"
    ${JAVA} ${JAVA_FLAGS} ${PACKAGE_MAIN} $args
}

# main
env_
tuxguitar_ "$@"

#eof "$Id: tuxguitar.sh,v 1.3 2008/04/16 17:04:50 rzr Exp $"