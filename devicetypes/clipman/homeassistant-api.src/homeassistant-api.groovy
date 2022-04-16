/**
*  HomeAssistant API (v.0.0.1)
*
*  Authors
*   - clipman@naver.com
*  Copyright 2021
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*/

metadata {
	definition (name: "HomeAssistant API", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "4058d82e-f66b-3ef3-a7a0-b9a3656d74f9") {
		capability "circlecircle06391.statusBar"
		capability "Refresh"

		command "services"
		command "states"
		command "command"
		command "api"

		command "script"
		command "esphome"
		command "rest_command"
		command "shell_command"
		command "_on"
		command "_off"
		command "_toggle"
		command "update"
		command "mqtt"

		command "set_text"
		command "set_number"
		command "set_date"
		command "set_time"
		command "set_datetime"

		command "automation_on"
		command "automation_off"
		command "automation_toggle"
		command "automation_reload"
		command "automation_trigger"

		command "climate_on"
		command "climate_off"
		command "climate_hvac_mode"
		command "climate_temperature"
		command "climate_temp_high"
		command "climate_temp_low"
		command "climate_humidity"
		command "climate_preset_mode"
		command "climate_fan_mode"
		command "climate_swing_mode"
		command "climate_aux_heat"

		command "fan_on"
		command "fan_off"
		command "fan_toggle"
		command "fan_speed"
		command "fan_percentage"
		command "fan_preset_mode"
		command "fan_direction"
		command "fan_increase_speed"
		command "fan_decrease_speed"
		command "fan_oscillate"

		command "media_on"
		command "media_off"
		command "media_toggle"
		command "media_play"
		command "media_stop"
		command "media_volume_up"
		command "media_volume_down"
		command "media_volume_mute"
		command "media_volume_set"
		command "media_pause"
		command "media_play_pause"
		command "media_next_track"
		command "media_previous_track"
		command "media_seek"
		command "media_shuffle_set"
		command "media_repeat_set"
		command "media_select_source"
		command "media_sound_mode"
		command "media_clear_playlist"
		command "media_play_media"
		command "media_play_music"
		command "media_play_list"

		command "timer_cancel"
		command "timer_finish"
		command "timer_pause"
		command "timer_start"
	}
	preferences {
		input name: "haAddress", type: "text", title:"HomeAssistant Address", required: true
		input name: "haToken", type: "text", title: "HomeAssistant Token", required: true
	}
}

def setStatus(status){
	sendEvent(name: "status", value: status, displayed: false)
}

def updated(){
	setStatus(settings.haAddress)
}

def refresh(){
	setStatus(settings.haAddress)
}

def automation_on(entity_id){
	def body = ["entity_id": entity_id]
	services("automation", "turn_on", body)
}
def automation_off(entity_id){
	def body = ["entity_id": entity_id]
	services("automation", "turn_off", body)
}
def automation_off(entity_id, stop_actions){
	def body = ["entity_id": entity_id, "stop_actions": stop_actions]
	services("automation", "turn_off", body)
}
def automation_toggle(entity_id){
	def body = ["entity_id": entity_id]
	services("automation", "toggle", body)
}
def automation_reload(entity_id){
	def body = ["entity_id": entity_id]
	services("automation", "reload", body)
}
def automation_trigger(entity_id){
	def body = ["entity_id": entity_id]
	services("automation", "trigger", body)
}
def automation_trigger(entity_id, skip_condition){
	def body = ["entity_id": entity_id, "skip_condition": skip_condition]
	services("automation", "trigger", body)
}

