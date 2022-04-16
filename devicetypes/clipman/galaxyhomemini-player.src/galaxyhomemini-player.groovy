//DEPRECATED. INTEGRATION MOVED TO SUPER LAN CONNECT

/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  GalaxyHomeMini Player
 *
 *  Author: clipman (Minsoo Kim)
 *
 */

metadata {
	definition (name: "GalaxyHomeMini Player", namespace: "clipman", author: "clipman") {
		capability "Actuator"
		capability "Switch"
		capability "Refresh"
		capability "Sensor"
		capability "Music Player"

		attribute "uri", "string"
		attribute "playbackStatus", "string"

		command "play"
		command "pause"
		command "mute"
		command "unmute"
		command "playNews"
		command "playRadio"
		command "playWeather"
		command "playTopMusic"
		command "volumeUp"
		command "volumeDown"
		command "callBixby"
		command "commandBixby", ["string"]
		command "playMP3", ["string"]
		command "setVolume", ["number"]					// 갤럭시홈미니의 볼륨값 변경
		command "setLevel", ["number"]					// Player의 볼륨표시값과 갤럭시홈미니의 볼륨값 변경
		command "_setLevel", ["number"]					// Player의 볼륨표시값만 변경(갤럭시홈미니의 볼륨값과 싱크용)
		command "setTrackDescription", ["string"]
	}

	preferences {
		input name: "galaxyHomeIP", type: "text", title:"Local IP Address of Galaxy Home Mini", required: false
		input name: "galaxyHomeID", type: "text", title: "Device ID of Galaxy Home Mini", required: false
		input name: "token", type: "text", title: "Token", required: false
	}

	tiles(scale: 2) {
		multiAttributeTile(name: "main", type:"mediaPlayer", width:6, height:4, canChangeIcon: true) {
			tileAttribute("device.status", key: "PRIMARY_CONTROL") {
				attributeState("paused", label:"Paused",)
				attributeState("playing", label:"Playing")
				attributeState("stopped", label:"Stopped")
			}
			tileAttribute("device.status", key: "MEDIA_STATUS") {
				attributeState("paused", label:"Paused", action:"play", nextState: "playing")
				attributeState("playing", label:"Playing", action:"pause", nextState: "paused")
				attributeState("stopped", label:"Stopped", action:"play", nextState: "playing")
			}
			tileAttribute("device.status", key: "PREVIOUS_TRACK") {
				attributeState("default", action:"previousTrack", defaultState: true)
			}
			tileAttribute("device.status", key: "NEXT_TRACK") {
				attributeState("default", action:"nextTrack", defaultState: true)
			}
			tileAttribute ("device.level", key: "SLIDER_CONTROL") {
				attributeState("level", action:"setLevel")
			}
			tileAttribute ("device.mute", key: "MEDIA_MUTED") {
				attributeState("unmuted", action:"mute", nextState: "muted")
				attributeState("muted", action:"unmute", nextState: "unmuted")
			}
			tileAttribute("device.trackDescription", key: "MARQUEE") {
				attributeState("default", label:"${currentValue}", defaultState: true)
			}
		}
	}
}

// parse events into attributes
def parse(description) {
	log.trace "parse($description)"
	def results = []
	try {
		//
	}
	catch (Throwable title) {
		sendEvent(name: "parseError", value: "$title", description: description)
		throw title
	}
	results
}

def installed() {
	log.trace "installed()"

	sendEvent(name: "switch", value: "off")
	sendEvent(name: "status", value: "stopped")
	sendEvent(name: "playbackStatus", value: "stopped")
	sendEvent(name: "mute", value: "unmuted")
	sendEvent(name: "uri", value: "http://192.168.219.130/mp3/test.mp3")
	sendEvent(name: "trackDescription", value: "Galaxy Home Mini Player")
	sendEvent(name: "level", value: 30)
	
	// 예약
	//schedulePoll()
	//poll()
}

def updated() {
	log.trace "updated()"
}

def configure() {
	log.trace "configure()"
}

