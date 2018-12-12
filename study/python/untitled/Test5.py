import logging
import socket
import time

import tornado.ioloop
import tornado.iostream


# Init logging
def init_logging():
    logger = logging.getLogger()
    logger.setLevel(logging.DEBUG)
    sh = logging.StreamHandler()
    formatter = logging.Formatter('%(asctime)s -%(module)s:%(filename)s-L%(lineno)d-%(levelname)s: %(message)s')
    sh.setFormatter(formatter)
    logger.addHandler(sh)
    logging.info("Current log level is : %s", logging.getLevelName(logger.getEffectiveLevel()))


class TCPClient(object):
    def __init__(self, host, port, io_loop=None):
        self.host = host
        self.port = port
        self.io_loop = io_loop
        self.shutdown = False
        self.stream = None
        self.sock_fd = None
        self.EOF = b' END'

    def get_stream(self):
        self.sock_fd = socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0)
        self.stream = tornado.iostream.IOStream(self.sock_fd)
        self.stream.set_close_callback(self.on_close)

    def connect(self):
        self.get_stream()
        self.stream.connect((self.host, self.port), self.send_message)

    def on_receive(self, data):
        logging.info("Received: %s", data)
        self.stream.close()

    def on_close(self):
        if self.shutdown:
            self.io_loop.stop()

    def send_message(self):
        logging.info("Send message....")

        message = b'''
        identification {
            category: HEARTBEAT
        }
        heartbeat
        {
            info: "TD1111"
        }
        '''
        # self.stream.write(b"Hello Server!" + self.EOF)
        # self.stream.write(message)
        time.sleep(2)
        self.stream.write(b'14')

        self.stream.read_until(self.EOF, self.on_receive)
        logging.info("After send....")

    def set_shutdown(self):
        self.shutdown = True


def main():
    init_logging()
    io_loop = tornado.ioloop.IOLoop.instance()
    c1 = TCPClient("localhost", 18830, io_loop)
    c1.connect()
    logging.info("**********************start ioloop******************")
    io_loop.start()


if __name__ == "__main__":
    try:
        main()
    except Exception  as ex:
        print("Ocurred Exception: %s" % str(ex))
        quit()
