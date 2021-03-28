Web application, counts size of symbols in text with and without spaces. Written in java with Vert.x and Apache 
FreeMaker. Proper handling 
of symbols, which do not fit in two bytes are not guaranteed 🙃.
#Build:
`mvn clean package`

#Install:

`cp target/symbol-counter*dependencies.jar /usr/bin/symbol-counter.jar`

`cp deployment/symbol-counter.service /etc/systemd/system/symbol-counter.service`

`systemctl start symbol-counter`
