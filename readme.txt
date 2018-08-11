FlightInfoBoard
|- executer
  |- Launcher
    A command to run this demo
  |- AdsbMain
    |- adsbDataProccessingMultithreading
      Initialize the caculated airport constant
      Set adsb data socket and port
      Call Netty to Read Socket multi-thread
    |- adsbRecordProccessing
      Call recoder to read ADS-B char record from Netty
      Call collector to format to AircraftsMap map
      Call watcher to plot real-time map chart
      Set maxDelyTime as a timeout to discard data if socket is full, and record the discard ratio
      Set refreshTime as a interval to refresh chart
      May add ADS-B data flow watcher here
|-surveyer
  |- AirportConstant
    |- Initialize
      Set a series constant as the physics or geographic parameters of airports as:
        Runway, bidirectional, multi-airport supported:
          Names,
          Landing position location,
          Taking Off postion loaction,
          Airport altitude;
          Now including Kuming, Lijiang, Hangzhou, Ningbo and Shanghai Hongqiao.
         Detach runway, multi-airport supported:
          Names,
          Runway side postion location,
          Airport side position location.
        Calculate runways' and detach runways' tracks and LLz marks' position location
        Transfer the data matrix format for functions used later
|- collector
  |- SocketHandler
  |- SocketReader
    Standard netty socket reading
    Separate data package if nessesary
    A trick: continue socket connection in "finnaly {"
|- recoder
  |- AdsbMomentRecord
    Format ADS-B char data as:
     msgType: ADS-B message type, int 1, 2, 3, 4
     transType: 
     sessionId:
     aircraftId:
     hexident: ICAO code, hex
     flghtId: Actually the message id, can use to recognize the hardware sent the message, long
     timeMsg: The time the message was sent from the device on aircraft, this data is not always avaliable
     timeMsgLog: The time the message was recieved by the device in airport or somewhere set by Huasky, this data is always avaliable. 
      In this project, we use this data as time stamp, even though it's not so resonable.
     callsign: Flight number with 3 initial letter, varchar
     altitude: Aircraft's absolute altitude, int, m
     groundSpeed: Aircraft's horizonal velocity, double, kt
     track: Aircraft's track, double
     latitude: Aircraft's latitude, double
     longitude: Aircraft's longitude, double
     verticalRate: Aircraft's vertical velocity, int, ft/min
     squawk:
     alert:
     emergency:
     spi
    |- convertCharToClass
      Extract all of above from messages with "," sepatared
      The defualt value is null
    |- recordAddToLogDatabase
      Add record to database, including:
        hexident,
        filghtId(Actually message ID),
        timeMsgLog,
        altitude,
        latitude,
        longitude,
        groundSpeed,
        track, 
        verticalRate
      can be add more data including calculated results later
     |- writeToConsole
      Output a specific part of the data to console for debug
  |- AircraftInfo
    Maintain the AircraftMap map
    |- recordAddToAircraftMap
      Update ADS-B records in Map database in memory, accroding to message types. The main key is the aircraft ICAO code, `             hexident. If this aircraft doesn't exist, create a new record in Map. Except parameters in ADS-B data, three more data
        has been add: 
          velocityEpoch: the time of the last time velocity parameters was recorded.
          acceleration: the mode of the all-direction speed vector changes between last two times the velocity parameters was
            recorded devide the time between these two.
          previousStatus: the status info given for the previous info update time. This parameter is given by logic part.
      Message types have labels below:
        1, include:
          Callsign;
        2, include all parameters:
          groundSpeed,
          verticalRate,
          track,
          latitude,
          longtitude,
          altitude;
          Map's timeMsgLog and velocityEpoch will be updated.
         3, include location parameters:
          latitude,
          longtitude,
          altitude;
         4, include velocity parameters:
          groundSpeed,
          verticalRate,
          track.
    |- statusUpdate
      A port for other class to change the Map. Now only the previousStatus can be edited.
    |- cleanAircraftsMap
      Search the Map, if:
        The Aircraft is not normally flying or parking and it has been lost contact for maxExpireTime (120s) 
        or
        The Aircraft is normally flying or parking and it had been lost contact for expireTime (60s)
      the remove the aircraft record from Map.
      This method will be called every refreshTime (1s) in main faction.
    |- writeToConsole
      Output a specific part of the Map to console for debug
|- logic
  Retuern a char form result for a set of an aircraft record.
  |- AircraftStatus
  |- AircraftStatusCalculator
  |- AircraftStatusInterpolation
|- watcher
|- gadget
