import logging
import tempfile

import paramiko

logging.basicConfig(level=logging.INFO, format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S',
                    filename='test.log',
                    filemode='w')


def link(ip, cmd):
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    client.connect(ip, 22, "root", "985595")
    stdin, stdout, stderr = client.exec_command(
        "sed  -i '$c sqls={}; ' /stock/sql.cnf && sqladvisor -f /stock/sql.cnf -v 1".format(cmd))
    cmd_result = stdout.read(), stderr.read()
    for line in cmd_result:
        if len(str(line).strip()) == 0:
            continue
        line = line.decode("utf-8")

        arr = str(line).split("\n")
        for li in arr:
            if "SQLAdvisor结束" in li:
                continue
            print(li)

        logging.info("---analysis : ---%s" % line)

    client.close()


if __name__ == '__main__':
    path = tempfile.gettempdir()
    file = open(path + "/sqlInput.db", "r")
    link("120.55.67.48", file.readline())
