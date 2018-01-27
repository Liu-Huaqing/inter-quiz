/********************************* front-end config start ***********************************/

var config = {
  apiServerHostPort: "api.inter-quiz.top:8080",
  adminName: "我是admin"
}

/********************************* front-end config end **************************************/
var app = new Vue({
  el: '#app',
  data: {
    state: 'waiting', // input-name, waiting, question, answer, finish
    alive: false,
    isAdmin: false,
    online_user: 0,
    username: "",
    admin: {
      question_category: 0,
      winners_count: 1
    },
    question: {
      id: "123",
      title: "孙俪的老公是谁",
      options: ["王宝强", "胡歌", "邓超"],
      selected_index: -1,
      timeCount: 10
    },
    answer: {
      id: "123",
      title: "孙俪的老公是谁",
      options: ["王宝强", "胡歌", "邓超"],
      stat: [3, 3, 1800],
      answer_index: 2,
      selected_index: 1,
      answer_animation_delay: 500
    },
    finish: {
      winners: ["小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李","小王小","小李"]
    }
  },
  computed: {
    stateBarCls: function() {
      if (this.state === 'question') {
        if (this.question.timeCount > 4) {
          return "panel-state-bar-healthy"
        }
        if (this.question.timeCount > 0) {
          return "panel-state-bar-danger"
        }
        if (this.question.timeCount <=0) {
          return "panel-state-bar-disabled"
        }
      } else {
        return "panel-state-bar-healthy"
      }
    },
    adminButtonText: function() {
      switch(this.state) {
        case "question":
          return "查看答案";
        case "answer":
          return "下一题";
        case "waiting":
          return "开始答题";
        default:
          return "error";
      }
    },
    stateBarText: function() {
      if (this.state === 'input-name' || this.state === 'waiting') {
        return "游戏马上开始";
      }

      if (this.state === 'question') {
        return "还剩 " + this.question.timeCount + " 秒";
      }

      if (this.state === 'answer') {
        return "查看答案";
      }
    },
    answerStatWidth: function() {
      if (this.answer.answer_animation_delay > 0) {
        var that = this;
        window.setTimeout(function() {
          that.answer.answer_animation_delay = 0;
        }, this.answer.answer_animation_delay)

        return ["5%", "5%", "5%"];
      }

      var sum = 0;
      var widthList = [];
      sum = this.answer.stat[0] + this.answer.stat[1] + this.answer.stat[2];

      if (sum === 0) {
        return ["10%", "10%", "10%"];
      }

      for (var i=0; i<3; i++) {
        var width = Math.floor(this.answer.stat[i] * 100 / sum);
        width = width < 10 ? 10 : width;
        widthList.push(width + "%");
      }

      return widthList;
    },
    answerClses: function() {
      var resCls = [];
      for (var i = 0; i<3; i++) {
        if (i === this.answer.answer_index) {
          resCls.push("answer-option-right");
        } else if (i === this.answer.selected_index) {
          resCls.push("answer-option-user-wrong")
        } else {
          resCls.push("answer-option-normal")
        }
      }
      return resCls;
    },
    questionClses: function() {
      // 用户已经失败
      if (!this.alive || this.isAdmin) {
        return ["question-option-disabled", "question-option-disabled", "question-option-disabled"]
      }

      // 时间到了用户依然没有选择
      if (this.question.timeCount <= 0 && this.question.selected_index < 0) {
        return ["question-option-disabled", "question-option-disabled", "question-option-disabled"]
      }

      // 正在选择
      if (this.question.selected_index < 0) {
        return ["question-option-available", "question-option-available", "question-option-available"]
      }

      // 已经选择
      var resCls = [];
      for (var i = 0; i<3; i++) {
        if (i === this.question.selected_index) {
          resCls.push("question-option-selected");
        } else {
          resCls.push("question-option-disabled")
        }
      }
      return resCls;
    }
  },
  methods: {
    adminCmd: function() {
      var sendCmd = function(jn) {
        ws.send(JSON.stringify(jn));
      };

      if (this.state === "question") {
        sendCmd({
          command: "next_answer",
          question_id: this.question.id
        });
      }

      if (this.state === "answer") {
        sendCmd({
          command: "next_question"
        });
      }

      if (this.state === "waiting") {
        sendCmd({
          command: "start",
          question_category: this.admin.question_category,
          winners_count: this.admin.winners_count
        });
      }
    },
    resetCmd: function() {
      var sendCmd = function(jn) {
        ws.send(JSON.stringify(jn));
      };

      sendCmd({
        command: "reset"
      });
    },
    selectAnswer: function(index) {
      if (this.alive
          && !this.isAdmin
          && this.question.timeCount > 0
          && this.question.selected_index === -1
      ) {
        this.question.selected_index = index;
        ws.send(JSON.stringify({
          command: "question_ack",
          question_id: this.question.id,
          selected_index: this.question.selected_index
        }));
      }
    },
    sendName: function() {
      var xhr = new XMLHttpRequest();
      xhr.open("POST", "http://" + config.apiServerHostPort  + "/user");
      xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
      xhr.setRequestHeader('Access-Control-Allow-Methods', 'GET, POST, PUT');
      xhr.setRequestHeader('Access-Control-Allow-Headers', 'Content-Type');
      xhr.withCredentials = true;
      xhr.onload = function() {
          if (xhr.status === 200) {
              buildSocket();
          }
          else if (xhr.status !== 200) {
              alert('服务端错误...');
          }
      };
      xhr.send(encodeURI('name=' + this.username));
      if (this.username === config.adminName) {
        this.isAdmin = true;
      }
    }
  }
});

