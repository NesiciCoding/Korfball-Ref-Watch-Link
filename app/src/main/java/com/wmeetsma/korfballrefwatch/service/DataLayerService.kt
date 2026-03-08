package com.wmeetsma.korfballrefwatch.service

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.wmeetsma.korfballrefwatch.repository.GameStateRepository

class DataLayerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/korfball_game_state") {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val dataMap = dataMapItem.dataMap
                    
                    val data = mutableMapOf<String, Any>()
                    
                    if (dataMap.containsKey("homeScore")) data["homeScore"] = dataMap.getInt("homeScore")
                    if (dataMap.containsKey("awayScore")) data["awayScore"] = dataMap.getInt("awayScore")
                    if (dataMap.containsKey("gameTime")) data["gameTime"] = dataMap.getLong("gameTime")
                    if (dataMap.containsKey("shotClock")) data["shotClock"] = dataMap.getLong("shotClock")
                    if (dataMap.containsKey("isGameTimeRunning")) data["isGameTimeRunning"] = dataMap.getBoolean("isGameTimeRunning")
                    if (dataMap.containsKey("isShotClockRunning")) data["isShotClockRunning"] = dataMap.getBoolean("isShotClockRunning")
                    if (dataMap.containsKey("period")) data["period"] = dataMap.getInt("period")
                    if (dataMap.containsKey("subPending")) data["subPending"] = dataMap.getBoolean("subPending")
                    if (dataMap.containsKey("latestSubId")) data["latestSubId"] = dataMap.getString("latestSubId") ?: ""
                    if (dataMap.containsKey("subOut")) data["subOut"] = dataMap.getString("subOut") ?: ""
                    if (dataMap.containsKey("subIn")) data["subIn"] = dataMap.getString("subIn") ?: ""
                    if (dataMap.containsKey("isReadOnly")) data["isReadOnly"] = dataMap.getBoolean("isReadOnly")
                    if (dataMap.containsKey("timeoutTeam")) data["timeoutTeam"] = dataMap.getString("timeoutTeam") ?: ""
                    
                    GameStateRepository.updateFromMap(data)
                    
                    Log.d("DataLayerService", "Received and stored new state: ${data["homeScore"]} - ${data["awayScore"]}. Time: ${data["gameTime"]}")
                }
            }
        }
    }
}
