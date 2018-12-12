import threading

import paramiko


def restartServer(ip, cmd):
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    client.connect(ip, 22, "root", "Gjbs2016")
    stdin, stdout, stderr = client.exec_command(
        "rm -rf  /{0}/jetty && cp -r /remark/jetty /{0}/jetty&&/{0}/jetty/bin/jetty.sh restart".format(cmd))
    cmd_result = stdout.read(), stderr.read()
    for line in cmd_result:
        print(line)

    client.close()


def restart():
    client = {"120.27.249.102": "opt", "121.43.59.216": "stock"}
    threads = []
    for k, v in client.items():
        threads.append(threading.Thread(target=restartServer, args=(k, v)))
    for x in threads:
        x.start()

import jieba

if __name__ == '__main__':
    restart()