/*
def uninstalled() {
	unschedule()
}
def schedulePoll() {
	unschedule()
	runEvery3Hours("poll")
}
def updated() {
	schedulePoll()
	poll()
}
def refresh() {
	poll()
}
def configure() {
	poll()
}
def poll() {
	// Last update time stamp
	def timeZone = location.timeZone ?: timeZone(timeOfDay)
	def timeStamp = new Date().format("yyyy MMM dd EEE h:mm:ss a", location.timeZone)
	send(name: "lastUpdate", value: timeStamp)
	
	// 이하 내용
}
*/

def on() {
	log.trace "on()"
	sendEvent(name: "switch", value: "on")
	//play()
}

def off() {
	log.trace "off()"
	stop()
	sendEvent(name: "switch", value: "off")
}

def refresh() {
	log.trace "refresh()"
}

def setLevel(Number val) {
	log.trace "setLevel($val)"
	sendEvent(name: "level", value: val)
	setVolume(val)
}

def _setLevel(Number val) {
	log.trace "setLevel($val)"
	sendEvent(name: "level", value: val)
	//setVolume(val)
}

def setTrackDescription(String val) {
	log.trace "setTrackDescription($val)"

	sendEvent(name: "trackDescription", value: val)
}

def play() {
	if(device.currentValue("switch") == "on") {
		log.trace "play()"
		_play()

		sendEvent(name: "trackDescription", value: device.currentValue("trackDescription"))

		sendEvent(name: "status", value: "playing")
		sendEvent(name: "playbackStatus", value: "playing")
	} else {
		log.trace "play(최초 1회 인증)"
		// 최초 1회에는 인증과정을 거쳐야 함(ST에서 Play 버튼을 누르고 빅스비에서 응답을 하면 액션버튼을 누름)
		if(device.currentValue("uri") == "http://192.168.219.130/mp3/test.mp3") {
			playMP3(device.currentValue("uri"))
		} else {
			sendEvent(name: "status", value: "paused")
			sendEvent(name: "playbackStatus", value: "paused")
		}
	}
}

def stop() {
	log.trace "stop()"
	_pause()

	sendEvent(name: "status", value: "stopped")
	sendEvent(name: "playbackStatus", value: "stopped")
	sendEvent(name: "trackDescription", value: "")
}

def pause() {
	log.trace "pause()"
	_pause()

	sendEvent(name: "status", value: "paused")
	sendEvent(name: "playbackStatus", value: "paused")
}

def mute()
{
	log.trace "mute()"
	_mute()
	sendEvent(name: "mute", value: "muted")
}

def unmute()
{
	log.trace "unmute()"
	_unmute()
	sendEvent(name: "mute", value: "unmuted")
}

def nextTrack() {
	log.trace "nextTrack()"

	sendEvent(name: "status", value: "next")
	sendEvent(name: "playbackStatus", value: "next")
}

def previousTrack() {
	log.trace "previousTrack()"
	sendEvent(name: "status", value: "prev")
	sendEvent(name: "playbackStatus", value: "prev")
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
def buildParms(String capability_, String command_, arg_ = "") {
	log.trace "buildParms($capability_, $command_, $arg_)"
	def builder = new groovy.json.JsonBuilder()
	def c

	if (arg_ instanceof List) {
		c = [component:"main", capability:capability_, command:command_, arguments:arg_.collect()]
	} else if(arg_ != "") {
		def d = [arg_]
		c = [component:"main", capability:capability_, command:command_, arguments:d.collect()]
	} else {
		c = [component:"main", capability:capability_, command:command_]
	}

	builder commands:[c]

	def params = [
		uri: "https://api.smartthings.com/v1/devices/" + settings.galaxyHomeID + "/commands",
		headers: ['Authorization' : "Bearer " + settings.token],
		body: builder.toString()
	]
	log.debug(builder.toString())
	return params
}

def post(params) {
	log.trace "post($params)"
	try {
		httpPostJson(params) { resp ->
			//if (resp.success) {
			//}
			if (resp.data) log.debug "${resp.data}"
		}
	} catch (Exception e) {
		log.warn "Call to on failed: ${e.message}"
	}
}

def setVolume(Number vol_) {
	log.trace "setVolume($vol_)"
	def params = buildParms("audioVolume", "setVolume", vol_)
	post(params)
}

def playRadio() {
	log.trace "playRadio()"
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "CBS음악FM틀어"])
	post(params)

	sendEvent(name: "status", value: "playing")
	sendEvent(name: "playbackStatus", value: "radio")
	sendEvent(name: "trackDescription", value: "CBS 음악 FM")
}

