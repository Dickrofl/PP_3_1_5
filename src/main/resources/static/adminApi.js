const url = "http://localhost:8088/api/admin/users/"

const currentUserId = document.querySelector("#user-nav-link").getAttribute("data")
const userInfo = document.querySelector("#current-user-info")
let usersTable = document.querySelector("#users-table")
const addUserForm = document.addUserForm
const editUserForm = document.userEditForm

const usersTableTemplate = {
    0: "ID",
    1: "First Name",
    2: "Last Name",
    3: "Age",
    4: "Email",
    5: "Role",
    6: "Edit",
    7: "Delete"
}

const userFetch = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json; charset=UTF-8'
    },
    getUsersList: async () => await fetch(url),
    getUser: async (id) => await fetch(url + id),
    createUser: async (user) => await fetch(url, {method: "POST", headers: userFetch.head, body: JSON.stringify(user)}),
    updateUser: async (user) => await fetch(url, {method: "PUT", headers: userFetch.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(url + id, {method: "DELETE"})
}


window.onload = () => {
    updateCurrentUserInfo()
    showAdminPanel()
    document.getElementById("submit-edit-user-btn").addEventListener("click", editUser)
}


async function updateCurrentUserInfo() {
    await getCurrentUser().then(user => {
        const username = document.createElement("b")
        const text = document.createTextNode(" with roles ")
        const roles = document.createElement("b")

        username.insertAdjacentText("afterbegin", user.username)
        roles.insertAdjacentText("beforeend", user.roles.map(role => " " + role.name))

        if (!userInfo.hasChildNodes()) {
            userInfo.appendChild(username)
            userInfo.appendChild(text)
            userInfo.appendChild(roles)
        } else {
            userInfo.firstChild.replaceWith(username)
            userInfo.lastChild.replaceWith(roles)
        }
    })
}

async function getCurrentUser() {
    console.log("Работает метод getCurrentUser")
    let response = await userFetch.getUser(currentUserId)
    let user = await response.json()
    return user
}

function showUserPanel() {
    console.log("Работает метод showUserPanel")
    getCurrentUser().then(user => {
        $("#users-table").remove()
        usersTable = document.createElement("table")
        usersTable.setAttribute("class", "table table-striped table-hover")
        usersTable.setAttribute("id", "users-table")
        document.querySelector("#all-users-card-body").appendChild(usersTable)

        $("#user-panel-title").text("User information-page")
        $("#admin-nav-tabs").addClass("collapse")
        $("#users-tab-name").text("About user")
        $("#admin-nav-link").removeClass("active").prop("aria-selected", false)
        $("#user-nav-link").addClass("active").prop("aria-selected", true)
        $("#new-user-panel").addClass("collapse")
        $("#admin-panel").removeClass("collapse")

        let tblHead = usersTable.createTHead()
        let tblBody = usersTable.createTBody()
        let tblHeadRow = tblHead.insertRow()
        let tblBodyRow = tblBody.insertRow()


        for (let i = 0; i < 6; i++) {
            let th = document.createElement("th")
            let text = document.createTextNode(usersTableTemplate[i])
            th.appendChild(text)
            tblHeadRow.appendChild(th)
        }

        tblBodyRow.insertCell().appendChild(document.createTextNode(user.id))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.firstName))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.lastName))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.age))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.username))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.roles.map(role => role.name)))
    })
}

async function showAdminPanel() {
    console.log("Работает метод showAdminPanel")

    $("#users-table").remove()
    usersTable = document.createElement("table")
    usersTable.setAttribute("class", "table table-striped table-hover")
    usersTable.setAttribute("id", "users-table")
    document.querySelector("#all-users-card-body").appendChild(usersTable)

    $("#user-panel-title").text("Admin panel")
    $("#admin-nav-tabs").removeClass("collapse")
    $("#users-tab-name").text("All users")
    $("#user-nav-link").removeClass("active").prop("aria-selected", false)
    $("#admin-nav-link").addClass("active").prop("aria-selected", true)
    $("#new-user-panel").addClass("collapse")
    $("#admin-panel").removeClass("collapse")
    $("#users-table-tab").addClass("active")
    $("#new-user-tab").removeClass("active")

    let tblHead = usersTable.createTHead()
    let tblBody = usersTable.createTBody()
    let tblHeadRow = tblHead.insertRow()

    // Filling table head
    for (let i = 0; i < 8; i++) {
        let th = document.createElement("th")
        let text = document.createTextNode(usersTableTemplate[i])
        th.appendChild(text)
        tblHeadRow.appendChild(th)
    }

    // Filling table body
    userFetch.getUsersList().then(res => {
        res.json().then(users => {
            users.forEach(user => {
                let editButton = document.createElement("button")
                editButton.setAttribute("type", "button")
                editButton.setAttribute("class", "btn btn-primary")
                editButton.textContent = "Edit"
                editButton.onclick = () => {
                    showEditUserForm(user.id)
                }


                let deleteButton = document.createElement("button")
                deleteButton.setAttribute("type", "button")
                deleteButton.setAttribute("class", "btn btn-danger")
                deleteButton.textContent = "Delete"
                deleteButton.onclick = () => {
                    showDeleteUserForm(user.id)
                }

                let tblBodyRow = tblBody.insertRow()
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.id))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.firstName))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.lastName))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.age))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.username))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.roles.map(roles => " " + roles.name)))
                tblBodyRow.insertCell().appendChild(editButton)
                tblBodyRow.insertCell().appendChild(deleteButton)
            })
        })
    })
}