var timeCountIntervalId = -1;
var startTimeCount = function() {
  timeCountIntervalId = window.setInterval(function() {
    if (app.question.timeCount > 0) {
      app.question.timeCount--;
    } else {
      window.clearInterval(timeCountIntervalId);
    }
  }, 1000);
};

/********************************* 注册 socket，并接收 socket 信息 ***********************************/
var ws = null;

// 构建初始 socket
function buildSocket() {
  // 如果已经创建，则退出，防止多次创建
  if (ws) {
    console.log("Avoiding recreate the websocket");
    return;
  }

  console.log("building socket...");

  ws = new ReconnectingWebSocket("ws://" + config.apiServerHostPort  + "/ws", null, {
    debug: true
  });
  ws.onopen = function(evt) {
    console.log("Connection open ...");
  };

  ws.onclose = function(evt) {
    console.log("Connection closed...");
  };

  ws.onmessage = function(evt) {
    console.log( "Received Message: " + evt.data);
    var cmd = JSON.parse(evt.data);

    if (cmd.command != "heartbeat") {
      // clear state
      window.clearInterval(timeCountIntervalId);
      app.alive = cmd.alive;
    }

    if (cmd.command === "waiting") {
      app.state = "waiting";
    }
    if (cmd.command === "question") {
      app.question.id = cmd.question_id;
      app.question.options = cmd.options;
      app.question.title = cmd.title;
      app.question.selected_index = -1;
      app.question.timeCount = 10;
      app.state = "question";

      startTimeCount();
    }

    if (cmd.command === "answer") {
      app.answer.id = cmd.id;
      app.answer.options = cmd.options;
      app.answer.title = cmd.title;
      app.answer.stat = cmd.stats;
      app.answer.answer_index = cmd.answer_index;
      app.answer.selected_index = cmd.selected_index;
      app.answer.answer_animation_delay = 500;
      app.state = "answer";
    }
    if (cmd.command === "finish") {
      app.state = "finish";
      app.finish.winners = cmd.winners;
    }
    if (cmd.command === "reset") {
      app.state = "input-name";
      app.online_user = 0;
      clearSocket();
    }
    if (cmd.command === "heartbeat") {
      app.online_user = cmd.online;
    }
  };
}

function clearSocket() {
  if (ws) {
    ws.close();
    ws = null;
  }
}