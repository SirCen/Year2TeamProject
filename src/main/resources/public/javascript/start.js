function selectTable() {
    var t = document.getElementById("tableSelect");
    var tableId = t.options[t.selectedIndex].value;
    document.getElementById("tableSelectButton").value = tableId;
    document.getElementById("tableSelectButton").disabled = false;
    //sessionStorage.setItem('table', tableId);
}