function showNewUserForm() {
    $("#new-user-panel").removeClass("collapse")
    $("#admin-panel").addClass("collapse")
}

function addNewUser() {
    console.log("Работает метод addNewUser")

    // Проверка на заполненность всех полей
    if (
        addUserForm.addFirstName.value === "" ||
        addUserForm.addLastName.value === "" ||
        addUserForm.addAge.value === "" ||
        addUserForm.addEmail.value === "" ||
        addUserForm.addPassword.value === "" ||
        addUserForm.addRoles.value === ""
    ) {
        alert("Please fill in all fields")
        return;
    }

    let user = {
        firstName: addUserForm.addFirstName.value,
        lastName: addUserForm.addLastName.value,
        age: addUserForm.addAge.value,
        username: addUserForm.addEmail.value,
        password: addUserForm.addPassword.value,
        roles: []
    }

    Array.from(addUserForm.addRoles.options)
        .filter(option => option.selected)
        .map(option => option.value)
        .forEach(name => {
            let role = {
                id: 0,
                name: ""
            }
            if (name === "ROLE_ADMIN") {
                role.id = 1
                role.name = "ROLE_ADMIN"
            }
            if (name === "ROLE_USER") {
                role.id = 2
                role.name = "ROLE_USER"
            }
            user.roles.push(role)
        })

    console.log(user)

    userFetch.createUser(user).then((response) => {
        document.getElementById('addFirstName').value = ""
        document.getElementById('addLastName').value = ""
        document.getElementById('addAge').value = ""
        document.getElementById('addEmail').value = ""
        document.getElementById('addPassword').value = ""
        document.getElementById('addRoles').value = ""
        if (response.ok) {
            showAdminPanel();
        } else {
            alert("Error. Try again")
            showNewUserForm();
        }
    })
}

function showEditUserForm(id) {
    console.log("Работает метод showEditUserForm")
    userFetch.getUser(id)
        .then((res) => {
            res.json().then((user) => {
                console.log(user)
                $("#editId").val(user.id)
                $("#editFirstName").val(user.firstName)
                $("#editLastName").val(user.lastName)
                $("#editAge").val(user.age)
                $("#editUsername").val(user.username)
                $("#editPassword").val("")
            })
            $("#editModal").modal()
        })
}




function editUser() {
    const user = {
        id: document.userEditForm.editId.value,
        firstName: document.userEditForm.editFirstName.value,
        lastName: document.userEditForm.editLastName.value,
        age: document.userEditForm.editAge.value,
        username: document.userEditForm.editUsername.value,
        password: document.userEditForm.editPassword.value,
        roles: []
    }

    Array.from(editUserForm.editRoles.options)
        .filter(option => option.selected)
        .map(option => option.value)
        .forEach(name => {
            let role = {
                id: 0,
                name: ""
            }
            if (name === "ROLE_ADMIN") {
                role.id = 1
                role.name = "ROLE_ADMIN"
            }
            if (name === "ROLE_USER") {
                role.id = 2
                role.name = "ROLE_USER"
            }
            user.roles.push(role)
        })

    console.log(user)

    // Проверка на выбор роли
    if (user.roles.length === 0) {
        alert("Please select a role")
        return;
    }

    // Проверка на наличие пароля
    if (user.password === "") {
        alert("Please enter a password")
        return;
    }

    userFetch.updateUser(user).then(res => {
        if (res.ok) {
            console.log("User with username \"" + user.username + "\" updated")
            $("#editModal").modal("hide")
            showAdminPanel()
            if (user.id == currentUserId) {
                updateCurrentUserInfo()
            }
        } else {
            showEditUserForm(user.id)
        }
    }).catch(e => console.log(e))
}

// Добавленный код для автоматического выбора роли
function showEditUserForm(id) {
    console.log("Работает метод showEditUserForm")
    userFetch.getUser(id)
        .then((res) => {
            res.json().then((user) => {
                console.log(user)
                $("#editId").val(user.id)
                $("#editFirstName").val(user.firstName)
                $("#editLastName").val(user.lastName)
                $("#editAge").val(user.age)
                $("#editUsername").val(user.username)
                $("#editPassword").val("")

                // Автоматически выбираем роль
                const rolesSelect = document.getElementById("editRoles");
                for (let i = 0; i < rolesSelect.options.length; i++) {
                    if (user.roles.some(role => role.name === rolesSelect.options[i].value)) {
                        rolesSelect.options[i].selected = true;
                    } else {
                        rolesSelect.options[i].selected = false;
                    }
                }
            })
            $("#editModal").modal()
        })
}

function showDeleteUserForm(id) {
    console.log("Работает метод showDeleteUserForm")
    userFetch.getUser(id)
        .then((res) => {
            res.json().then((user) => {
                console.log(user)
                $("#deleteId").val(user.id)
                $("#deleteFirstName").val(user.firstName)
                $("#deleteLastName").val(user.lastName)
                $("#deleteAge").val(user.age)
                $("#deleteUsername").val(user.username)
                $("#deletePassword").val("")
            })
            $("#deleteModal").modal()
        })
}

function deleteUser() {
    userFetch.deleteUser($("#deleteId").val())
        .then(res => {
            if (res.ok) {
                showAdminPanel();
                $("#deleteModal").modal("hide")
            } else {
                alert("Error. Try again")
                showDeleteUserForm();
            }
        })
}