// Validate email.
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

// Creates the context menu.
chrome.contextMenus.create({
    id: 'keepInTouch',
    title: 'Keep in touch',
    contexts: ['link', 'selection']
});

// Add a link to loose touch dashboard.
chrome.browserAction.onClicked.addListener(function () {
    return chrome.tabs.create({url: "http://www.scub.net"})
});

// Opens loose touch form if there is a click.
chrome.contextMenus.onClicked.addListener(function (info, tab) {
    if (info.menuItemId === "keepInTouch") {
        // Getting what is selected.
        var value = "";
        if (info.linkUrl != null) {
            // If it's a link.
            value = info.linkUrl.replace('mailto:', '');
        } else {
            // If it's a selection.
            value = info.selectionText.sub(0, 100).replace('mailto:', '').replace('<sub>', '').replace('</sub>', '');
        }
        if (!validateEmail(value)) {
            alert("This is not a valid email address");
        } else {
            chrome.windows.create({url: 'https://www.google.com?q=' + value, width: 520, height: 660});
        }
    }
});