def climate_on(entity_id){
	def body = ["entity_id": entity_id]
	services("climate", "turn_on", body)
}
def climate_off(entity_id){
	def body = ["entity_id": entity_id]
	services("climate", "turn_off", body)
}
def climate_hvac_mode(entity_id, hvac_mode){
	def body = ["entity_id": entity_id, "hvac_mode": hvac_mode]
	services("climate", "set_hvac_mode", body)
}
def climate_temperature(entity_id, temperature){
	def body = ["entity_id": entity_id, "temperature": temperature]
	services("climate", "set_temperature", body)
}
def climate_temp_high(entity_id, target_temp_high){
	def body = ["entity_id": entity_id, "target_temp_high": target_temp_high]
	services("climate", "set_temperature", body)
}
def climate_temp_low(entity_id, target_temp_low){
	def body = ["entity_id": entity_id, "target_temp_low": target_temp_low]
	services("climate", "set_temperature", body)
}
def climate_humidity(entity_id, humidity){
	def body = ["entity_id": entity_id, "humidity": humidity]
	services("climate", "set_humidity", body)
}
def climate_preset_mode(entity_id, preset_mode){
	def body = ["entity_id": entity_id, "preset_mode": preset_mode]
	services("climate", "set_preset_mode", body)
}
def climate_fan_mode(entity_id, fan_mode){
	def body = ["entity_id": entity_id, "fan_mode": fan_mode]
	services("climate", "set_fan_mode", body)
}
def climate_swing_mode(entity_id, swing_mode){
	def body = ["entity_id": entity_id, "swing_mode": swing_mode]
	services("climate", "set_swing_mode", body)
}
def climate_aux_heat(entity_id, aux_heat){
	def body = ["entity_id": entity_id, "aux_heat": aux_heat]
	services("climate", "set_aux_heat", body)
}

def fan_on(entity_id){
	def body = ["entity_id": entity_id]
	services("fan", "turn_on", body)
}
def fan_off(entity_id){
	def body = ["entity_id": entity_id]
	services("fan", "turn_off", body)
}
def fan_toggle(entity_id){
	def body = ["entity_id": entity_id]
	services("fan", "toggle", body)
}
def fan_speed(entity_id, speed){
	def body = ["entity_id": entity_id, "speed": speed]
	services("fan", "set_speed", body)
}
def fan_percentage(entity_id, percentage){
	def body = ["entity_id": entity_id, "percentage": percentage]
	services("fan", "set_percentage", body)
}
def fan_preset_mode(entity_id, preset_mode){
	def body = ["entity_id": entity_id, "preset_mode": preset_mode]
	services("fan", "set_preset_mode", body)
}
def fan_direction(entity_id, direction){
	def body = ["entity_id": entity_id, "direction": direction]
	services("fan", "set_direction", body)
}
def fan_increase_speed(entity_id, percentage_step){
	def body = ["entity_id": entity_id, "percentage_step": percentage_step]
	services("fan", "increase_speed", body)
}
def fan_decrease_speed(entity_id, percentage_step){
	def body = ["entity_id": entity_id, "percentage_step": percentage_step]
	services("fan", "decrease_speed", body)
}
def fan_oscillate(entity_id, oscillating){
	def body = ["entity_id": entity_id, "oscillating": oscillating]
	services("fan", "oscillate", body)
}

def media_on(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "turn_on", body)
}
def media_off(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "turn_off", body)
}
def media_toggle(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "turn_toggle", body)
}
def media_play(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "media_play", body)
}
def media_stop(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "media_stop", body)
}
def media_volume_up(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "volume_up", body)
}
def media_volume_down(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "volume_down", body)
}
def media_volume_mute(entity_id, is_volume_muted){
	def body = ["entity_id": entity_id, "is_volume_muted": is_volume_muted]
	services("media_player", "volume_mute", body)
}
def media_volume_set(entity_id, volume_level){
	def body = ["entity_id": entity_id, "volume_level": volume_level]
	services("media_player", "volume_set", body)
}
def media_pause(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "media_pause", body)
}
def media_play_pause(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "media_play_pause", body)
}
def media_next_track(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "media_next_track", body)
}
def media_previous_track(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "media_previous_track", body)
}
def media_seek(entity_id, seek_position){
	def body = ["entity_id": entity_id, "seek_position": seek_position]
	services("media_player", "media_seek", body)
}
def media_shuffle_set(entity_id, shuffle){
	def body = ["entity_id": entity_id, "shuffle": shuffle]
	services("media_player", "shuffle_set", body)
}
def media_repeat_set(entity_id, repeat){
	def body = ["entity_id": entity_id, "repeat": repeat]
	services("media_player", "repeat_set", body)
}
def media_select_source(entity_id, source){
	def body = ["entity_id": entity_id, "source": source]
	services("media_player", "select_source", body)
}
def media_sound_mode(entity_id, sound_mode){
	def body = ["entity_id": entity_id, "sound_mode": sound_mode]
	services("media_player", "select_sound_mode", body)
}
def media_clear_playlist(entity_id){
	def body = ["entity_id": entity_id]
	services("media_player", "clear_playlist", body)
}
def media_play_media(entity_id, media_content_id){
	def body = ["entity_id": entity_id, "media_content_id": media_content_id, "media_content_type": "music"]
	services("media_player", "play_media", body)
}
def media_play_media(entity_id, media_content_id, media_content_type){
	def body = ["entity_id": entity_id, "media_content_id": media_content_id, "media_content_type": media_content_type]
	services("media_player", "play_media", body)
}
def media_play_music(entity_id, media_content_id){
	def body = ["entity_id": entity_id, "media_content_id": media_content_id, "media_content_type": "music"]
	services("media_player", "play_media", body)
}
def media_play_list(entity_id, media_content_id){
	def body = ["entity_id": entity_id, "media_content_id": media_content_id, "media_content_type": "playlist"]
	services("media_player", "play_media", body)
}

