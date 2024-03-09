let users = []
let dishes = []
let dishesMinId = Number.MIN_VALUE
let dishesMaxId = Number.MAX_VALUE
let revenue = 0
let comparators = {
    "users": userComp,
    "avgRate": avgComp,
    "freq": freqComp,
    "rev": revenueComp
}

function revenueComp(a, b) {
    return a.revenue - b.revenue
}

function refreshDishes() {

    let checkBox = document.getElementById("select-all")

    let min = document.getElementById("min-id")
    let max = document.getElementById("max-id")

    dishesMinId = (min === null || min.value === "") ? dishesMinId : min.value
    dishesMaxId = (max === null || max.value === "") ? dishesMaxId : max.value

    document.getElementById("revenue")?.remove()
    document.getElementById("table")?.remove()
    document.getElementById("container-text").innerHTML += getDishTable()
}

function checkInRange(value) {

    return dishesMinId <= value && value <= dishesMaxId
}

function reverse() {
    let tbody = document.getElementById("tbody")
    tbody.append(...Array.from(tbody.childNodes).reverse())
}

function setUsers() {
    users = JSON.parse(document.querySelector("#users").value)
}

function setDishes() {
    dishes = JSON.parse(document.querySelector("#dishes").value)
}

function avgComp(a, b) {
    return a.avgRate - b.avgRate
}

function userComp(a, b) {
    return a.id - b.id
}

function freqComp(a, b) {
    return a.quantity - b.quantity
}


function selection() {
    let checkBox = document.getElementById("select-all")
    let filter = document.getElementById("filter-text")
    if (checkBox.checked === false) {
        filter.innerHTML +=
            '<form id="checked-true">' +
            '<input id="min-id" placeholder="Input new min id" type="number">' +
            '<input id="max-id" placeholder="Input new max id" type="number">' +
            '</form>'
    } else {
        dishesMinId = Number.MIN_VALUE
        dishesMaxId = Number.MAX_VALUE
        document.getElementById("checked-true")?.remove()
    }

}

function buildDishInfo(text) {
    text.innerHTML =
        '<filter-text id="filter-text">' +
        '<form id="id_span">' +
        '<input id="select-all" type="checkbox" class="selection" onchange="selection()">' +
        '<label for="select-all">Select all</label>' +
        '<button  type="button" onclick="refreshDishes()">&#8634</button>' +
        '</form>' +
        '</filter-text>'
    //selection()
    text.innerHTML += getDishTable()
}

function getDishTable() {
    let table = '<table id="table"><thead><th>Id</th><th colspan="2">Name</th><th>Quantity sold</th><th>Average rate</th><th>Revenue</th></> </thead><tbody id="tbody">'
    revenue = 0
    for (let i = 0; i < dishes.length; ++i) {
        let dish = dishes[i]
        if (checkInRange(dish.id)) {
            revenue += dish.revenue
            table += '<tr><td>' + dish.id + '</td><td colspan="2">' + dish.name + '</td><td>' + dish.quantity + '</td><td>' + dish.avgRate + '</td><td>' + dish.revenue + '</td> </tr>'
        }
    }
    let rev = '<rev id="revenue"> Revenue per range:' + revenue + '</rev>'
    return (rev + table + '</tbody></table>')

}

function buildUserInfo(text) {
    text.innerHTML = '<div id="users-info">Number of user in system:' + users.length + '</div>'
    text.innerHTML += getUserTable()
}

function getUserTable() {
    let table = '<table id="table">' +
        '<thead><tr>' +
        '<th>Id</th>' +
        '<th>Login</th>' +
        '<th>Role</th>' +
        '</tr></thead><tbody id="tbody">'

    for (let i = 0; i < users.length; ++i) {
        let user = users[i]
        table +=
            '<tr><td>' + user.id + '</td><td>'
            + user.login + '</td><td>' + user.root +
            '</td> </tr>'
    }
    return table + '</tbody></table>'
}

function showAll(compName) {
    let text = document.getElementById("container-text")
    if (typeof compName != "string" || !(compName in comparators)) {
        text.innerHTML = ""
        return
    }

    if (compName === "users") {
        users.sort(comparators[compName])
        buildUserInfo(text)
    } else {
        dishes.sort(comparators[compName])
        buildDishInfo(text)
    }
}