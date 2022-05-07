/**
 *	SmartWeather v2022-04-17
 *	clipman@naver.com
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *	in compliance with the License. You may obtain a copy of the License at:
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *	for the specific language governing permissions and limitations under the License.
 *
 */

metadata {
	definition (name: "SmartWeather", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.airqualitysensor",
		mnmn: "SmartThingsCommunity", vid: "b606bfec-3258-3faa-9517-35a1787d8d9d") {
		capability "Air Quality Sensor"						//airQuality
		capability "Dust Sensor"							//dustLevel, fineDustLevel
		capability "Temperature Measurement"				//temperature
		capability "Relative Humidity Measurement"			//humidity
		capability "Ultraviolet Index"						//ultravioletIndex
		capability "Illuminance Measurement"				//illuminance
		capability "circlecircle06391.dustClass"			//dustClass
		capability "circlecircle06391.fineDustClass"		//fineDustClass
		capability "circlecircle06391.windspeed"			//windSpeed
		capability "circlecircle06391.windbearing"			//windBearing
		capability "circlecircle06391.todayfeeltemp"		//temperatureFeel
		capability "circlecircle06391.todaymintemp"			//temperatureMin
		capability "circlecircle06391.todaymaxtemp"			//temperatureMax
		capability "circlecircle06391.weatherforecast"		//weatherForecast
		capability "circlecircle06391.ozon"					//ozonLevel
		capability "circlecircle06391.ozonClass"			//ozonClass
		capability "circlecircle06391.ultravioletClass"		//ultravioletClass
		capability "circlecircle06391.locationinfo"			//locationInfo
		capability "circlecircle06391.precipChance"			//precipChance
		capability "circlecircle06391.airClass"				//airClass
		capability "circlecircle06391.pressure"				//pressure
		capability "circlecircle06391.pressureTrend"		//pressureTrend
		capability "circlecircle06391.visibility"			//visibility
		capability "circlecircle06391.sunrise"				//sunrise
		capability "circlecircle06391.sunset"				//sunset
		capability "circlecircle06391.moonrise"				//moonrise
		capability "circlecircle06391.moonset"				//moonset
		capability "circlecircle06391.moonDay"				//moonDay
		capability "circlecircle06391.status"				//statusbar
		capability "circlecircle06391.statusBar"			//status
		capability "Refresh"

		command "pollAirKorea"
		command "pollWeather"
	}
	preferences {
		input "accessKey", "text", type: "password", title: "AirKorea API Key", required: true
		input "stationName", "text", title: "Station name", description: "측청소 이름", defaultValue: "청라", required: true
		input "subStationName", "text", title: "Sub station name", description: "예비측청소 이름", defaultValue: "중봉", required: true

		input "status_1", "enum", title: "Select a status1", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "날씨", "비올확율", "체감온도", "최저온도", "최고온도", "풍속", "풍향", "기압", "기압변화", "밝기", "시계", "일출", "일몰", "월출", "월몰", "달의일수"], defaultValue: "온도"
		input "status_2", "enum", title: "Select a status2", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "날씨", "비올확율", "체감온도", "최저온도", "최고온도", "풍속", "풍향", "기압", "기압변화", "밝기", "시계", "일출", "일몰", "월출", "월몰", "달의일수", "표시안함"], defaultValue: "습도"
		input "status_3", "enum", title: "Select a status3", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "날씨", "비올확율", "체감온도", "최저온도", "최고온도", "풍속", "풍향", "기압", "기압변화", "밝기", "시계", "일출", "일몰", "월출", "월몰", "달의일수", "표시안함"], defaultValue: "공기질"
		input "status_4", "enum", title: "Select a status4", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "날씨", "비올확율", "체감온도", "최저온도", "최고온도", "풍속", "풍향", "기압", "기압변화", "밝기", "시계", "일출", "일몰", "월출", "월몰", "달의일수", "표시안함"], defaultValue: "시계"
		input "status_5", "enum", title: "Select a status5", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "날씨", "비올확율", "체감온도", "최저온도", "최고온도", "풍속", "풍향", "기압", "기압변화", "밝기", "시계", "일출", "일몰", "월출", "월몰", "달의일수", "표시안함"], defaultValue: "달의일수"

		input type: "paragraph", element: "paragraph", title: "AirKorea API Key", description: " https://www.data.go.kr/data/15073861/openapi.do<br> 위 사이트에서 활용신청하고 API Key를 발급 받으세요.", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "측정소 조회 방법", description: " 브라우저 통해 원하시는 지역을 검색하세요.<br> http://www.airkorea.or.kr/web/realSearch", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]", displayDuringSetup: false
	}
}

