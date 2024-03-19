const api = window.axios;

const BackButton = Vue.component('BackButton', {
    template: `
    <a href="/practiceApp/home" class="back-button">
        <img src="img/left.png" alt="Back">
    </a>
    `,
})

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
    methods: {},
    created: function () {
        this.updateDb();
    }
})

Vue.component('user-row', {
    props: ['user', 'setSelectedUser'],
    template:
        '<tr :class="rowClass" :user="user" @click="select()">' +
        '<td>{{user.username}}</td><td>{{user.password}}</td><td>{{user.roles[0].name}}</td>' +
        '</tr>',
    data() {
        return {
            rowClass: ""
        }
    },
    methods: {
        select() {
            this.setSelectedUser(this);
        }
    }
})

Vue.component('users-table', {
    props: ['users', 'updateUsers', 'setSelectedUser'],
    template:
        '<table>' +
        '<thead>' +
        '<tr>' +
        '<td>логин</td><td>пароль</td><td>роль</td>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<user-row v-for="user in users" :user="user" :key="user.userid" :setSelectedUser="setSelectedUser"></user-row>' +
        '</tbody>' +
        '</table>',
    methods: {},
    created: function () {
        this.updateUsers();
    }
})

const AdminComponent = Vue.component('AdminComponent', {
    template: `
    <div class="page">
        <BackButton></BackButton>
        <div class="block">
            <div class="section">
                <h2>Пользователи</h2>
                <div class="table-container">
                    <users-table :users="users" :updateUsers="updateUsers" :setSelectedUser="setSelectedUser"></users-table>
                </div>
                <div class="container">
                    <button @click="openAddDialog()">Добавить</button>
                    <button @click="deleteUser()">Удалить</button>
                    <button @click="updateUsers()">Обновить</button>
                </div>    
            </div>
            <a href="/practiceApp/settings">Settings</a>
        </div>
        <div class="dialog" v-if="isOpen">
            <img src="img/close.png" class="close-icon" @click="closeDialog()">
            <h3>Информация пользователя</h3>

            <label>Логин</label><input type="text" v-model="userData.username" v-if="!isEditMode">
            <label>Пароль</label><input type="text" v-model="userData.password">
            <label>Роль</label><select v-model="userData.role">
                <option v-for="role in roles" :role="role" :key="role.roleid">{{role.name}}</option>
            </select>
            
            <button @click="saveNewUser()">Добавить</button>
        </div>
        <div v-if="isOpen" class="overlay"></div>
    </div>
    `,
    created() {
        this.getAllRoles();
    },
    data() {
        return {
            userData: {
                username: null,
                password: null,
                role: null
            },
            selectedUserRow: null,
            users: [],
            isOpen: false,
            selectedRole: null,
            roles: []
        }
    },
    methods: {
        setSelectedUser(userRow) {
            this.userData = {
                username: userRow.user.username,
                password: userRow.user.password,
                role: userRow.user.roles[0].name,
            };
            console.log(this.userData);

            this.dropSelectedUser();
            userRow.rowClass = "selected-row";
            this.selectedUserRow = userRow;
        },
        dropSelectedUser() {
            if (this.selectedUserRow != null) {
                this.selectedUserRow.rowClass = "";
                this.selectedUserRow = null;
            }
        },
        dropUserData() {
            this.userData = {
                username: null,
                password: null,
                role: null
            }
        },
        deleteUser() {
            if (this.selectedUserRow != null) {
                if (this.userData.username === "admin") {
                    alert("Нельзя удалять admin!");
                    this.dropSelectedUser();
                    return;
                }

                api.post("/practiceApp/remove", this.userData).then(result => {
                    this.dropSelectedUser();
                    this.updateUsers();
                    alert(result.data.data);
                }).catch(error => {
                    alert("Error! " + error);
                });
                this.dropUserData();
            }
        },
        saveNewUser() {
            if (this.userData.username != null && this.userData.password != null && this.userData.role != null) {
                api.post("/practiceApp/registration", this.userData).then(result => {
                    this.dropSelectedUser();
                    this.updateUsers();
                    alert(result.data.data);
                }).catch(error => {
                    alert("Error! " + error);
                });
            }
        },
        getAllRoles() {
            api.get("/practiceApp/roles").then(response => {
                this.roles = [];
                const data = response.data;
                data.forEach(role => {
                    this.roles.push(role);
                })
            })
        },
        updateUsers() {
            api.get("/practiceApp/users").then(response => {
                this.users = [];
                const data = response.data;
                data.forEach(user => {
                    this.users.push(user);
                })
            })
        },
        openAddDialog() {
            this.dropUserData();
            this.isOpen = true;
        },
        closeDialog() {
            this.dropUserData();
            this.isOpen = false;
        }
    }
})

