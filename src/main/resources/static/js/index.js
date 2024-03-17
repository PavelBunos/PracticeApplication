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
    props: ['databases', 'updateDb'],
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
    methods: {

    },
    created: function () {
        this.updateDb();
    }
})

const SettingsComponent = Vue.component('SettingsComponent', {
    template: `
        <div class="page">
        <div class="block">
        <h1>Настройки</h1>

        <div class="section">
            <h2>Базы данных</h2>
            <div class="table-container">
                <databases-table :databases="databases" :updateDb="updateDatabases"></databases-table>
            </div>
            <div class="container">
                <button @click="openAddDialog()">Добавить</button>
                <button @click="updateDatabases()">Обновить</button>
            </div>
            <div class="container">
                <select v-model="selectedDatabase">
                    <option v-for="database in databases" :key="database.databaseId">{{ database.name }}</option>
                </select>
                <button @click="deleteDatabase()">Удалить</button>
                <button @click="openEditDialog()">Изменить</button>
            </div>

        </div>

        <div class="dialog" v-if="isOpen">
            <img src="img/close.png" class="close-icon" @click="closeDialog()">
            <h3>Информация базы данных</h3>

            <label v-if="isNotEditMode">Название</label><input type="text" v-model="newDatabase.name" v-if="isNotEditMode">
            <label>Хост</label><input type="text" v-model="newDatabase.hostname">
            <label>Порт</label><input type="text" v-model="newDatabase.port">
            <label>Пользователь</label><input type="text" v-model="newDatabase.user">
            <label>Пароль</label><input type="text" v-model="newDatabase.password">

            <button @click="addDatabase()" v-if="isNotEditMode">Добавить</button>
            <button @click="editDatabase()" v-if="!isNotEditMode">Изменить</button>
        </div>
        <div v-if="isOpen" class="overlay"></div>
        </div>
        </div>
        `,
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
            api.get("/practiceApp/databases").then(response => {
                this.databases = [];
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
                        alert(response.data.data);
                    }
                }).catch(error => {
                    alert(error.response.data.data);
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
                        alert(response.data.data);
                    }
                }).catch(error => {
                    alert(error.response.data.data);
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
                        alert(response.data.data);
                    }
                }).catch(error => {
                    alert(error.response.data.data);
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

            this.newDatabase = {
                name: null,
                    hostname: null,
                    port: null,
                    user: null,
                    password: null
            }
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
                        alert(error.response.data.data);
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

Vue.component('database-option', {
    props: ['database'],
    template:
        '<option>' +
        '{{database.name}}' +
        '</option>'
});

Vue.component('databases-list', {
    props: ['databases'],
    template:
        '<select class="databases-list" v-model="selectedValue" @change="handleChange">' +
        '<database-option v-for="database in databases" :database="database" :key="database.databaseId" />' +
        '</select>',
    created: function () {
        api.get("/practiceApp/databases").then(response => {
            const data = response.data.data;
            data.forEach(database => {
                this.databases.push(database);
            })
        })
    },
    data() {
        return {
            selectedValue: null
        }
    },
    methods: {
        handleChange() {
            this.$emit('selected', this.selectedValue);
        }
    }
})

const BackupComponent = Vue.component('BackupComponent', {
    template: `
    <div class="page">
         <div class="block">
            <h2>База данных</h2>
            <databases-list :databases="databases" @selected="handleSelected"></databases-list>
         </div>
        <div class="block">
            <h1>Копирование и восстановление</h1>
    
            <div class="section">
                <h2>Настройки копирования</h2>
                <h3>Аргументы</h3>
                <div class="args">
                    <label>schema only</label><input type="checkbox" value="--schema-only" v-model="backupArguments">
                    <label>format</label><input type="checkbox" v-model="tChecked">
                    <select v-if="tChecked" v-model="tArg">
                        <option value="--format=custom">custom</option>
                        <option value="--format=plain">plain</option>
                        <option value="--format=directory">directory</option>
                        <option value="--format=tar">tar</option>
                    </select>
                </div>
    
                <label for="backupPath">Путь сохранения</label>
                <input v-model="backupPath" id="backupPath" type="text" placeholder="Введите путь сохранения" />
    
                <button @click="startBackup">Начать копирование</button>
            </div>
    
            <div class="section">
                <h2>Настройки восстановления</h2>
                <h3>Аргументы</h3>
                <div class="args">
                    <label class="arg-label">clean</label><input type="checkbox" value="--clean" v-model="restoreArguments">
                    <label class="arg-label">if exists</label><input type="checkbox" value="--if-exists" v-model="restoreArguments">
                    <label class="arg-label">data only</label><input type="checkbox" value="--data-only" v-model="restoreArguments" id="fDataOnly">
                    <label class="arg-label">schema only</label><input type="checkbox" value="--schema-only" v-model="restoreArguments" id="fSchemaOnly">
                </div>
    
                <label for="restorePath">Путь к бэкапам</label>
                <input v-model="restorePath" id="restorePath" type="text" placeholder="Путь к бэкапам"/>
    
                <div v-if="backupList.length > 0">
                    <label for="selectedBackup">Выберите бэкап</label>
                    <select v-model="selectedBackup" id="selectedBackup">
                        <option v-for="backup in backupList" :key="backup">{{ backup }}</option>
                    </select>
                </div>
    
                <div class="container">
                    <button @click="updateBackupList">Обновить список файлов</button>
                    <button @click="startRestore">Начать восстановление</button>
                </div>
            </div>
        </div>
    </div>
    `,
    data() {
        return {
            selectedDatabase: null,
            databases: [],
            backupArguments: [],
            backupPath: '',
            restoreArguments: [],
            restorePath: '',
            backupList: [],
            selectedBackup: '',
            tChecked: false,
            tArg: '-T p',

        };
    },
    methods: {
        startBackup() {
            if (this.backupPath.length > 0 && this.backupArguments) {
                let dumpData = {
                    path: this.backupPath,
                    dumpFileName: "",
                    args: this.backupArguments.join(' ') + ' ' + this.tArg,
                    database: this.selectedDatabase
                }

                api.post(`/practiceApp/dump/create`, dumpData).then(response => {
                    if (response.status == 200) {
                        alert("Бэкап успешно создан!");
                    } else {
                        alert(response.data.data);
                    }
                }).catch(error => {
                    switch (error.status) {
                        case 500:
                            alert("Не удалось создать - INTERNAL SERVER ERROR - 500");
                            break;
                        default:
                            alert("Непредвиденная ошибка!");
                            break;
                    }
                });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        updateBackupList() {
            if (this.restorePath) {
                api.get(`/practiceApp/dump/backups?path=${encodeURIComponent(this.restorePath)}`).then(response => {
                    if (response.status == 200) {
                        this.backupList = response.data.data;
                    } else {
                        alert(response.data.data);
                    }

                    console.log(response.data.data);
                })
                    .catch(error => {
                        alert(error.response.data.data);
                    });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        startRestore() {
            if (this.restoreArguments && this.selectedBackup) {
                let restoreData = {
                    path: this.restorePath,
                    dumpFileName: this.selectedBackup,
                    args: this.restoreArguments.join(' '),
                    database: this.selectedDatabase
                }

                api.post(`/practiceApp/dump/restore`, restoreData).then(response => {
                    alert(response.data.data);
                }).catch(error => {
                    alert(error.response.data.data);
                });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        handleSelected(value) {
            this.selectedDatabase = value;
            console.log(this.selectedDatabase);
        }
    }
})

const LoginComponent = Vue.component('LoginComponent', {
    template: `
    <div class="page">
    <div class="block">
        <h1>Вход</h1>

        <input v-model="userLogin" id="userLogin" type="text" placeholder="Введите логин"/>
        <input v-model="userPassword" id="userPassword" type="text" placeholder="Введите пароль"/>

        <button v-on:click="login()">Войти</button>
    </div>
    </div>
    `,
    data() {
        return {
            userLogin: '',
            userPassword: ''
        }
    },
    methods: {
        login() {
            const credentials = {
                username: this.userLogin,
                password: this.userPassword
            };

            axios.post('/practiceApp/auth', credentials).then(response => {
                localStorage.setItem('Authorization', response.data.token);
                setCookie('Authorization', response.data.token, 1);
                router.push('/practiceApp/settings');
            }).catch(error => {
                alert('Ошибка авторизации: ' + error);
            });
        }
    },
    beforeRouteEnter(to, from, next) {
        const token = localStorage.getItem('authToken');
        if (token) {
            axios.defaults.headers.common['Authorization'] = `${token}`;
        }
        next();
    }
})

const routes = [
    {
        path: '/practiceApp/settings',
        component: SettingsComponent,
        meta: {
            title: 'Settings'
        }
    },
    {
        path: '/practiceApp/backup',
        component: BackupComponent,
        meta: {
            title: 'Backups and restore'
        }
    },
    {
        path: '/practiceApp/login',
        component: LoginComponent,
        meta: {
            title: 'Login'
        }
    }
];

const router = new VueRouter({
    mode: 'history',
    routes: routes
});

function setCookie(name, value, hours) {
    var expires = "";
    if (hours) {
        var date = new Date();
        date.setTime(date.getTime() + (hours * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

new Vue({
    router: router,
    el: '#app'
});