def statusbar(){
	def statusMap = ["온도":"temperature", "습도":"humidity", "미세먼지":"dustLevel", "미세먼지등급":"dustClass", "초미세먼지":"fineDustLevel", "초미세먼지등급":"fineDustClass", "공기질":"airClass", "날씨":"weatherForecast", "비올확율":"precipChance", "체감온도":"temperatureFeel", "최저온도":"temperatureMin", "최고온도":"temperatureMax", "풍속":"windSpeed", "풍향":"windBearing", "기압":"pressure", "기압변화":"pressureTrend", "밝기":"illuminance", "시계":"visibility", "일출":"sunrise", "일몰":"sunset", "월출":"moonrise", "월몰":"moonset", "달의일수":"moonDay"]
	if(settings.status_1 == null || settings.status_1 == "") settings.status_1 = "온도"
	if(settings.status_2 == null || settings.status_2 == "") settings.status_2 = "습도"
	if(settings.status_3 == null || settings.status_3 == "") settings.status_3 = "공기질"
	if(settings.status_4 == null || settings.status_4 == "") settings.status_4 = "시계"
	if(settings.status_5 == null || settings.status_5 == "") settings.status_5 = "달의일수"

	def status = device.currentValue(statusMap[settings.status_1])+getUnit(settings.status_1)
	if(settings.status_2 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_2])+getUnit(settings.status_2)
	if(settings.status_3 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_3])+getUnit(settings.status_3)
	if(settings.status_4 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_4])+getUnit(settings.status_4)
	if(settings.status_5 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_5])+getUnit(settings.status_5)

	sendEvent(name: "statusbar", value: status, displayed: false)
}

def getUnit(attributes){
	if(attributes == "온도") return "°C"
	if(attributes == "체감온도") return "°C"
	if(attributes == "최저온도") return "°C"
	if(attributes == "최고온도") return "°C"
	if(attributes == "습도") return "%"
	if(attributes == "비올확율") return "%"
	if(attributes == "미세먼지") return "㎍"
	if(attributes == "초미세먼지") return "㎍"
	if(attributes == "풍속") return "m/s"
	if(attributes == "기압") return "hPa"
	if(attributes == "시계") return "Km"
	if(attributes == "밝기") return "lux"
	return ""
}

def setStatusbar(String status){
	sendEvent(name: "statusbar", value: status, displayed: false)
}

def installed() {
	refresh()
}

def uninstalled() {
	unschedule()
}

def updated() {
	refresh()
}

def refresh() {
	pollAirKorea()
	pollWeather()

	unschedule()
	def airKoreaHealthCheckInterval = 15
	schedule("0 $airKoreaHealthCheckInterval * * * ?", pollAirKorea)

	runEvery10Minutes(pollWeather)
}

def pollAirKorea() {
	airKorea(settings.stationName)
	if(settings.subStationName && device.currentValue("dustLevel") == 0 && device.currentValue("fineDustLevel") == 0) {
		airKorea(settings.subStationName)
	}
}

