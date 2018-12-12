import paramiko
class SSH:
    def connec(self, cmd):
        try:
            ##连接远程主机
            client = paramiko.SSHClient()
            client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
            client.connect("47.97.174.183", 22, 'root', '879227577')
            stdin, stdout, stderr = client.exec_command(cmd)
            ##读取信息
            for line in stdout:
                print(line)
            ##关闭连接
        except Exception as e:
            print(e)
        finally:
            client.close()


