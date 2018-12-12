import threading

threads = []


class runs(threading.Thread):
    def __init__(self, id=1, name="sd"):
        threading.Thread.__init__(self, name=name)
        self.id = id
        self.name = name


    def run(self):
        print("Name: %s + do..%s" % (str(self.id), self.name))
        super.run(self)

    def start(self):
        print("Name: %s +do..%s" % (str(self.id), self.name))
        super.start(self)


for i in range(16):
    threads.append(runs(id=i))

for v in threads:
    v.start()
for v in threads:
    v.join()

print("end")
