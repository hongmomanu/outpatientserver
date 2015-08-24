# outpatientserver

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

## License

Copyright © 2015 FIXME

localhost=199.199.4.21
http://localhost:3000/firebydoctorlogin?roomno=100&doctorid=0316 (医生登录接口)
http://localhost:3000/firebycall?roomno=100&area=201&hzxh=11&hzxm=王小明&zsmc=诊室1&status=3  (房间医生触发接口)
http://localhost:3000/updaterefreshtime?times=10000 (后台刷新时间接口单位毫秒)
http://localhost:3000/firebychangeroom?oldno=100&newno=101&newname=儿童科 (房间诊区变更接口)
http://localhost:3000/firetipbyroom?room=11&content=<div style="color:blue;text-align: center;font-size:xx-large">12121212</div>(诊区温馨提示)
http://localhost:3000/firerefreshsystem?room=201(系统刷新)
http://localhost:3000/fireprop?room=08&name=showlines&value=2(诊区大屏显示行数设置)
http://localhost:3000/clearscreen?room=100(小房间清屏)