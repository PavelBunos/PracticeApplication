const api = window.axios;

Vue.component('database-row', {
    props: ['database'],
    template:
        '<tr :database="database" :class="dbRowClass">' +
        '<td>{{database.name}}</td><td>{{database.hostname}}</td><td>{{database.port}}</td><td>{{database.user}}</td><td>{{database.password}}</td>' +
        '</tr>',
    computed: {
        dbRowClass() {
            return this.database.connectionStatus ? "" : "db-disconnected";
        }
    }
})

Vue.component('databases-table', {
    template:
        '<table>' +
        '<thead>' +
        '<tr>' +
        '<td>название</td><td>хост</td><td>порт</td><td>пользователь</td><td>пароль</td>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<database-row v-for="database in databases" :database="database" :key="database.databaseId"></database-row>' +
        '</tbody>' +
        '</table>',
    props: ['databases'],
    created: function () {
        this.$root.updateDatabases();
    }
})

let app = new Vue({
    el: '#app',
    data() {
        return {
            isNotEditMode: true,
            dbRowClass: "",
            databases: [],
            selectedDatabase: null,
            isOpen: false,
            newDatabase: {
                name: null,
                hostname: null,
                port: null,
                user: null,
                password: null
            }
        }
    },
    methods: {
        updateDatabases() {
            this.databases = [];
            api.get("/practiceApp/databases").then(response => {
                const data = response.data.data;
                data.forEach(database => {
                    this.databases.push(database);
                })
            })
        },
        deleteDatabase() {
            if (this.selectedDatabase) {
                api.delete("/practiceApp/databases/remove/" + this.selectedDatabase).then(response => {
                    if (response.status == 200) {
                        this.updateDatabases();
                        alert("База данных убрана из подключенных!");
                    } else {
                        alert("Не удалось убрать базу данных");
                    }
                }).catch(error => {
                    alert("Произошла непредвиденная ошибка!");
                    console.error(error);
                });
            } else {
                alert("База данных не выбрана!");
            }
        },
        openEditDialog() {
            if (this.selectedDatabase) {
                this.isNotEditMode = false;
                this.isOpen = true;

                let database = api.get(`/practiceApp/databases/database?name=${encodeURIComponent(this.selectedDatabase)}`).then(response => {
                    if (response.status == 200) {
                        this.newDatabase = response.data.data;
                    } else {
                        alert("Произошла ошибка!");
                    }
                }).catch(error => {
                    alert("Произошла непредвиденная ошибка!");
                    console.error(error);
                    return;
                });
            } else {
                alert("База данных не выбрана!");
            }
        },
        editDatabase() {
            if (!isNaN(this.newDatabase.port)) {
                api.patch("/practiceApp/databases/edit", this.newDatabase).then(response => {
                    if (response.status == 200) {
                        this.updateDatabases();
                        alert("Изменения сохранены!");
                    } else {
                        alert("Произошла ошибка!");
                        console.log(response.status);
                    }
                }).catch(error => {
                    alert("Произошла непредвиденная ошибка!");
                    console.error(error);
                    return;
                });

                this.closeDialog();
            } else {
                alert("Значения поля \"Порт\" - не число!");
            }
        },
        closeDialog() {
            this.isOpen = false;
        },
        openAddDialog() {
            this.isOpen = true;
            this.isNotEditMode = true;
        },
        addDatabase() {
            if (this.newDatabase.name && this.newDatabase.port && this.newDatabase.hostname
                && this.newDatabase.user && this.newDatabase.password) {

                if (!isNaN(this.newDatabase.port)) {
                    api.post("/practiceApp/databases/add", this.newDatabase).then(response => {
                        if (response.status == 200) {
                            this.updateDatabases();
                        }

                        alert(response.data.data);
                    }).catch(error => {
                        alert("Произошла непредвиденная ошибка!");
                        console.error(error);
                        return;
                    });
                } else {
                    alert("Значения поля \"Порт\" - не число!");
                }

                this.isOpen = false;
            } else {
                alert("Все поля должны быть заполнены!");
            }
        }
    }
})