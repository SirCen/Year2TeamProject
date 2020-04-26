//
// Add user JS

// Get the modal
var addUserModal = document.getElementById("addUserModal");

// Get the button that opens the modal
var addUserBtn = document.getElementById("addUserBtn");

// Get the <span> element that closes the modal
var addUserSpan = document.getElementById("close-add-user");

// When the user clicks on the button, open the modal
addUserBtn.onclick = function() {
    addUserModal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
addUserSpan.onclick = function() {
    addUserModal.style.display = "none";
}

//
// Enable/disable users JS

// Get the modal
var modifyUserModal = document.getElementById("modifyUserModal");

// Get the button that opens the modal
var modifyUserBtn = document.getElementById("modifyUserBtn");

// Get the <span> element that closes the modal
var modifyUserSpan = document.getElementById("close-modify-user");

// When the user clicks on the button, open the modal
modifyUserBtn.onclick = function() {
    modifyUserModal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
modifyUserSpan.onclick = function() {
    modifyUserModal.style.display = "none";
}

// Enabled inputs after selecting a staff member
function selectStaff() {
    document.getElementById("enabled").disabled = false;
    document.getElementById("disabled").disabled = false;
    document.getElementById("nameModify").disabled = false;
    document.getElementById("pswModify").disabled = false;
    document.getElementById("psw2Modify").disabled = false;
    document.getElementById("roleModify").disabled = false;
    document.getElementById("confirmModifyBtn").disabled = false;
    document.getElementById("confirmModifyBtn").value = document.getElementById("staffSelect").value;
}

//
// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target === modifyUserModal) {
        modifyUserModal.style.display = "none";
    }
    if (event.target === addUserModal) {
        addUserModal.style.display = "none";
    }
}