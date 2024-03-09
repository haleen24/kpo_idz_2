function review_checker() {
    let radios = document.querySelectorAll("input[type='radio']")
    let check = false
    let form = document.querySelector("#form")
    radios.forEach(function (elem) {
        check |= elem.checked
    })
    let error = document.querySelector(".error-message")
    if (!check) {
        error.innerText = "ratio was not added"
        return
    }
    if (form.querySelector("#text").length > 256) {
        error.innerText = "Text is too long"
        return;
    }
    form.submit()
}