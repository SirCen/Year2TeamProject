sendEvent = function(sel, step) {
    var sel_event = new CustomEvent('next.m.' + step, {detail: {step: step}});
    window.dispatchEvent(sel_event);
}

function selectAll(formName) {
    var checkboxes = document[formName].getElementsByTagName('input');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].type == 'checkbox') {
            checkboxes[i].checked = !checkboxes[i].checked;
        }
    }
}

function enableSubmit() {
    var checkboxes = document.getElementsByTagName('input');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].type == 'checkbox') {
            if (checkboxes[i].checked == true) {
                $('#continueButton').prop('disabled', false);
                break;
            } else {
                $('#continueButton').prop('disabled', true);
            }
        }
    }
    var selectAll = document.getElementById('selectAll');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].type == 'checkbox') {
            if (checkboxes[i].checked == false) {
                selectAll.checked = false;
                break;
            } else {
                selectAll.checked = true;
            }
        }
    }
}