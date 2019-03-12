// Validate email.
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

// Creates the context menu.
chrome.contextMenus.create({
    id: 'keepInTouch',
    title: 'Keep in touch',
    contexts: ['link']
});

// Add a link to loose touch dashboard.
chrome.browserAction.onClicked.addListener(function () {
    return chrome.tabs.create({url: "http://www.serli.fr"})
});

// Opens loose touch form if there is a click.
chrome.contextMenus.onClicked.addListener(function (info, tab) {
    if (info.menuItemId === "keepInTouch") {
        var value = info.linkUrl.replace('mailto:', '');
        if (!validateEmail(value)) {
            alert(value + " is not a valid email address");
        } else {
            chrome.windows.create({url: 'https://www.google.com?q=' + value, width: 520, height: 660});
        }
    }
});