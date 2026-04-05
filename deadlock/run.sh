export JAVA_HOME=/home/vuavisao/.jdks/openjdk-25.0.2
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw -q compile exec:java -Dexec.mainClass="com.deadlock.algorithms.Main" -Dexec.args="Detection /home/vuavisao/Documents/World-OS/deadlock/src/main/resources/test/detection1.txt"
