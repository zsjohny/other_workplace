import re


def analysis():
    with open("./gc.log", "r+") as f:
        line = f.readline()
        while line:
            if re.match(r"^\d", str(line)):
                number = line.split(":")[2].split(",")[0].split("]")
                index = 0
                lastClac = 0
                if line.find("[Full") > 0:
                    line = f.readline()
                    continue
                for tmp in number:
                    index += 1
                    clacArr = tmp.split("(")[0].replace("K", "").split("->")
                    tmp = int(clacArr[0].lstrip()) - int(clacArr[1])
                    if index == 1:
                        lastClac = tmp
                    else:
                        print(tmp - lastClac)
            line = f.readline()


if __name__ == "__main__":
    analysis()
