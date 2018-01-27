# -*- coding: utf-8 -*-

import json

import requests
import websocket

try:
    import thread
except ImportError:
    import _thread as thread


class User(object):
    url = "http://localhost:9000/user"

    def __init__(self, name):
        self.name = name
        self.id = None

    def register(self):
        data = {"name": self.name}
        response = requests.post(url=self.url, json=data, headers={"Content-Type": "application/json"})
        self.id = json.loads(response.text)['id']


class WebSocket(object):
    def __init__(self, user_id):
        self.user_id = user_id
        self.cookies = "userId=%s" % self.user_id
        self.now_question = None

    def on_message(self, ws, message):
        msg = json.loads(message)
        if msg['command'] == 'question' or msg['command'] == 'finish':
            # print message
            self.now_question = msg

    def on_error(self, ws, error):
        print(error)

    def on_close(self, ws):
        print("### closed ###")

    def on_open(self, ws):
        def run(*args):
            while True:
                command = raw_input("please input command: ")
                if command == "end":
                    ws.close()
                    break
                elif command == "next_answer":
                    ws.send("""
                    {
                        "command": "%s",
                        "question_id": %s
                    }
                    """ % (command, self.now_question['question_id']))
                else:
                    ws.send('{"command": "%s"}' % command)
            print("thread terminating...")

        thread.start_new_thread(run, ())

    def connect(self):
        ws = websocket.WebSocketApp("ws://localhost:9000/ws",
                                    on_message=self.on_message,
                                    on_error=self.on_error,
                                    on_close=self.on_close,
                                    cookie=self.cookies
                                    )
        ws.on_open = self.on_open
        ws.run_forever()


if __name__ == "__main__":
    user = User("毅哥admin")
    user.register()

    ws = WebSocket(user_id=user.id)
    ws.connect()
