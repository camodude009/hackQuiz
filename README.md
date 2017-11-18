# hackQuiz

## Elevator pitch

A socialising, diversifying pub-quiz, spicing up Hack-Pub, while providing ALL the functionality you could ask for.
## Inspiration

We wanted to bring the all beloved PubQuiz to a Smart-Bar for Hackers. It will enhance the social aspects of a night out, getting people to socialise outside of their own friend-group and providing full evening entertainment.

## What it does

Every quiz fan participates by simply scanning a barcode printed on their table. Through an intuitive web app ther hacker provides basic and fun facts about themselves. Thses are then used by an intelligent Matching-Algorithm, shuffeling together groups for each quiz-table. These groups are matched to maximise diversity and create interesting compositiona of hackers. (mood-color, age, etc.)

Once at the talbles, users use their own, or a provieded Android device, to access an intuitive UI for taking part in the quiz.
From here on out, each group answers different trivia questions on the subject of computer science and hacking in a given amount of time.
After each quiz, the winning team will be selected and get a round of shots or other alcoholic beverages.

## How we built it

The entire Project is build in Java and Javascript.
The registration - web app and the serverside matching system are implemented in Javascript, with Nodejs.
The quiz-app is built for Android and the serverside quiz back-end is implemented in Java.

There are a number of network-connections, that tie the different parts together. These consist of tcp-Sockets on the quiz-server/Android side, an http-request-based connection between the JavaServer and NodejsServer, and a websocket-based communication used by the web app and the NodejsServer.

## Challenges we ran into

Most of the challenges we encountered concerned the cross-plattform communication part of the project. Especially the browserside - web apps and the java server caused major incompabilities because of used communication-protocolls. This caused us to split the work to a Java- and a Nodejs-Server and build upon http-request-based communication.

## Accomplishments that we're proud of

Not once did any teammember have to re-clone our git-repo due to merge-conflicts! :D
Team communication was awesome! We quickly decided on our project, had a great time brainstorming and finished almost everythin on scedule.

Before we began coding, me made detailed concepts and diagrams, which helped us a lot in developing interfaces and dealing with cross plattform communication. We were able to directly apply software development practices learned at university.

## What We learned

A lot of progress was made on understanding cross plattform communication and different communication-protocolls.
The most important thing we learned was: communication early on avoids lot of hurt in the longrun.

## What's next for hackQuiz

We developed hackQuiz with expandability in mind. 
Tons of additional HackBar features can be integrated into our project. These include chaning table color, music selection/voting, calling a bartender, browsing the cocktail menu and directly ordering from the table.
Also there are a lot of Quiz-Related aspects that can be improved or can be addded: different game modes, more question types, or other topics.

As of right now, (Saturday, 18:00) these feature are in development! We plan on completing as many as possible before the final deadline.