def airKorea(stationName) {
	//log.debug "pollAirKorea()"
	if (stationName && settings.accessKey) {
		def accessKey_encode = URLEncoder.encode(URLDecoder.decode(settings.accessKey.toString(), "UTF-8"), "UTF-8").replace("+", "%2B");
		def params = [
			uri: "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=${stationName}&dataTerm=DAILY&pageNo=1&numOfRows=1&ServiceKey=${accessKey_encode}&ver=1.3&returnType=json",
			contentType: 'application/json'
		]

		def currentStatus = ""
		try {
			httpGet(params) {resp ->
				resp.headers.each {
					//log.debug "${it.name} : ${it.value}"
				}
				if (resp.status == 200) {
					//log.debug "response data: ${resp.data}"
					if(resp.data.response.body.items[0].pm10Flag != null) {
						currentStatus = resp.data.response.body.items[0].pm10Flag + ", 측정 시간: " + resp.data.response.body.items[0].dataTime
					}

					if( resp.data.response.body.items[0].pm10Value != "-" ) {
						def dustLevel = resp.data.response.body.items[0].pm10Value as Integer
						def dustClass
						if (dustLevel > 150) dustClass = "최악"
						else if (dustLevel > 80) dustClass = "나쁨"
						else if (dustLevel > 30) dustClass = "보통"
						else if (dustLevel >= 0) dustClass = "좋음"

						sendEvent(name: "dustLevel", value: dustLevel, unit: "㎍/㎥")
						sendEvent(name: "dustClass", value: dustClass)
					} else {
						sendEvent(name: "dustLevel", value: 0, unit: "㎍/㎥")
						sendEvent(name: "dustClass", value: "모름")
					}

					if( resp.data.response.body.items[0].pm25Value != "-" ) {
						def fineDustLevel = resp.data.response.body.items[0].pm25Value as Integer
						def fineDustClass
						if (fineDustLevel > 75) fineDustClass = "최악"
						else if (fineDustLevel > 35) fineDustClass = "나쁨"
						else if (fineDustLevel > 15) fineDustClass = "보통"
						else if (fineDustLevel >= 0) fineDustClass = "좋음"

						sendEvent(name: "fineDustLevel", value: fineDustLevel, unit: "㎍/㎥")
						sendEvent(name: "fineDustClass", value: fineDustClass)
					} else {
						sendEvent(name: "fineDustLevel", value: 0, unit: "㎍/㎥")
						sendEvent(name: "fineDustClass", value: "모름")
					}

					if( resp.data.response.body.items[0].khaiValue != "-" ) {
						def airQuality = resp.data.response.body.items[0].khaiValue as Integer
						def airClass
						if (airQuality > 250) airClass = "최악"
						else if (airQuality > 100) airClass = "나쁨"
						else if (airQuality > 50) airClass = "보통"
						else if (airQuality >= 0) airClass = "좋음"

						sendEvent(name: "airQuality", value: airQuality)
						sendEvent(name: "airClass", value: airClass)
					} else {
						sendEvent(name: "airQuality", value: 0)
						sendEvent(name: "airClass", value: "모름")
					}

					if( resp.data.response.body.items[0].o3Value != "-" ) {
						def ozonLevel = resp.data.response.body.items[0].o3Value as float
						def ozonClass
						if (ozonLevel > 0.15) ozonClass = "최악"
						else if (ozonLevel > 0.1) ozonClass = "나쁨"
						else if (ozonLevel > 0.03) ozonClass = "보통"
						else if (ozonLevel >= 0) ozonClass = "좋음"

						sendEvent(name: "ozonLevel", value: ozonLevel, unit: "ppm")
						sendEvent(name: "ozonClass", value: ozonClass)
					} else {
						sendEvent(name: "ozonLevel", value: 0, unit: "ppm")
						sendEvent(name: "ozonClass", value: "모름")
					}

					sendEvent(name: "status", value: stationName + ", 측정 시간: " + resp.data.response.body.items[0].dataTime)
				} else if (resp.status == 429) {
					sendEvent(name: "status", value: stationName + ", 최대조회수 초과")
				} else if (resp.status == 500) {
					sendEvent(name: "status", value: stationName + ", 서버에러")
				} else {
					sendEvent(name: "status", value: stationName + ", Error: " + resp.status)
				}
			}
		} catch (e) {
			sendEvent(name: "ozonLevel", value: 0, unit: "ppm")
			sendEvent(name: "ozonClass", value: "모름")
			sendEvent(name: "status", value: stationName + ", " + currentStatus)
			log.debug "uri: ${params.uri}"
		}
	}
	else {
		//log.debug "Missing data from the device settings station name or access key"
	}
	statusbar()
}

