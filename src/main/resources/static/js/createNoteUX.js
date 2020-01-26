const sharedText = document.getElementById("sharedwith");
const radioPublic = document.getElementById("public");
const radioPrivate = document.getElementById("private");
const radioShared = document.getElementById("shared");
sharedText.disabled = true;

function toggleShared() {
    if (radioPublic.checked) {
        sharedText.disabled = true;
    } else if (radioPrivate.checked) {
        sharedText.disabled = true;
    } else if (radioShared.checked) {
        sharedText.disabled = false;
    }
}