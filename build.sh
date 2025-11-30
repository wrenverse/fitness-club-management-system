javac -d bin -classpath postgresql-42.7.8.jar app/*.java app/db/*.java app/db/helper/*.java
jar cfm App.jar ./META-INF/MANIFEST.MF -C bin/ .
