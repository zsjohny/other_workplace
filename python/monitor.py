import os


def monitor():
    with open("monitor.txt", "w+", encoding="utf-8") as f:
        # while True:
        echo = os.system("top")
        f.write(echo)


if __name__ == '__main__':
    monitor()
