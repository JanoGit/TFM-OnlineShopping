$(document).ready(function () {
    $('#navbar').load('../templates/navbar.html');
    $('#container_spa').load('../templates/product.html', "", function () {
        $('#start_page').click("start_page", loadContainerSpa);
        $('#preferences').click("preferences", loadContainerSpa);
        $('#play').click("play", loadContainerSpa);
        $('#records').click("records", loadContainerSpa);
        $('#register').click("register", loadContainerSpa);
        $('#login').click("login", loadContainerSpa);
        $('#logout').click("logout", loadContainerSpa);
    });
});

function loadContainerSpa(event) {
    if (window.countdown) {
        clearInterval(countdown);
    }
    $('#container_spa').load("html/" + event.data + ".html");
}
