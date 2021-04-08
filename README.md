# symbol-counter
Web application, counts symbols in a text, including and excluding spaces. Written in Java with Vert.x and Apache 
FreeMaker. Proper handling of symbols, which do not fit in two bytes are not guaranteed 🙃.
##Build:
`mvn clean package`

##Install:

`cp target/symbol-counter.jar /usr/bin/symbol-counter.jar`

`cp target/symbol-counter.service /etc/systemd/system/symbol-counter.service`

`systemctl start symbol-counter`
`systemctl enable symbol-counter`