def timer_cancel(entity_id){
	def body = ["entity_id": entity_id]
	services("timer", "cancel", body)
}
def timer_finish(entity_id){
	def body = ["entity_id": entity_id]
	services("timer", "finish", body)
}
def timer_pause(entity_id){
	def body = ["entity_id": entity_id]
	services("timer", "pause", body)
}
def timer_start(entity_id){
	def body = ["entity_id": entity_id]
	services("timer", "start", body)
}
def timer_start(entity_id, duration){
	def body = ["entity_id": entity_id, "duration": duration]
	services("timer", "start", body)
}

//script("pc_on")
def script(data){
	def body = []
	services("script", data, body)
}
def script(data, body){
	services("script", data, body)
}

//esphome("servo_run")
//esphome("roll_control", '["level": 50]')
def esphome(data){
	def body = []
	services("esphome", data, body)
}
def esphome(data, body){
	services("esphome", data, body)
}

//rest_command("set_airconditioner_mode_wind")
def rest_command(data){
	def body = []
	services("rest_command", data, body)
}
def rest_command(data, body){
	services("rest_command", data, body)
}

//shell_command("python_mp3tojpg")
def shell_command(data){
	def body = []
	services("shell_command", data, body)
}
def shell_command(data, body){
	services("shell_command", data, body)
}

//update("sensor.ceongrayeog701")
def update(entity_id){
	def body = ["entity_id": entity_id]
	services("homeassistant", "update_entity", body)
}

//_on("switch.aircon")
def _on(entity_id){
	def body = ["entity_id": entity_id]
	services("homeassistant", "turn_on", body)
}

//_off("switch.aircon")
def _off(entity_id){
	def body = ["entity_id": entity_id]
	services("homeassistant", "turn_off", body)
}

//_toggle("light.kocom_livingroom_light1")
def _toggle(entity_id){
	def body = ["entity_id": entity_id]
	services("homeassistant", "toggle", body)
}

//set_text("input_text.title", "일어나")
def set_text(entity_id, value){
	def body = ["entity_id": entity_id, "value": value]
	services("input_text", "set_value", body)
}

//set_number("input_number.number", 100)
def set_number(entity_id, value){
	def body = ["entity_id": entity_id, "value": value]
	services("input_number", "set_value", body)
}

//set_date("input_datetime.start_date", "2021-06-15")
def set_date(entity_id, value){
	def body = ["entity_id": entity_id, "date": value]
	services("input_datetime", "set_datetime", body)
}

//set_time("input_datetime.start_time", "09:00:00")
def set_time(entity_id, value){
	def body = ["entity_id": entity_id, "time": value]
	services("input_datetime", "set_datetime", body)
}

//set_datetime("input_datetime.start_datetime", "2021-06-15 10:30:00")
def set_datetime(entity_id, value){
	def body = ["entity_id": entity_id, "datetime": value]
	services("input_datetime", "set_datetime", body)
}

