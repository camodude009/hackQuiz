var express = require('express');
var app = express();
var io = require('socket.io');
var bodyParser = require('body-parser');
var http = require("http").Server(app);

app.use(express.static('public'));

app.use("/config", bodyParser.json());

http.listen(80, () => console.log('listening on *:80'));

let server = io(http);

let tables = [{
  id: 1,
  seats: 4
},{
  id: 2,
  seats: 4
},{
  id: 3,
  seats: 5
},{
  id: 4,
  seats: 6
}];

let users = [{info: {
  name: "a",
  gender: 0, age: 1, hair: 4, color: 3, alcohol: 2
}},{info: {
  name: "b",
  gender: 1, age: 3, hair: 4, color: 2, alcohol: 1
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
  startTime: Date.now()+3000
}

app.get("/config/quiz", (req, res) => {
  console.log(req.body)
});

app.get("/config/tables", (req, res) => {
  console.log(req.body);
});

onQuizStart(() => {
  quiz.running = true;
  if (users.filter(el => el.info).length > 0) {
    quiz.matching = createMatching();
    console.log(quiz.matching);
    users.filter(el => el.info).forEach(usr => {
      usr.socket.emit("matching", {table: quiz.matching.find(m => m.users.find(u => u == usr.info.name)).table});
    })
  }
})

server.on('connection', (socket) => {
  let user = {
    socket: socket,
    info: null
  }
  users.push(user);

  socket.emit("quiz", {startTime: quiz.startTime});

  socket.on('register-user', (usr, callback) => {
    if (!quiz.running) {
      if (!user.info) {
        if (users.filter(el => el.info).length < tables.reduce((sum, t) => sum + t.seats, 0)) {
          if (!users.find(el => el.info && el.info.name == usr.name)) {
            user.info = usr;
            callback({});
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
  setTimeout(() => {
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

let paring = {
  gender: [[2,3]],
  age: [],
  hair: [[0,1],[2,3]],
  color: [[0,1],[3,4]]
}

function createMatching() {

  let _tables = tables.slice();
  let _users = users.filter(el => el.info).map(el => el.info);

  for (let i = tables.reduce((sum, el) => sum + el.seats, 0); i > _users.length; i--) {
    if (_tables[_tables.length-1].seats <= 1) {
      _tables.splice(_tables.length-1, 1);
    } else {
      _tables[_tables.length-1].seats--;
    }
  }

  console.log(_tables);

  let min, max;
  do {
    min = _tables.reduce((min, t) => t.seats < min.seats ? t : min, _tables[0]);
    max = _tables.reduce((max, t) => t.seats > max.seats ? t : max, _tables[0]);
    max.seats--;
    min.seats++;
  } while(min.seats + 1 < max.seats);

  console.log(_tables);

  let match =  _tables.map(t => ({
    table: t.id,
    users: _users.splice(Math.floor(Math.random()*_users.length), 1)
  }));
  console.log(match.map(t => t.users.map(u => u.name)));
  let i = 0;
  while (_users.length > 0) {
    match[i].users.push(_users.splice(_users.indexOf(bestUser(match[i].users[0], _users), 1)));
    i = (i+1) % tables.length;
  }
  match.forEach(t => t.users = t.users.map(el => el.name));
  return match;
}

function bestUser(usr, _users) {
  return _users.map(u => {
    let pnts = 0;
    for (let p in u) {
      if (p == "alcohol") {
        if (u.alcohol == usr.alcohol) pnts += 2;
      } else if (u[p] != usr[p] && paring[p] && !paring[p].find(el => el && el.find(e => e == usr[p]) && el.find(e => e == u[p])))
        pnts += 1;
    }
    u.pnts = pnts;
    return u;
  }).reduce((max, el) => el.pnts > max.pnts ? el : max, {pnts: -1});
}
