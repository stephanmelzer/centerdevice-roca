document.getElementById('newUploadButton').style.visibility = "visible";
document.getElementById('fallback-input').className = "hidden";

document.getElementById('fileSelector').addEventListener('change', function(event) {
    if (event.target.value.length > 0) {
        document.getElementById('uploadButton').click();
    }
});

selectFile = function() {
    document.getElementById("fileSelector").click();
};