def pollWeather() {
	//log.debug "pollWeather()"
	def tempUnits = getTemperatureScale()
	def obs = getTwcConditions()
	if (obs) {
		sendEvent(name: "temperature", value: obs.temperature, unit: tempUnits)
		sendEvent(name: "temperatureFeel", value: obs.temperatureFeelsLike, unit: tempUnits)

		sendEvent(name: "humidity", value: obs.relativeHumidity, unit: "%")
		def weatherForecast = obs.wxPhraseMedium
		weatherForecast = weatherForecast.replace("Sunny","맑음").replace("Clear","맑음").replace("Fair","맑음").replace("Cloudy","흐림").replace("Rain Shower","소나기")
		weatherForecast = weatherForecast.replace("Snow Shower","눈").replace("Showers in Vicinity","지역에 따라 소나기").replace("Heavy Rain","폭우")
		weatherForecast = weatherForecast.replace("Freezing Rain","우박").replace("Thunderstorm","뇌우").replace("Thunder","천둥").replace("Rain","비").replace("Wind","바람")
		weatherForecast = weatherForecast.replace("Flurries","돌풍").replace("Snow","눈").replace("Fog","안개").replace("Ice","빙판").replace("Light","약한").replace("Heavy","강한")
		weatherForecast = weatherForecast.replace("Partly","대체로").replace("Mostly","대체로").replace("and","그리고").replace("Haze","연무")
		sendEvent(name: "weatherForecast", value: weatherForecast)
		sendEvent(name: "windSpeed", value: new BigDecimal(convertWindSpeed(obs.windSpeed, tempUnits == "F" ? "imperial" : "metric", "metric") / 3.6).setScale(2, BigDecimal.ROUND_HALF_UP), unit: "m/s")
		sendEvent(name: "visibility", value: obs.visibility as int, unit: "Km")
		sendEvent(name: "pressure", value: obs.pressureAltimeter as int, unit: "hPa")

		def pressureTendencyTrend = obs.pressureTendencyTrend
		pressureTendencyTrend = pressureTendencyTrend.replace("Steady","안정적").replace("Rising","상승중").replace("Falling","하락중")
		sendEvent(name: "pressureTrend", value: pressureTendencyTrend)

		def windBearing = obs.windDirectionCardinal
		windBearing = windBearing.replace("E","동").replace("W","서").replace("S","남").replace("N","북").replace("CALM","없음")
		sendEvent(name: "windBearing", value: windBearing)

		def loc = getTwcLocation()?.location
		def cityValue = createCityName(loc)
		if (cityValue != device.currentValue("locationInfo")) {
			sendEvent(name: "locationInfo", value: cityValue)
		}

		sendEvent(name: "ultravioletIndex", value: obs.uvIndex)

		def ultravioletClass = "모름"
		if (obs.uvDescription == "Extreme") ultravioletClass = "최악"
		else if (obs.uvDescription == "Very High") ultravioletClass = "최악"
		else if (obs.uvDescription == "High") ultravioletClass = "나쁨"
		else if (obs.uvDescription == "Moderate") ultravioletClass = "보통"
		else if (obs.uvDescription == "Low") ultravioletClass = "좋음"

		sendEvent(name: "ultravioletClass", value: ultravioletClass)

		def dtf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
		def sunriseDate = dtf.parse(obs.sunriseTimeLocal)
		def sunsetDate = dtf.parse(obs.sunsetTimeLocal)
		def tf = new java.text.SimpleDateFormat("HH:mm:ss")
		tf.setTimeZone(TimeZone.getTimeZone(loc?.ianaTimeZone))
		def localSunrise = "${tf.format(sunriseDate)}"
		def localSunset = "${tf.format(sunsetDate)}"
		sendEvent(name: "sunrise", value: localSunrise)
		sendEvent(name: "sunset", value: localSunset)
		sendEvent(name: "illuminance", value: estimateLux(obs, sunriseDate, sunsetDate), unit: "lux")

		// Forecast
		def f = getTwcForecast()
		if (f) {
			def precipChance = f.daypart[0].precipChance[0] != null ? f.daypart[0].precipChance[0] : f.daypart[0].precipChance[1]
			sendEvent(name: "precipChance", value: precipChance as int, unit: "%")
			def temperatureMin = f.temperatureMin[0] != null ? f.temperatureMin[0] : f.temperatureMin[1]
			sendEvent(name: "temperatureMin", value: temperatureMin as int, unit: tempUnits)
			def temperatureMax = f.temperatureMax[0] != null ? f.temperatureMax[0] : f.temperatureMax[1]
			sendEvent(name: "temperatureMax", value: temperatureMax as int, unit: tempUnits)

			def moonriseDate = dtf.parse(f.moonriseTimeLocal[0] != null ? f.moonriseTimeLocal[0] : f.moonriseTimeLocal[1])
			def moonsetDate = dtf.parse(f.moonsetTimeLocal[0] != null ? f.moonsetTimeLocal[0] : f.moonsetTimeLocal[1])
			def localMoonrise = "${tf.format(moonriseDate)}"
			def localMoonset = "${tf.format(moonsetDate)}"
			sendEvent(name: "moonrise", value: localMoonrise)
			sendEvent(name: "moonset", value: localMoonset)

			def moonDay = f.moonPhaseDay[0] != null ? f.moonPhaseDay[0] : f.moonPhaseDay[1]
			sendEvent(name: "moonDay", value: moonDay)
		}
	} else {
		//log.warn "No response from TWC API"
	}
	statusbar()
}

