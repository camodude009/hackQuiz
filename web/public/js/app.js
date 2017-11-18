
let SERVER_HOST = "ws://localhost:80";

let app = new Vue({
  el: "#vue",
  data: {
    socket: null,
    user: {
      error: null,
      registered: false,
      name: "",
      info: [ // all user properties
        {
          title: "age",
          status: 0,
          options: [
            {title: "Select your age", val: -1},
            {title: "0 - 19", val: 0},
            {title: "20 - 25", val: 1},
            {title: "26 - 30", val: 2},
            {title: "31 - 💀", val: 3}
          ],
          value: -1
        },
        {
          title: "gender",
          status: 0,
          options: [
            {title: "Select your Gender", val: -1},
            {title: "Male", val: 0},
            {title: "Female", val: 1},
            {title: "Unicorn", val: 2},
            {title: "All of the above", val: 3}
          ],
          value: -1
        },
        {
          title: "hair",
          status: 0,
          options: [
            {title: "Select your Haircolor", val: -1},
            {title: "Blonde / Brown", val: 0},
            {title: "Black", val: 1},
            {title: "Red", val: 2},
            {title: "Gray", val: 3},
            {title: "Rainbow", val: 4}
          ],
          value: -1
        },
        {
          title: "color",
          status: 0,
          options: [
            {title: "Select the Color of your Heart", val: -1},
            {title: "Orange", val: 0},
            {title: "Red", val: 1},
            {title: "Pink", val: 2},
            {title: "Blue", val: 3},
            {title: "Black", val: 4}
          ],
          value: -1
        },
        {
          title: "alcohol",
          status: 0,
          options: [
            {title: "Select your favourite Alcohol", val: -1},
            {title: "Gin", val: 0},
            {title: "Whiskey", val: 1},
            {title: "Beer", val: 2},
            {title: "Wine", val: 3},
            {title: "Cocktail from all above!", val: 4},
            {title: "Spezi", val: 5}
          ],
          value: -1
        }
      ]
    },
    quiz: {
      matching: null,
      remainingTime: "0",
      startTime: null,
      timer: null //used for updating 'remainingTime'
    }
  },
  created() {

    this.socket = io(SERVER_HOST);

    this.socket.on("quiz", this.onNextQuiz);
    this.socket.on("matching", this.onMatching);

  },
  methods: {
    sendReady() {
      if (!this.user.name || this.user.info.find(el => el.value == -1)) { // is a property not filled out correctly?
        this.user.namestat = this.user.name ? 0 : 1;
        this.user.info.filter(el => el.value == -1).forEach(e => e.status = 1);
      } else {
        this.user.registered = true; // send 'ready' event
        this.socket.emit("register-user", this.user.info.reduce((json, e) => { json[e.title] = e.value; return json; }, {name: this.user.name}), (res) => {
          if (res.error)
            this.user.error = res.error;
          else
            $("html").animate({"scrollTop": 0}, 500, () => {
              $(".user-wrapper").fadeOut();
            });
        });
      }
    },
    onNextQuiz(next) {
      console.log(next);
      this.quiz.startTime = next.startTime;
      this.quiz.remainingTime = this.toTimeString(this.quiz.startTime - Date.now() < 0 ? 0 : this.quiz.startTime - Date.now());
      this.quiz.timer = setInterval(() => {
        this.quiz.remainingTime = this.toTimeString(this.quiz.startTime - Date.now() < 0 ? 0 : this.quiz.startTime - Date.now());
      }, 1000);
      let dur = (this.quiz.startTime - Date.now())/2;
      console.log(dur);
      $("#ll").css("transform", "rotate(0)");
      $("#rl").css("transform", "rotate(0)");

      $("#ll").animate({"textIndent": "180"}, {
        duration: dur,
        easing: "linear",
        step: function(now,fx) {
          $(this).css('transform','rotate('+now+'deg)');
        }
      });
      setTimeout(() => {
        $("#rl").animate({"textIndent": "180"}, {
          duration: dur,
          easing: "linear",
          step: function(now,fx) {
            $(this).css('transform','rotate('+now+'deg)');
          }
        });
      }, dur);
    },
    onMatching(matching) {
      clearInterval(this.quiz.timer);
      this.quiz.remainingTime = "00:00:00";
      this.quiz.matching = matching;
    },
    toTimeString(ms) {
      let t = Math.round(ms / 1000);
      let s = t%60;
      let m = (t-s)/60%60;
      let h = (t-m*60-s)/60/60;
      return (h<10?"0":"")+h+":"+(m<10?"0":"")+m+":"+(s<10?"0":"")+s;
    },
    scrollDown() {
      $("html").animate({scrollTop: $(".user-wrapper").position().top+"px"}, 800);
    }
  }
})
