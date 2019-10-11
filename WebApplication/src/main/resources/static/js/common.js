function getRequest(url, onSuccess, onError) {
    $.ajax({
        type: 'GET',
        url: url,
        async: true,
        success: onSuccess,
        error: onError
    });
}
function getRequestF(url, onSuccess, onError) {
    $.ajax({
        type: 'GET',
        url: url,
        async: false,
        success: onSuccess,
        error: onError
    });
}
function postRequest(url, data, onSuccess, onError) {
    $.ajax({
        type: 'POST',
        url: url,
        async: true,
        data: JSON.stringify(data),
        contentType: 'application/json',
        processData: false,
        success: onSuccess,
        error: onError
    });
}
function sleep(d){
  for(var t = Date.now();Date.now() - t <= d;);
}