const HomePageComponent = Vue.component('HomePageComponent', {
    template: `
    <div class="home page">
        <div class="block">
            <ul>
              <li><a href="/practiceApp/backup">Backup</a></li>
              <li><a href="/practiceApp/migration">Migration</a></li>
              <li><a href="/practiceApp/journalization">Logs</a></li>
              <li><a href="/practiceApp/admin">Admin</a></li>
            </ul>
        </div>
    </div>
    `,
    methods: {},
    data() {
        return {}
    }
})

const SettingsComponent = Vue.component('SettingsComponent', {
    template: `
        <div class="page">
        <BackButton></BackButton>
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
        <a href="/practiceApp/connections-settings">Connections</a>

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
        '<option :class="dbRowClass">' +
        '{{database.name}}' +
        '</option>',
    computed: {
        dbRowClass() {
            return this.database.connectionStatus ? "" : "db-disconnected";
        }
    }
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
        <BackButton></BackButton>
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
                if (this.selectedDatabase == null) {
                    alert("Выберите базу данных!");
                    return;
                }

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
                if (this.selectedDatabase == null) {
                    alert("Выберите базу данных!");
                    return;
                }

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
                router.push('/practiceApp/home');
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

Vue.component('journal-option', {
    props: ['journal'],
    template:
        '<option>' +
        '{{journal.date}}' +
        '</option>'
});

Vue.component('journals-list', {
    props: ['journals', 'selectJournal'],
    template:
        '<select v-model="selected" @change="changeSelection">' +
        '<journal-option v-for="journal in journals" :journal="journal" :key="journal.journalid" />' +
        '</select>',
    data() {
        return {
            selected: null
        }
    },
    methods: {
        changeSelection() {
            this.selectJournal(this.selected);
        }
    }
})

Vue.component('logs-row', {
    props: ['log'],
    template:
        '<tr :log="log">' +
        '<td>{{log.journal.user.username}}</td><td>{{log.data}}</td><td>{{log.time}}</td><td>{{log.status}}</td>' +
        '</tr>'
})

Vue.component('logs-table', {
    props: ['logs'],
    template:
        '<table>' +
        '<thead>' +
        '<tr>' +
        '<td>пользователь</td><td>информация</td><td>время</td><td>статус</td>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<logs-row v-for="log in logs" :log="log" :key="logs.logId"></logs-row>' +
        '</tbody>' +
        '</table>'
})

Vue.component('users-list-row', {
    props: ['user'],
    template:
        `
        <option>{{user.username}}</option>
        `
})

Vue.component('users-list', {
    props: ['users', 'selectUser'],
    template:
        `
        <select v-model="selected" @change="changeSelection"> +
        <users-list-row v-for="user in users" :user="user" :key="user.userid" /> +
        </select>
        `,
    methods: {
        changeSelection() {
            this.selectUser(this.selected);
        }
    },
    data() {
        return {
            selected: null
        }
    }
})

const LogsComponent = Vue.component('LogsComponent', {
    template: `
    <div class="page">
    <BackButton></BackButton>
    <div class="block">
        <h1>Журналы</h1>
        <users-list :users="users" :selectUser="selectUser"></users-list>
        <journals-list v-if="isUserSelected && journals.length > 0" :journals="journals" :selectJournal="selectJournal"></journals-list>
        <p v-if="journals.length === 0">Нет журналов</p>
        <logs-table :logs="logs" v-if="isJournalSelected"></logs-table>
    </div>
    </div>
    `,
    data() {
        return {
            users: [],
            journals: [],
            logs: [],
            selectedJournal: null,
            selectedUser: null,
            isUserSelected: false,
            isJournalSelected: false
        }
    },
    created: function () {
        api.get("/practiceApp/users").then(response => {
            const data = response.data;
            data.forEach(user => {
                this.users.push(user);
            });
        });
    },
    methods: {
        selectUser(user) {
            this.selectedUser = user;
            this.isUserSelected = true;
            this.updateJournals();
            if (this.selectedJournal != null) {
                this.updateLogs();
            }
        },
        selectJournal(journal) {
            this.selectedJournal = journal;
            this.isJournalSelected = true;
            this.updateLogs();
        },
        updateLogs() {
            this.logs = [];
            api.get("/practiceApp/logs/data/" + this.selectedJournal + "_" + this.selectedUser).then(response => {
                const data = response.data.data;
                data.forEach(log => {
                    this.logs.push(log);
                });
            });
        },
        updateJournals() {
            this.journals = [];
            api.get("/practiceApp/logs/journals/user/" + this.selectedUser).then(response => {
                const data = response.data.data;
                data.forEach(journal => {
                    this.journals.push(journal);
                });
            });

            this.isJournalSelected = false;
        }
    }
})

Vue.component("db-table-option", {
    props: ['table'],
    template: `
    <option>{{table.name}}</option>
    `,
})

const MigrationComponent = Vue.component("MigrationComponent", {
    template: `
    <div class="page">
        <BackButton></BackButton>
        <div class="block">
            <h1>Миграции</h1>
            <div class="section">
                <h2>Выбор пары баз данных</h2>
                <connections-table :connections="connections" :selectConnection="selectConnection"></connections-table>
                <button @click="openDialog()">Продожить</button>
            </div>
        </div>
        <div class="dialog" v-if="isOpen">
            <img src="img/close.png" class="close-icon" @click="closeDialog()">
            <h3>Выберите таблицы</h3>
            
            <label>Таблица источника</label>
            <select class="databases-list" v-model="selectedSource" @change="sourceSelected()">
                <db-table-option v-for="table in sourceTables" :table="table" :key="table.name"></db-table-option>
            </select>
            <label v-if="selectedSource">Таблица приёмника</label>
            <select v-if="selectedSource" class="databases-list" v-model="selectedOutput">
                <db-table-option v-for="table in outputTables" :table="table" :key="table.name"></db-table-option>
            </select>
            
            <button @click="start()">Начать</button>
        </div>
        <div v-if="isOpen" class="overlay"></div>
    </div>
    `,
    data() {
        return {
            isOpen: false,
            connections: [],
            sourceTables: [],
            outputTables: [],
            selectedConnectionRow: null,
            connectionData: {
                databaseSource: null,
                databaseOutput: null
            },
            selectedSource: null,
            selectedOutput: null
        }
    },
    created() {
        this.updateConnections();
    },
    methods: {
        start() {
            if (this.selectedSource == null && this.selectedOutput == null) {
                alert("Выберите таблицы!");
                return
            }

            this.closeDialog();

            let migrationData = {
                tablenameSource: this.selectedSource,
                tablenameOutput: this.selectedOutput,
                connection: this.selectedConnectionRow.connection
            }

            console.log(migrationData);

            api.post("/practiceApp/migration/start", migrationData).then(result => {
                alert(result.data.data);
            });
        },
        getTables(databaseName) {
            let tables = [];
            api.get("/practiceApp/databases/database/tables/" + databaseName).then(result => {
                const data = result.data.data;
                data.forEach(table => {
                    tables.push(table);
                });
            });

            return tables;
        },
        sourceSelected() {
            this.outputTables = this.getTables(this.connectionData.databaseOutput);
        },
        openDialog() {
            if (this.connectionData.databaseSource == null) {
                alert("Выберите соединение!");
                return;
            }

            this.sourceTables = this.getTables(this.connectionData.databaseSource);
            this.isOpen = true;
        },
        closeDialog() {
            this.isOpen = false;
        },
        updateConnections() {
            this.connections = [];
            api.get("/practiceApp/connections").then(result => {
                const data = result.data;
                data.forEach(connection => {
                    if (connection.fromDatabase.connectionStatus && connection.toDatabase.connectionStatus) {
                        this.connections.push(connection);
                    }
                });
            });
        },
        dropSelection() {
            if (this.selectedConnectionRow != null) {
                this.selectedConnectionRow.removeSelection();
                this.selectedConnectionRow = null;
            }

            this.connectionData = {
                databaseSource: null,
                databaseOutput: null
            }
        },
        selectConnection(connectionRow) {
            if (this.selectedConnectionRow === connectionRow) {
                return;
            }

            this.dropSelection();
            this.selectedConnectionRow = connectionRow;

            this.connectionData = {
                databaseSource: this.selectedConnectionRow.connection.fromDatabase.name,
                databaseOutput: this.selectedConnectionRow.connection.toDatabase.name
            }
        }
    }
});

Vue.component('connection-row', {
    props: ['connection', 'selectConnection'],
    template: `
    <tr :class="rowClass" @click="changeSelection()">
    <td>{{connection.fromDatabase.name}}<img class="row-warning-icon" v-if="!this.connection.fromDatabase.connectionStatus" src="img/warning.png"></td>
    <td>{{connection.toDatabase.name}}<img class="row-warning-icon" v-if="!this.connection.toDatabase.connectionStatus" src="img/warning.png"></td>
    </tr>
    `,
    created() {
        if (this.connection.fromDatabase.connectionStatus && this.connection.toDatabase.connectionStatus) {
            this.isConnected = true;
        } else {
            this.isConnected = false;
            this.rowClass = "db-disconnected";
        }
    },
    methods: {
        removeSelection() {
            if (this.isConnected) {
                this.rowClass = "";
            } else {
                this.rowClass = "db-disconnected";
            }
        },
        changeSelection() {
            this.rowClass = "selected-row";
            this.selectConnection(this);
        }
    },
    data() {
        return {
            isConnected: false,
            rowClass: ""
        }
    },
})

Vue.component('connections-table', {
    props: ['connections', 'selectConnection'],
    template: `
    <table>
      <thead>
      <tr>
      <td>Источник</td>
      <td>Приёмник</td>
      </tr>
      </thead>
      <tbody>
      <connection-row v-for="connection in connections" :connection="connection" :key="connection.connectionId" :selectConnection="selectConnection"></connection-row>
      </tbody>
    </table>
    `,
})

const ConnecitonsComponent = Vue.component("ConnecitonsComponent", {
    template: `
    <div class="page">
        <BackButton></BackButton>
        <div class="block">
            <h1>Настройки</h1>
            <div class="section">
                <h2>Соединения</h2>
                <connections-table :connections="connections" :selectConnection="selectConnection"></connections-table>
                <button @click="openDialog()">Добавить</button>
                <button @click="updateConnections()">Обновить</button>
                <button @click="deleteSelected()">Удалить</button>
            </div>
        </div>
        <div class="dialog" v-if="isOpen">
            <img src="img/close.png" class="close-icon" @click="closeDialog()">
            <h3>Новое соединение</h3>

            <label>Имя базы источника</label>
            <select class="databases-list" v-model="connectionData.databaseSource">
                <database-option v-for="database in databases" :database="database" :key="database.databaseId" />
            </select>
            <label>Имя базы приёмника</label>
            <select class="databases-list" v-model="connectionData.databaseOutput">
                <database-option v-for="database in databases" :database="database" :key="database.databaseId" />
            </select>
            
            <button @click="addConnection()">Добавить</button>
        </div>
        <div v-if="isOpen" class="overlay"></div>
    </div>
    `,
    data() {
        return {
            isOpen: false,
            connections: [],
            databases: [],
            selectedConnectionRow: null,
            connectionData: {
                databaseSource: null,
                databaseOutput: null
            }
        }
    },
    methods: {
        openDialog() {
            this.isOpen = true;
            this.updateDatabases();
        },
        closeDialog() {
            this.isOpen = false;
        },
        addConnection() {
            console.log(this.connectionData);
            if (this.connectionData.databaseSource != null && this.connectionData.databaseOutput != null) {
                api.post("/practiceApp/connections/add", this.connectionData).then(result => {
                    this.updateConnections();
                    this.dropSelection();
                });
            } else {
                alert("Заполните все поля!");
            }
        },
        deleteSelected() {
            if (this.selectedConnectionRow != null) {
                api.post("/practiceApp/connections/remove", this.connectionData).then(result => {
                    this.updateConnections();
                    this.dropSelection();
                })
            } else {
                alert("Выберите соединение!");
            }
        },
        dropSelection() {
            if (this.selectedConnectionRow != null) {
                this.selectedConnectionRow.removeSelection();
                this.selectedConnectionRow = null;
            }

            this.connectionData = {
                databaseSource: null,
                databaseOutput: null
            }
        },
        selectConnection(connectionRow) {
            if (this.selectedConnectionRow === connectionRow) {
                return;
            }
            this.dropSelection();
            this.selectedConnectionRow = connectionRow;

            this.connectionData = {
                databaseSource: this.selectedConnectionRow.connection.fromDatabase.name,
                databaseOutput: this.selectedConnectionRow.connection.toDatabase.name
            }
        },
        updateConnections() {
            this.connections = [];
            api.get("/practiceApp/connections").then(result => {
                const data = result.data;
                data.forEach(connection => {
                    this.connections.push(connection);
                });
            });
        },
        updateDatabases() {
            this.databases = [];
            api.get("/practiceApp/databases").then(response => {
                const data = response.data.data;
                data.forEach(database => {
                    this.databases.push(database);
                })
            })
        }
    },
    created() {
        this.updateConnections();
    }
});

const routes = [
    {
        path: '/practiceApp/connections-settings',
        component: ConnecitonsComponent,
        meta: {
            title: 'Settings'
        }
    },
    {
        path: '/practiceApp/migration',
        component: MigrationComponent,
        meta: {
            title: 'Migration'
        }
    },
    {
        path: '/practiceApp/journalization',
        component: LogsComponent,
        meta: {
            title: 'Logs'
        }
    },
    {
        path: '/practiceApp/settings',
        component: SettingsComponent,
        meta: {
            title: 'Settings'
        }
    },
    {
        path: '/practiceApp/admin',
        component: AdminComponent,
        meta: {
            title: 'Admin-panel'
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
    },
    {
        path: '/practiceApp/home',
        component: HomePageComponent,
        meta: {
            title: 'Home'
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
        date.setTime(date.getTime() + (hours * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

new Vue({
    router: router,
    el: '#app',
    created() {

    }
});