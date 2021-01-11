function resetClicked(button){
    var panel = button.parentElement;
    var buttonElements = panel.getElementsByClassName("skillButton");
    var buttons = [].slice.call(buttonElements);
    buttons.reverse();
    buttons.forEach((button) => {
        skillClicked(button, {"button":1,"shiftKey":true});
    });
    plusClicked(panel.getElementsByClassName("plusButton")[0], {"button":1,"shiftKey":true});
}