def playNews() {
	log.trace "playNews()"
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", "news")
	post(params)

	sendEvent(name: "status", value: "playing")
	sendEvent(name: "playbackStatus", value: "news")
	sendEvent(name: "trackDescription", value: "최신 뉴스")
}

def playWeather() {
	log.trace "playWeather()"
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", "weather")
	post(params)

	sendEvent(name: "playbackStatus", value: "weather")
	//sendEvent(name: "trackDescription", value: "오늘 날씨")	// 멘트가 끝나면 이전 상태로 되돌아 가기 때문에 변경하지 말아야 함
}

def playTopMusic() {
	log.trace "playTopMusic()"
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", "music")
	post(params)

	sendEvent(name: "status", value: "playing")
	sendEvent(name: "playbackStatus", value: "music")
	sendEvent(name: "trackDescription", value: "인기 챠트")
}

def commandBixby(String commandStr_) {
	log.trace "commandBixby($commandStr_)"
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", commandStr_])
	post(params)
}

def playMP3(String uri) {
	//log.trace "playMP3($uri)"
	if (settings.galaxyHomeIP != null) {
		// 오른쪽에서 / 검색해서 그 이후의 문자에서 .mp3를 제거하여 제목으로 만든다.
		def pos = uri.lastIndexOf('/') + 1
		def currentTitle = uri.substring(pos).replace(".mp3", "").replace(".MP3", "")

		sendEvent(name: "uri", value: uri)
		sendEvent(name: "trackDescription", value: currentTitle)

		sendEvent(name: "status", value: "playing")
		sendEvent(name: "playbackStatus", value: "playing")

		//uri = URLEncoder.encode(uri, "UTF-8").replaceAll(/\+/,'%20').replace('%3A',':').replace('%2F','/').replace('%40','@').replace('%25','%').replace('%3F','?')
		uri = URLEncoder.encode(uri, "UTF-8").replace('%3A',':').replace('%2F','/').replace('+','%20')

		sendUri(uri)
		sendPlay()
	} else {
		log.debug("galaxyHomeIP is not set. Please go to settings and setup galaxyHomeIP")
	}
}

private sendUri(String uri) {
	//log.trace "send($uri)"
	def	action = "\"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\""
	def	data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><CurrentURI>"+uri+"</CurrentURI><CurrentURIMetaData></CurrentURIMetaData></u:SetAVTransportURI></s:Body></s:Envelope>"
	def options = [
		"method": "POST",
		"path": "/upnp/control/AVTransport1",
		"headers": [
			"HOST": (settings.galaxyHomeIP+":9197"),
			"Content-Type": "text/xml; charset=utf-8",
			"SOAPAction": action
		],
		"body": data
	]
	log.debug(options)

	def myhubAction = new physicalgraph.device.HubAction(options, null)
	sendHubCommand(myhubAction)
}

private sendPlay() {
	def action = "\"urn:schemas-upnp-org:service:AVTransport:1#Play\""
	def data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><u:Play xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Speed>1</Speed></u:Play></s:Body></s:Envelope>"
	def options = [
		"method": "POST",
		"path": "/upnp/control/AVTransport1",
		"headers": [
			"HOST": (settings.galaxyHomeIP+":9197"),
			"Content-Type": "text/xml; charset=utf-8",
			"SOAPAction": action
		],
		"body": data
	]
	log.debug(options)

	def myhubAction = new physicalgraph.device.HubAction(options, null)
	sendHubCommand(myhubAction)
}

def volumeUp() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "음량 올려"])
	post(params)
}

def volumeDown() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "음량 내려"])
	post(params)
}

def _play() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "재생"])
	post(params)
}

def _pause() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "정지"])
	post(params)
}

def _mute() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "음량 꺼"])
	post(params)
}

def _unmute() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "음량 켜"])
	post(params)
}

def callBixby() {
	def params = buildParms("samsungim.bixbyContent", "bixbyCommand", ["search_all", "빅스비"])
	post(params)
}
