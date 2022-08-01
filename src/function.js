function getActivity(type) {
    var response = $http.get("https://www.boredapi.com/api/activity", {
        timout: 15000,
        query: {
            type: type,
            lang: "en"
        }
    });
    
//    log("@@@@" + toPrettyString(response)); 
    
    if (!response.isOk || !response.data) {
        return false;
    }
    
    var task = {};
    task.activity = response.data.activity;
    task.participants = response.data.participants;
    return task; 
}