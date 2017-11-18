# hackQuiz

## Inspiration

We wanted to bring the all beloved PubQuiz to a Smart-Bar for Hackers. The Quiz shall enhance the sozial Aspects of a Bar-Night by bringing People together and provide an Full-Evening Entertainment.

## What it does

Every Quiz-Fan can participate by simply scan a Barcode when entering the Smart-Bar. Thru a intuitive WebApp he or she provides a few basic and fun Facts about himself, which are used by an intelligent Matching-Algorithm to create Groups for each Quiz-Table. These groups are matched to a very diverging and interesting Composition of the Quizzers, which means that e.g. People of different Gender or Age are grouped together.

At the Quiz-Tables are Tablets, that provide a intuitive User-Interface for the Quiz where each Group has to answer different Trivia-Questions on the subject of Computer Science and Hacking in a given amount of time.
After a series of Questions, the Winning Team will be announced automatically and get a round of Shots.

## How We built it

The entire Project is build in Java and Javascript.
The Registration-WebApp and the Serverside Matching System is implemented in Javascript and with Nodejs.
The Quiz-App is build for Android and the Questioning System is implemented in Java.

There are a number of Network-Connections, that tie the Systemparts together. These are Tcp-Sockets on the JavaServer to Android side, an http-Request-based connection between the JavaServer and NodejsServer, and a Websocket-based Communication used by the WebApp and the NodejsServer.

## Challenges We ran into

We encountered the most Challenges with the Communication Part of the Project. Especially the Communication between browserside-WebApps and Java-Servers contained some major incompabilities of used Communication-Protocolls, which lead us to split the Work to a Java- and a Nodejs-Server and build upon http-Request-based Communication.

## Accomplishments that We're proud of

Not once a Teammate had to re-clone our Git-Repository due to major Merge-Conflicts etc.! :D
We had a great Team-Communication which goes hand in hand with us working together like a Spider.
(As Spiderman, just the other way around... 4 Teammembers... each 2 Arms...)

Before we began coding, me made some detailed Concepts and Diagrams, which helped us a lot in future development. Therefor we could use the Software-Engineering techniches learned during our studies.

## What We learned

We definitely learned a lot about different Communication-Protokolls.
We could also see how some of the Team-Managing Aspects of Software-Engineering played out.

## What's next for hackQuiz

We developed hackQuiz with the goal of expandability in mind. There are a lot of additional SmartBar related Features, that could be implemented at the Quiz-Tablets, like Musicselection, Requesting for Service, Browsing the Cocktail Menu and Ordering.
Also there are a lot of Quiz-Related aspects that can be improved or can be addded, like different Game-Modes, more Questions, or other Topics.
