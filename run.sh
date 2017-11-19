sudo /usr/local/src/noip-2.1.9-1/noip2
sudo killall -9 java
java -jar ./server/out/artifacts/server_jar/server.jar &
sudo node ./web/server.js
