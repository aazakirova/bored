function getActivity(type) {
    var response = $http.get("http://www.boredapi.com/api/activity/", {
        timout: 15000,
        query: {
            type: type,
            lang: "en"
        }
    });
    if (!response.isOk || !response.data) {
        return false;
    }
    
    var task = {};
    task.activity = response.data.activity;
    task.participants = response.data.participants;
    return task; 
}
