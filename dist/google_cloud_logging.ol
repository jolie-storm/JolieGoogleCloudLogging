
type SyncWriteLog:void{
  logName:string
  severity:string
  payload:undefined
}

type SyncWriteLogReponse:void


type ReadLogsRequests:void{
    logName:string
    severity:string
}

type ReadLogsResponse:void{
  logs*:void{
    severity:string
    logName:string 
    payload:undefined
    insertId:string
    timestamp:long
  }
}




interface GoogleCloudLoggingInterface {
    RequestResponse:
    syncWriteLog(SyncWriteLog)(SyncWriteLogReponse),
    readLogs(ReadLogsRequests)(ReadLogsResponse)

}

service GoogleCloudLogging{

inputPort GoogleCloudLogging {
  location:"local"
  Interfaces: GoogleCloudLoggingInterface
}


foreign java {
  class: "joliex.google.GoogleCloudLoggingService" 
  }

}







