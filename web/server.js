var express = require('express');
var app = express();
var io = require('socket.io');
var bodyParser = require('body-parser');
var http = require("http").Server(app);
var fetch = require("node-fetch");

app.use(express.static('public'));

app.use("/config", bodyParser.json());

http.listen(80, () => console.log('listening on *:80'));

let server = io(http);

let tables = [{
	  id: 1,
	  seats: 2
	},{
	  id: 2,
	  seats: 4
	},{
	  id: 3,
	  seats: 3
	}];

let users = [{info: {
  name: "a",
  gender: 0, age: 1, hair: 4, color: 3, alcohol: 2
}},{info: {
  name: "b",
  gender: 0, age: 1, hair: 4, color: 3, alcohol: 1
}},{info: {
  name: "c",
  gender: 2, age: 3, hair: 4, color: 1, alcohol: 2
}},{info: {
  name: "d",
  gender: 0, age: 0, hair: 2, color: 1, alcohol: 0
}},{info: {
  name: "e",
  gender: 2, age: 2, hair: 1, color: 0, alcohol: 0
}}];

let quiz = {
  running: false,
  startTime: null
}

newQuiz( Date.now()+50000);

//configure next quiz
app.post("/config/quiz", (req, res) => {
  if (!quiz.running && req.body && req.body.startTime) {
    newQuiz(Date.now() + parseInt(req.body.startTime));
    console.log("Quiz: ", quiz);
  }
  res.end();
});

//configure tables
app.post("/config/tables", (req, res) => {
  if (!quiz.running && req.body && req.body.tables) {
    tables = req.body.tables;
    console.log("Tables: ", tables);
  }
  res.end();
});

app.post("/config/remove", (req, res) => {
  if (req.body && req.body.username && users.find(u => u.info && u.info.name == req.body.username)) {
    users.splice(users.indexOf(users.find(u => u.info && u.info.name == req.body.username)), 1);
    console.log("Remove: ",req.body.username)
    res.send(users.filter(u => u.info).map(u => u.info));
  } else {
    res.end();
  }
});

//when quiz is starting
function newQuiz(startTime) {
  quiz.startTime = startTime;
  quiz.running = false;
  users.filter(el => el.socket).forEach(u => u.socket.emit("quiz", {startTime: quiz.startTime}));;
  onQuizStart(() => {
    quiz.running = true;
    if (users.filter(el => el.info).length > 0) {
      //create a matching
      quiz.matching = createMatching();
      console.log(quiz.matching);
      //send table numbers to all users
      users.filter(el => el.info).forEach(usr => {
        usr.matching = {table: quiz.matching.find(m => m.users.find(u => u == usr.info.name)).table};
        if (usr.socket) usr.socket.emit("matching", usr.matching);
      })

      sendMatchingToServer();
    }
    quiz.running = false;
  })
}

function sendMatchingToServer() {
  fetch("http://131.159.211.197:8000/matching", {
    method: "POST",
    body: {matching: quiz.matching}
  })
  .catch(console.error);
}

server.on('connection', (socket) => {
  let user = {
    socket: socket,
    info: null
  }
  users.push(user);

  socket.emit("quiz", {startTime: quiz.startTime});

  socket.on('register-by-token', (data, callback) => {
    let usr = users.find(el => el.token == data.token);
    if (usr) {
      users.splice(users.indexOf(user), 1);
      usr.socket = socket;
      user = usr;
      let ret = {token: usr.token, username: usr.info.name};
      if (usr.matching && quiz.startTime < Date.now()) ret.matching = usr.matching;
      callback(ret);
    } else {
      callback({error: "Re-Login failed!"});
    }
  })

  socket.on('register-user', (usr, callback) => {
    if (!quiz.running) {
      if (!user.info) {
        if (users.filter(el => el.info).length < tables.reduce((sum, t) => sum + t.seats, 0)) {
          if (!users.find(el => el.info && el.info.name == usr.name)) {
            user.info = usr;
            user.token = getToken();
            callback({token: user.token});
          } else {
            callback({error: "Name already taken!"});
          }
        } else {
          callback({error: "All Seats taken."});
        }
      } else {
        callback({error: "Already registered!"});
      }
    } else {
      callback({error: "Quiz is running!"})
    }
  });

});

function onQuizStart(onStart) {
  console.log((quiz.startTime - Date.now()) / 1000);
  //wait till quiz starts
  setTimeout(() => {
    //check if startTime has changed and wait again if yes
    if (Date.now() >= quiz.startTime)
      onStart();
    else
      onQuizStart(onStart);
  }, quiz.startTime - Date.now())
}


/*
  gender:
  diverge
  0 - 1 - [2, 3]
  age:
  diverge
  0 - 1 - 2 - 3
  hair
  diverge
  [0, 1] - [2, 3] - 4
  color
  diverge
  [0, 1] - 2 - [3, 4]
  alc
  equals
*/

//used for matching algorithm
let paring = {
  gender: [[2,3]],
  age: [],
  hair: [[0,1],[2,3]],
  color: [[0,1],[3,4]]
}

function createMatching() {

  let _tables = JSON.parse(JSON.stringify(tables));
  let _users = users.filter(el => el.info).map(el => el.info);

  //reduce table seats to count of users
  for (let i = tables.reduce((sum, el) => sum + el.seats, 0); i > _users.length; i--) {
    let min = _tables.reduce((min, t) => t.seats < min.seats ? t : min, _tables[0]);
    if (min.seats <= 1) {
      _tables.splice(_tables.indexOf(min), 1);
    } else {
      min.seats--;
    }
  }

  console.log(_tables);

  //equally divide all free seats
  let min, max;
  do {
    min = _tables.reduce((min, t) => t.seats < min.seats ? t : min, _tables[0]);
    max = _tables.reduce((max, t) => t.seats > max.seats ? t : max, _tables[0]);
    console.log(min, max);
    if (tables.find(el => el.id == min.id).seats > min.seats) {
      max.seats--;
      min.seats++;
    } else {
      break;
    }
  } while(min.seats + 1 < max.seats);

  console.log(_tables);

  //set a first user at each table
  let match =  _tables.map(t => ({
    table: t.id,
    users: _users.splice(Math.floor(Math.random()*_users.length), 1)
  }));

  console.log(match.map(t => t.users.map(u => u.name)));

  //match all other users to the first users on each table
  let i = 0;
  console.log(_users.length, match.length);
  while (_users.length > 0) {
    let ind = _users.indexOf(bestUser(match[i].users[0], _users));
    let u = _users.splice(ind, 1)[0];
    console.log(ind, u.name, _users.length);
    match[i].users.push(u);
    i = (i+1) % _tables.length;
  }

  match.forEach(t => t.users = t.users.map(el => el.name));
  return match;
}

//get the best match for usr from _users
function bestUser(usr, _users) {
  return _users.map(u => {
    let pnts = 0;
    for (let p in u) {
      if (p == "alcohol") {
        if (u.alcohol == usr.alcohol) pnts += 2; //same alc should be together
      } else if (u[p] != usr[p] && paring[p] && !paring[p].find(el => el && el.find(e => e == usr[p]) && el.find(e => e == u[p])))
        pnts += 1; //different properties should be together
    }
    u.pnts = pnts;
    return u;
  }).reduce((max, el) => el.pnts > max.pnts ? el : max, {pnts: -1}); //get user with most matching-points
}


var tokenlength = 8;
function getToken() {
  return Math.round(Math.random()*(Math.pow(16, tokenlength)-1)+Math.pow(16, tokenlength-1)).toString(16);
}
