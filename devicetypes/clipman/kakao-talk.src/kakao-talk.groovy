/**
 *  Kakao Talk v2022-04-28
 *  fison67@nate.com/clipman@naver.com
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

import groovy.json.*

include 'asynchttp_v1'

metadata {
	definition (name: "Kakao Talk", namespace: "clipman", author: "clipman") {
		capability "Speech Synthesis"
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def setInfo(String app_url) {
	log.debug "${app_url}"
	state.app_url = app_url
}

def speak(text){
	log.debug "Speak :" + text
	sendData(text)
}

def updated() {}

def sendData(text){
	parent.saveData(text)

	_sendData(makeParam("me", text))
	if(parent.getFriendsUUID().size() > 0){
		_sendData(makeParam("friends", text))
	}
}

def _sendData(data){
	try {
		httpPost(data) { resp ->
			if(resp.data.result_code == 0){
				log.debug "Success to send Message}"
			} else if(resp.data.successful_receiver_uuids){
				log.debug "Success to send Message}"
			} else{
				log.debug "Failed to send Message >> ${resp.data}"
			}
		}
	} catch (e) {
		log.debug "something went wrong: $e"
	}
}

def makeParam(type, text){
	def body = [
		"object_type": "text",
		"text": text,
		"link": [
			"web_url": parent.getMessageCheckURL(),
			"mobile_web_url": parent.getMessageCheckURL()
		 ]
	]

	def params = []
	switch(type){
	case "me":
		params = [
			uri: "https://kapi.kakao.com/v2/api/talk/memo/default/send",
			headers: [
				'Authorization': 'Bearer ' + parent.getAccessToken(),
		  		'content-type' : 'application/x-www-form-urlencoded'
			],
			body: [
				"template_object": JsonOutput.toJson(body).replace(">>", "\\n")
			]
		]
		break
	case "friends":
		params = [
			uri: "https://kapi.kakao.com/v1/api/talk/friends/message/default/send",
			headers: [
				'Authorization': 'Bearer ' + parent.getAccessToken(),
				'content-type' : 'application/x-www-form-urlencoded'
			],
			body: [
				"receiver_uuids": JsonOutput.toJson(parent.getFriendsUUID()),
				"template_object": JsonOutput.toJson(body).replace(">>", "\\n")
			]
		]
		break
	}
	return params
}