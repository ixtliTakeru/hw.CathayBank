package g.takeru.homework.cathaybank.datamodel

//"result": {
//    "limit": 20,
//    "offset": 0,
//    "count": 16,
//    "sort": "",
//    "results": [
//    {
//        "_id": 1,
//        "_importdate": {
//         "date": "2022-05-09 14:47:38.971190",
//        "timezone_type": 3,
//        "timezone": "Asia/Taipei"
//    },
//        "e_no": "1",
//        "e_category": "戶外區",
//        "e_name": "臺灣動物區",
//        "e_pic_url": "http://www.zoo.gov.tw/iTAP/05_Exhibit/01_FormosanAnimal.jpg",
//        "e_info": "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、歐亞水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
//        "e_memo": "",
//        "e_geo": "MULTIPOINT ((121.5805931 24.9985962))",
//        "e_url": "https://youtu.be/QIUbzZ-jX_Y"
//    },

data class AreaResult(
    val result: AreaResults)

data class AreaResults(
    val results: List<Area>)

data class Area(
    val e_no: String = "",
    val e_name: String = "",
    val e_category: String = "",
    val e_pic_url: String = "",
    val e_info: String = "",
    val e_memo: String = "",
    val e_url: String = "")