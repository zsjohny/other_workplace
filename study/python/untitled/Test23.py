import paramiko


def restartServer(ip, cmd):
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    client.connect(ip, 22, "root", "Gjs2016")
    stdin, stdout, stderr = client.exec_command(cmd)
    cmd_result = stdout.read(), stderr.read()
    for line in cmd_result:
        print(line)


if __name__ == '__main__':
    restartServer("120.27.249.102", "/opt/jetty/bin/jetty.sh restart")
