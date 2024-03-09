function check_quantity(obj) {
    let form = obj.parentElement
    let input = form.querySelector("input[name='quantity']")
    let value = input.value
    let error = form.querySelector(".error-message")
    if (value === "") {
        error.innerText = "Value not presented"
    } else if (value < 0) {
        error.innerText = "Value cannot be negative"
    } else if (parseFloat(value) !== parseInt(value)) {
        error.innerText = "Value must be integer"
    } else {
        input.value = parseInt(value)
        form.submit()
    }
}