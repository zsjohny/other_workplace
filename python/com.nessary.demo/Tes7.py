import grpc
import Market_pb2
import Market_pb2_grpc

def run():
    channel = grpc.insecure_channel('tdpqa.goldplusgold.com:18830')
    identification = Market_pb2.Identification.category= Market_pb2.QUOTEMINPAGE
    Market_pb2.Response.identification = identification

if __name__ == '__main__':
    run()
