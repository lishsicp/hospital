
    //Validation checkmarks
    let inputs = document.querySelectorAll(".needs-validation");
    Array.prototype.slice.call(inputs).forEach(function (input) {
    input.addEventListener("submit", function (event) {
        if (!input.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        input.classList.add("was-validated");
    })
});

    function dateValidation() {
        let field = document.getElementById("date_of_birth");
        let curDate = new Date();
        let inputDate = new Date(field.value);
        let error = field.parentElement.querySelector(".text-danger");
        if (curDate.getDate() > inputDate.getDate()) {
            error.innerHTML = "Birth day can only be in the past";
            error.setCustomValidity("Birth day can only be in the past");
        } else {
            error.innerHTML = "";
            error.setCustomValidity("");
        }
    }

    function validateLogin() {
	    let field = document.getElementById("login");
	    let pattern = /^([a-zA-Z0-9_]+){4,30}$/;
	    let invalidFeedback = field.parentElement.querySelector(".text-danger");
	    if (!pattern.test(field.value)) {
	    	invalidFeedback.innerHTML = "Login can only have latin characters, numbers, or underscores and must be between 4 and 30 characters in length";
	    	field.setCustomValidity("Login can only have latin characters, numbers, or underscores");
	    	return false;
		} else {
	    	invalidFeedback.innerHTML = "";
	    	field.setCustomValidity("")
	    	return true;
		}
	}

    function passwordMatch() {
    let psw = document.getElementById("psw");
    let psw_repeat = document.getElementById("psw-repeat");
    let psw_repeat_err = psw_repeat.parentElement.querySelector(".text-danger");
    checkPassword(psw);
    if (psw_repeat.value.length > 0) {
    if (psw.value !== psw_repeat.value) {
    psw_repeat_err.innerHTML = "Passwords dont match";
    psw_repeat.setCustomValidity("Passwords do not match");
    return false;
} else {
    psw_repeat_err.innerHTML = null;
    psw_repeat.setCustomValidity('');
}
}
    return true;
}

    function validateEmail() {
    let input = document.getElementById("email");
    let error = input.parentElement.querySelector(".text-danger");
    let pattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (!pattern.test(input.value)) {
    input.setCustomValidity("Email must match the following format: example@example.com");
    error.innerHTML = "E-mail must match the following format: username@example.com";
} else {
    input.setCustomValidity('');
    error.innerHTML = null;
}
}

    function validateGender() {
        const gender = document.getElementById("gender");
        let error = gender.parentElement.querySelector(".text-danger");
        if (gender.value === "Chose your gender") {
            gender.setCustomValidity("Please select your gender");
            error.innerHTML = "Please select your gender";
        } else {
            gender.setCustomValidity("");
            error.innerHTML = null;
        }
    }

    function isBlank(input) {
    if (input.value.trim() === "") {
    input.setCustomValidity("Cannot be blank")
    input.parentElement.querySelector(".text-danger").innerHTML = "Cannot be blank";
} else {
    input.setCustomValidity("");
    input.parentElement.querySelector(".text-danger").innerHTML = null;
}
}