/*
//Send a MQTT message
curl -X POST \
  -H "Content-Type: application/json" \
  -H "x-ha-access:YOUR_PASSWORD" \
  -d '{"payload": "OFF", "topic": "home/fridge", "retain": "True"}' \
  http://localhost:8123/api/services/mqtt/publish
*/
//mqtt('["payload": "OFF", "topic": "home/fridge", "retain": "True"]')
def mqtt(String data){
	services("mqtt", "publish", data)
}

def command(data1, data2){
	def body = []
	services(data1, data2, body)
}

def command(entity_id, command, data){
	def body = ["entity_id": data]
	services(entity_id, command, body)
}

def command(entity_id, command, data1, data2){
	def body = ["${data1}": data2]
	services(entity_id, command, body)
}

def command(entity_id, command, data1, data2, data3, data4){
	def body = ["${data1}": data2, "${data3}": data4]
	services(entity_id, command, body)
}

def command(entity_id, command, data1, data2, data3, data4, data5, data6){
	def body = ["${data1}": data2, "${data3}": data4, "${data5}": data6]
	services(entity_id, command, body)
}

def command(entity_id, command, data1, data2, data3, data4, data5, data6, data7, data8){
	def body = ["${data1}": data2, "${data3}": data4, "${data5}": data6, "${data7}": data8]
	services(entity_id, command, body)
}

def services(entity_id, command, String data){
	try{
		Map body = evaluate(data)
		services(entity_id, command, body)
	}catch(error){
		log.error "${error}"
	}
}
def services(entity_id, command, body){
	def options = [
		"method": "POST",
		"path": "/api/services/${entity_id}/${command}",
		"headers": [
			"HOST": settings.haAddress,
			"Authorization": "Bearer " + settings.haToken,
			"Content-Type": "application/json"
		],
		"body": body
	]
	//log.debug options
	sendCommand(options)
}

/*
curl -X POST -H "Authorization: Bearer ABCDEFGH" \
  -H "Content-Type: application/json" \
  -d '{"state": "25", "attributes": {"unit_of_measurement": "°C"}}' \
  http://localhost:8123/api/states/sensor.kitchen_temperature
*/
//states("sensor.gajeongyeog701", '["state": "2분 30초 (2번째 전)"]')
//states("sensor.kitchen_temperature", '["state": "25", "attributes": ["unit_of_measurement": "°C"]]')
def states(entity_id, String data){
	try{
		Map body = evaluate(data)
		states(entity_id, body)
	}catch(error){
		log.error "${error}"
	}
}
def states(entity_id, body){
	def options = [
		"method": "POST",
		"path": "/api/states/${entity_id}",
		"headers": [
			"HOST": settings.haAddress,
			"Authorization": "Bearer " + settings.haToken,
			"Content-Type": "application/json"
		],
		"body": body
	]
	//log.debug options
	sendCommand(options)
}

//api("/api/services/shell_command/python_mp3tojpg", "[]")
//command("switch", "toggle", "switch.caegsangbul")
//api('/api/services/switch/toggle', '["entity_id": "switch.caegsangbul"]')
def api(path, String data){
	try{
		Map body = evaluate(data)
		api(path, body)
	}catch(error){
		log.error "${error}"
	}
}
def api(path, body){
/*
	//haAddress=  "https://clipman.duckdns.org"
	def params = [
        uri: haAddress,
        path: path,
        headers: ["Authorization": "Bearer " + haToken],
        requestContentType: "application/json",
        body: data
    ]
	sendCommandPost(options)
*/
	//haAddress=  "192.168.219.130:8123"
	def options = [
		"method": "POST",
		"path": path,
		"headers": [
			"HOST": settings.haAddress,
			"Authorization": "Bearer " + settings.haToken,
			"Content-Type": "application/json"
		],
		"body": body
	]
	//log.debug options
	sendCommand(options)
}

/*
def sendCommandPost(options){
	try {
		httpPost(options) { resp ->
			return true
		}
	} catch (e) {
		log.error "sendCommand Error: $e"
		return false
	}
}
*/
def sendCommand(options){
	def myhubAction = new physicalgraph.device.HubAction(options)
	sendHubCommand(myhubAction)
}

