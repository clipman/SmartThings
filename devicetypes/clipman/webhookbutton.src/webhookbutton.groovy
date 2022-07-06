/**
 *  WebhookButton v2022-06-17
 *  clipman@naver.com
 *  날자
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

import groovy.json.JsonSlurper

metadata {
	definition (name: "WebhookButton", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.remotecontroller", mnmn: "SmartThingsCommunity", vid: "05d8f755-c62d-376f-ad5c-54eb57befb7c") {
		capability "Button"
		capability 'Momentary'
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]", displayDuringSetup: false
		input "haURL", "text", title: "HomeAssistant External URL (http://xxx)", required: false
		input "haLocalIP", "text", title: "HomeAssistant Local IP (허브, 192.xxx)", required: false
		//input "haToken", "text", title: "HomeAssistant Token", required: false
		input "webHookID", "text", title: "WebHook ID", required: true
	}
}

def installed() {
	sendEvent(name: "supportedButtonValues", value: ['pushed'], displayed: false)
	sendEvent(name: "numberOfButtons", value: 1, displayed: false)
}

def push() {
	def services = "/api/webhook/" + settings.webHookID
	if(settings.haLocalIP && settings.webHookID) {
		localServices(services)
	} else if(settings.haURL && settings.webHookID) {
		externalServices(services)
	}
	sendEvent(name: "button", value: "pushed", isStateChange: true)
}

def externalServices(service) {
	def params = [
		uri: settings.haURL,
		path: service,
		requestContentType: "application/json",
		body: []
	]
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "HomeAssistant externalServices($service) Error: $e"
		return false
	}
}

def localServices(service){
	def params = [
	 	method: "POST",
		path: service,
		headers: [
			HOST: settings.haLocalIP,
			ContentType: "application/json"
		],
		body: []
	]
	def myhubAction = new physicalgraph.device.HubAction(params)
	sendHubCommand(myhubAction)
}

/*
//def myhubAction = new physicalgraph.device.HubAction(params, null, [callback: callback])
def callback(physicalgraph.device.HubResponse hubResponse){
	try {
		def msg = parseLanMessage(hubResponse.description)
		def jsonObj = new JsonSlurper().parseText(msg.body)
		log.debug jsonObj
	} catch (e) {
		log.error "Exception caught while parsing data: " + e 
	}
}
def localServices(service, data){
	def params = [
	 	method: "POST",
		path: service,
		headers: [
			HOST: settings.haLocalIP,
			Authorization: "Bearer " + settings.haToken,
			Content-Type: "application/json"
		],
		body: data
	]
	def myhubAction = new physicalgraph.device.HubAction(params)
	sendHubCommand(myhubAction)
}
*/