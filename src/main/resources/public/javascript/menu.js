let order = {};
let totalSize = 0;
let itemLimit = 25;

function addItem(id, name, modification="") {
  modification = "{" + modification + "}";
  if (totalSize < itemLimit * 3) {
    if (!(id in order)) {
      order[id] = {
        'name' : name
      };
      order[id][modification] = 1;
      totalSize++;
    } else {
      let itemQuantity = 0;
      for (let value of Object.values(order[id])) {
        if (value !== order[id]['name']) itemQuantity += value
      }

      if (itemQuantity < itemLimit) {
        if (!(modification in order[id])) order[id][modification] = 0;
        order[id][modification]++;
        totalSize++;
      } else {
        document.getElementById("limitDescription").innerHTML = "You have added too many " + name + " to your order!";
        $('#order-limit-modal').modal('toggle');
      }
    }
  } else {
    document.getElementById("limitDescription").innerHTML = "You have too many items in your order!";
    $('#order-limit-modal').modal('toggle');
  }
  displayOrder();
  document.getElementById("adjustments").value = "";
}

function removeItem(id, modification="") {
  modification = "{" + modification + "}";
  if (id in order && modification in order[id]) {
    order[id][modification]--;
    totalSize--;
    if (order[id][modification] <= 0) delete order[id][modification];
    if (Object.keys(order[id]).length <= 1) delete order[id];
  } else {
    console.log("Item not found in order");
  }
  displayOrder();
}

function displayOrder() {
  let html = `<ul class="list-group list-group-flush">`
  for (let id in order) {
    for (let mod in order[id]) {
      if (order[id][mod] !== order[id]['name']) {
        html += makeItem(id, mod);
      }
    }
  }
  document.getElementById("orderList").innerHTML = (html + "</ul>");
}

function makeItem(id, modification) {
  let mod = modification.slice(1, modification.length - 1);
  let disabled = ``;
  if (totalSize >= itemLimit * 3 || order[id][modification] >= 25) disabled = `disabled`;
  return `
            <li class="list-group-item order-list">
              <div class="container-fluid">
                <div class="row">
                  <div class="col-md-6">
                    <div class="row">
                      <div class="col-md-12">
                        <h4>` + order[id]['name'] + `</h4>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-12">
                        <p>` + mod + `</p>
                      </div>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="form-inline form-group order-list-form-group">
                      <div class="btn info-button" id="orderRemoveItem" onclick="removeItem(` + id + `, '` + mod + `')">-</div>
                      <input type="text" class="form-control order-textbox" size="2" value="` + order[id][modification] + `" onkeypress="userChangeQuantity(` + id + `, '` + mod + `', this.value, event)">
                      <div class="btn info-button" id="orderAddItem" onclick="addItem(` + id + `, '` + order[id]['name'] + `', '` + mod + `')" ` + disabled + `>+</div>
                    </div>
                  </div>
                </div>
              </div>
            </li>
            `
}

function userChangeQuantity(id, modification, quantity, event) {
  if (event.keyCode !== 13) return;
  quantity = +quantity;
  if (isNaN(quantity)) return;

  if (quantity <= 0) {
    delete order[id][modification];
  } else {
    quantity = Math.min(25, quantity);
    order[id]["{" + modification + "}"] = quantity;
  }
  displayOrder();
}

function openModifyModal(id, name) {
  document.getElementById("mod-modal-body").setAttribute("data-id", id);
  document.getElementById("mod-modal-body").setAttribute("data-name", name);
}

function submitOrder() {
  if (Object.entries(order).length !== 0 && order.constructor === Object) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/menu/submitOrder", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(order));
    setTimeout("location.href = '/postorder';", 2000);
  }
}