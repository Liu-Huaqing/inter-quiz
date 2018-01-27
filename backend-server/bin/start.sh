#!/bin/bash

nohup java -Xms2048m -Xmx2048m -Dplay.http.secret.key=quiz -jar -Dhttp.address=10.10.68.210 -Dhttp.port=8080 interact_quiz-assembly-1.0.jar 2>&1 > start.log &




