include "./interfaces/GoogleCloudLoggingInterface.iol"

main{
    syncRequest.logName ="testJolie"
    syncRequest.severity = "ERROR"
    syncRequest.payload.node1 = "node1"
    syncRequest.payload.node2.subNode = "dsa"
    syncRequest.payload.node2.subNodeA = "fffa"
    syncWriteLog@GoogleCloudLogging( syncRequest )(  )


    readRequest.logName ="testJolie"
    readRequest.severity = "ERROR"
    readLogs@GoogleCloudLogging( readRequest )(  )
}