//services("media_player", "play_media", '["entity_id": "media_player.ytube_music_player", "media_content_id": "PL0dVna1LyJYxElJfIZ4J1KcnZbZr97ZxC", "media_content_type": "playlist"]')
//command("media_player", "play_media", "entity_id", "media_player.ytube_music_player", "media_content_id", "PL0dVna1LyJYxElJfIZ4J1KcnZbZr97ZxC", "media_content_type", "playlist")
//media_play_media("media_player.ytube_music_player", "PL0dVna1LyJYxElJfIZ4J1KcnZbZr97ZxC", "playlist")
//media_play_list("media_player.ytube_music_player", "PL0dVna1LyJYxElJfIZ4J1KcnZbZr97ZxC")

//services("media_player", "play_media", '["entity_id": "media_player.gugeulhommini_seojae", "media_content_id": "http://192.168.219.130/mp3/김광석-일어나.mp3", "media_content_type": "music"]')
//command("media_player", "play_media", "entity_id", "media_player.gugeulhommini_seojaer", "media_content_id", "http://192.168.219.130/mp3/김광석-일어나.mp3", "media_content_type", "music")
//media_play_media("media_player.gugeulhommini_seojae", "http://192.168.219.130/mp3/김광석-일어나.mp3", "music")
//media_play_music("media_player.gugeulhommini_seojae", "http://192.168.219.130/mp3/김광석-일어나.mp3")

//command("media_player", "media_play", "entity_id", "media_player.ytube_music_player")
//command("media_player", "media_stop", "entity_id", "media_player.ytube_music_player")
//command("media_player", "media_pause", "entity_id", "media_player.ytube_music_player")
//command("media_player", "media_next_track", "entity_id", "media_player.ytube_music_player")
//command("media_player", "media_previous_track", "entity_id", "media_player.ytube_music_player")
//command("media_player", "volume_up", "entity_id", "media_player.ytube_music_player")
//command("media_player", "volume_down", "entity_id", "media_player.ytube_music_player")
//command("media_player", "play_media", "entity_id", "media_player.ytube_music_player", "media_content_id", "PL0dVna1LyJYxElJfIZ4J1KcnZbZr97ZxC", "media_content_type", "playlist")
//http://192.168.219.130:8123/api/services/shell_command/python_mp3tojpg/
//shell_command.python_mp3tojpg
//command("shell_command", "python_mp3tojpg")
//command("script", "pc_on")
//http://192.168.219.130:8123/api/services/input_text/set_value/
//["entity_id": "input_text.title", "value": "CBS 음악FM 라디오"]
//command("input_text", "set_value", ["entity_id": "input_text.title", "value": "제목"])
//command("media_player", "media_play", ["entity_id": state.entity_id])
//body:[entity_id:switch.caegsangbul]
//command("switch", "turn_on", "switch.caegsangbul")
//command("switch", "turn_off", "switch.caegsangbul")
//command("switch", "toggle", "switch.caegsangbul")
//def body = ["entity_id": "switch.caegsangbul"]
//command("media_player", "volume_up", "entity_id", "media_player.ytube_music_player")
//command("media_player", "volume_down", "entity_id", "media_player.ytube_music_player")
//command("switch", "turn_on", "entity_id", "switch.caegsangbul")
//command("switch", "turn_off", "entity_id", "switch.caegsangbul")
//command("switch", "toggle", "entity_id", "switch.caegsangbul")
//def body = ["entity_id": "switch.caegsangbul"]
//command("input_text", "set_value", "entity_id", "input_text.title", "value", "제목")
//def body = ["entity_id": "input_text.title", "value": "제목" ]
//command("media_player", "play_media", "entity_id", "media_player.gugeulhommini_seojae", "media_content_id", "http://192.168.219.130/mp3/test.mp3", "media_content_type", "music")
//def body = ["entity_id": "media_player.gugeulhommini_seojae", "media_content_id": "http://192.168.219.130/mp3/test.mp3", "media_content_type": "music"]