private estimateLux(obs, sunriseDate, sunsetDate) {
	def lux = 0
	def now = new Date().time

	if(obs.dayOrNight != 'N') {
		switch(obs.iconCode) {
			case '04':
				lux = 200
				break
			case ['05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26']:
				lux = 1000
				break
			case ['27','28']:
				lux = 2500
				break
			case ['29','30']:
				lux = 7500
				break
			default:
				//sunny, clear
				lux = 10000
		}
		//adjust for dusk/dawn
		def afterSunrise = now - sunriseDate.time
		def beforeSunset = sunsetDate.time - now
		def oneHour = 1000 * 60 * 60

		if(afterSunrise < oneHour) {
			//dawn
			lux = (long)(lux * (afterSunrise/oneHour))
		} else if (beforeSunset < oneHour) {
			//dusk
			lux = (long)(lux * (beforeSunset/oneHour))
		}
		if(lux < 1) lux = 1	// obs.dayOrNight이 늦게 변경되는 경우가 있음
	} else {
		//night - always set to 10 for now
		//could do calculations for dusk/dawn too
		lux = 1
	}
	return lux
}

private convertWindSpeed(value, fromScale, toScale) {
	def fs = fixScale(fromScale)
	def ts = fixScale(toScale)
	if (fs == ts) {
		return value
	}
	if (ts == 'imperial') {
		return value / 1.609
	}
	return value * 1.609
}

private createCityName(location) {
	def cityName = null

	if (location) {
		cityName = location.city + ", "

		if (location.adminDistrictCode) {
			cityName += location.adminDistrictCode
			cityName += " "
			cityName += location.countryCode ?: location.country
		} else {
			cityName += location.country
		}
	}

	cityName
}

private fixScale(scale) {
	switch (scale.toLowerCase()) {
		case "c":
		case "metric":
			return "metric"
		default:
			return "imperial"
	}
}