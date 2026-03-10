package com.wmeetsma.korfballrefwatch.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

data class WifiConfig(val ip: String, val port: Int)

@Composable
fun SetupScreen(onConfigured: (WifiConfig?) -> Unit) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("korfstat_prefs", Context.MODE_PRIVATE) }
    
    var mode by remember { mutableStateOf<String?>(null) } // null = choosing, "IP" = entering IP
    
    // IP octets
    var part1 by remember { mutableStateOf(192) }
    var part2 by remember { mutableStateOf(168) }
    var part3 by remember { mutableStateOf(1) }
    var part4 by remember { mutableStateOf(5) }
    
    // Port (default 3002 = KorfStat node backend)
    var port by remember { mutableStateOf(3002) }
    
    // Which field is selected: 0-3 = IP parts, 4 = port
    var activePart by remember { mutableStateOf(3) }
    
    LaunchedEffect(Unit) {
        val savedIp = sharedPrefs.getString("saved_ip", null)
        val savedPort = sharedPrefs.getInt("saved_port", 3002)
        port = savedPort
        if (savedIp != null) {
            val parts = savedIp.split(".")
            if (parts.size == 4) {
                part1 = parts[0].toIntOrNull() ?: 192
                part2 = parts[1].toIntOrNull() ?: 168
                part3 = parts[2].toIntOrNull() ?: 1
                part4 = parts[3].toIntOrNull() ?: 5
            }
        }
    }

    if (mode == null) {
        // Mode selection screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Connection Mode", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onConfigured(null) },
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Text("Phone / ADB", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { mode = "IP" },
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Text("Direct Wi-Fi", fontSize = 12.sp)
            }
        }
    } else {
        // IP + Port config screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Host Address", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            
            // IP Row
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IpPart(value = part1, isActive = activePart == 0) { activePart = 0 }
                Text(".", color = Color.Gray, fontSize = 18.sp)
                IpPart(value = part2, isActive = activePart == 1) { activePart = 1 }
                Text(".", color = Color.Gray, fontSize = 18.sp)
                IpPart(value = part3, isActive = activePart == 2) { activePart = 2 }
                Text(".", color = Color.Gray, fontSize = 18.sp)
                IpPart(value = part4, isActive = activePart == 3) { activePart = 3 }
            }
            
            // Port Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(":", color = Color.Gray, fontSize = 18.sp)
                Text(
                    text = port.toString(),
                    color = if (activePart == 4) Color.Cyan else Color.White,
                    fontSize = if (activePart == 4) 22.sp else 18.sp,
                    fontWeight = if (activePart == 4) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable { activePart = 4 }.padding(2.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // +/- Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    when(activePart) {
                        0 -> part1 = (part1 - 1).coerceAtLeast(0)
                        1 -> part2 = (part2 - 1).coerceAtLeast(0)
                        2 -> part3 = (part3 - 1).coerceAtLeast(0)
                        3 -> part4 = (part4 - 1).coerceAtLeast(0)
                        4 -> port = (port - 1).coerceAtLeast(1)
                    }
                }) {
                    Text("-", fontSize = 20.sp)
                }
                
                Button(onClick = {
                    when(activePart) {
                        0 -> part1 = (part1 + 1).coerceAtMost(255)
                        1 -> part2 = (part2 + 1).coerceAtMost(255)
                        2 -> part3 = (part3 + 1).coerceAtMost(255)
                        3 -> part4 = (part4 + 1).coerceAtMost(255)
                        4 -> port = (port + 1).coerceAtMost(65535)
                    }
                }) {
                    Text("+", fontSize = 20.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    val ip = "$part1.$part2.$part3.$part4"
                    sharedPrefs.edit()
                        .putString("saved_ip", ip)
                        .putInt("saved_port", port)
                        .apply()
                    onConfigured(WifiConfig(ip, port))
                },
                modifier = Modifier.height(30.dp)
            ) {
                Text("Connect", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun IpPart(value: Int, isActive: Boolean, onClick: () -> Unit) {
    Text(
        text = value.toString(),
        color = if (isActive) Color.Cyan else Color.White,
        fontSize = if (isActive) 22.sp else 18.sp,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.clickable { onClick() }.padding(2.dp)
    )
}
